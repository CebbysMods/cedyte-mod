package lv.cebbys.cedyte.model.utils;

import java.util.Set;

import lv.cebbys.celib.loggers.CelibLogger;
import lv.cebbys.celib.utilities.CelibBlockPos;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class StemMeshBuilder {
	
	public static Mesh buildStemMesh(int stage, byte byteMask, Sprite sprite) {
		MeshBuilder builder = RendererAccess.INSTANCE.getRenderer().meshBuilder();
		QuadEmitter emitter = builder.getEmitter();
		float stemWidth = 2.0f * (1.0f + stage) / 16.0f;
		float border = (1.0f - stemWidth) * 0.5f;

		boolean[] mask = StemMeshBuilder.decodeByteMask(byteMask);
		for (Direction face : Direction.values()) {
			if (!mask[face.getId()]) {
				StemMeshBuilder.square(emitter, face, border, border, border + stemWidth, border + stemWidth, border,
						sprite);
			} else {
				int maskId = face.getId();
				StemMeshBuilder.square(emitter, Direction.byId(maskId), border, border, border + stemWidth,
						border + stemWidth, 0, sprite);
				switch (maskId) {
				case 0: // down
					for (Direction d : Direction.values()) {
						if (d != Direction.byId(maskId) && d != Direction.byId(maskId).getOpposite()) {
							StemMeshBuilder.square(emitter, d, border, 0, border + stemWidth, border, border, sprite);
						}
					}
					break;
				case 1: // up
					for (Direction d : Direction.values()) {
						if (d != Direction.byId(maskId) && d != Direction.byId(maskId).getOpposite()) {
							StemMeshBuilder.square(emitter, d, border, border + stemWidth, border + stemWidth, 1,
									border, sprite);
						}
					}
					break;
				// border, 0, border + stemWidth, border, border
				// border, border + stemWidth, border + stemWidth, 1, border
				// 0, border, border, border + stemWidth, border
				// border + stemWidth, border, 1, border + stemWidth, border

				case 2: // north
					StemMeshBuilder.square(emitter, Direction.WEST, 0, border, border, border + stemWidth, border,
							sprite);
					StemMeshBuilder.square(emitter, Direction.EAST, border + stemWidth, border, 1, border + stemWidth,
							border, sprite);
					StemMeshBuilder.square(emitter, Direction.DOWN, border, 0, border + stemWidth, border, border,
							sprite);
					StemMeshBuilder.square(emitter, Direction.UP, border, border + stemWidth, border + stemWidth, 1,
							border, sprite);
					break;
				case 3: // south
					StemMeshBuilder.square(emitter, Direction.WEST, border + stemWidth, border, 1, border + stemWidth,
							border, sprite);
					StemMeshBuilder.square(emitter, Direction.EAST, 0, border, border, border + stemWidth, border,
							sprite);
					StemMeshBuilder.square(emitter, Direction.DOWN, border, border + stemWidth, border + stemWidth, 1,
							border, sprite);
					StemMeshBuilder.square(emitter, Direction.UP, border, 0, border + stemWidth, border, border,
							sprite);
					break;
				case 4: // west
					StemMeshBuilder.square(emitter, Direction.NORTH, border + stemWidth, border, 1, border + stemWidth,
							border, sprite);
					StemMeshBuilder.square(emitter, Direction.SOUTH, 0, border, border, border + stemWidth, border,
							sprite);
					StemMeshBuilder.square(emitter, Direction.UP, 0, border, border, border + stemWidth, border,
							sprite);
					StemMeshBuilder.square(emitter, Direction.DOWN, 0, border, border, border + stemWidth, border,
							sprite);
					break;
				case 5: // east
					StemMeshBuilder.square(emitter, Direction.NORTH, 0, border, border, border + stemWidth, border,
							sprite);
					StemMeshBuilder.square(emitter, Direction.SOUTH, border + stemWidth, border, 1, border + stemWidth,
							border, sprite);
					StemMeshBuilder.square(emitter, Direction.UP, border + stemWidth, border, 1, border + stemWidth,
							border, sprite);
					StemMeshBuilder.square(emitter, Direction.DOWN, border + stemWidth, border, 1, border + stemWidth,
							border, sprite);
					break;
				}
			}
		}
		return builder.build();
	}

	private static void square(QuadEmitter emitter, Direction face, float left, float bottom, float right, float top,
			float depth, Sprite sprite) {
		emitter.square(face, left, bottom, right, top, depth);
		emitter.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV);
		emitter.spriteColor(0, -1, -1, -1, -1);
		emitter.emit();
	}

	private static byte createByteMask(CelibBlockPos branchPos, Set<CelibBlockPos> childPos, Direction branchDir) {
		CelibBlockPos[] posArray = new CelibBlockPos[childPos.size()];
		posArray = childPos.toArray(posArray);
		Vec3i v = branchDir.getOpposite().getVector();
		byte mask = StemMeshBuilder.maskVector(new Vec3d(v.getX(), v.getY(), v.getZ()));
		for (int i = 0; i < posArray.length; i++) {
			mask += StemMeshBuilder.maskVector(CelibBlockPos.toVec3d(posArray[i].sub(branchPos)));
		}
		return mask;
	}

	private static byte maskVector(Vec3d v) {
		return (byte) ((((byte) v.x + 3) % 3) + (4 * (((byte) v.y + 3) % 3)) + (16 * (((byte) v.z + 3) % 3)));
	}

	private static boolean[] decodeByteMask(byte mask) {
		boolean[] decoded = new boolean[6];
		decoded[2] = mask >= 32;
		if (decoded[2])
			mask -= 32;

		decoded[3] = mask >= 16;
		if (decoded[3])
			mask -= 16;

		decoded[0] = mask >= 8;
		if (decoded[0])
			mask -= 8;

		decoded[1] = mask >= 4;
		if (decoded[1])
			mask -= 4;

		decoded[4] = mask >= 2;
		if (decoded[4])
			mask -= 2;

		decoded[5] = mask >= 1;
		if (decoded[5])
			mask -= 1;
		return decoded;
	}
}
