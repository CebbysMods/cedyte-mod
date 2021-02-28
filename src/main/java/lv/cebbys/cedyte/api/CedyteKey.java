package lv.cebbys.cedyte.api;

public enum CedyteKey {
	LENGTH("length"),
	FACTOR("factor"),
	BRANCHES("branches"),
	ROT_X("rotation_x"),
	ROT_Y("rotation_y");
	
	private String key;
	
	CedyteKey(String key) {
		this.key = key;
	}
	
	public String toKey() {
		return this.key;
	}
}
