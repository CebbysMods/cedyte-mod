package com.cebbys.cedyte.content.blocks;

import java.util.function.Supplier;

import com.cebbys.cedyte.Cedyte;
import com.cebbys.cedyte.content.blocks.abstracts.AbstractBranchBlock;
import com.cebbys.cedyte.content.entities.tree.DynamicBranchEntity;
import com.cebbys.celib.registrators.BlockRegistry;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public final class DynamicBranchBlock extends AbstractBranchBlock {

	public DynamicBranchBlock(String blockName, Supplier<? extends DynamicBranchEntity> entity) {
		super(entity);
		BlockRegistry.registerBlockWithItem(this, Cedyte.MOD_ID, blockName, RenderLayer.getCutoutMipped(),
				new Item.Settings().group(ItemGroup.BUILDING_BLOCKS).maxCount(64));
	}

}
