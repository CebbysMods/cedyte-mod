package lv.cebbys.cedyte.utilities.builder;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.swordglowsblue.artifice.api.builder.JsonObjectBuilder;

import lv.cebbys.celib.loggers.CelibLogger;

import static lv.cebbys.cedyte.api.CedyteKey.*;
import net.minecraft.util.Identifier;

public class TreeTemplateBuilder {

	public static JsonObject getSpruce() {
		return createTree(new Identifier("spruce"), tree -> {
			tree.length(5);
			for (int i = 0; i < 4; i++) {
				final int j = i;
				tree.addBranch(b0 -> {
					b0.length(1);
					b0.rotationX(90);
					b0.rotationY(45 + (j * 90));
					b0.addBranch(b1 -> {
						b1.length(6);
						b1.rotationX(-30);
						b1.rotationY(0);
						b1.addBranch(b2 -> {
							b2.length(1);
							b2.rotationX(-60);
							b2.rotationY(0);
							for (int k = 0; k < 3; k++) {
								final int l = k;
								b2.addBranch(b3 -> {
									b3.length(5);
									b3.rotationX(90);
									b3.rotationY(120 - 120 * l);
								});
							}
						});
					});
				});
			}
			tree.addBranch(b4 -> {
				b4.length(8);
				for (int k = 0; k < 4; k++) {
					final int l = k;
					b4.addBranch(b5 -> {
						b5.length(6);
						b5.rotationX(60);
						b5.rotationY(60 + 90 * l);
					});
				}
			});
		});
	}

	public static JsonObject getWeird() {
		return createTree(new Identifier("weird"), tree -> {
			tree.length(2);
			for (int i = 0; i < 4; i++) {
				int ci = i;
				tree.addBranch(b0 -> {
					b0.length(1);
					b0.rotationX(90);
					b0.rotationY(ci * 90);
					b0.addBranch(b1 -> {
						b1.rotationX(-30);
						b1.length(6);
						b1.addBranch(b2 -> {
							b2.length(1);
							b2.rotationX(-60);
							for (int j = 0; j < 4; j++) {
								int cj = j;
								b2.addBranch(b3 -> {
									b3.length(1);
									b3.rotationX(90);
									b3.rotationY(cj * 90);
									b3.addBranch(b4 -> {
										b4.length(8);
										b4.rotationX(-60);
									});
								});
							}
						});
					});
				});
			}
		});

	}

	public static JsonObject createTree(Identifier treeId, Builder<Branch> stem) {
		return stem.build(new Branch()).build();
	}

	public static class Branch {
		private List<Branch> branches;
		private float weight;
		private int factor;
		private int rotY;
		private int rotYOff;
		private int rotX;
		private int rotXOff;
		private int len;
		private int lenOff;

		Branch() {
			this.branches = new ArrayList<>();
			this.factor = 0;
			this.rotY = 0;
			this.rotX = 0;
			this.len = 1;
		}

		public void addBranch(Builder<Branch> branch) {
			if (this.branches.size() < 4) {
				this.branches.add(branch.build(new Branch()));
			}
		}

		public void weight(float w) {
			this.weight = Math.max(0.1F, Math.min(1.0F, w));
		}

		public void rotationY(int angle) {
			this.rotY = angle;
		}

		public void rotationYOffset(int angle) {
			this.rotYOff = angle;
		}

		public void rotationX(int angle) {
			this.rotX = angle;
		}

		public void rotationXOffset(int angle) {
			this.rotXOff = angle;
		}

		public void length(int stemCount) {
			this.len = Math.max(0, stemCount);
		}

		public void lengthOffset(int stemCount) {
			this.lenOff = Math.max(0, stemCount);
		}

		public void factor(int factor) {
			this.factor = factor;
		}

		public JsonObject build() {
			return new JsonObjectBuilder() //
					.add(WEIGHT.toKey(), this.weight) //
					.add(ROT_Y.toKey(), this.rotY).add(ROT_Y_OFF.toKey(), this.rotYOff) //
					.add(ROT_X.toKey(), this.rotX).add(ROT_X_OFF.toKey(), this.rotXOff) //
					.add(LENGTH.toKey(), this.len).add(LENGTH_OFF.toKey(), this.lenOff) //
					.add(FACTOR.toKey(), this.factor) //
					.addArray(BRANCHES.toKey(), array -> {
						for (Branch branch : this.branches) {
							array.add(branch.build());
						}
					}).build();
		}
	}
}
