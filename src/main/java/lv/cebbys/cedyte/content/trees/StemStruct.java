package lv.cebbys.cedyte.content.trees;

import java.util.ArrayList;

import lv.cebbys.celib.loggers.CelibLogger;
import lv.cebbys.celib.utilities.CelibBlockPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class StemStruct {
	public CelibBlockPos stemPosition;
	public Direction stemDirection;
	
	private int age;
	public int fac;
	private int stage;
	
	// Has to have this for model creation... Later... Really?
	private ArrayList<StemStruct> children;



	public static StemStruct forRoot(CelibBlockPos pos) {
		return new StemStruct(pos, Direction.UP, 0, 0);
	}
	
	/**
	 * @param off Offset of a stem part from which new stem will grow
	 * @param dir Part direction
	 * @param factor Branch factor
	 * @return
	 */
	public static StemStruct forBranchPart(CelibBlockPos pos, Direction dir, int factor) {
		return new StemStruct(pos.offset(dir), dir, 0, factor);
	}
	
	private StemStruct(CelibBlockPos pos, Direction dir, int age, int recursionFactor) {
		this.children = new ArrayList<StemStruct>();
		this.stemPosition = pos;
		this.stemDirection = dir;
		this.fac = recursionFactor;
		this.age = age;
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
				if (stem.stemDirection == child.stemDirection) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				this.children.add(index, child);
			}
		} else {
			CelibLogger.log("Child is null");
		}
	}

	public ArrayList<StemStruct> getChildren() {
		return this.children;
	}
	
	public CelibBlockPos getPos() {
		return this.stemPosition;
	}
	
	public Direction getDir() {
		return this.stemDirection;
	}
}
