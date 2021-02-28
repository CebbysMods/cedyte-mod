package lv.cebbys.cedyte.content.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.cebbys.celib.loggers.CelibLogger;
import com.cebbys.celib.utilities.CelibBlockPos;

import lv.cebbys.cedyte.model.utils.StemMeshBuilder;
import lv.cebbys.cedyte.utilities.CedyteNbtHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.Direction;

public abstract class DynamicStemEntity extends BlockEntity implements BlockEntityClientSerializable {

	private Set<Direction> children;
	private Direction parent;
	private int stemIndex;
	private int branchLength;
	private int maxLength;
	
	
	public DynamicStemEntity(BlockEntityType<?> type, Direction parent, Set<Direction> children, int stemIndex, int branchLength) {
		super(type);
		this.parent = parent;
		this.children = children;
		this.stemIndex = stemIndex;
		this.branchLength = branchLength;
	}
	
	protected DynamicStemEntity(BlockEntityType<?> type, Direction parent, int stemIndex, int branchLength) {
		this(type, parent, new TreeSet<>(), stemIndex, branchLength);
	}
	
	protected DynamicStemEntity(BlockEntityType<?> type) {
		this(type, Direction.DOWN, 0, 1);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("stem_index", this.stemIndex);
		tag.putInt("branch_length", this.branchLength);
		tag.putString("parent_direction", this.parent.toString());
		tag.put("child_directions", CedyteNbtHelper.fromDirectionSet(this.children));
		return tag;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.stemIndex = tag.getInt("stem_index");
		this.branchLength = tag.getInt("branch_length");
		this.parent = Direction.byName(tag.getString("parent_direction"));
		this.children = CedyteNbtHelper.toDirectionSet(tag.getCompound("child_directions"));
	}
	
	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(null, tag);
		this.reloadModel();
	}
	
	public void setBranchLength(int l) {
		this.branchLength = l;
		this.markDirty();
	}
	
	public int getBranchIndex() {
		return this.stemIndex;
	}
	
	public int getBranchLength() {
		return this.branchLength;
	}
	
	public void addChild(Direction d) {
		if(d != this.parent) {
			this.children.add(d);
			this.markDirty();
		}
	}
	
	public Direction getDirection() {
		return this.parent.getOpposite();
	}
	
	public CelibBlockPos getParentPosition() {
		return CelibBlockPos.of(this.getPos()).offset(this.parent);
	}
	
	public Set<Direction> getBranchingDirections() {
		Set<Direction> set = new HashSet<>();
		set.addAll(this.children);
		set.add(this.parent);
		return set;
	}
	
	@Override
	public CelibBlockPos getPos() {
		return CelibBlockPos.of(super.getPos());
	}
	
	@Environment(EnvType.CLIENT)
	@SuppressWarnings("resource")
	private void reloadModel() {
		MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
	}
}
