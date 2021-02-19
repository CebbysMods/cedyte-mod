package com.cebbys.cedyte.content.entities;

import com.cebbys.cedyte.content.DynamicTreeEntities;
import com.cebbys.cedyte.content.entities.tree.DynamicBranchEntity;
import com.cebbys.celib.utilities.CelibBlockPos;

public class SpruceBranchEntity extends DynamicBranchEntity {

	public SpruceBranchEntity() {
		this(new CelibBlockPos(0, 0, 0));
	}
	
	public SpruceBranchEntity(CelibBlockPos off) {
		super(DynamicTreeEntities.SPRUCE_TREE.getRight(), off);
	}
}
