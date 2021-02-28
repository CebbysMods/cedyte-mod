package lv.cebbys.cedyte.content.entities;

import com.cebbys.celib.utilities.CelibBlockPos;

import lv.cebbys.cedyte.content.DynamicTreeEntities;
import lv.cebbys.cedyte.content.trees.handlers.SpruceTreeHandler;

public class SpruceRootEntity extends DynamicRootEntity {
	
	public SpruceRootEntity() {	
		this(null);
	}
	public SpruceRootEntity(CelibBlockPos pos) {
		super(DynamicTreeEntities.SPRUCE_TREE.getLeft(), SpruceTreeHandler::new, pos);
	}
	
}
