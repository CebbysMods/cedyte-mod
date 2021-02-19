package com.cebbys.cedyte.content.trees.structs;

import java.util.ArrayList;

import com.cebbys.celib.loggers.CelibLogger;
import com.cebbys.celib.utilities.CelibBlockPos;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class StemStruct {
	public CelibBlockPos off;
	public Direction dir;
	
	private int age;
	public int fac;
	private int stage;
	
	// Has to have this for model creation
	private ArrayList<StemStruct> children;



	public static StemStruct forRoot() {
		return new StemStruct(new CelibBlockPos(0, 0, 0), Direction.UP, Direction.UP, 0, 0);
	}
	
	/**
	 * @param off Branch parent stem offset
	 * @param dir Part direction
	 * @param factor Branch factor
	 * @return
	 */
	public static StemStruct forBranchRoot(CelibBlockPos off, Direction dir, int factor) {
		return new StemStruct(off, dir, 0, factor);
	}

	
	/**
	 * @param off Offset of a stem part from which new stem will grow
	 * @param dir Part direction
	 * @param factor Branch factor
	 * @return
	 */
	public static StemStruct forBranchPart(CelibBlockPos off, Direction dir, int factor) {
		return new StemStruct(off.offset(dir), dir, 0, factor);
	}
	
	public StemStruct(CelibBlockPos off, Direction dir, int age, int recursionFactor) {
		this.children = new ArrayList<StemStruct>();
		this.off = off;
		this.dir = dir;
		this.fac = recursionFactor;
		this.age = age;
	}

	public StemStruct(BlockPos off, Direction dir, Direction prefDir, int age, int recursionFactor) {
		this.children = new ArrayList<StemStruct>();
		this.off = CelibBlockPos.of(off);
		this.dir = dir;
		this.fac = recursionFactor;
		this.age = age;
	}

	public StemStruct(StemStruct parent, Direction dir) {
		this(parent.off.offset(dir), dir, dir, 0, parent.fac);
	}

	public StemStruct(StemStruct parent, Direction dir, int factor) {
		this(parent.off.offset(dir), dir, dir, 0, factor);
	}

	public void addChild(StemStruct child) {
		this.addChild(this.children.size(), child);
	}
	
	
	public int getAge() {
		return this.age;
	}
	
	public void setAge(int a) {
		this.age = a;
	}
	
	/**@return Returns stem growth stage */
	public int getStage() {
		return this.stage;
	}

	/**@param s Sets stems growth stage with constraints from 0 - 7 */
	public void setStage(int s) {
		this.stage = Math.max(0, Math.min(7, s));
	}

	public void addChild(int index, StemStruct child) {
		if (child != null) {
			boolean contains = false;
			for (StemStruct stem : this.children) {
				if (stem.dir == child.dir) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				this.children.add(index, child);
			}
		} else {
			CelibLogger.log("TreeStructure", "Child is null");
		}
	}

	public ArrayList<StemStruct> getChildren() {
		return this.children;
	}

	@Override
	public String toString() {
		String ret = "Stem:{" + "offset:[ " + off.getX() + ", " + off.getY() + ", " + off.getZ() + " ], " + "direction:"
				+ dir.toString() + ", " + "age:" + String.valueOf(this.age) + ", " + "factor:"
				+ String.valueOf(this.fac) + ", " + "stage:" + String.valueOf(this.stage) + ", " + "children:{count:"
				+ String.valueOf(this.children.size()) + "}" + "}";
		return ret;
	}
}
