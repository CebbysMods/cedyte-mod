package lv.cebbys.cedyte.content.blocks;

import java.util.function.Supplier;

import com.cebbys.celib.registrators.BlockRegistry;

import lv.cebbys.cedyte.Cedyte;
import lv.cebbys.cedyte.content.blocks.abstracts.AbstractRootBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public final class DynamicRootBlock extends AbstractRootBlock {

	public DynamicRootBlock(String blockName, Supplier<? extends BlockEntity> entity) {
		super(entity);
		BlockRegistry.registerBlockWithItem(this, Cedyte.MOD_ID, blockName, RenderLayer.getCutoutMipped(),
				new Item.Settings().group(ItemGroup.BUILDING_BLOCKS).maxCount(64));
	}
}
