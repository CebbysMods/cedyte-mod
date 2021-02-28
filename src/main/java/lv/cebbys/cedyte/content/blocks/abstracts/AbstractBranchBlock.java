package lv.cebbys.cedyte.content.blocks.abstracts;

import java.util.Random;
import java.util.function.Supplier;

import com.cebbys.celib.loggers.CelibLogger;
import com.cebbys.celib.utilities.CelibBlockPos;
import com.cebbys.celib.utilities.CelibVoxelShapes;

import lv.cebbys.cedyte.content.blocks.DynamicBranchBlock;
import lv.cebbys.cedyte.content.blocks.DynamicRootBlock;
import lv.cebbys.cedyte.content.entities.DynamicBranchEntity;
import lv.cebbys.cedyte.content.entities.DynamicRootEntity;
import lv.cebbys.cedyte.content.entities.DynamicStemEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class AbstractBranchBlock extends Block implements BlockEntityProvider {

	private static final VoxelShape[][] SHAPE;

	public static final EnumProperty<Direction> DIRECTION;

	private Supplier<? extends BlockEntity> factory;

	public AbstractBranchBlock(Supplier<? extends DynamicBranchEntity> entity) {
		super(FabricBlockSettings.of(Material.WOOD, MaterialColor.BROWN).nonOpaque());
		this.factory = entity;
		this.setDefaultState(this.getDefaultState().with(DIRECTION, Direction.UP));
	}

//	@Override
//	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
//		return this.getVoxelShape(state, world, pos);
//	}
//
//	@Override
//	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
//		return this.getVoxelShape(state, world, pos);
//	}

//	private VoxelShape getVoxelShape(BlockState state, BlockView world, BlockPos pos) {
//		int stage = state.get(GROWTH_STAGE);
//		Direction dir = state.get(AbstractBranchBlock.DIRECTION);
//		switch (dir) {
//		case UP:
//			return SHAPE[stage][1];
//		case NORTH:
//			return SHAPE[stage][2];
//		case SOUTH:
//			return SHAPE[stage][3];
//		case WEST:
//			return SHAPE[stage][4];
//		case EAST:
//			return SHAPE[stage][5];
//		default:
//			return SHAPE[stage][0];
//		}
//	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(DIRECTION);
	}

//	@Override
//	public BlockState getPlacementState(ItemPlacementContext ctx) {
//		return this.getDefaultState().with(DIRECTION, Direction.EAST).with(GROWTH_STAGE, 1);
//	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return false;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		// TODO For now leave empty. Root handles random ticking
	}

	private static VoxelShape[][] getOutlineShapes() {
		VoxelShape[][] shapes = new VoxelShape[8][6];
		for (int i = 0; i < 8; i++) {
			float[] arr = new float[] { 7.0F - i, 9.0F + i };
			shapes[i][1] = Block.createCuboidShape(arr[0], -arr[0], arr[0], arr[1], arr[1], arr[1]); // UP
			shapes[i][3] = Block.createCuboidShape(arr[0], arr[0], -arr[0], arr[1], arr[1], arr[1]); // NORTH
			if (i == 0) {
				shapes[i][1] = VoxelShapes.union(shapes[i][1], Block.createCuboidShape(0, 0, 0, 16, 16, 16));
				shapes[i][3] = VoxelShapes.union(shapes[i][3], Block.createCuboidShape(0, 0, 0, 16, 16, 16));
			}
			shapes[i][0] = CelibVoxelShapes.mirrorAxisY(shapes[i][1]); // DOWN
			shapes[i][4] = CelibVoxelShapes.rotatePosY(shapes[i][3]); // WEST
			shapes[i][2] = CelibVoxelShapes.rotatePosY(shapes[i][4]); // EAST
			shapes[i][5] = CelibVoxelShapes.rotatePosY(shapes[i][2]); // SOUTH
		}
		return shapes;
	}

	static {
		DIRECTION = EnumProperty.of("direction", Direction.class);
		SHAPE = getOutlineShapes();
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return factory.get();
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
//		((DynamicRootEntity) world.getBlockEntity(pos)).onStemBroken(pos);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		if (!this.canPlaceAt(state, world, pos)) {
			world.breakBlock(pos, false);
		}
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		// TODO Find why parentPos is null
		DynamicStemEntity entity = (DynamicStemEntity) world.getBlockEntity(pos);
		if(entity != null) {
			CelibBlockPos parentPos = entity.getParentPosition();
			if(parentPos != null) {
				Block block = world.getBlockState(parentPos).getBlock();
				if(block != null) {
					return block instanceof DynamicBranchBlock || block instanceof DynamicRootBlock;
				}
			}
		}
		return false;
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState,
			WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (!state.canPlaceAt(world, pos)) {
			world.getBlockTickScheduler().schedule(pos, this, 1);
			return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
		} else {
			return state;
		}
	}

}
