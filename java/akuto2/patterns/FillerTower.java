package akuto2.patterns;

import javax.annotation.Nonnull;

import akuto2.tiles.TileEntityFillerEX;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class FillerTower extends FillerPatternCore{
	public FillerTower() {
		super(11);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		return fill(fillerEX.sx, fillerEX.sy, fillerEX.sz, fillerEX.ex, fillerEX.ey, fillerEX.ez, fillerEX, stack);
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.setPower(32, 8);
	}

	@Override
	protected boolean fill(int sx, int sy, int sz, int ex, int ey, int ez, TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		if(stack.isEmpty()) {
			return false;
		}
		while(fillerEX.cy >= sy && fillerEX.cy <= ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(fillerEX.cz >= sz && fillerEX.cz <= ez) {
				while(fillerEX.cx >= sx && fillerEX.cx <= ex) {
					if(isBlock(fillerEX) && isFilling(fillerEX.getWorld(), new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz))) {
						setBlock(fillerEX, new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz), stack);
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

	private boolean isBlock(TileEntityFillerEX fillerEX) {
		return !isSoftBlock(fillerEX.getWorld(), new BlockPos(fillerEX.cx, fillerEX.sy, fillerEX.cz));
	}
}
