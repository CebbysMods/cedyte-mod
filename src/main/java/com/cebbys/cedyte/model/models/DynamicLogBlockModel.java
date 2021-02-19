package com.cebbys.cedyte.model.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.cebbys.cedyte.content.trees.structs.StemStruct;
import com.cebbys.cedyte.model.models.abstracts.AbstractBlockModel;
import com.mojang.datafixers.util.Pair;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

public class DynamicLogBlockModel extends AbstractBlockModel {

	public static final float PIXEL = 1.0F / 16.0F;
	private final Sprite particle;
	
	public DynamicLogBlockModel(Identifier spriteId) {
		this.particle = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, spriteId).getSprite();
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos,
			Supplier<Random> randomSupplier, RenderContext context) {
	}
	
	private Mesh generateStemModel(StemStruct s, int stage) {
		int size = (stage + 1) * 2;
		int length = (16 - size) / 2;
		
		MeshBuilder builder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
		QuadEmitter emitter = builder.getEmitter();
		List<Boolean> mask = this.getModelMask(s);
		for(int i = 0; i < mask.size(); i++) {
			boolean bit = mask.get(i);
			Direction dir = Direction.byId(i);
			if(bit) {
				emitter.square(dir, length, length, length + size, length + size, 0);
				emitter.square(dir, length, length, length + size, length + size, 0);
			}
			emitter.square(dir, length, length, length + size, length + size, length);
			emitter.emit();
		}
		
		for(Direction d : Direction.values()) {
			switch(d.getAxis()) {
			case X:
				
				break;
			case Y:
				
				break;
			case Z:
				
				break;
			}
		}
		return builder.build();
	}
	
	private void addSquares(QuadEmitter emitter, Direction dir, int stage) {
		int size = (stage + 1) * 2;
		int length = (16 - size) / 2;
		emitter.square(dir, length, length, length + size, length + size, length);
		emitter.square(dir, length, length, length + size, length + size, 0);
		
	}
	
	/**Mask indexes
	 * DOWN  - 0
	 * UP    - 1
	 * NORTH - 2
	 * SOUTH - 3
	 * WEST  - 4
	 * EAST  - 5*/ 
	private List<Boolean> getModelMask(StemStruct s) {
		// TODO Calculate outgoing branches
		List<Boolean> list = new ArrayList<Boolean>();
		list.add(true);
		list.add(true);
		list.add(true);
		list.add(true);
		list.add(true);
		list.add(true);
		return list;
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return false;
	}

	@Override
	public Sprite getSprite() {
		return this.particle;
	}

	@Override
	public ModelTransformation getTransformation() {
		return ModelTransformation.NONE;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}

	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptyList();
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter,
			Set<Pair<String, String>> unresolvedTextureReferences) {
		return Collections.emptyList();
	}

	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter,
			ModelBakeSettings rotationContainer, Identifier modelId) {
		return this;
	}

}
