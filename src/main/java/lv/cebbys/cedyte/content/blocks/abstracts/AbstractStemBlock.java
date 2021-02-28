package lv.cebbys.cedyte.content.blocks.abstracts;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class AbstractStemBlock extends Block implements BlockEntityProvider {

	private Supplier<? extends BlockEntity> entitySupplier;

	public AbstractStemBlock(Settings settings, Supplier<? extends BlockEntity> supplier) {
		super(settings);
		this.entitySupplier = supplier;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return this.entitySupplier.get();
	}

}
