package com.cebbys.cedyte.content.trees.structs;

public class DelayedBranchStruct {
	
	public BranchStruct parentBranch;
	public StemStruct parentStem;
	public int delayTime;
	
	public DelayedBranchStruct(BranchStruct parent, StemStruct stem, int delay) {
		this.parentBranch = parent;
		this.parentStem = stem;
		this.delayTime = delay;
	}
}
