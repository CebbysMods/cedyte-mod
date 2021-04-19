package lv.cebbys.cedyte.content.entities;

import java.util.Set;
import java.util.TreeSet;

import lv.cebbys.cedyte.content.trees.StemStruct;
import lv.cebbys.celib.loggers.CelibLogger;
import lv.cebbys.celib.utilities.CelibBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DynamicBranchEntity extends DynamicStemEntity {
	
	public DynamicBranchEntity(BlockEntityType<?> type, Direction parent, int index, int length) {
		super(type, parent, index, length);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		return tag;
	}
	
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
	}
	
	public void onStemBroken() {
	}
}
