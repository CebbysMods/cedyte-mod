package com.cebbys.cedyte.content.entities;

import com.cebbys.cedyte.content.DynamicTreeEntities;
import com.cebbys.cedyte.content.entities.tree.DynamicRootEntity;
import com.cebbys.cedyte.content.trees.handlers.SpruceTreeHandler;

public class SpruceRootEntity extends DynamicRootEntity {
	
	public SpruceRootEntity() {
		super(DynamicTreeEntities.SPRUCE_TREE.getLeft(), SpruceTreeHandler::new);
	}
	
}
