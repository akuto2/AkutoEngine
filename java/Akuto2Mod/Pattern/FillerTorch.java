package Akuto2Mod.Pattern;

import Akuto2Mod.Akuto2Core;
import Akuto2Mod.TileEntity.TileFillerEX;
import net.minecraft.item.ItemStack;

public class FillerTorch extends FillerPatternCore{
	public FillerTorch() {
		super(10);
	}

	@Override
	public boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack) {
		return set(fillerEX, stack);
	}

	@Override
	public void initialize(TileFillerEX fillerEX) {
		fillerEX.initTargetPosition();
		fillerEX.mx = fillerEX.sx <= fillerEX.xCoord ? fillerEX.sx : fillerEX.cx;
		fillerEX.mz = fillerEX.sz <= fillerEX.zCoord ? fillerEX.sz : fillerEX.ez;
		fillerEX.setPower(32, 16);
	}

	protected boolean set(TileFillerEX fillerEX, ItemStack stack) {
		if(stack == null) {
			return false;
		}
		while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
			while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
				if(isFilling(fillerEX.getWorldObj(), fillerEX.cx, fillerEX.cy, fillerEX.cz) && Math.abs(fillerEX.cx - fillerEX.mx) % Akuto2Core.intervalTorch == 0 && Math.abs(fillerEX.cz - fillerEX.mz) % Akuto2Core.intervalTorch == 0) {
					setBlock(fillerEX, fillerEX.cx, fillerEX.cy, fillerEX.cz, stack);
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
