package com.cebbys.cedyte.content.trees.structs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.cebbys.celib.loggers.CelibLogger;
import com.cebbys.celib.utilities.CelibBlockPos;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TreeStruct {

	private StemStruct root;
	public HashMap<BlockPos, StemStruct> stems;
	public HashMap<BlockPos, StemStruct> grownStems;
	public ArrayList<BranchStruct> branches;
	public ArrayList<DelayedBranchStruct> delayedBranches;

	public TreeStruct() {
		this.stems = new HashMap<BlockPos, StemStruct>();
		this.grownStems = new HashMap<BlockPos, StemStruct>();
		this.branches = new ArrayList<BranchStruct>();
		this.delayedBranches = new ArrayList<DelayedBranchStruct>();
	}

	public void init(Random r) {
		StemStruct stem = StemStruct.forRoot();
//		double s1 = r.nextDouble() * (3.14 / 4);
//		double s2 = r.nextDouble() * 6.28;
		Vec3d v = new Vec3d(0, 1, 0)/*.rotateX((float)s1).rotateY((float)s2)*/;
		BranchStruct branch = new BranchStruct(v);
		branch.addStem(stem);
		this.root = stem;
		this.stems.put(this.root.off, this.root);
		this.branches.add(branch);
	}
	
	public List<BranchStruct> getBranches() {
		return this.branches;
	}
	
	@Deprecated
	public boolean addStem(StemStruct stem) {
		CelibBlockPos pos = stem.off;
		if(!this.stems.keySet().contains(pos)) {
			this.stems.put(pos, stem);
			return true;
		}
		CelibLogger.log("TreeStruct", "Failed to add stem");
		return false;
	}
	
	public boolean addGrownStem(StemStruct s) {
		CelibBlockPos p = s.off;
		if(!this.grownStems.keySet().contains(p)) {
			this.grownStems.put(p, s);
		}
		return false;
	}
	
	public void addBranch(BranchStruct b) {
		this.branches.add(b);
	}
	
	public void addDelayedBranch(DelayedBranchStruct branch) {
		this.delayedBranches.add(branch);
	}
	
	public StemStruct getStem(CelibBlockPos p) {
		if(this.stems.containsKey(p)) {
			return this.stems.get(p);
		}
		return null;
	}
	
	public StemStruct popStem(CelibBlockPos p) {
		return this.stems.remove(p);
	}

	public Collection<StemStruct> getStems() {
		return this.stems.values();
	}

	public Collection<StemStruct> getAllStems() {
		Collection<StemStruct> all = new ArrayList<StemStruct>();
		all.addAll(this.stems.values());
		all.addAll(this.grownStems.values());
		return all;
	}
	
//	@Deprecated
//	/**
//	 * 
//	 * @param off Branch offset
//	 * @param dir Branch direction
//	 * @param ml Branch maximum length
//	 * @param f Branch factor. If main branch than 0. If branc of a branch (1) of a branch (2)
//	 * so far so on.
//	 * @return new branch structure with stem
//	 */
//	public BranchStruct growNewBranch(CelibBlockPos off, Vec3d dir, int ml, int f) {
//		BranchStruct branchNew = new BranchStruct(off, dir, ml);
//		StemStruct stemNew = StemStruct.forBranchRoot(off, branchNew.getBudDirection(new TreeStruct()), f);
//		branchNew.addStem(stemNew);
//		this.stems.put(off, stemNew);
//		this.branches.add(branchNew);
//		return branchNew;
//	}

//	public void updateTreeStems() {
//		this.stems.clear();
//		this.appendStems(this.stems, this.root);
//	}

//	private void appendStems(HashMap<BlockPos, StemStruct> appendable, StemStruct parent) {
//		appendable.put(parent.off, parent);
//		for (StemStruct child : parent.getChildren()) {
//			this.appendStems(appendable, child);
//		}
//	}

//	private ArrayList<StemStruct> getChildrenStems(StemStruct stem) {
//		ArrayList<StemStruct> list = new ArrayList<StemStruct>();
//		for (StemStruct child : stem.getChildren()) {
//			ArrayList<StemStruct> childChildren = this.getChildrenStems(child);
//			list.addAll(childChildren);
//		}
//		list.add(stem);
//		return list;
//	}
	
//	private void growTree(World world, BlockPos entityPos, Random random) {
//		this.updateTreeStems();
//		for (StemStruct stem : this.stems.values()) {
//			stem.age += 1;
//			stem.setStage(handler.getStemStage(stem));
//		}
//		for (int i = 0; i < this.branches.size(); i++) {
//			BranchStruct branch = this.branches.get(i);
//			StemStruct bud = branch.getBud();
//			boolean budCanGrow = handler.canBranchGrow(branch, bud);
//			if (budCanGrow) {
//				handler.growBranch(branch, this.delayedBranches, entityPos, world, random);
//			}
//		}
//		this.updateDelayedBudding(world, entityPos, random);
//		this.updateTreeBranches();
//	}
	
//	public void updateTreeBranches() {
//		this.branches.clear();
//		BranchStruct mainBranch = new BranchStruct(this.root, new Vec3d(0, 1, 0));
//
//		this.appendBranches(this.branches, mainBranch, this.root);
//		// TODO If stem missing then remove branch, stop growing;
//	}

//	private void appendBranches(ArrayList<BranchStruct> appendable, BranchStruct branch, StemStruct parent) {
//		if (!appendable.contains(branch)) {
//			appendable.add(branch);
//		}
//		int parentChildSize = parent.getChildren().size();
//		if (parentChildSize > 0) {
//			for (int i = 0; i < parentChildSize; i++) {
//				StemStruct child = parent.getChildren().get(i);
//				if (i == 0) {
//					branch.addStem(child);
//					this.appendBranches(appendable, branch, child);
//				} else {
//					Vec3i v = Direction.UP.getVector();
//					BranchStruct newBranch = new BranchStruct(new Vec3d(v.getX(), v.getY(), v.getZ()));
//					newBranch.addStem(child);
//					newBranch.offspringIndex = branch.getStemCount() / 2 - 4;
//					appendable.add(newBranch);
//					this.appendBranches(appendable, newBranch, child);
//				}
//			}
//		}
//	}
}
