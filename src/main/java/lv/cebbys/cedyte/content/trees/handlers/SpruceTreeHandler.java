package lv.cebbys.cedyte.content.trees.handlers;

import java.util.Random;

import lv.cebbys.cedyte.content.DynamicTreeBlocks;
import lv.cebbys.cedyte.content.blocks.abstracts.AbstractBranchBlock;
import lv.cebbys.cedyte.content.blocks.abstracts.AbstractRootBlock;
import lv.cebbys.cedyte.content.entities.SpruceBranchEntity;
import lv.cebbys.cedyte.content.trees.BranchStruct;
import lv.cebbys.cedyte.content.trees.StemStruct;
import lv.cebbys.cedyte.content.trees.TreeStruct;
import lv.cebbys.cedyte.content.trees.handlers.abstracts.AbstractTreeHandler;
import lv.cebbys.celib.loggers.CelibLogger;
import lv.cebbys.celib.utilities.CelibBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

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
	protected int getMaxBranchLength(TreeStruct t, BranchStruct b) {
		return 10;
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

	@Override
	protected BlockEntity createBranchEntity(Direction parentDirection, int stemIndex) {
		return new SpruceBranchEntity(parentDirection, stemIndex, stemIndex);
	}
	
}
