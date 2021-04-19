package lv.cebbys.cedyte.content.entities;

import lv.cebbys.cedyte.content.DynamicTreeEntities;
import lv.cebbys.cedyte.content.trees.handlers.OakTreeHandler;
import lv.cebbys.celib.utilities.CelibBlockPos;

public class OakRootEntity extends DynamicRootEntity  {

	public OakRootEntity() {
		this(null);
	}
	
	public OakRootEntity(CelibBlockPos pos) {
		super(DynamicTreeEntities.OAK_TREE.getLeft(), OakTreeHandler::new, pos);
	}
}
