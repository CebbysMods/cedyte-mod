package com.cebbys.cedyte.content.entities;

import com.cebbys.cedyte.content.DynamicTreeEntities;
import com.cebbys.cedyte.content.entities.tree.DynamicRootEntity;
import com.cebbys.cedyte.content.trees.handlers.OakTreeHandler;

public class OakRootEntity extends DynamicRootEntity  {
	
	public OakRootEntity() {
		super(DynamicTreeEntities.OAK_TREE.getLeft(), OakTreeHandler::new);
	}
}
