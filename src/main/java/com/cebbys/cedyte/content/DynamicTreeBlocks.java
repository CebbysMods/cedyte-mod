package com.cebbys.cedyte.content;

import com.cebbys.cedyte.content.blocks.DynamicBranchBlock;
import com.cebbys.cedyte.content.blocks.DynamicRootBlock;
import com.cebbys.cedyte.content.entities.OakBranchEntity;
import com.cebbys.cedyte.content.entities.OakRootEntity;
import com.cebbys.cedyte.content.entities.SpruceBranchEntity;
import com.cebbys.cedyte.content.entities.SpruceRootEntity;

public class DynamicTreeBlocks {

    public static final DynamicRootBlock OAK_ROOT;
    public static final DynamicBranchBlock OAK_BRANCH;
    public static final DynamicRootBlock SPRUCE_ROOT;
    public static final DynamicBranchBlock SPRUCE_BRANCH;

    static {
        OAK_ROOT = new DynamicRootBlock( "oak_root", OakRootEntity::new );
        OAK_BRANCH = new DynamicBranchBlock( "oak_branch", OakBranchEntity::new );
        SPRUCE_ROOT = new DynamicRootBlock( "spruce_root", SpruceRootEntity::new );
        SPRUCE_BRANCH = new DynamicBranchBlock( "spruce_branch", SpruceBranchEntity::new );
    }
}
