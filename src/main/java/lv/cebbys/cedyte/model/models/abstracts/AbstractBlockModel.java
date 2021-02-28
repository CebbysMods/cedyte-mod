package lv.cebbys.cedyte.model.models.abstracts;

import java.util.List;
import java.util.Random;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.math.Direction;

public abstract class AbstractBlockModel implements UnbakedModel, BakedModel, FabricBakedModel {
	
	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
		return null;
	}
	
	@Override
	public boolean isBuiltin() {
		return false;
	}

}
