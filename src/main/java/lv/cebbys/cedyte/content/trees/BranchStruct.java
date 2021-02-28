package lv.cebbys.cedyte.content.trees;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cebbys.celib.loggers.CelibLogger;
import com.cebbys.celib.utilities.CelibBlockPos;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.math.Vec3d;

public class BranchStruct {

	private Set<CelibBlockPos> stemPositions;
	private CelibBlockPos branchBasePosition;
	private BranchPart branchPart;
	private StemStruct branchBud;
	private int branchFactor;
	
	private JsonObject data;

	public BranchStruct(JsonObject data, CelibBlockPos pos) {
		this.stemPositions = new HashSet<>();
		this.branchPart = new BranchPart(data);
		this.branchFactor = data.get("factor").getAsInt();
		this.branchBasePosition = pos;
	}
	
	public BranchStruct(BranchStruct branch) {
		this.stemPositions = new HashSet<>();
	}

	public Vec3d getVector() {
		return this.branchPart.getVector();
	}
	
	public StemStruct getBud() {
		return this.branchBud;
	}

	public void addStem(StemStruct stem) {
		this.stemPositions.add(stem.getPos());
		this.branchBud = stem;
	}

	public int getStemCount() {
		return this.stemPositions.size();
	}
	
	public int maxLength() {
		return this.branchPart.maxLength();
	}
	
	public int getFactor() {
		return this.branchFactor;
	}

	public Set<CelibBlockPos> getStemPositions() {
		return this.stemPositions;
	}
	
	public boolean hasNextStage() {
		return this.branchPart.hasNextStage();
	}
	
	public void nextStage(CelibBlockPos pos) {
		this.branchPart.nextStage();
		this.branchBasePosition = pos;
	}
	
	public List<BranchPart> getParts() {
		return this.branchPart.getParts();
	}
	
	public CelibBlockPos getBasePosition() {
		return this.branchBasePosition;
	}
}
