package lv.cebbys.cedyte.content.entities;

import com.cebbys.celib.utilities.CelibBlockPos;

import lv.cebbys.cedyte.content.DynamicTreeEntities;
import net.minecraft.util.math.Direction;

public class OakBranchEntity extends DynamicBranchEntity {

	public OakBranchEntity() {
		this(Direction.DOWN, 1, 10);
	}
	
	public OakBranchEntity(Direction parentDirection, int index, int branchLength) {
		super(DynamicTreeEntities.OAK_TREE.getRight(), parentDirection , index, branchLength);
	}

}
