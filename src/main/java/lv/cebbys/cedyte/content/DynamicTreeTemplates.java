package lv.cebbys.cedyte.content;

import lv.cebbys.cedyte.registry.CedyteRegistries;
import lv.cebbys.cedyte.utilities.builder.TreeTemplate;
import lv.cebbys.cedyte.utilities.builder.TreeTemplateBuilder;

public class DynamicTreeTemplates {

	// Spruce
	public static final TreeTemplate SPRUCE_0;
	
	// Undefined
	public static final TreeTemplate WEIRD_0;
	
	static {
		SPRUCE_0 = CedyteRegistries.registerTreeTemplate("spruce", "spruce", TreeTemplateBuilder.getSpruce());
		WEIRD_0 = CedyteRegistries.registerTreeTemplate("weird", "acacia", TreeTemplateBuilder.getWeird());
	}
}
