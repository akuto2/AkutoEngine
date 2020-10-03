package akuto2.patterns;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import akuto2.tiles.TileEntityFillerEX;
import buildcraft.api.core.BuildCraftAPI;
import buildcraft.api.core.IBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class FillerPatternCore {
	public ItemStack moduleItem = ItemStack.EMPTY;
	private int meta;
	public static final Map<Integer, FillerPatternCore> PATTERN = new TreeMap();

	public FillerPatternCore(int i) {
		meta = i;
		PATTERN.put(i, this);
	}

	public void initialize(TileEntityFillerEX fillerEX) {
		fillerEX.initTargetPosition();
		fillerEX.setPower(25, 4);
	}

	public boolean iteratePattern(TileEntity fillerEX, IBox box, ItemStack stack) {
		if(fillerEX instanceof TileEntityFillerEX) {
			return iteratePattern((TileEntityFillerEX)fillerEX, stack);
		}
		return false;
	}

	public abstract boolean iteratePattern(TileEntityFillerEX fillerEX, ItemStack stack);

	protected boolean fill(TileEntityFillerEX fillerEX, ItemStack stack) {
		return fill(fillerEX.sx, fillerEX.sy, fillerEX.sz, fillerEX.ex, fillerEX.ey, fillerEX.ez, fillerEX, stack);
	}

	protected boolean fill(int sx, int sy, int sz, int ex, int ey, int ez, TileEntityFillerEX fillerEX, ItemStack stack) {
		if(stack.isEmpty())
			return false;

		while(fillerEX.cy >= sy && fillerEX.cy <= ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(fillerEX.cz >= sz && fillerEX.cz <= ez) {
				while(fillerEX.cx >= sx && fillerEX.cx <= ex) {
					if(isFilling(fillerEX.getWorld(), new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz))) {
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

	protected boolean isFilling(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return isSoftBlock(world, pos) || state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FLOWING_LAVA;
	}

	private boolean isSoftBlock(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		if(block == null) {
			return true;
		}
		if(isAir(block) || isLiquidBlock(block)) {
			return true;
		}
		if(block == Blocks.SNOW_LAYER || block == Blocks.VINE || block == Blocks.FIRE) {
			return true;
		}
		if(block.isReplaceable(world, pos)) {
			return true;
		}

		return false;
	}

	public boolean isLiquidBlock(Block block) {
		return block instanceof BlockLiquid;
	}

	public boolean isFallBlock(Block block) {
		return block instanceof BlockSand;
	}

	public boolean unbleakBlock(Block block) {
		return block == Blocks.BEDROCK || block == Blocks.LAVA || block == Blocks.FLOWING_LAVA || block == Blocks.END_PORTAL || block == Blocks.END_PORTAL_FRAME;
	}

	public boolean isAir(Block block) {
		return block == Blocks.AIR;
	}

	protected void setBlock(TileEntityFillerEX fillerEX, BlockPos pos, ItemStack stack) {
		fillerEX.getWorld().setBlockToAir(pos);
		fillerEX.player.inventory.setInventorySlotContents(0, stack);
		if(stack.getItem().onItemUse(fillerEX.player, fillerEX.getWorld(), pos, EnumHand.MAIN_HAND, EnumFacing.UP, 0.0F, 0.0F, 0.0F) == EnumActionResult.FAIL) {
			List entityList = fillerEX.getWorld().getEntitiesWithinAABBExcludingEntity(BuildCraftAPI.fakePlayerProvider.getBuildCraftPlayer((WorldServer)fillerEX.getWorld()), new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 1.0D, pos.getZ() + 1.0D));
			for(Object obj : entityList) {
				if(obj instanceof EntityLiving) {
					((EntityLiving)obj).setDead();
				}
			}
			stack.getItem().onItemUse(fillerEX.player, fillerEX.getWorld(), pos, EnumHand.MAIN_HAND, EnumFacing.UP, 0.0F, 0.0F, 0.0F);
		}
		fillerEX.player.inventory.setInventorySlotContents(0, ItemStack.EMPTY);
	}
}
