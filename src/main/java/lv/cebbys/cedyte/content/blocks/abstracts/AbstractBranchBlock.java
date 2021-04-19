package lv.cebbys.cedyte.content.blocks.abstracts;

import java.util.Random;
import java.util.function.Supplier;

import lv.cebbys.cedyte.content.blocks.DynamicBranchBlock;
import lv.cebbys.cedyte.content.blocks.DynamicRootBlock;
import lv.cebbys.cedyte.content.entities.DynamicBranchEntity;
import lv.cebbys.cedyte.content.entities.DynamicRootEntity;
import lv.cebbys.cedyte.content.entities.DynamicStemEntity;
import lv.cebbys.celib.loggers.CelibLogger;
import lv.cebbys.celib.utilities.CelibBlockPos;
import lv.cebbys.celib.utilities.CelibVoxelShapes;
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

public class AbstractBranchBlock extends AbstractStemBlock {

	public AbstractBranchBlock(Supplier<? extends DynamicBranchEntity> entity) {
		super(FabricBlockSettings.of(Material.WOOD, MaterialColor.BROWN).nonOpaque(), entity);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return false;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		// TODO For now leave empty. Root handles random ticking
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
