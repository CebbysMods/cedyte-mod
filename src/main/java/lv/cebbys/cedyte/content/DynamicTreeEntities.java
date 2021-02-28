package lv.cebbys.cedyte.content;

import java.util.function.Supplier;

import lv.cebbys.cedyte.content.entities.DynamicBranchEntity;
import lv.cebbys.cedyte.content.entities.DynamicRootEntity;
import lv.cebbys.cedyte.content.entities.OakBranchEntity;
import lv.cebbys.cedyte.content.entities.OakRootEntity;
import lv.cebbys.cedyte.content.entities.SpruceBranchEntity;
import lv.cebbys.cedyte.content.entities.SpruceRootEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class DynamicTreeEntities {

	public static final Pair<BlockEntityType<OakRootEntity>, BlockEntityType<OakBranchEntity>> OAK_TREE;
	public static final Pair<BlockEntityType<SpruceRootEntity>, BlockEntityType<SpruceBranchEntity>> SPRUCE_TREE;

	private static <R extends DynamicRootEntity, B extends DynamicBranchEntity> Pair<BlockEntityType<R>, BlockEntityType<B>> registerEntity(
			String entityId, Supplier<R> rootSupplier, Block rootBlock, Supplier<B> branchSupplier, Block branchBlock) {
		BlockEntityType<R> e0 = registerEntity(entityId + "_root_entity", rootSupplier, rootBlock);
		BlockEntityType<B> e1 = registerEntity(entityId + "_branch_entity", branchSupplier, branchBlock);
		return new Pair<BlockEntityType<R>, BlockEntityType<B>>(e0, e1);
	}

	private static <T extends BlockEntity> BlockEntityType<T> registerEntity(String entityId, Supplier<T> supplier,
			Block block) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, entityId,
				BlockEntityType.Builder.create(supplier, block).build(null));
	}

	static {
		OAK_TREE = registerEntity("oak", OakRootEntity::new, DynamicTreeBlocks.OAK_ROOT, OakBranchEntity::new,
				DynamicTreeBlocks.OAK_BRANCH);
		SPRUCE_TREE = registerEntity("spruce", SpruceRootEntity::new, DynamicTreeBlocks.SPRUCE_ROOT,
				SpruceBranchEntity::new, DynamicTreeBlocks.SPRUCE_BRANCH);
	}
}
