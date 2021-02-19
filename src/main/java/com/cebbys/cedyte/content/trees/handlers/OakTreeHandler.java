package com.cebbys.cedyte.content.trees.handlers;

import com.cebbys.cedyte.content.DynamicTreeBlocks;
import com.cebbys.cedyte.content.blocks.abstracts.AbstractBranchBlock;
import com.cebbys.cedyte.content.blocks.abstracts.AbstractRootBlock;
import com.cebbys.cedyte.content.trees.handlers.abstracts.SimpleTreeHandler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

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

}
