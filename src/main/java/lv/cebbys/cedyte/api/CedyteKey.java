package lv.cebbys.cedyte.api;

public enum CedyteKey {
	WEIGHT("weight"),
	LENGTH("length"),
	LENGTH_OFF("length_off"),
	ROT_X("rotation_x"),
	ROT_X_OFF("rotation_x_off"),
	ROT_Y("rotation_y"),
	ROT_Y_OFF("rotation_y_off"),
	FACTOR("factor"),
	BRANCHES("branches");
	
	private String key;
	
	CedyteKey(String key) {
		this.key = key;
	}
	
	public String toKey() {
		return this.key;
	}
}
