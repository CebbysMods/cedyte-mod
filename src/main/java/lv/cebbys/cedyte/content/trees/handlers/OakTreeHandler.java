package lv.cebbys.cedyte.content.trees.handlers;

import lv.cebbys.cedyte.content.DynamicTreeBlocks;
import lv.cebbys.cedyte.content.blocks.abstracts.AbstractBranchBlock;
import lv.cebbys.cedyte.content.blocks.abstracts.AbstractRootBlock;
import lv.cebbys.cedyte.content.trees.handlers.abstracts.SimpleTreeHandler;
import lv.cebbys.celib.utilities.CelibBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class OakTreeHandler extends SimpleTreeHandler{

	@Override
	public AbstractBranchBlock getBranchBlock() {
		return DynamicTreeBlocks.OAK_BRANCH;
	}

	@Override
	public AbstractRootBlock getRootBlock() {
		return DynamicTreeBlocks.OAK_ROOT;
	}

	@Override
	public Block getLeafBlock() {
		return Blocks.OAK_LEAVES;
	}

	@Override
	protected BlockEntity createBranchEntity(Direction parentDirection, int stemIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
