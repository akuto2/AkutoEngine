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

	@Override
	public void initialize(TileFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.cy = fillerEX.sy - 1;
	}

	protected boolean fillerHoe(TileFillerEX fillerEX) {
		while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
			while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
				if(isDirt(fillerEX.getWorldObj().getBlock(fillerEX.cx, fillerEX.cy, fillerEX.cz))){
					fillerEX.getWorldObj().setBlock(fillerEX.cx, fillerEX.cy, fillerEX.cz, Blocks.farmland);
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

	private boolean isDirt(Block block) {
		return block == Blocks.dirt || block == Blocks.grass;
	}
}
