package lv.cebbys.cedyte.content.trees;

import java.util.ArrayList;
import java.util.List;

import com.cebbys.celib.loggers.CelibLogger;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static lv.cebbys.cedyte.api.CedyteKey.*;
import net.minecraft.util.math.Vec3d;

public class BranchPart {
	private Vec3d vector;
	private int maxPartCount;
	private List<BranchPart> parts;
	private JsonObject data;

	public BranchPart(JsonObject data) {
		this.data = data;
		this.parts = new ArrayList<>();
		for (JsonElement element : data.get(BRANCHES.toKey()).getAsJsonArray()) {
			JsonObject part = element.getAsJsonObject();
			parts.add(new BranchPart(part));
		}
		this.maxPartCount = data.get("length").getAsInt();
		float ax = (float) (data.get(ROT_X.toKey()).getAsInt() * Math.PI / 180.0);
		float ay = (float) (data.get(ROT_Y.toKey()).getAsInt() * Math.PI / 180.0);
		this.vector = new Vec3d(0, 1, 0).rotateX(ax).rotateY(ay);
	}
	
	public JsonObject getData() {
		return this.data;
	}

	public int maxLength() {
		return this.maxPartCount;
	}

	public Vec3d getVector() {
		return this.vector;
	}
	
	public boolean hasNextStage() {
		return this.parts.size() > 0;
	}
	
	public List<BranchPart> getParts() {
		return this.parts;
	}
	
	public void nextStage() {
		if(this.parts.size() > 0) {
			BranchPart part = this.parts.get(0);
			this.maxPartCount += part.maxLength();
			this.vector = part.getVector();
			this.parts = part.getParts();
		}
	}

	/**
	 * Creates rotation vector from branch data
	 */
	private Vec3d fromRotations(JsonObject data) {
		float ax = (float) (data.get("rotationX").getAsInt() * Math.PI / 180.0);
		float ay = (float) (data.get("rotationY").getAsInt() * Math.PI / 180.0);
		Vec3d v = new Vec3d(0, 1, 0);
		return v.rotateX(ax).rotateY(ay);
	}
}
