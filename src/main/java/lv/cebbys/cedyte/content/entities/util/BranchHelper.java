package lv.cebbys.cedyte.content.entities.util;

import com.cebbys.celib.loggers.CelibLogger;
import com.cebbys.celib.utilities.CelibBlockPos;
import com.cebbys.celib.utilities.CelibDirections;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class BranchHelper {
	public static Direction getBudDirection(CelibBlockPos basePos, CelibBlockPos stemPos, int stemCount, Vec3d vector) {
		return CelibDirections.toDirection(basePos.toVec3d().add(vector.multiply(stemCount)).subtract(stemPos.toVec3d()));
	}
}
