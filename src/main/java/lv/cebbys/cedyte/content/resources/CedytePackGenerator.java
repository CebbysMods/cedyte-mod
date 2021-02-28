package lv.cebbys.cedyte.content.resources;

import com.swordglowsblue.artifice.api.Artifice;
import com.swordglowsblue.artifice.api.ArtificeResourcePack.ClientResourcePackBuilder;
import com.swordglowsblue.artifice.api.builder.assets.ModelBuilder;

import lv.cebbys.cedyte.Cedyte;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class CedytePackGenerator {

	@Environment(EnvType.CLIENT)
	public static void generateClientPack() {
		Artifice.registerAssetPack(new Identifier(Cedyte.MOD_ID, "resource_pack"), pack -> {
			pack.setDisplayName("Cedyte Resources");
			pack.setDescription("Default CebbyS Dynamic Tree Resources");
			pack.setVisible();
			addTreeBlockStates("spruce", pack);
			addTreeBlockStates("oak", pack);
		});
	}

	private static void addTreeBlockStates(String tree, ClientResourcePackBuilder pack) {
		pack.addBlockState(id(tree + "_root_block"), state -> {
			state.variant("", s -> {
				s.model(id("block/tree/" + tree + "_root"));
			});
		});
		pack.addBlockState(id(tree + "_branch_block"), state -> {
			state.variant("", s -> {
				s.model(id("block/tree/" + tree + "_branch"));
			});
		});
	}
	
	private static void generateTreeModel(ClientResourcePackBuilder pack, String name) {
		pack.addBlockState(id(name + "_root_block"), state -> {
			for (int i = 0; i < 8; i++) {
				final int stage = i;
				state.variant("growth_stage=" + String.valueOf(stage), s -> {
					s.model(id("block/tree/" + name + "/root/" + String.valueOf(stage)));
				});
			}
		});
		pack.addBlockState(id(name + "_branch_block"), state -> {
			for (int i = 0; i < 8; i++) {
				final int stage = i;
				for (Direction d : Direction.values()) {
					state.variant("direction=" + d.toString() + ",growth_stage=" + String.valueOf(stage), s -> {
						Identifier id = id("block/tree/" + name + "/branch/" + String.valueOf(stage));
						if (d.equals(Direction.DOWN)) {
							s.model(id).rotationX(180);
						} else if (d.equals(Direction.NORTH)) {
							s.model(id).rotationX(90);
						} else if (d.equals(Direction.SOUTH)) {
							s.model(id).rotationX(-90);
						} else if (d.equals(Direction.WEST)) {
							s.model(id).rotationX(90).rotationY(-90);
						} else if (d.equals(Direction.EAST)) {
							s.model(id).rotationX(-90).rotationY(-90);
						} else {
							s.model(id);
						}
					});
					state.build();
				}
			}
		});
		for (int i = 0; i < 8; i++) {
			final int index = i;
			pack.addBlockModel(id("tree/" + name + "/branch/" + String.valueOf(i)), model -> {
				generateBranchModel(model, index, name);
			});
			pack.addBlockModel(id("tree/" + name + "/root/" + String.valueOf(i)), model -> {
				generateRootModel(model, index, name);
			});
		}
	}

	private static void generateBranchTemplateModel(ModelBuilder model, int index) {
		index = (int) Math.min(Math.max(index, 0), 7);
		int min = 7 - index;
		int max = 9 + index;
		model.element(b -> {
			b.from(min, -min, min);
			b.to(max, max, max);
			b.face(Direction.NORTH, f -> {
				f.uv(min, 0, max, 16).texture("log").tintindex(-1);
			});
			b.face(Direction.EAST, f -> {
				f.uv(min, 0, max, 16).texture("log").tintindex(-1);
			});
			b.face(Direction.SOUTH, f -> {
				f.uv(min, 0, max, 16).texture("log").tintindex(-1);
			});
			b.face(Direction.WEST, f -> {
				f.uv(min, 0, max, 16).texture("log").tintindex(-1);
			});
			b.face(Direction.UP, f -> {
				f.uv(min, min, max, max).texture("log").tintindex(-1);
			});
		});
		if (index < 2) {
			model.element(b -> {
				b.from(0, 0, 0);
				b.to(16, 16, 16);
				b.face(Direction.UP, f -> {
					f.uv(0, 0, 16, 16).texture("leaf").cullface(Direction.UP).tintindex(0);
				});
				b.face(Direction.DOWN, f -> {
					f.uv(0, 0, 16, 16).texture("leaf").cullface(Direction.DOWN).tintindex(0);
				});
				b.face(Direction.NORTH, f -> {
					f.uv(0, 0, 16, 16).texture("leaf").cullface(Direction.NORTH).tintindex(0);
				});
				b.face(Direction.SOUTH, f -> {
					f.uv(0, 0, 16, 16).texture("leaf").cullface(Direction.SOUTH).tintindex(0);
				});
				b.face(Direction.WEST, f -> {
					f.uv(0, 0, 16, 16).texture("leaf").cullface(Direction.WEST).tintindex(0);
				});
				b.face(Direction.EAST, f -> {
					f.uv(0, 0, 16, 16).texture("leaf").cullface(Direction.EAST).tintindex(0);
				});
			});
		}
		model.build();
	}

	private static void generateBranchModel(ModelBuilder model, int index, String name) {
		model.parent(id("block/template/tree/branch/" + String.valueOf(index)));
		model.texture("log", new Identifier("minecraft", "block/" + name + "_log"));
		model.texture("particle", new Identifier("minecraft", "block/" + name + "_log"));
		if (index <= 2) {
			model.texture("leaf", new Identifier("minecraft", "block/" + name + "_leaves"));
		}
		model.build();
	}

	private static void generateRootTemplateModel(ModelBuilder model, int index) {
		index = (int) Math.min(Math.max(index, 0), 7);
		int min = 7 - index;
		int max = 9 + index;
		model.element(b -> {
			b.from(min, 0, min);
			b.to(max, max, max);
			b.face(Direction.NORTH, f -> {
				f.uv(min, 0, max, max).texture("log");
			});
			b.face(Direction.EAST, f -> {
				f.uv(min, 0, max, max).texture("log");
			});
			b.face(Direction.SOUTH, f -> {
				f.uv(min, 0, max, max).texture("log");
			});
			b.face(Direction.WEST, f -> {
				f.uv(min, 0, max, max).texture("log");
			});
			b.face(Direction.UP, f -> {
				f.uv(min, min, max, max).texture("log");
			});
		});
		model.build();
	}

	private static void generateRootModel(ModelBuilder model, int index, String name) {
		model.parent(id("block/template/tree/root/" + String.valueOf(index)));
		model.texture("log", new Identifier("minecraft", "block/" + name + "_log"));
		model.texture("particle", new Identifier("minecraft", "block/" + name + "_log"));
		model.build();
	}

	private static Identifier id(String name) {
		return new Identifier(Cedyte.MOD_ID, name);
	}
}
