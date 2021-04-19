package lv.cebbys.cedyte.content.blocks.abstracts;

import java.util.Random;
import java.util.function.Supplier;

import lv.cebbys.cedyte.content.blocks.DynamicBranchBlock;
import lv.cebbys.cedyte.content.blocks.DynamicRootBlock;
import lv.cebbys.cedyte.content.entities.DynamicRootEntity;
import lv.cebbys.cedyte.content.entities.DynamicStemEntity;
import lv.cebbys.celib.utilities.CelibBlockPos;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class AbstractRootBlock extends BlockWithEntity {

	private Supplier<? extends BlockEntity> factory;

	public AbstractRootBlock(Supplier<? extends BlockEntity> entity) {
		super(FabricBlockSettings.of(Material.WOOD, MaterialColor.BROWN).nonOpaque());
		this.factory = entity;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		Block.createCuboidShape(0, 0, 0, 0, 0, 0);
		// TODO Auto-generated method stub
		return super.getOutlineShape(state, world, pos, context);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockEntity e = world.getBlockEntity(pos);
		if (e instanceof DynamicRootEntity) {
			DynamicRootEntity treeEntity = (DynamicRootEntity) e;
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return factory.get();
	}

	
	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);
		if(this.shouldBreak(world, pos, random)) {
			// TODO Set drop to true
			world.breakBlock(pos, false);
		}
	}
	
	private boolean shouldBreak(World world, BlockPos pos, Random random) {
		DynamicStemEntity entity = (DynamicStemEntity) world.getBlockEntity(pos);
		CelibBlockPos parentPos = entity.getParentPosition();
		Block block = world.getBlockState(parentPos).getBlock();
		return !(block instanceof DynamicBranchBlock || block instanceof DynamicRootBlock);
	}
}
