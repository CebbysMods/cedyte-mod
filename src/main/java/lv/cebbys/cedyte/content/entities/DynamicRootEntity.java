package lv.cebbys.cedyte.content.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import lv.cebbys.cedyte.content.entities.util.IntArrayTagParser;
import lv.cebbys.cedyte.content.trees.BranchStruct;
import lv.cebbys.cedyte.content.trees.StemStruct;
import lv.cebbys.cedyte.content.trees.TreeStruct;
import lv.cebbys.cedyte.content.trees.handlers.abstracts.AbstractTreeHandler;
import lv.cebbys.celib.loggers.CelibLogger;
import lv.cebbys.celib.utilities.CelibBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DynamicRootEntity extends DynamicStemEntity implements Tickable {

	private int treeAge;

	public AbstractTreeHandler handler;
	
	public TreeStruct tree;

	public DynamicRootEntity(BlockEntityType<?> type, Supplier<AbstractTreeHandler> handler, CelibBlockPos rootPos) {
		super(type);
		this.treeAge = 0;
		// TODO Change dynamic handler to static registered version
		this.handler = handler.get();
	}

	@Override
	public void tick() {
		if(this.tree == null) {
			this.tree = new TreeStruct();
			this.tree.init(this.getWorld().getRandom(), this.getPos());
		} else {
			if (this.treeAge % 8 == 0) {
				this.treeAge = 0;
				if(!this.world.isClient) {
					this.handler.randomTick(this.tree, this.getWorld().getRandom(), CelibBlockPos.of(this.getPos()), this.getWorld());
				}
			}
			this.treeAge += 1;
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("tree_age", this.treeAge);
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.treeAge = tag.getInt("tree_age");
	}
	
	public BranchStruct getBranch(CelibBlockPos stemPos) {
		if(this.tree != null) {
			List<BranchStruct> branches = this.tree.getBranches();
			for(int i = 0; i < branches.size(); i++) {
				BranchStruct branch = branches.get(i);
				if(branch.getStemPositions().contains(stemPos)) {
					return branch;
				}
			}
		}
		return null;
	}
}
