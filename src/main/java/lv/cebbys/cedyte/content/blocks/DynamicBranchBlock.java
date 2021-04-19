package lv.cebbys.cedyte.content.blocks;

import java.util.function.Supplier;

import lv.cebbys.cedyte.Cedyte;
import lv.cebbys.cedyte.content.blocks.abstracts.AbstractBranchBlock;
import lv.cebbys.cedyte.content.entities.DynamicBranchEntity;
import lv.cebbys.celib.registrators.BlockRegistry;
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
