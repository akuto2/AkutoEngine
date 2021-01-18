package akuto2.akutoengine.patterns;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import akuto2.akutoengine.ObjHandler;
import akuto2.akutoengine.tiles.TileEntityFillerEX;
import akuto2.akutoengine.utils.WorldHelper;
import buildcraft.api.core.BuildCraftAPI;
import buildcraft.api.core.IBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSand;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
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

	public boolean iteratePattern(TileEntity fillerEX, IBox box,@Nonnull ItemStack stack) {
		if(fillerEX instanceof TileEntityFillerEX) {
			return iteratePattern((TileEntityFillerEX)fillerEX, stack);
		}
		return false;
	}

	public abstract boolean iteratePattern(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack);

	protected boolean fill(TileEntityFillerEX fillerEX, ItemStack stack) {
		return fill(fillerEX.sx, fillerEX.sy, fillerEX.sz, fillerEX.ex, fillerEX.ey, fillerEX.ez, fillerEX, stack);
	}

	protected boolean fill(int sx, int sy, int sz, int ex, int ey, int ez, TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
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

	protected boolean fill2(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		if(stack.isEmpty())
			return false;

		int y = fillerEX.sy - 1;
		while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
			while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
				if(isFilling(fillerEX.getWorld(), new BlockPos(fillerEX.cx, y, fillerEX.cz))) {
					while(y > 0 && isFilling(fillerEX.getWorld(), new BlockPos(fillerEX.cx, y - 1, fillerEX.cz))) {
						y--;
					}
					setBlock(fillerEX, new BlockPos(fillerEX.cx, y, fillerEX.cz), stack);
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

	protected boolean emptyUD(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		while(fillerEX.cy >= fillerEX.sy && fillerEX.cy <= fillerEX.ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
				while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
					BlockPos pos = new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz);
					Block block = fillerEX.getWorld().getBlockState(pos).getBlock();
					if(!isSoftBlock(fillerEX.getWorld(), pos) && !unbleakBlock(block)) {
						breakBlock(fillerEX.getWorld(), pos, 20);
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
		fillerEX.cy = fillerEX.ey;
		return true;
	}

	protected boolean emptyDU(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		while(fillerEX.cy >= fillerEX.sy && fillerEX.cy <= fillerEX.ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(isRange(fillerEX.sz, fillerEX.ez, fillerEX.cz, fillerEX.mz)) {
				while(isRange(fillerEX.sx, fillerEX.ex, fillerEX.cx, fillerEX.mx)) {
					Block block = fillerEX.getWorld().getBlockState(new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz)).getBlock();
					if(!isSoftBlock(fillerEX.getWorld(), new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz)) && !unbleakBlock(block)) {
						int y = fillerEX.cy;
						Block block2 = fillerEX.getWorld().getBlockState(new BlockPos(fillerEX.cx, y + 1, fillerEX.cz)).getBlock();
						if(fillerEX.ey > fillerEX.sy && y == fillerEX.sy && !isSoftBlock(fillerEX.getWorld(), new BlockPos(fillerEX.cx, y + 1, fillerEX.cz)) && !unbleakBlock(block2))
							y++;

						if(isFallBlock(fillerEX.getWorld().getBlockState(new BlockPos(fillerEX.cx, y + 1, fillerEX.cz)).getBlock())) {
							y++;
							while(isFallBlock(fillerEX.getWorld().getBlockState(new BlockPos(fillerEX.cx, y + 1, fillerEX.cz)).getBlock()))	y++;
						}
						breakBlock(fillerEX.getWorld(), new BlockPos(fillerEX.cx, y, fillerEX.cz), 6000);
						return false;
					}
					else if(isLiquidBlock(block)) {
						ItemStack sand = new ItemStack(Blocks.SAND);
						if(!stack.isEmpty() && stack == sand) {
							IBlockState state = block.getStateFromMeta(1);
							fillerEX.getWorld().setBlockState(new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz), state, 0);
							stack.shrink(1);
							fillerEX.cx += fillerEX.mx;
							return false;
						}
					}
					fillerEX.cx += fillerEX.mx;
				}
				fillerEX.cx = fillerEX.sx;
				fillerEX.cz += fillerEX.mz;
			}
			fillerEX.cz = fillerEX.sz;
			fillerEX.cy++;
		}
		fillerEX.cy = fillerEX.sy;
		return true;
	}

	protected boolean erase(TileEntityFillerEX fillerEX,@Nonnull ItemStack stack) {
		while(fillerEX.cy >= fillerEX.sy && fillerEX.cy <= fillerEX.ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
				while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
					if(isErase(fillerEX)) {
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
			fillerEX.cy--;
		}
		fillerEX.cy = fillerEX.ey;
		return true;
	}

	protected boolean isSoftBlock(World world, BlockPos pos) {
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

	protected boolean isFilling(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return isSoftBlock(world, pos) || state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FLOWING_LAVA;
	}

	protected boolean isErase(TileEntityFillerEX fillerEX) {
		Block block = fillerEX.getWorld().getBlockState(new BlockPos(fillerEX.cx, fillerEX.cy, fillerEX.cz)).getBlock();
		return !isAir(block) && block != ObjHandler.fillerEX && block != Blocks.END_PORTAL && block != Blocks.END_PORTAL_FRAME;
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

	protected boolean isRange(int start, int end, int current, int vector) {
		if(vector > 0) {
			return current >= start && current <= end;
		}
		else if(vector < 0) {
			return current >= end && current <= start;
		}
		return false;
	}

	/**
	 * ブロックの設置
	 */
	protected void setBlock(TileEntityFillerEX fillerEX, BlockPos pos,@Nonnull ItemStack stack) {
		eraseBlock(fillerEX.getWorld(), pos);
		fillerEX.player.setHeldItem(EnumHand.MAIN_HAND, stack);
		if(stack.getItem().onItemUse(fillerEX.player, fillerEX.getWorld(), pos, EnumHand.MAIN_HAND, EnumFacing.UP, 0.0F, 0.0F, 0.0F) != EnumActionResult.SUCCESS) {
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

	/**
	 * ブロックの破壊
	 */
	protected void breakBlock(World world, BlockPos pos, int lifeSpan) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		int metadata = block.getMetaFromState(state);
		if(world.isRemote || isAir(block) || block == null)
			return;

		NonNullList<ItemStack> dropped = NonNullList.create();
		block.getDrops(dropped, world, pos, state, 0);
		for(ItemStack stack : dropped) {
			float range = 0.9F;
			double xx = (double)(world.rand.nextFloat() * range) * (double)(1.0F - range) * 0.5D;
			double yy = (double)(world.rand.nextFloat() * range) * (double)(1.0F - range) * 0.5D;
			double zz = (double)(world.rand.nextFloat() * range) * (double)(1.0F - range) * 0.5D;
			EntityItem item = new EntityItem(world, (double)pos.getX() + xx, (double)pos.getY() + yy, (double)pos.getZ() + zz, stack);
			item.setDefaultPickupDelay();;
			item.lifespan = lifeSpan;
			world.spawnEntity(item);
		}
		world.setBlockToAir(pos);
		SoundType soundType = block.getSoundType(state, world, pos, null);
		world.playSound(pos.getX() + 0.5F , pos.getY() + 0.5F, pos.getZ() + 0.5F, soundType.getBreakSound(), SoundCategory.BLOCKS, (soundType.volume + 1.0F) / 2.0F, soundType.getPitch() * 0.8F, false);
	}

	/**
	 * ブロックの消去(アイテム化させない)
	 */
	protected void eraseBlock(World world, BlockPos pos) {
		WorldHelper.setBlockToAir(world, pos);
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
	}
}
