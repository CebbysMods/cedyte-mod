package lv.cebbys.cedyte.utilities;

import java.util.Set;
import java.util.TreeSet;

import com.cebbys.celib.utilities.CelibBlockPos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.util.math.Direction;

public class CedyteNbtHelper {
	public static Tag fromBlockPosSet(Set<CelibBlockPos> childrenPos) {
		CompoundTag tag = new CompoundTag();
		childrenPos.forEach(pos -> {
			tag.put(String.valueOf(tag.getSize()), NbtHelper.fromBlockPos(pos));
		});
		return tag;
	}

	public static Set<CelibBlockPos> toBlockPosSet(CompoundTag tag) {
		Set<CelibBlockPos> set = new TreeSet<>();
		for(int i = 0; i < tag.getSize(); i++) {
			set.add(CelibBlockPos.of(NbtHelper.toBlockPos(tag.getCompound(String.valueOf(i)))));
		}
		return set;
	}
	
	public static CompoundTag fromDirectionSet(Set<Direction> directions) {
		CompoundTag tag = new CompoundTag();
		directions.forEach(d -> {
			tag.putString(String.valueOf(tag.getSize()), d.toString());
		});
		return tag;
	}
	
	public static Set<Direction> toDirectionSet(CompoundTag tag) {
		Set<Direction> set = new TreeSet<>();
		for(int i = 0; i < tag.getSize(); i++) {
			set.add(Direction.byName(tag.getString(String.valueOf(i))));
		}
		return set;
	}
}
