package Akuto2Mod.Blocks;

import java.util.Random;

import Akuto2Mod.Akuto2Core;
import Akuto2Mod.TileEntity.TileFillerEX;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFilllerEX extends BlockContainer {
	@SideOnly(Side.CLIENT)
	private IIcon front;
	@SideOnly(Side.CLIENT)
	private IIcon top;
	@SideOnly(Side.CLIENT)
	private IIcon top_On;
	private final Random random = new Random();

	public BlockFilllerEX() {
		super(Material.iron);
		setBlockName("fillerEX");
		setHardness(1.5F);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFillerEX();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			player.openGui(Akuto2Core.instance, 1, world, x, y, z);
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess iBlockAccess, int x, int y, int z, int side) {
		int meta = iBlockAccess.getBlockMetadata(x, y, z);
		TileEntity tileEntity = iBlockAccess.getTileEntity(x, y, z);
		if(tileEntity != null && tileEntity instanceof TileFillerEX) {
			TileFillerEX fillerEX = (TileFillerEX)tileEntity;
			if(side == 1 || side == 0) {
				return fillerEX.hasWork() ? top_On : top;
			}
			else {
				return front;
			}
		}
		else {
			return null;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int meta) {
		return (side == 0 || side == 1) ? top : front;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister register) {
		front = register.registerIcon("akutoengine:filler_front");
		top = register.registerIcon("akutoengine:filler_top");
		top_On = register.registerIcon("akutoengine:filler_top_on");
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int notice) {
		TileFillerEX fillerEX = (TileFillerEX)world.getTileEntity(x, y, z);
		if(fillerEX != null) {
			for(int i = 0; i < fillerEX.getSizeInventory(); ++i) {
				ItemStack stack = fillerEX.getStackInSlot(i);

				if(stack != null) {
					float f = random.nextFloat() * 0.8F + 0.1F;
					float f1 = random.nextFloat() * 0.8F + 0.1F;
					float f2 = random.nextFloat() * 0.8F + 0.1F;

					while(stack.stackSize > 0) {
						int j1 = random.nextInt(21) + 10;

						if(j1 > stack.stackSize) {
							j1 = stack.stackSize;
						}

						stack.stackSize -= j1;
						EntityItem item = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(stack.getItem(), j1, stack.getItemDamage()));

						if(stack.hasTagCompound()) {
							item.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
						}
						float f3 = 0.05F;
						item.motionX = (double)((float)random.nextGaussian() * f3);
						item.motionY = (double)((float)random.nextGaussian() * f3 + 0.2F);
						item.motionZ = (double)((float)random.nextGaussian() * f3);
						world.spawnEntityInWorld(item);
					}
				}
			}
			world.func_147453_f(x, y, z, block);
		}
		super.breakBlock(world, x, y, z, block, notice);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return false;
	}
}
