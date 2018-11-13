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

	protected boolean quarry(TileFillerEX fillerEX) {
		int y = fillerEX.sy - 1;
		while(y > 0 && y <= 255) {
			while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
				while(fillerEX.cx >= fillerEX.ex && fillerEX.cx <= fillerEX.ex) {
					if(isErase(fillerEX)) {
						eraseBlock(fillerEX.getWorldObj(), fillerEX.cx, y, fillerEX.cz);
						fillerEX.cx++;
						return false;
					}
					fillerEX.cx++;
				}
				fillerEX.cx = fillerEX.sx;
				fillerEX.cz++;
			}
			fillerEX.cz = fillerEX.sz;
			y--;
		}
		return true;
	}

	protected boolean flatBedrock(TileFillerEX fillerEX) {
		int y = 0;
		while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
			while(fillerEX.cx >= fillerEX.ex && fillerEX.cx <= fillerEX.ex) {
				if(!isAir(fillerEX.getWorldObj().getBlock(fillerEX.cx, y, fillerEX.cz))) {
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
