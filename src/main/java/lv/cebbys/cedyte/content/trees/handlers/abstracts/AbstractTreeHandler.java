package lv.cebbys.cedyte.content.trees.handlers.abstracts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cebbys.celib.loggers.CelibLogger;
import com.cebbys.celib.utilities.CelibBlockPos;

import lv.cebbys.cedyte.content.blocks.abstracts.AbstractBranchBlock;
import lv.cebbys.cedyte.content.blocks.abstracts.AbstractRootBlock;
import lv.cebbys.cedyte.content.entities.DynamicBranchEntity;
import lv.cebbys.cedyte.content.entities.DynamicRootEntity;
import lv.cebbys.cedyte.content.entities.DynamicStemEntity;
import lv.cebbys.cedyte.content.entities.util.BranchHelper;
import lv.cebbys.cedyte.content.trees.BranchPart;
import lv.cebbys.cedyte.content.trees.BranchStruct;
import lv.cebbys.cedyte.content.trees.StemStruct;
import lv.cebbys.cedyte.content.trees.TreeStruct;
import lv.cebbys.cedyte.utilities.builder.TreeStructureBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractTreeHandler {

	public AbstractTreeHandler() {
	}

	public abstract AbstractBranchBlock getBranchBlock();

	public abstract AbstractRootBlock getRootBlock();

	public abstract Block getLeafBlock();

	public abstract int getStemStage(StemStruct stem);

	protected abstract int getRequiredGrowthAge(int branchFactor);

	protected abstract Block[] getGrowtroughtBlocks();

	protected abstract int getMaxBranchLength(TreeStruct t, BranchStruct branch);

	protected abstract BlockEntity createBranchEntity(Direction parentDirection, int stemIndex);

	public void randomTick(TreeStruct t, Random r, CelibBlockPos ep, World w) {
		this.growBranches(t, ep, w, r);
	}

	private void growBranches(TreeStruct t, CelibBlockPos ep, World w, Random r) {
		List<BranchStruct> branches = t.getBranches();
		for (int i = 0; i < branches.size(); i++) {
			BranchStruct branch = branches.get(i);
			if (branch.getStemCount() < branch.maxLength()) {
				StemStruct bud = branch.getBud();
				Direction newBudDir = BranchHelper.getBudDirection(branch.getBasePosition(), bud.getPos(),
						branch.getStemCount(), branch.getVector());
				StemStruct newStem = null;
				Direction real = this.getStemDirection(bud.getPos(), newBudDir, w, r);
				if (real != null) {
					newStem = StemStruct.forBranchPart(bud.getPos(), real, bud.fac);
					t.addStemToBranch(branch, newStem);
					this.placeStem(t, branch, newStem, w);
				} else {
					t.branches.remove(branch);
				}
			} else {
				// TODO This is very ugly, redo asap.
				if (branch.hasNextStage()) {
					List<BranchPart> parts = branch.getParts();
					if (parts.size() > 1) {
						for (int j = 1; j < parts.size(); j++) {
							StemStruct bud = branch.getBud();
							CelibBlockPos parentPos = bud.getPos();
							BranchStruct newBranch = new BranchStruct(parts.get(j).getData(), parentPos);
							Direction prefered = BranchHelper.getBudDirection(newBranch.getBasePosition(), parentPos,
									newBranch.getStemCount() + 1, newBranch.getVector());
							Direction real = this.getStemDirection(parentPos, prefered, w, r);
							if (real != null) {
								StemStruct stem = StemStruct.forBranchPart(parentPos, real, bud.fac + 1);
								t.addBranch(newBranch);
								t.addStemToBranch(newBranch, stem);
								this.placeStem(t, newBranch, stem, w);
							}
						}
					}
					branch.nextStage(branch.getBud().getPos());
				} else {
					t.branches.remove(branch);
				}
			}
		}
	}

	private Direction getStemDirection(CelibBlockPos pos, Direction prefered, World world, Random random) {
		if (this.isValidDirection(pos, prefered, world)) {
			return prefered;
		} else {
			Direction[] sequence = new Direction[5];
			int index = 0;
			for (Direction d : Direction.values()) {
				if (!d.equals(prefered)) {
					sequence[index] = d;
					index++;
				}
			}
			sequence = this.randomDirectionSequence(random, 4, sequence);
			for (Direction d : sequence) {
				if (this.isValidDirection(pos, d, world)) {
					return d;
				}
			}
		}
		return null;
	}

	private Direction[] randomDirectionSequence(Random r, int iterations, Direction[] dirs) {
		for (int i = 0; i < iterations; i++) {
			int i0 = r.nextInt(dirs.length);
			int i1 = r.nextInt(dirs.length);
			Direction n0 = dirs[i0];
			dirs[i0] = dirs[i1];
			dirs[i1] = n0;
		}
		return dirs;
	}

	/**
	 * @param off StemStruct parent position.
	 * @param dir StemStruct preferred direction.
	 * @param ep  Tree entity position / tree origin point.
	 * @param w   World which contains tree entity.
	 * @return
	 */
	private boolean isValidDirection(CelibBlockPos parentPos, Direction childDir, World world) {
		boolean valid = this.isValidPosition(parentPos.offset(childDir), world);
		return valid;
	}

	/**
	 * @param pos   Position to test if the branch can grow trough that position.
	 * @param world World in which the test has to be done
	 * @return
	 */
	private boolean isValidPosition(BlockPos pos, World world) {
		Block[] blocks = this.getGrowtroughtBlocks();
		Block testable = world.getBlockState(pos).getBlock();
		for (Block block : blocks) {
			if (block == testable) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param stem Stem structure that is going to be placed in world.
	 * @param ep   Tree origin position / Tree entity position.
	 * @param w    World in which part will be placed.
	 */
	private void placeStem(TreeStruct tree, BranchStruct branch, StemStruct stem, World w) {

		int branchLength = branch.getStemCount();
		BlockState state = this.getBranchBlock().getDefaultState().with(AbstractBranchBlock.DIRECTION, stem.getDir());
		DynamicStemEntity entity = (DynamicStemEntity) this.createBranchEntity(stem.getDir().getOpposite(),
				branchLength);
		w.setBlockState(stem.getPos(), state);
		w.setBlockEntity(stem.getPos(), entity);

		DynamicStemEntity parent = (DynamicStemEntity) w.getBlockEntity(entity.getParentPosition());
		if(parent == null) {
			tree.branches.remove(branch);
			return;
		}
		parent.addChild((stem.getDir()));
		parent.sync();

		for (CelibBlockPos p : branch.getStemPositions()) {
			if (w.getBlockEntity(p) == null) {
				tree.branches.remove(branch);
				return;
			}
			DynamicStemEntity e = (DynamicStemEntity) w.getBlockEntity(p);
			if (e != null) {
				e.setBranchLength(branchLength);
				e.sync();
			}
		}
	}
}
