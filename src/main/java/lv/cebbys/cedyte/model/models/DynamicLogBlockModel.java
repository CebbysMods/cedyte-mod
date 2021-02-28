package lv.cebbys.cedyte.model.models;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.cebbys.celib.loggers.CelibLogger;
import com.cebbys.celib.utilities.CelibBlockPos;
import com.mojang.datafixers.util.Pair;

import lv.cebbys.cedyte.content.entities.DynamicBranchEntity;
import lv.cebbys.cedyte.content.entities.DynamicRootEntity;
import lv.cebbys.cedyte.content.entities.DynamicStemEntity;
import lv.cebbys.cedyte.content.trees.BranchStruct;
import lv.cebbys.cedyte.content.trees.StemStruct;
import lv.cebbys.cedyte.model.models.abstracts.AbstractBlockModel;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.model.BakedModel;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockRenderView;

public class DynamicLogBlockModel extends AbstractBlockModel {

	public static final float PIXEL = 1.0f / 16.0f;
	private final SpriteIdentifier spriteId;

	public DynamicLogBlockModel(Identifier spriteId) {
		this.spriteId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, spriteId);
	}

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos,
			Supplier<Random> randomSupplier, RenderContext context) {
		BlockEntity defaultEntity = blockView.getBlockEntity(pos);
		if (defaultEntity instanceof DynamicStemEntity) {
			DynamicStemEntity entity = (DynamicStemEntity) defaultEntity;
			Direction dir = entity.getDirection();
			Set<Direction> childPos = entity.getBranchingDirections();
			int length = entity.getBranchLength();
			int maxLength = 30;
			int index = entity.getBranchIndex();
			int stage = (int) (Math.floor(Math.sqrt(length - index - 0.1) / Math.sqrt(maxLength) * 8.0)) -1;
			stage = Math.min(6, Math.max(stage, 0));
			if(dir != null && childPos != null) {
				byte mask = this.createByteMask(childPos);
				context.meshConsumer().accept(this.generateStemModel(dir, stage, mask));
			} else {
				CelibLogger.log("Model", "Missing, dir, pos, childPos");
			}
		}
	}
	
	private Mesh generateStemModel(Direction dir, int stage, byte byteMask) {
		MeshBuilder builder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
		QuadEmitter emitter = builder.getEmitter();
		if (dir != null) {
			float stemWidth = 2.0f * (1.0f + stage) * PIXEL;
			float border = (1.0f - stemWidth) * 0.5f;

			boolean[] mask = this.decodeByteMask(byteMask);
			for (Direction face : Direction.values()) {
				if (!mask[face.getId()]) {
					this.square(emitter, face, border, border, border + stemWidth, border + stemWidth, border);
				} else {
					int maskId = face.getId();
					this.square(emitter, Direction.byId(maskId), border, border, border + stemWidth, border + stemWidth, 0);
					switch(maskId) {
					case 0: // down
						for (Direction d : Direction.values()) {
							if (d != Direction.byId(maskId) && d != Direction.byId(maskId).getOpposite()) {
								this.square(emitter, d, border, 0, border + stemWidth, border, border);
							}
						}
						break;
					case 1: // up
						for (Direction d : Direction.values()) {
							if (d != Direction.byId(maskId) && d != Direction.byId(maskId).getOpposite()) {
								this.square(emitter, d, border, border + stemWidth, border + stemWidth, 1, border);
							}
						}
						break;
						// border, 0, border + stemWidth, border, border
						// border, border + stemWidth, border + stemWidth, 1, border
						// 0, border, border, border + stemWidth, border
						// border + stemWidth, border, 1, border + stemWidth, border
						
					case 2: // north
						this.square(emitter, Direction.WEST,  0, border, border, border + stemWidth, border);
						this.square(emitter, Direction.EAST,  border + stemWidth, border, 1, border + stemWidth, border);
						this.square(emitter, Direction.DOWN,  border, 0, border + stemWidth, border, border);
						this.square(emitter, Direction.UP,    border, border + stemWidth, border + stemWidth, 1, border);
						break;
					case 3: // south
						this.square(emitter, Direction.WEST,  border + stemWidth, border, 1, border + stemWidth, border);
						this.square(emitter, Direction.EAST,  0, border, border, border + stemWidth, border);
						this.square(emitter, Direction.DOWN,  border, border + stemWidth, border + stemWidth, 1, border);
						this.square(emitter, Direction.UP,    border, 0, border + stemWidth, border, border);
						break;
					case 4: // west
						this.square(emitter, Direction.NORTH, border + stemWidth, border, 1, border + stemWidth, border);
						this.square(emitter, Direction.SOUTH, 0, border, border, border + stemWidth, border);
						this.square(emitter, Direction.UP,    0, border, border, border + stemWidth, border);
						this.square(emitter, Direction.DOWN,  0, border, border, border + stemWidth, border);
						break;
					case 5: // east
						this.square(emitter, Direction.NORTH, 0, border, border, border + stemWidth, border);
						this.square(emitter, Direction.SOUTH, border + stemWidth, border, 1, border + stemWidth, border);
						this.square(emitter, Direction.UP,    border + stemWidth, border, 1, border + stemWidth, border);
						this.square(emitter, Direction.DOWN,  border + stemWidth, border, 1, border + stemWidth, border);
						break;
					}
				}
			}
		} else {
			CelibLogger.log("DynamicLogBlock", "Direction is null");
		}
		return builder.build();
	}

	private void square(QuadEmitter emitter, Direction face, float left, float bottom, float right, float top,
			float depth) {
		emitter.square(face, left, bottom, right, top, depth);
		emitter.spriteBake(0, this.spriteId.getSprite(), MutableQuadView.BAKE_LOCK_UV);
		emitter.spriteColor(0, -1, -1, -1, -1);
		emitter.emit();
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
		return this.spriteId.getSprite();
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

	private byte createByteMask(Set<Direction> branchingDir) {
		byte mask = 0;
		Iterator<Direction> i = branchingDir.iterator();
		while(i.hasNext()) {
			Vec3i v0 = i.next().getVector();
			mask += this.maskVector(new Vec3d(v0.getX(), v0.getY(), v0.getZ()));
		}
		return mask;
	}
	
	private byte maskVector(Vec3d v) {
		return (byte) ((((byte) v.x + 3) % 3) + (4 * (((byte) v.y + 3) % 3)) + (16 * (((byte) v.z + 3) % 3)));
	}

	private boolean[] decodeByteMask(byte mask) {
		boolean[] decoded = new boolean[6];
		decoded[2] = mask >= 32;
		if(decoded[2]) mask -= 32;
		
		decoded[3] = mask >= 16;
		if(decoded[3]) mask -= 16;
		
		decoded[0] = mask >= 8;
		if(decoded[0]) mask -= 8;
		
		decoded[1] = mask >= 4;
		if(decoded[1]) mask -= 4;
		
		decoded[4] = mask >= 2;
		if(decoded[4]) mask -= 2;
		
		decoded[5] = mask >= 1;
		if(decoded[5]) mask -= 1;
		return decoded;
	}
}
