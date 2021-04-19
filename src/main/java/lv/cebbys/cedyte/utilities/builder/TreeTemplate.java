package lv.cebbys.cedyte.utilities.builder;

import com.google.gson.JsonObject;

public class TreeTemplate {
	private final JsonObject template;
	private final String tree;
	
	public TreeTemplate(String tree, JsonObject template) {
		this.template = template;
		this.tree = tree;
	}
	
	public boolean isFor(String tree) {
		return this.tree.equals(tree);
	}
	
	public JsonObject getTemplate() {
		return this.template;
	}
}
