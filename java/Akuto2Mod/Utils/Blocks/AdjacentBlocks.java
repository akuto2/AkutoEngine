package Akuto2Mod.Utils.Blocks;

import java.util.ArrayList;

import Akuto2Mod.Utils.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class AdjacentBlocks {
	protected transient ArrayList<PosRange> blocks = new ArrayList<PosRange>(1);
	protected transient boolean[] flags;
	protected int rangeMax = 4;
	protected int listSize;
	protected float radius;
	protected World world;
	protected int xCoord;
	protected int yCoord;
	protected int zCoord;
	protected boolean anyData = true;
	protected Block target;
	protected int targetMeta;

	private int size;
	private int size2;
	private int rx;
	private int ry;
	private int rz;

	public AdjacentBlocks(World world, int x, int y, int z, int range) {
		init(world, x, y, z, range);
	}

	public AdjacentBlocks(World world, int x, int y, int z) {
		init(world, x, y, z, 4);
	}

	public void clear() {
		blocks.clear();
		flags = null;
	}

	public void init(World world, int x, int y, int z, int range) {
		setPosition(world, x, y, z);
		if(world != null) {
			setTarget(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z));
		}
		setRange(range);
	}

	public void setPosition(World world, int x, int y, int z) {
		this.world = world;
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}

	public void setTarget(Block block, int meta) {
		target = block;
		targetMeta = meta;
		anyData = meta == -1 || block == Blocks.air;
	}

	public void setRange(int range) {
		setRange(range, 0.6F);
	}

	public void setRange(int range, float addValue) {
		if(range < 1) {
			range = 1;
		}
		if(range > 127) {
			range = 127;
		}
		rangeMax = range;

		size = rangeMax * 2 + 1;
		size2 = size * size;
		rx = rangeMax - xCoord;
		ry = rangeMax - yCoord;
		rz = rangeMax - zCoord;

		listSize = size * size * size;
		setRadius(addValue);
		flags = new boolean[listSize];
	}

	public void setRadius(float addValue) {
		if(addValue < 0.0F) {
			addValue = 0.0F;
		}
		if(addValue >= 1.0F) {
			addValue = 0.9999F;
		}
		radius = (rangeMax + addValue) * (rangeMax + addValue);
	}

	public boolean isConnectBlock() {
		return isBlockConnection(0);
	}

	public ArrayList<PosRange> getConnectBlocksList(){
		isConnectBlock();
		return blocks;
	}

	public boolean isSquareConnectBlock() {
		return isBlockConnection(1);
	}

	public ArrayList<PosRange> getSquareConnectBlocksList(){
		isSquareConnectBlock();
		return blocks;
	}

	public boolean isCircleConnectBlock() {
		return isBlockConnection(2);
	}

	public ArrayList<PosRange> getCircleConnectBlocksList(){
		isCircleConnectBlock();
		return blocks;
	}

	public ArrayList<PosRange> getBlocksList(){
		return blocks;
	}

	protected boolean isBlockConnection(int mode) {
		if(!WorldHelper.checkChunkBlock(world, xCoord, yCoord, zCoord, rangeMax)) {
			return false;
		}
		if(blocks != null && !blocks.isEmpty()) {
			blocks.clear();
		}
		if(flags == null) {
			flags = new boolean[listSize];
		}
		int modeSize = 0;
		switch (mode) {
		case 0:
			modeSize = (int)(listSize * 0.17D);
			break;
		case 2:
			modeSize = (int)(listSize * 0.53D);
			break;
		default:
			modeSize = listSize;
			break;
		}
		blocks = new ArrayList<PosRange>(modeSize);
		return isBlockConnection(xCoord, yCoord, zCoord, mode);
	}

	public boolean isBlockConnection(int x, int y, int z, int mode) {
		add(x, y, z, 0);

		int i = 0;
		boolean isEnd = false;
		while(!isEnd && i < blocks.size()) {
			PosRange pos = blocks.get(i);{
				int px = pos.x;
				int py = pos.y;
				int pz = pos.z;
				int pr = pos.range;
				if(!isEnd) isEnd = checkBlock(px + 1, py, pz, pr, mode);
				if(!isEnd) isEnd = checkBlock(px - 1, py, pz, pr, mode);
				if(!isEnd) isEnd = checkBlock(px, py, pz + 1, pr, mode);
				if(!isEnd) isEnd = checkBlock(px, py, pz - 1, pr, mode);
				if(!isEnd) isEnd = checkBlock(px, py + 1, pz, pr, mode);
				if(!isEnd) isEnd = checkBlock(px, py - 1, pz, pr, mode);
			}
			++i;
		}
		return isEnd;
	}

	protected boolean checkBlock(int x, int y, int z, int range, int mode) {
		if(y > 255 || y < 0) {
			return false;
		}
		if(isRange(x, y, z, range, mode) && !contains(x, y, z)) {
			Block block = world.getBlock(x, y, z);
			int meta = world.getBlockMetadata(x, y, z);
			if(block != null) {

			}
		}
		return false;
	}

	protected final int index(int x, int y, int z){
		return (x + rx) * size2 + (y + ry) * size + (z + rz);
	}

	protected final boolean contains(int x, int y, int z) {
		return flags[index(x, y, z)];
	}

	protected final boolean contains(PosRange pos) {
		return contains(pos.x, pos.y, pos.z);
	}

	protected void add(int x, int y, int z, int range) {
		blocks.add(new PosRange(x, y, z, range));
		flags[index(x, y, z)] = true;
	}

	protected boolean isRange(int x, int y, int z, int range, int mode) {
		if(mode == 0) {
			return range <= rangeMax;
		}
		if(mode == 1) {
			return isRangeSquare(x, y, z);
		}
		if(mode == 2) {
			return isRangeCircle(x, y, z);
		}
		return false;
	}

	protected boolean isRangeSquare(int x, int y, int z) {
		return Math.abs(x - xCoord) <= rangeMax && Math.abs(y - yCoord) <= rangeMax && Math.abs(z - zCoord) <= rangeMax;
	}

	protected boolean isRangeCircle(int x, int y, int z) {
		int rx = x - xCoord;
		int ry = y - yCoord;
		int rz = z - zCoord;
		return rx * rx + ry * ry + rz * rz <= radius;
	}

	protected boolean canEndBlock(int x, int y, int z, Block block, int meta, int range) {
		return false;
	}

	protected boolean canAddBlock(int x, int y, int z, Block block, int meta, int range) {
		return target == block && (anyData || targetMeta == meta);
	}
}
