package Akuto2Mod.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Akuto2Mod.Akuto2Core;
import Akuto2Mod.TileEntity.TileFillerEX;
import Akuto2Mod.Utils.WorldHelper;
import buildcraft.api.core.BuildCraftAPI;
import buildcraft.api.core.IBox;
import buildcraft.core.proxy.CoreProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class FillerPatternCore{
	public ItemStack moduleItem;
	private int meta;
	public static final Map<Integer, FillerPatternCore> pattern = new TreeMap();
	protected boolean creative = false;
	@SideOnly(Side.CLIENT)
	public IIcon icon;

	public FillerPatternCore(int i) {
		meta = i;
		pattern.put(Integer.valueOf(i), this);
	}

	public IIcon getIcon() {
		return icon;
	}

	public void registerIcon(IIconRegister register) {
		icon = register.registerIcon("akutoengine:pattern/fillerpattern_" + meta);
	}

	public void initialize(TileFillerEX fillerEX) {
		fillerEX.initTargetPosition();
		fillerEX.setPower(25, 4);
	}

	public boolean iteratePattern(TileEntity fillerEX, IBox box, ItemStack stack) {
		creative = isCreative(fillerEX.getWorldObj());
		if(fillerEX instanceof TileFillerEX) {
			return iteratePattern((TileFillerEX)fillerEX, stack);
		}
		return false;
	}

	public abstract boolean iteratePattern(TileFillerEX fillerEX, ItemStack stack);

	protected boolean fill(TileFillerEX fillerEX, ItemStack stack) {
		return fill(fillerEX.sx, fillerEX.sy, fillerEX.sz, fillerEX.ex, fillerEX.ey, fillerEX.ez, fillerEX, stack);
	}

	protected boolean fill(int sx, int sy, int sz, int ex, int ey, int ez, TileFillerEX fillerEX, ItemStack stack) {
		if(stack == null) {
			return false;
		}
		while(fillerEX.cy >= sy && fillerEX.cy <= ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(fillerEX.cz >= sz && fillerEX.cz <= ez) {
				while(fillerEX.cx >= sx && fillerEX.cx <= ex) {
					if(isFilling(fillerEX.getWorldObj(), fillerEX.cx, fillerEX.cy, fillerEX.cz)) {
						setBlock(fillerEX, fillerEX.cx, fillerEX.cy, fillerEX.cz, stack);
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
		}fillerEX.cy = sy;
		return true;
	}

	protected boolean fill2(TileFillerEX fillerEX, ItemStack stack) {
		if(stack == null) {
			return false;
		}
		int y = fillerEX.sy - 1;
		while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
			while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
				if(isFilling(fillerEX.getWorldObj(), fillerEX.cx, y, fillerEX.cz)) {
					while(y > 0 && isFilling(fillerEX.getWorldObj(), fillerEX.cx, y - 1, fillerEX.cz)) {
						y--;
					}
					setBlock(fillerEX, fillerEX.cx, y, fillerEX.cz, stack);
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

	protected boolean isFilling(World world, int x, int y, int z) {
		return BuildCraftAPI.isSoftBlock(world, x, y, z) || world.getBlock(x, y, z) == Blocks.lava || world.getBlock(x, y, z) == Blocks.flowing_lava;
	}

	protected boolean emptyUD(TileFillerEX fillerEX, ItemStack stack) {
		while(fillerEX.cy >= fillerEX.sy && fillerEX.cy <= fillerEX.ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
				while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
					Block block = fillerEX.getWorldObj().getBlock(fillerEX.cx, fillerEX.cy, fillerEX.cz);
					if(!BuildCraftAPI.isSoftBlock(fillerEX.getWorldObj(), fillerEX.cx, fillerEX.cy, fillerEX.cz) && !unbleakBlock(block)) {
						breakBlock(fillerEX.getWorldObj(), fillerEX.cx, fillerEX.cy, fillerEX.cz, 5980);
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

	protected boolean emptyDU(TileFillerEX fillerEX, ItemStack stack) {
		while(fillerEX.cy >= fillerEX.sy && fillerEX.cy <= fillerEX.ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(isRange(fillerEX.sz, fillerEX.ez, fillerEX.cz, fillerEX.mz)) {
				while(isRange(fillerEX.sx, fillerEX.ex, fillerEX.cx, fillerEX.mx)) {
					Block block = fillerEX.getWorldObj().getBlock(fillerEX.cx, fillerEX.cy, fillerEX.cz);
					if(!BuildCraftAPI.isSoftBlock(fillerEX.getWorldObj(), fillerEX.cx, fillerEX.cy, fillerEX.cz) && !unbleakBlock(block)) {
						int y = fillerEX.cy;
						Block block2 = fillerEX.getWorldObj().getBlock(fillerEX.cx, y + 1, fillerEX.cz);
						if(fillerEX.ey > fillerEX.sy && y == fillerEX.sy && !BuildCraftAPI.isSoftBlock(fillerEX.getWorldObj(), fillerEX.cx, y + 1, fillerEX.cz) && !unbleakBlock(block2)) {
							y++;
						}
						if(isFallBlock(fillerEX.getWorldObj().getBlock(fillerEX.cx, y + 1, fillerEX.cz))) {
							y++;
							while(isFallBlock(fillerEX.getWorldObj().getBlock(fillerEX.cx, y + 1, fillerEX.cz))) y++;
						}
						breakBlock(fillerEX.getWorldObj(), fillerEX.cx, y, fillerEX.cz, 0);
						return false;
					}
					else if(isLiquidBlock(block)) {
						ItemStack sand = new ItemStack(Blocks.sand);
						if(stack != null && stack == sand) {
							fillerEX.getWorldObj().setBlockMetadataWithNotify(fillerEX.cx, fillerEX.cy, fillerEX.cz, 1, 0);
							stack.stackSize--;
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

	public boolean isLiquidBlock(Block block) {
		return block.getMaterial().isLiquid();
	}

	public boolean isFallBlock(Block block) {
		return block instanceof BlockSand;
	}

	public boolean unbleakBlock(Block block) {
		return block == Blocks.bedrock || block == Blocks.lava || block == Blocks.flowing_lava || block == Blocks.end_portal_frame || block == Blocks.end_portal;
	}

	public boolean isAir(Block block) {
		return block == Blocks.air;
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

	protected boolean erase(TileFillerEX fillerEX, ItemStack stack) {
		while(fillerEX.cy >= fillerEX.sy && fillerEX.cy <= fillerEX.ey && fillerEX.cy <= 255 && fillerEX.cy > 0) {
			while(fillerEX.cz >= fillerEX.sz && fillerEX.cz <= fillerEX.ez) {
				while(fillerEX.cx >= fillerEX.sx && fillerEX.cx <= fillerEX.ex) {
					if(isErase(fillerEX)) {
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
			fillerEX.cy--;
		}
		fillerEX.cy = fillerEX.ey;
		return true;
	}

	protected boolean isErase(TileFillerEX fillerEX) {
		Block block = fillerEX.getWorldObj().getBlock(fillerEX.cx, fillerEX.cy, fillerEX.cz);
		return block != Blocks.air && block != Akuto2Core.fillerEX && block != Blocks.end_portal && block != Blocks.end_portal_frame;
	}

	protected void eraseBlock(World world, int x, int y, int z) {
		WorldHelper.setBlockToAir(world, x, y, z);
		world.markBlockForUpdate(x, y, z);
	}

	protected void breakBlock(World world, int x, int y, int z, int age) {
		Block block = world.getBlock(x, y, z);
		int metadata = world.getBlockMetadata(x, y, z);
		if(world.isRemote || block == Blocks.air || block == null) {
			return;
		}
		ArrayList<ItemStack> dropped = block.getDrops(world, x, y, z, metadata, 0);
		for(ItemStack stack : dropped) {
			float range = 0.9F;
			double xx = (double)(world.rand.nextFloat() * range) + (double)(1.0F - range) * 0.5D;
			double yy = (double)(world.rand.nextFloat() * range) + (double)(1.0F - range) * 0.5D;
			double zz = (double)(world.rand.nextFloat() * range) + (double)(1.0F - range) * 0.5D;
			EntityItem item = new EntityItem(world, (double)x + xx, (double)y + yy, (double)z + zz, stack);
			item.delayBeforeCanPickup = 10;
			item.age = age;
			world.spawnEntityInWorld(item);
		}
		world.setBlockToAir(x, y, z);
		world.playSoundEffect((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, block.stepSound.getBreakSound(), (block.stepSound.volume + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
	}

	ItemStack oldStack = null;
	protected void setBlock(TileFillerEX fillerEX, int x, int y, int z, ItemStack stack) {
		fillerEX.getWorldObj().setBlock(x, y, z, Blocks.air);
		fillerEX.player.inventory.setInventorySlotContents(0, stack);
		if(!stack.getItem().onItemUse(stack, fillerEX.player, fillerEX.getWorldObj(), x, y, z, 1, 0.0F, 0.0F, 0.0F)) {
			List entityList = fillerEX.getWorldObj().getEntitiesWithinAABBExcludingEntity(CoreProxy.proxy.getBuildCraftPlayer((WorldServer)fillerEX.getWorldObj()).get(), AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)x + 1.0D, (double)y + 1.0D, (double)z + 1.0D));
			for(Object obj : entityList) {
				if(obj instanceof EntityLiving) {
					((EntityLiving)obj).setDead();
				}
			}
			stack.getItem().onItemUse(stack, fillerEX.player, fillerEX.getWorldObj(), x, y, z, 1, 0.0F, 0.0F, 0.0F);
		}
		fillerEX.player.inventory.setInventorySlotContents(0, null);
	}

	public boolean isSetBlock(ItemStack stack) {
		return true;
	}

	public boolean isCreative(World worldObj) {
		return	CoreProxy.proxy.getBuildCraftPlayer((WorldServer)worldObj).get().capabilities.isCreativeMode;
	}
}
