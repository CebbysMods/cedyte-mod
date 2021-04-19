package lv.cebbys.cedyte;

import com.google.gson.JsonObject;
import com.sun.jna.platform.unix.X11.XDeviceByReference;

import lv.cebbys.cedyte.content.DynamicTreeBlocks;
import lv.cebbys.cedyte.content.DynamicTreeEntities;
import lv.cebbys.cedyte.content.DynamicTrees;
import lv.cebbys.cedyte.content.resources.CedytePackGenerator;
import lv.cebbys.cedyte.model.DynamicSpruceModelProvider;
import lv.cebbys.cedyte.registry.CedyteRegistries;
import lv.cebbys.cedyte.utilities.builder.TreeTemplate;
import lv.cebbys.cedyte.utilities.builder.TreeTemplateBuilder;
import lv.cebbys.cedyte.utilities.builder.TreeTemplateParser;
import lv.cebbys.celib.loggers.CelibLogger;
import lv.cebbys.celib.registrators.BlockRenderLayerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Cedyte implements ModInitializer, ClientModInitializer {

	public static final String MOD_ID;

	@Override
	public void onInitialize() {
		new DynamicTreeBlocks();
		new DynamicTreeEntities();
		new DynamicTrees();
	}

	@Override
	public void onInitializeClient() {
		BlockRenderLayerRegistry.registerLayersToInstance();
		BlockRenderLayerRegistry.clearRenderLayerRegistry();
		
		CedytePackGenerator.generateClientPack();
		
//		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
//			return FoliageColors.getSpruceColor();
//		}, DynamicTreeBlocks.SPRUCE_BRANCH);
		
		ModelLoadingRegistry.INSTANCE.registerResourceProvider(rp -> new DynamicSpruceModelProvider());
		
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			return FoliageColors.getDefaultColor();
		}, DynamicTreeBlocks.OAK_BRANCH);
	}
	
	static {
		MOD_ID = "cedyte";
	}
}
