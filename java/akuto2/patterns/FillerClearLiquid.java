package akuto2.patterns;

import javax.annotation.Nonnull;

import akuto2.tiles.TileEntityFillerEX;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class FillerClearLiquid extends FillerPatternCore{
	public FillerClearLiquid() {
		super(12);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		return clearLiquid(fillerEX);
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.setPower(16, 256);
	}

	private boolean clearLiquid(TileEntityFillerEX fillerEX) {
		while(fillerEX.cy >= fillerEX.sy && fillerEX.cy <= fillerEX.ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
				while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
					if(isLiquidBlock(fillerEX.getWorld().getBlockState(new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz)).getBlock())) {
						eraseBlock(fillerEX.getWorld(), new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz));
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
