package com.cebbys.cedyte.content.trees.handlers;

import java.util.Random;

import com.cebbys.cedyte.content.DynamicTreeBlocks;
import com.cebbys.cedyte.content.blocks.abstracts.AbstractBranchBlock;
import com.cebbys.cedyte.content.blocks.abstracts.AbstractRootBlock;
import com.cebbys.cedyte.content.trees.handlers.abstracts.AbstractTreeHandler;
import com.cebbys.cedyte.content.trees.structs.BranchStruct;
import com.cebbys.cedyte.content.trees.structs.StemStruct;
import com.cebbys.cedyte.content.trees.structs.TreeStruct;
import com.cebbys.celib.loggers.CelibLogger;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Direction;

public class SpruceTreeHandler extends AbstractTreeHandler {

	private final Block[] growtroughtBlocks;

	public SpruceTreeHandler() {
		this.growtroughtBlocks = new Block[] { Blocks.AIR, Blocks.SPRUCE_LEAVES };
	}

	@Override
	public int getStemStage(StemStruct stem) {
		int stemAge = stem.getAge();
		int stemFac = 1 + (stem.fac * 6);
		if (stemAge / stemFac < 3) {
			return 0;
		} else if (stemAge / stemFac < 6) {
			return 1;
		} else if (stemAge / stemFac < 10) {
			return 2;
		} else if (stemAge / stemFac < 14) {
			return 3;
		} else if (stemAge / stemFac < 19) {
			return 4;
		} else if (stemAge / stemFac < 24) {
			return 5;
		} else if (stemAge / stemFac < 30) {
			return 6;
		} else {
			return 7;
		}
	}

	@Override
	public Block[] getGrowtroughtBlocks() {
		return this.growtroughtBlocks;
	}

	@Override
	protected int getRequiredGrowthAge(int branchFactor) {
		if(branchFactor == 0) {
			return 1;
		} else {
			return 4;
		}
	}

	@Override
	public Direction getPreferedStemDirection(TreeStruct t, BranchStruct branch, StemStruct parent, Random random) {
		return branch.getBudDirection(t);
	}

	@Override
	protected int getMaxBranchLength(TreeStruct t, BranchStruct b) {
		final float branchMinHeight = 3.0F;
		final float treeMaxHeight = 30.0F;
		final float maxWidth = 6.0F;
		int stemFac = b.getStem(t, 0).fac;
		if (stemFac == 0) {
			return (int) treeMaxHeight;
		} else if(stemFac == 1) {
			float height = (float) b.getStem(t, 0).off.getY();
			int len = this.lenFunc(height, branchMinHeight, treeMaxHeight, maxWidth);
			CelibLogger.log("SpruceHandler", "Branch length at y off: " + height + " length: " + len);
			return len;
		}
		return 0;
	}
	
	public int lenFunc(float h, float bh, float th, float w) {
		float v0 = h - bh;
		float v1 = th - bh - 2;
		float v2 = v1 / w;
		float v3 = v0 / v2;
		return (int) (w - v3);
	}

	@Override
	public AbstractBranchBlock getBranchBlock() {
		return DynamicTreeBlocks.SPRUCE_BRANCH;
	}

	@Override
	public AbstractRootBlock getRootBlock() {
		return DynamicTreeBlocks.SPRUCE_ROOT;
	}

	@Override
	public Block getLeafBlock() {
		return Blocks.SPRUCE_LEAVES;
	}
	
}
