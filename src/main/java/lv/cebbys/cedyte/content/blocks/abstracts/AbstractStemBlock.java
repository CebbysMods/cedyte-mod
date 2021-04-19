package lv.cebbys.cedyte.content.blocks.abstracts;

import java.util.Set;
import java.util.function.Supplier;

import lv.cebbys.cedyte.content.entities.DynamicStemEntity;
import lv.cebbys.celib.utilities.CelibVoxelShapes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class AbstractStemBlock extends Block implements BlockEntityProvider {


	private VoxelShape[][] collisionShape;
	
	private Supplier<? extends BlockEntity> entitySupplier;

	public AbstractStemBlock(Settings settings, Supplier<? extends BlockEntity> supplier) {
		super(settings);
		this.entitySupplier = supplier;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		DynamicStemEntity entity = (DynamicStemEntity) world.getBlockEntity(pos);
		if(entity != null) {
			int stage = entity.getGrowthStage();
			VoxelShape[] shapes = this.generateVoxelShapes()[stage];
			Set<Direction> directions = entity.getBranchingDirections();
			VoxelShape shape = shapes[0];
			for(Direction dir : directions) {
				if(dir.equals(Direction.UP)) {
					shape = VoxelShapes.union(shape, shapes[1]);
				} else if(dir.equals(Direction.DOWN)) {
					shape = VoxelShapes.union(shape, CelibVoxelShapes.mirrorAxisY(shapes[1]));
				} else if(dir.equals(Direction.EAST)) {
					shape = VoxelShapes.union(shape, CelibVoxelShapes.rotateNegY(shapes[2]));
				} else if(dir.equals(Direction.WEST)) {
					shape = VoxelShapes.union(shape, CelibVoxelShapes.rotatePosY(shapes[2]));
				} else if(dir.equals(Direction.SOUTH)) {
					shape = VoxelShapes.union(shape, shapes[2]);
				} else if(dir.equals(Direction.NORTH)) {
					shape = VoxelShapes.union(shape, CelibVoxelShapes.rotatePosY(CelibVoxelShapes.rotatePosY(shapes[2])));
				}
			}
			return shape;
		}
		return VoxelShapes.empty();
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return this.entitySupplier.get();
	}
	
	private VoxelShape[][] generateVoxelShapes() {
		VoxelShape[][] shapes = new VoxelShape[8][3];
		for(int i = 0; i < shapes.length; i++) {
			int size = (i + 1) * 2;
			int border = (16 - size) / 2;
			shapes[i][0] = Block.createCuboidShape(border, border, border, border + size, border + size, border + size);
			shapes[i][1] = Block.createCuboidShape(border, border + size, border, border + size, 16, border + size);
			shapes[i][2] = Block.createCuboidShape(border, border, border + size, border + size, border + size, 16);
		}
		return shapes;
	}

}
