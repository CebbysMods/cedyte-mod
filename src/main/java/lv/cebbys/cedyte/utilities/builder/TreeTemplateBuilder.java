package lv.cebbys.cedyte.utilities.builder;

import java.util.ArrayList;
import java.util.List;

import com.cebbys.celib.loggers.CelibLogger;
import com.google.gson.JsonObject;
import com.swordglowsblue.artifice.api.builder.JsonObjectBuilder;

import static lv.cebbys.cedyte.api.CedyteKey.*;
import net.minecraft.util.Identifier;

public class TreeStructureBuilder {

	public static JsonObject getSpruce() {
		return createTree(new Identifier("spruce"), tree -> {
			final int angle = 330;
			tree.length(4);
			for(int i = 0; i < 4; i++) {
				final int j = i;
				tree.addBranch(b0 -> {
					b0.length(1);
					b0.rotationX(90);
					b0.rotationY(60 + (j * 90));
					b0.addBranch(b1 -> {
						b1.length(6);
						b1.rotationX(60);
						b1.rotationY(60 + (j * 90));
						b1.addBranch(b2 -> {
							b2.length(1);
							for(int k = 0; k < 2; k++) {
								final int l = k;
								b2.addBranch(b3 -> {
									b3.length(6);
									b3.rotationX(-60);
									b3.rotationY((l * 180) + 45);
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
		private int factor;
		private int rotY;
		private int rotX;
		private int len;

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

		public void rotationY(int angle) {
			this.rotY = angle;
		}

		public void rotationX(int angle) {
			this.rotX = angle;
		}

		public void length(int stemCount) {
			this.len = stemCount;
		}

		public void factor(int factor) {
			this.factor = factor;
		}

		public JsonObject build() {
			return new JsonObjectBuilder() //
					.add(ROT_Y.toKey(), this.rotY) //
					.add(ROT_X.toKey(), this.rotX) //
					.add(LENGTH.toKey(), this.len)
					.add(FACTOR.toKey(), this.factor)
					.addArray(BRANCHES.toKey(), array -> {
						for (Branch branch : this.branches) {
							array.add(branch.build());
						}
					}).build();
		}
	}
}
