package com.cebbys.cedyte.content.blocks.abstracts;

import java.util.Random;
import java.util.function.Supplier;

import com.cebbys.cedyte.content.entities.tree.DynamicRootEntity;

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

	private static final int MAX_GROWTH_STAGE = 7;

	public static final IntProperty GROWTH_STAGE;

	private Supplier<? extends BlockEntity> factory;

	public AbstractRootBlock(Supplier<? extends BlockEntity> entity) {
		super(FabricBlockSettings.of(Material.WOOD, MaterialColor.BROWN).nonOpaque());
		this.setDefaultState(this.getDefaultState().with(GROWTH_STAGE, 0));
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
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(GROWTH_STAGE);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(GROWTH_STAGE, 0);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return state.get(GROWTH_STAGE) < MAX_GROWTH_STAGE;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockEntity e = world.getBlockEntity(pos);
		if (e instanceof DynamicRootEntity) {
			DynamicRootEntity treeEntity = (DynamicRootEntity) e;
			treeEntity.onRandomTick(world, pos, random);
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity rootEntity = world.getBlockEntity(pos);
		if (rootEntity instanceof DynamicRootEntity) {
			DynamicRootEntity e = (DynamicRootEntity) rootEntity;
			e.onRootBroken();
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return factory.get();
	}

	static {
		GROWTH_STAGE = IntProperty.of("growth_stage", 0, MAX_GROWTH_STAGE);
	}
}
