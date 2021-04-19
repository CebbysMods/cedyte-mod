package lv.cebbys.cedyte.registry;

import com.google.gson.JsonObject;
import com.mojang.serialization.Lifecycle;

import lv.cebbys.cedyte.Cedyte;
import lv.cebbys.cedyte.utilities.builder.TreeTemplate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CedyteRegistries {
	
	public static final Registry<Object> TREE_HANDLER;
	public static final Registry<TreeTemplate> TREE_TEMPLATE;
	
	public static TreeTemplate registerTreeTemplate(String id, String tree, JsonObject template) {
		return Registry.register(TREE_TEMPLATE, new Identifier(Cedyte.MOD_ID, id), new TreeTemplate(tree, template));
	}	
	
	static {
		TREE_HANDLER = RegistryBuilder.createRegistry("tree_handlers", Lifecycle.stable());
		TREE_TEMPLATE = RegistryBuilder.createRegistry("tree_templates", Lifecycle.stable());
	}
}
