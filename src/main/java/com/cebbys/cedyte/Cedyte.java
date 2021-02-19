package com.cebbys.cedyte;

import com.cebbys.cedyte.content.DynamicTreeBlocks;
import com.cebbys.cedyte.content.DynamicTreeEntities;
import com.cebbys.cedyte.content.DynamicTrees;
import com.cebbys.cedyte.content.resources.CedytePackGenerator;
import com.cebbys.celib.registrators.BlockRenderLayerRegistry;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.FoliageColors;

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
		
//		CedytePackGenerator.generateClientPack();
		
//		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
//			return FoliageColors.getSpruceColor();
//		}, DynamicTreeBlocks.SPRUCE_BRANCH);
		
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			return FoliageColors.getDefaultColor();
		}, DynamicTreeBlocks.OAK_BRANCH);
	}
	
	static {
		MOD_ID = "cedyte";
	}
}
