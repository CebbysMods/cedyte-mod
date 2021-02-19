package com.cebbys.cedyte.content.trees.handlers.abstracts;

import java.util.Random;

import com.cebbys.cedyte.content.trees.structs.BranchStruct;
import com.cebbys.cedyte.content.trees.structs.StemStruct;
import com.cebbys.cedyte.content.trees.structs.TreeStruct;
import com.cebbys.celib.loggers.CelibLogger;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Direction;

public abstract class SimpleTreeHandler extends AbstractTreeHandler {

	@Override
	public int getStemStage(StemStruct stem) {
		int stemAge = stem.getAge();
		int stemFac = 1 + (stem.fac * 4);
		if (stemAge / stemFac < 2) {
			return 0;
		} else if (stemAge / stemFac < 4) {
			return 1;
		} else if (stemAge / stemFac < 7) {
			return 2;
		} else if (stemAge / stemFac < 10) {
			return 3;
		} else if (stemAge / stemFac < 14) {
			return 4;
		} else if (stemAge / stemFac < 18) {
			return 5;
		} else if (stemAge / stemFac < 23) {
			return 6;
		} else {
			return 7;
		}
	}

	@Override
	protected int getRequiredGrowthAge(int branchFactor) {
		return (branchFactor * 4) + 2;
	}

	@Override
	protected Block[] getGrowtroughtBlocks() {
		return new Block[] { Blocks.AIR, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.DARK_OAK_LEAVES,
				Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES };
	}

	@Override
	public Direction getPreferedStemDirection(TreeStruct t, BranchStruct branch, StemStruct parent, Random random) {
		return branch.getBudDirection(t);
	}

	@Override
	protected int getMaxBranchLength(TreeStruct t, BranchStruct branch) {
		if(branch != null) {
			StemStruct stem = branch.getStem(t, 0);
			if(stem != null) {
				float l0 = 20.0F;
				int x = stem.off.getY();
				float h0 = 3.0F;
				if(stem.fac == 0) {
					return (int) (l0 + 3.0F);
				}
				else if(stem.fac == 1 && x >= h0) {
					float l1 = (l0 - h0) / 2.0F;
					float w0 = 1.5F;
					float v0 = (float) Math.pow(l1, 2);
					float v1 = (float) Math.pow(x - l1 - h0, 2);
					float top = (float) Math.sqrt(v0 - v1);
					return (int) (top / w0);
				}
			}
		}
		return 0;
	}
}
