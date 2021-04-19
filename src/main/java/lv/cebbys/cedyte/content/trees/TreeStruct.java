package lv.cebbys.cedyte.content.trees;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonObject;

import lv.cebbys.cedyte.content.DynamicTreeTemplates;
import lv.cebbys.cedyte.utilities.builder.TreeTemplateBuilder;
import lv.cebbys.cedyte.utilities.builder.TreeTemplateParser;
import lv.cebbys.celib.loggers.CelibLogger;
import lv.cebbys.celib.utilities.CelibBlockPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TreeStruct {

	public Map<BlockPos, StemStruct> stems;
	public List<CelibBlockPos> stemPositions;
	public List<BranchStruct> branches;
	private CelibBlockPos rootPosition;

	public TreeStruct() {
		this.stems = new TreeMap<BlockPos, StemStruct>();
		this.branches = new ArrayList<BranchStruct>();
	}

	public void init(Random r, CelibBlockPos pos) {
		double s1 = r.nextDouble() * (3.14 / 4);
		double s2 = r.nextDouble() * 6.28;
		Vec3d v = new Vec3d(0, 1, 0).rotateX((float)s1).rotateY((float)s2);
		JsonObject template = DynamicTreeTemplates.WEIRD_0.getTemplate();
		BranchStruct branch = new BranchStruct(TreeTemplateParser.fromTemplate(template), pos);
		StemStruct stem = StemStruct.forRoot(pos);
		this.addBranch(branch);
		this.addStemToBranch(branch, stem);
		this.rootPosition = pos;
	}
	
	public List<BranchStruct> getBranches() {
		return this.branches;
	}
	
	public boolean addStemToBranch(BranchStruct branch, StemStruct stem) {
		CelibBlockPos pos = stem.getPos();
		if(!this.stems.keySet().contains(pos)) {
			if(this.branches.contains(branch)) {
				branch.addStem(stem);
				this.stems.put(pos, stem);
				return true;
			}
		}
		return false;
	}
	
	public void addBranch(BranchStruct b) {
		this.branches.add(b);
	}

	public Collection<StemStruct> getStems() {
		return this.stems.values();
	}
}
