package Akuto2Mod.Pattern;

import Akuto2Mod.TileEntity.TileFillerEX;
import net.minecraft.item.ItemStack;

public class FillerClearLiquid extends FillerPatternCore{

	public FillerClearLiquid() {
		super(12);
	}

	@Override
	public boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack) {
		return clearLiquid(fillerEX);
	}

	@Override
	public void initialize(TileFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.setPower(16, 256);
	}

	public boolean clearLiquid(TileFillerEX fillerEX) {
		while(fillerEX.cy >= fillerEX.sy && fillerEX.cy <= fillerEX.ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
				while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
					if(isLiquidBlock(fillerEX.getWorldObj().getBlock(fillerEX.cx, fillerEX.cy, fillerEX.cz))) {
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
			fillerEX.cy++;
		}
		fillerEX.cy = fillerEX.sy;
		return true;
	}
}
