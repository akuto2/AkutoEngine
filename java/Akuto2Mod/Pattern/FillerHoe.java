package Akuto2Mod.Pattern;

import Akuto2Mod.TileEntity.TileFillerEX;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class FillerHoe extends FillerPatternCore{
	public FillerHoe() {
		super(13);
	}

	@Override
	public boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack) {
		return fillerHoe(fillerEX);
	}

	protected boolean fillerHoe(TileFillerEX fillerEX) {
		int y = fillerEX.sy - 1;
		while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
			while(fillerEX.cx >= fillerEX.sx && fillerEX.ex <= fillerEX.ex) {
				if(isDirt(fillerEX.getWorldObj().getBlock(fillerEX.cx, y, fillerEX.cz))){
					fillerEX.getWorldObj().setBlock(fillerEX.cx, y, fillerEX.cz, Blocks.farmland);
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

	private boolean isDirt(Block block) {
		return block == Blocks.dirt || block == Blocks.grass;
	}
}
