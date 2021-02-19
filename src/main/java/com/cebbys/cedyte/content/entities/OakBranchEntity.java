package com.cebbys.cedyte.content.entities;

import com.cebbys.cedyte.content.DynamicTreeEntities;
import com.cebbys.cedyte.content.entities.tree.DynamicBranchEntity;
import com.cebbys.celib.utilities.CelibBlockPos;

public class OakBranchEntity extends DynamicBranchEntity {

	public OakBranchEntity() {
		this(new CelibBlockPos(0, 0, 0));
	}
	
	public OakBranchEntity(CelibBlockPos pos) {
		super(DynamicTreeEntities.OAK_TREE.getRight(), pos);
	}

}
