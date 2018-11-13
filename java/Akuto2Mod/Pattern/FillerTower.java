package Akuto2Mod.Pattern;

import Akuto2Mod.TileEntity.TileFillerEX;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class FillerTower extends FillerPatternCore{

	public FillerTower() {
		super(11);
	}

	@Override
	public boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack) {
		return fill(fillerEX.sx, fillerEX.sy, fillerEX.sz, fillerEX.ex, fillerEX.ey, fillerEX.ez, fillerEX, stack);
	}

	@Override
	public void initialize(TileFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.setPower(32, 8);
	}

	@Override
	protected boolean fill(int sx, int sy, int sz, int ex, int ey, int ez, TileFillerEX fillerEX, ItemStack stack) {
		if(stack == null) {
			return false;
		}
		while(fillerEX.cy >= sy && fillerEX.cy <= ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(fillerEX.cz >= sz && fillerEX.cz <= ez) {
				while(fillerEX.cx >= sx && fillerEX.cx <= ex) {
					if(isBlock(fillerEX) && isFilling(fillerEX.getWorldObj(), fillerEX.cx, fillerEX.cy, fillerEX.cz)) {
						setBlock(fillerEX, fillerEX.cx, fillerEX.cy, fillerEX.cz, stack);
						fillerEX.cx++;
						return false;
					}
					fillerEX.cx++;
				}
				fillerEX.cx = sx;
				fillerEX.cz++;
			}
			fillerEX.cz = sz;
			fillerEX.cy++;
		}
		fillerEX.cy = sy;
		return true;
	}

	private boolean isBlock(TileFillerEX fillerEX) {
		Block checkBlock = fillerEX.getWorldObj().getBlock(fillerEX.cx, fillerEX.sy, fillerEX.cz);
		return checkBlock != Blocks.air && checkBlock.getMaterial() != Material.vine && checkBlock != Blocks.double_plant;
	}
}
