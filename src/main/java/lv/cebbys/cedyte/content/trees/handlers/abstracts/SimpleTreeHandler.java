package lv.cebbys.cedyte.content.trees.handlers.abstracts;

import java.util.Random;

import lv.cebbys.cedyte.content.trees.BranchStruct;
import lv.cebbys.cedyte.content.trees.StemStruct;
import lv.cebbys.cedyte.content.trees.TreeStruct;
import lv.cebbys.celib.loggers.CelibLogger;
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
	protected int getMaxBranchLength(TreeStruct t, BranchStruct branch) {
		return 10;
	}
}
