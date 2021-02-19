package com.cebbys.cedyte.content.trees.handlers.abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cebbys.cedyte.content.blocks.abstracts.AbstractBranchBlock;
import com.cebbys.cedyte.content.blocks.abstracts.AbstractRootBlock;
import com.cebbys.cedyte.content.trees.structs.BranchStruct;
import com.cebbys.cedyte.content.trees.structs.DelayedBranchStruct;
import com.cebbys.cedyte.content.trees.structs.StemStruct;
import com.cebbys.cedyte.content.trees.structs.TreeStruct;
import com.cebbys.celib.loggers.CelibLogger;
import com.cebbys.celib.utilities.CelibBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractTreeHandler {
	
	private List<StemStruct> thickenable;
	
	public AbstractTreeHandler() {
		this.thickenable = new ArrayList<StemStruct>();
	}
	
	public abstract AbstractBranchBlock getBranchBlock();
	public abstract AbstractRootBlock getRootBlock();
	public abstract Block getLeafBlock();
	public abstract int getStemStage(StemStruct stem);
	public abstract Direction getPreferedStemDirection(TreeStruct t, BranchStruct branch, StemStruct parent, Random random);
	protected abstract int getRequiredGrowthAge(int branchFactor);
	protected abstract Block[] getGrowtroughtBlocks();
	protected abstract int getMaxBranchLength(TreeStruct t, BranchStruct branch);
	
	public void randomTick(TreeStruct t, Random r, CelibBlockPos ep, World w) {
		CelibLogger.log("AbstractHandler", "Branch count: " + t.getBranches().size());
		CelibLogger.log("AbstractHandler", "Stem count: " + t.getStems().size());
		this.ageTree(t);
		this.growBranches(t, ep, w, r);
		this.growDelayedBranches(t, ep, w, r);
	}
	
	private void growDelayedBranches(TreeStruct t, CelibBlockPos ep, World w, Random r) {
		for(int i = 0; i < t.delayedBranches.size(); i++) {
			DelayedBranchStruct d = t.delayedBranches.get(i);
			if(d.delayTime >= 0) {
				if(t.addStem(d.parentStem)) {
					t.addBranch(d.parentBranch);
					this.placeStem(d.parentStem, ep, w);
				} 
				t.delayedBranches.remove(d);
			} else {
				d.delayTime += 1;
			}
		}
	}

	public void tick(CelibBlockPos ep, World w) {
		this.thickenStems(ep, w);
	}

	private void ageTree(TreeStruct tree) {
		tree.getStems().forEach(stem -> {
			stem.setAge(stem.getAge() + 1);
			int oldStage = stem.getStage();
			int newStage = this.getStemStage(stem);
			if(oldStage < newStage) {
				stem.setStage(newStage);
				this.thickenable.add(stem);
			}
		});
	}
	
	private void growBranches(TreeStruct t, CelibBlockPos ep, World w, Random r) {
		for(int i = 0; i < t.getBranches().size(); i++) {
			BranchStruct branch = t.getBranches().get(i);
			if(this.canBranchGrow(t, branch)) {
				this.growBranch(t, branch, ep, w, r);
			} else {
				// Try growing other directions
				if(true) {
//					branch.stemPositions.forEach(pos -> {
//						StemStruct grown = t.popStem(pos);
//						t.addGrownStem(grown);
//					});
//					t.branches.remove(branch);
//					return;
				}
			}
			if(this.canBranchBud(t, branch)) {
				this.growDelayedBranch(t, branch, ep, w, r);
			}
		}
	}

	private void growBranch(TreeStruct t, BranchStruct b, CelibBlockPos ep, World w, Random r) {
		StemStruct bud = b.getBud(t);
		Direction dir = b.getBudDirection(t);
		if(this.isValidDirection(bud.off, dir, ep, w)) {
			// Create new StemStruct stemNew
			// Add stemNew to branch stems
			// Add stemNew to tree stems
			// Place stem in world
			StemStruct stemNew = StemStruct.forBranchPart(bud.off, dir, bud.fac);
			b.addStem(stemNew);
			t.addStem(stemNew);
			this.placeStem(stemNew, ep, w);
		}
	}
	
	private void growDelayedBranch(TreeStruct t, BranchStruct b, CelibBlockPos ep, World w, Random r) {
		if(r.nextFloat() > 0.1) {
			int branchFac = b.getBud(t).fac;
			if(branchFac == 0) {
				int branchCount = 2 + r.nextInt(3);
				float maxAngle = (float) (Math.PI * 2.0 / branchCount);
				float rotY = (float) (Math.PI * 2.0 * r.nextDouble() + (Math.PI / 6.0));
				for(int i = 0; i < branchCount; i++) {
					float rotX = (float) (r.nextDouble() * Math.PI / 10.0);
					rotY += (float) (maxAngle * (1 + r.nextDouble()) / 2.0);
					Vec3d newVec = new Vec3d(0, 0, 1).rotateX(rotX).rotateY(rotY);
					BranchStruct branchNew = new BranchStruct(newVec);
					StemStruct stemNew = StemStruct.forBranchPart(b.getBud(t).off, branchNew.getBudDirection(t), 1);
					branchNew.addStem(stemNew);
					DelayedBranchStruct delay = new DelayedBranchStruct(branchNew, stemNew, - (4 + r.nextInt(7)));
					t.addDelayedBranch(delay);
				}
			}
		}
	}

	public boolean canBranchGrow(TreeStruct t, BranchStruct b) {
		StemStruct bud = b.getBud(t);
		int reqAge = this.getRequiredGrowthAge(bud.fac);
		int budAge = bud.getAge();
		int maxLen = this.getMaxBranchLength(t, b);
		int stemCount = b.getStemCount();
		return ((budAge >= reqAge) && (stemCount < maxLen));
	}

	public boolean canBranchBud(TreeStruct t, BranchStruct branch) {
		return branch.getStemCount() > 3;
	}
	
	public StemStruct getStemFromPos(CelibBlockPos pos, TreeStruct t, CelibBlockPos ep) {
		return t.getStem(pos.sub(ep));
	}

	/**
	 * @param off StemStruct parent offset.
	 * @param dir StemStruct preferred direction.
	 * @param ep Tree entity position / tree origin point.
	 * @param w World which contains tree entity.
	 * @return
	 */
	private boolean isValidDirection(CelibBlockPos off, Direction dir, CelibBlockPos ep, World w) {
		return this.isValidPosition(off.offset(dir).add(ep), w);
	}

	/**
	 * @param pos Position to test if the branch can grow trough that position.
	 * @param world World in which the test has to be done
	 * @return
	 */
	private boolean isValidPosition(BlockPos pos, World world) {
		Block[] blocks = this.getGrowtroughtBlocks();
		if (blocks != null) {
			Block testable = world.getBlockState(pos).getBlock();
			for (Block block : blocks) {
				if (block == testable) {
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * @param stem Stem structure that is going to be placed in world.
	 * @param ep Tree origin position / Tree entity position.
	 * @param w World in which part will be placed.
	 */
	private void placeStem(StemStruct stem, CelibBlockPos ep, World w) {
		BlockState state = this.getBranchBlock().getDefaultState();
		state = state.with(AbstractBranchBlock.DIRECTION, stem.dir);
		state = state.with(AbstractBranchBlock.GROWTH_STAGE, stem.getStage());
		w.setBlockState(stem.off.add(ep), state);
	}
	
	private void updateStemStage(StemStruct s, CelibBlockPos ep, World w) {
		CelibBlockPos pos = ep.add(s.off);
		BlockState state = w.getBlockState(pos);
		if(state.getBlock() instanceof AbstractBranchBlock) {
			w.setBlockState(pos, state.with(AbstractBranchBlock.GROWTH_STAGE, s.getStage()));
			return;
		}
		if(state.getBlock() instanceof AbstractRootBlock) {
			w.setBlockState(pos, state.with(AbstractRootBlock.GROWTH_STAGE, s.getStage()));
			return;
		}
		throw new RuntimeException("Block position " + pos.toString() + " does not contain tree block");
	}
	
	/**
	 * @param ep Tree entity position / tree origin point.
	 * @param w World which contains tree entity.
	 */
	private void thickenStems(CelibBlockPos ep, World w) {
		if(this.thickenable.size() > 1) {
			int size = 1 + (int) Math.sqrt(this.thickenable.size());
			for(int i = 0; i < size; i++) {
				StemStruct thicken = this.thickenable.remove(0);
				this.updateStemStage(thicken, ep, w);
			}
		}
	}

	public void onStemBroken(CelibBlockPos off) {
		CelibLogger.log("AbstractHandler", "Ahah: " + off.toString());
	}
	
	// TODO Give this method a normal name
//		private ArrayList<Direction> someRandomDirections(Direction failed, Random random) {
//			if (failed.getAxis() == Axis.Y) {
//				ArrayList<Direction> possible = CelibArrays.getArrayList(Direction.class);
//				possible.remove(failed);
//				possible.remove(failed.getOpposite());
//				Direction d0 = possible.remove(random.nextInt(4));
//				possible.remove(d0.getOpposite());
//				Direction d1 = possible.remove(random.nextInt(2));
//				possible.remove(d1);
//				possible.add(d1);
//				possible.add(d0);
//				possible.add(d0.getOpposite());
//				possible.add(failed.getOpposite());
//				return possible;
//			} else {
//				ArrayList<Direction> possible = CelibArrays.getArrayList(Direction.class);
//				possible.remove(failed);
//				possible.remove(failed.getOpposite());
//				possible.remove(Direction.DOWN);
//				possible.remove(Direction.UP);
//				Direction d0 = possible.remove(random.nextInt(2));
//				possible.add(d0);
//				possible.add(Direction.UP);
//				possible.add(Direction.DOWN);
//				possible.add(failed.getOpposite());
//				return possible;
//			}
//		}

//	/**
//	 * @param t 
//	 * @param branch Stems branch
//	 * @param parent Stem to whom will be added child
//	 * @param pos    Tree block entity position
//	 * @param world  ServerWorld
//	 * @return Stem child
//	 */
//	protected boolean tryGrowingNewStem(TreeStruct t, BranchStruct branch, StemStruct parent, CelibBlockPos pos, World world,
//			Random random) {
//		Direction prefered = this.getPreferedStemDirection(t, branch, parent, random);
//		if (this.isValidDirection(parent.off, prefered, pos, world)) {
//			StemStruct child = new StemStruct(parent, branch.getBudDirection(t));
//			parent.addChild(child);
//			this.placeStem(child, pos, world);
//			return true;
//		} else {
//			ArrayList<Direction> list = this.someRandomDirections(prefered, random);
//			for (Direction dir : list) {
//				if (this.isValidDirection(parent.off, dir, pos, world)) {
//					StemStruct child = new StemStruct(parent, dir);
//					parent.addChild(child);
//					this.placeStem(child, pos, world);
//					return true;
//				}
//			}
//		}
//		return false;
//	}

//	protected StemStruct tryGrowingNewBud(Direction prefered, StemStruct parent, CelibBlockPos pos, World world,
//			Random random) {
//		if (this.isValidDirection(parent.off, prefered, pos, world)) {
//			StemStruct child = new StemStruct(parent, prefered, parent.fac + 1);
//			this.placeStem(child, pos, world);
//			return child;
//		} else {
//			ArrayList<Direction> list = this.someRandomDirections(prefered, random);
//			list.remove(Direction.DOWN);
//			list.remove(Direction.UP);
//			for (Direction dir : list) {
//				if (this.isValidDirection(parent.off, dir, pos, world)) {
//					StemStruct child = new StemStruct(parent, dir, parent.fac + 1);
//					this.placeStem(child, pos, world);
//					return child;
//				}
//			}
//		}
//		return null;
//	}
}
