package lv.cebbys.cedyte.utilities.builder;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.swordglowsblue.artifice.api.builder.JsonObjectBuilder;

import static lv.cebbys.cedyte.api.CedyteKey.*;

public class TreeTemplateParser {

	
	public static JsonObject fromTemplate(JsonObject template) {
		return new Part(template).build();
	}
	
	private static class Part {
		private List<Part> parts;
		private int rotationX;
		private int rotationY;
		private int factor;
		private int length;
		
		public Part(JsonObject data) {
			this.rotationX = data.get(ROT_X.toKey()).getAsInt();
			this.rotationY = data.get(ROT_Y.toKey()).getAsInt();
			this.factor = data.get(FACTOR.toKey()).getAsInt();
			this.length = data.get(LENGTH.toKey()).getAsInt();
			this.parts = new ArrayList<>();
			JsonArray arr = data.get(BRANCHES.toKey()).getAsJsonArray();
			for(JsonElement element : arr) {
				JsonObject child = element.getAsJsonObject();
				this.parts.add(new Part(child, this.rotationX, this.rotationY));
			}
		}
		
		public Part(JsonObject data, int rX, int rY) {
			this.rotationX = data.get(ROT_X.toKey()).getAsInt() + rX;
			this.rotationY = data.get(ROT_Y.toKey()).getAsInt() + rY;
			this.factor = data.get(FACTOR.toKey()).getAsInt();
			this.length = data.get(LENGTH.toKey()).getAsInt();
			this.parts = new ArrayList<>();
			JsonArray arr = data.get(BRANCHES.toKey()).getAsJsonArray();
			for(JsonElement element : arr) {
				JsonObject child = element.getAsJsonObject();
				this.parts.add(new Part(child, this.rotationX, this.rotationY));
			}
		}
		
		public JsonObject build() {
			return new JsonObjectBuilder()
					.add(ROT_X.toKey(), this.rotationX)
					.add(ROT_Y.toKey(), this.rotationY)
					.add(LENGTH.toKey(), this.length)
					.add(FACTOR.toKey(), this.factor)
					.addArray(BRANCHES.toKey(), array -> {
						for(Part part : this.parts) {
							array.add(part.build());
						}
					})
					.build();
		}
	}
}
