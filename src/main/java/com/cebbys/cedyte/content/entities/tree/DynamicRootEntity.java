package com.cebbys.cedyte.content.entities.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.function.Supplier;

import com.cebbys.cedyte.content.entities.util.IntArrayTagParser;
import com.cebbys.cedyte.content.trees.handlers.abstracts.AbstractTreeHandler;
import com.cebbys.cedyte.content.trees.structs.StemStruct;
import com.cebbys.cedyte.content.trees.structs.TreeStruct;
import com.cebbys.celib.utilities.CelibBlockPos;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;

public abstract class DynamicRootEntity extends BlockEntity implements Tickable {


	private BlockPos[] branches = new BlockPos[0];
	
	private ArrayList<BlockPos> leaves;
	private int treeAge;
	
	public TreeStruct tree;
	public AbstractTreeHandler handler;

	public DynamicRootEntity(BlockEntityType<?> type, Supplier<AbstractTreeHandler> handler) {
		super(type);
		this.treeAge = 0;
		this.leaves = new ArrayList<BlockPos>();
		
		this.tree = new TreeStruct();
		// TODO Initialize tree from sapling random update
		this.tree.init(new Random());
		// TODO Change dynamic handler to static registered version
		this.handler = handler.get();
	}
	
	public StemStruct getStemFromPos(CelibBlockPos pos) {
		return this.handler.getStemFromPos(pos, this.tree, CelibBlockPos.of(this.getPos()));
	}

	public void onRandomTick(ServerWorld world, BlockPos pos, Random random) {
	}

	@Override
	public void tick() {
		if (!this.getWorld().isClient()) {
			this.handler.tick(CelibBlockPos.of(this.getPos()), this.getWorld());
			if (this.treeAge % 8 == 0) {
				this.treeAge = 0;
				this.handler.randomTick(this.tree, new Random(), CelibBlockPos.of(this.getPos()), this.getWorld());	
			}
			this.treeAge += 1;
		}
	}

	public void onRootBroken() {
		if (!this.world.isClient) {
			Collection<StemStruct> list = tree.getAllStems();
			for (StemStruct stem : list) {
				BlockPos p = CelibBlockPos.of(stem.off).add(pos);
				this.world.breakBlock(p, false);
			}
			for(BlockPos pos : this.leaves) {
				if(this.world.getBlockState(pos).getBlock() == this.handler.getLeafBlock()) {
					this.world.breakBlock(pos, false);
				}
			}
			this.leaves.clear();
			this.markDirty();
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putIntArray("branches", IntArrayTagParser.PosArray.toIntArray(branches));
		tag.putInt("tree_age", treeAge);
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.branches = IntArrayTagParser.PosArray.toBlockPosArray(tag.getIntArray("branches"));
		this.treeAge = tag.getInt("tree_age");
	}

	public void onStemBroken(BlockPos pos) {
		this.handler.onStemBroken(this.getPos().sub(pos));
	}
	
	@Override
	public CelibBlockPos getPos() {
		return CelibBlockPos.of(super.getPos());
	}
}
