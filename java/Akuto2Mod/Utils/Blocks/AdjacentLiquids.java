package Akuto2Mod.Utils.Blocks;

import Akuto2Mod.Utils.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class AdjacentLiquids extends AdjacentBlocks{
	public Block ice;
	private int type;

	public AdjacentLiquids(World world, int x, int y, int z, int range) {
		super(world, x, y, z, range);
	}

	public AdjacentLiquids(World world, int x, int y, int z) {
		this(world, x, y, z, 64);
	}

	public AdjacentLiquids(TileEntity tileEntity, int type) {
		this(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, 64);
		this.type = type;
		System.out.println("Adjacent");
	}

	@Override
	public void setTarget(Block block, int meta) {
		System.out.println("setTarget");
		target = block;
		targetMeta = 0;
	}

	public boolean isPumpableLiquids() {
		try {
			return isBlockConnection(1);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isBlockConnection(int x, int y, int z, int mode) {
		add(x, y, z, 0);

	    int i = 0;
	    while (i < this.blocks.size()){
	      PosRange pos = (PosRange)this.blocks.get(i);

	      int px = pos.x;
	      int py = pos.y;
	      int pz = pos.z;
	      int pr = pos.range + 1;
	      checkBlock(px + 1, py, pz, pr, mode);
	      checkBlock(px - 1, py, pz, pr, mode);
	      checkBlock(px, py, pz - 1, pr, mode);
	      checkBlock(px, py, pz + 1, pr, mode);
	      checkBlock(px, py + 1, pz, pr, mode);
	      checkBlock(px, py - 1, pz, pr, mode);

	      i++;
	    }
	    return this.blocks.size() > 0;
	}

	@Override
	protected boolean checkBlock(int x, int y, int z, int range, int mode) {
		if((y > 255) || (y < 0)) {
			return false;
		}
		if((isRange(x, y, z, range, mode)) && (!contains(x, y, z))) {
			if(canAddBlock(world.getBlock(x, y, z))) {
				add(x, y, z, range);
			}
		}
		return false;
	}

	@Override
	protected boolean isRange(int x, int y, int z, int range, int mode) {
		int rx = x - xCoord;
		int ry = Math.abs(y - yCoord);
		int rz = z - zCoord;
		return (rx * rx + rz * rz <= radius) && (ry <= rangeMax);
	}

	public boolean canAddBlock(Block block) {
		return block == target || (ice != null) && (block == ice);
	}

	public boolean isInit() {
		return blocks != null && !blocks.isEmpty();
	}

	public boolean doWork() {
		if(!blocks.isEmpty()) {
			System.out.println("doWork");
			int index = blocks.size() - 1;
			PosRange pos = (PosRange)blocks.get(index);
			int px = pos.x;
			int py = pos.y;
			int pz = pos.z;
			Block block = world.getBlock(px, py, pz);
			int meta = world.getBlockMetadata(px, py, pz);
			blocks.remove(index);
			if(canAddBlock(block)) {
				System.out.println("canAdd");
				WorldHelper.setBlockToAir(world, px, py, pz);
				world.markBlockForUpdate(px, py, pz);
				return meta == 0;
			}
		}
		return false;
	}
}
