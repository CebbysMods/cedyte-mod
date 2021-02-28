package lv.cebbys.cedyte.model;

import org.jetbrains.annotations.Nullable;

import lv.cebbys.cedyte.Cedyte;
import lv.cebbys.cedyte.model.models.DynamicLogBlockModel;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

public class DynamicSpruceModelProvider implements ModelResourceProvider {

	@Override
	public @Nullable UnbakedModel loadModelResource(Identifier id, ModelProviderContext context)
			throws ModelProviderException {
        if(id.equals(new Identifier(Cedyte.MOD_ID, "block/tree/spruce_root"))) {
            return new DynamicLogBlockModel(new Identifier("minecraft", "block/spruce_log"));
        } else if (id.equals(new Identifier(Cedyte.MOD_ID, "block/tree/spruce_branch"))) {
            return new DynamicLogBlockModel(new Identifier("minecraft", "block/spruce_log"));
        } else {
            return null;
        }
	}

}
