package com.cebbys.cedyte.content.entities.tree;

import com.cebbys.celib.utilities.CelibBlockPos;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;

public class DynamicBranchEntity extends BlockEntity {

	private CelibBlockPos rootPosition;
	
	public DynamicBranchEntity(BlockEntityType<?> type, CelibBlockPos root) {
		super(type);
		this.rootPosition = root;
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.put("root_position", NbtHelper.fromBlockPos(this.rootPosition));
		return tag;
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.rootPosition = CelibBlockPos.of(NbtHelper.toBlockPos((CompoundTag) tag.get("root_position")));
	}
	
	public void onStemBroken() {
		DynamicRootEntity entity = (DynamicRootEntity) this.getWorld().getBlockEntity(this.rootPosition);
		entity.onStemBroken(CelibBlockPos.of(this.getPos()));
	}

}
