package Akuto2Mod.Utils.Blocks;

public class PosRange {
	public int x = 0;
	public int y = 0;
	public int z = 0;
	public int range = 0;

	public PosRange(int x, int y, int z, int r) {
		this.x = x;
		this.y = y;
		this.z = z;
		range = r;
	}

	public PosRange(int x, int y, int z) {
		this(x, y, z, 0);
	}

	public boolean equals(Object obj) {
		if(obj instanceof PosRange) {
			PosRange p = (PosRange)obj;
			return x == p.x && y == p.y && z == p.z;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (this.x << 20 & 0xfff00000) | (this.z << 8 & 0xfff00) | (this.y & 0xff);
	}

	@Override
	public String toString() {
		return String.format("PosRange: x%d y%d z%d range%d", x, y, z, range);
	}
}
