package com.cebbys.cedyte.content.trees.structs;

import java.util.ArrayList;
import java.util.List;

import com.cebbys.celib.loggers.CelibLogger;
import com.cebbys.celib.utilities.CelibBlockPos;
import com.cebbys.celib.utilities.CelibDirections;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class BranchStruct {
	
	// OLD
	public boolean branchCut;
	
	public int branchAge;
	public int offspringIndex;
	
	// NEW
	public Vec3d directionVector;
	public List<CelibBlockPos> stemPositions;
	
	public BranchStruct(Vec3d vec) {
		this.stemPositions = new ArrayList<CelibBlockPos>();
		this.branchCut = false;
		this.branchAge = 0;
		this.offspringIndex = 0;
		this.directionVector = vec;
	}
	
	/**
	 * @param ml Max branch length, to limit the length of the branch.
	 * @param bv Branch growth vector. This is used to determine direction of new stems.
	 * 
	 * @author CebbyS
	 */
	public BranchStruct(CelibBlockPos off, Vec3d bv, int ml) {
		this.stemPositions = new ArrayList<CelibBlockPos>();
		this.directionVector = bv;
	}

	public StemStruct getBud(TreeStruct t) {
		int size = this.stemPositions.size();
		if(size >= 1) {
			return t.getStem(this.stemPositions.get(size - 1));
		}
		throw new RuntimeException("Stem position count cant be empty!");
	}

	public StemStruct getStem(TreeStruct t, int index) {
		if(index >= 0 && index < this.stemPositions.size()) {
			CelibBlockPos off = this.stemPositions.get(0);
			return t.getStem(off);
		}
		return null;
	}
	
	public void addStem(StemStruct stem) {
		this.addStem(this.stemPositions.size(), stem);
	}
	
	public void addStem(int index, StemStruct stem) {
		if(!this.stemPositions.contains(stem.off)) {
			this.stemPositions.add(stem.off);
		}
		else {
			CelibLogger.log("BranchStructure", "Tried to add duplicate stem...");
		}
	}
	
	public int getStemCount() {
		return this.stemPositions.size();
	}
	
	public boolean hasStem(StemStruct stem) {
		return this.stemPositions.contains(stem.off);
	}
	
	public Direction getBudDirection(TreeStruct t) {
		if(this.getStemCount() < 1) {
			return CelibDirections.toDirection(this.directionVector);
		} else {
			StemStruct stem = this.getBud(t);
			Vec3d origin = CelibBlockPos.toVec3d(this.getStem(t, 0).off);
			Vec3d vec0 = this.directionVector.multiply(this.getStemCount() + 1);
			Vec3d vec1 = CelibBlockPos.toVec3d(stem.off).subtract(origin);
//			CelibLogger.log("BranchStruct", "Direction vector: " + vec0.toString() + " branch vector: " + vec1.toString());
			return CelibDirections.toDirection(vec0.subtract(vec1));
		}
	}
}
