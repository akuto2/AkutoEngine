package Akuto2Mod.Pattern;

import Akuto2Mod.TileEntity.TileFillerEX;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class FillerQuarry extends FillerPatternCore{
	public FillerQuarry() {
		super(14);
	}

	@Override
	public boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack) {
		if(fillerEX.now == 0 && quarry(fillerEX)) {
			fillerEX.now++;
		}
		if(fillerEX.now == 1 && flatBedrock(fillerEX)) {
			return true;
		}
		return false;
	}

	@Override
	public void initialize(TileFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.cy = fillerEX.sy - 1;
	}

	protected boolean quarry(TileFillerEX fillerEX) {
		while(fillerEX.cy > 0 && fillerEX.cy <= 255) {
			while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
				while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
					if(isErase(fillerEX)) {
						eraseBlock(fillerEX.getWorldObj(), fillerEX.cx, fillerEX.cy, fillerEX.cz);
						fillerEX.cx++;
						return false;
					}
					fillerEX.cx++;
				}
				fillerEX.cx = fillerEX.sx;
				fillerEX.cz++;
			}
			fillerEX.cz = fillerEX.sz;
			fillerEX.cy--;
		}
		fillerEX.cy = fillerEX.sy - 1;
		return true;
	}

	protected boolean flatBedrock(TileFillerEX fillerEX) {
		int y = 0;
		while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
			while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
				if(isAir(fillerEX.getWorldObj().getBlock(fillerEX.cx, y, fillerEX.cz))) {
					fillerEX.getWorldObj().setBlock(fillerEX.cx, y, fillerEX.cz, Blocks.bedrock);
					fillerEX.cx++;
					return false;
				}
				fillerEX.cx++;
			}
			fillerEX.cx = fillerEX.sx;
			fillerEX.cz++;
		}
		fillerEX.cz = fillerEX.sz;
		return true;
	}
}
