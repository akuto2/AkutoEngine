package akuto2.akutoengine.patterns;

import javax.annotation.Nonnull;

import akuto2.akutoengine.ObjHandler;
import akuto2.akutoengine.tiles.TileEntityFillerEX;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class FillerFlooring extends FillerPatternCore{
	public FillerFlooring() {
		super(9);
	}

	@Override
	public boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		return flooring(fillerEX, stack);
	}

	@Override
	public void initialize(TileEntityFillerEX fillerEX) {
		super.initialize(fillerEX);
		fillerEX.cy--;
		fillerEX.setPower(32, 128);
	}

	private boolean flooring(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		if(stack.isEmpty()) {
			return false;
		}
		while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
			while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
				if(isChange(fillerEX, stack)) {
					setBlock(fillerEX, new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz), stack);
					IBlockState state = fillerEX.getWorld().getBlockState(new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz));
					fillerEX.getWorld().notifyBlockUpdate(new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz), state, state, 3);
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

	private boolean isChange(TileEntityFillerEX fillerEX,@Nonnull ItemStack checkStack) {
		Block changeBlock = fillerEX.getWorld().getBlockState(new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz)).getBlock();
		ItemStack stack = new ItemStack(changeBlock);
		return changeBlock != Blocks.AIR && changeBlock != ObjHandler.fillerEX && stack != checkStack;
	}
}
