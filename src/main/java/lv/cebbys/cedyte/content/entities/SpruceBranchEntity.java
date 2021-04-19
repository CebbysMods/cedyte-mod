package lv.cebbys.cedyte.content.entities;

import lv.cebbys.cedyte.content.DynamicTreeEntities;
import lv.cebbys.celib.utilities.CelibBlockPos;
import net.minecraft.util.math.Direction;

public class SpruceBranchEntity extends DynamicBranchEntity {

	public SpruceBranchEntity() {
		this(Direction.DOWN, 1, 10);
	}
	
	public SpruceBranchEntity(Direction parentDirection, int index, int branchLength) {
		super(DynamicTreeEntities.SPRUCE_TREE.getRight(), parentDirection, index, branchLength);
	}
}
