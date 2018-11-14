package Akuto2Mod.Blocks;

import java.util.List;

import Akuto2Mod.Akuto2Core;
import Akuto2Mod.TileEntity.TileEntityUmfsu;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.tile.IWrenchable;
import ic2.core.Ic2Items;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.wiring.TileEntityElectricBlock;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockUMFSU extends Block {
	@SideOnly(Side.CLIENT)
	private IIcon top;
	@SideOnly(Side.CLIENT)
	private IIcon output;
	@SideOnly(Side.CLIENT)
	private IIcon input;

	public BlockUMFSU(){
		super(Material.iron);
		setHardness(1.5F);
		setStepSound(soundTypeMetal);
		setBlockName("UMFSU");
	}

	@Override
	public IIcon getIcon(IBlockAccess iBlockAccess, int x, int y, int z, int side){
		TileEntity tile = iBlockAccess.getTileEntity(x, y, z);
		if((tile instanceof TileEntityBlock)){
			switch (new Short(((TileEntityBlock)tile).getFacing()).intValue()) {
			case 0:
				switch(side){
				case 2:
					return this.top;
				case 3:
					return this.top;
				case 0:
					return this.output;
				}
				return this.input;
			case 1:
				switch(side){
				case 2:
					return this.top;
				case 3:
					return this.top;
				case 1:
					return this.output;
				}
				return this.input;
			case 2:
		        switch (side)
		        {
		        case 0:
		          return this.top;
		        case 1:
		          return this.top;
		        case 2:
		          return this.output;
		        }
		        return this.input;
		      case 3:
		        switch (side)
		        {
		        case 0:
		          return this.top;
		        case 1:
		          return this.top;
		        case 3:
		          return this.output;
		        }
		        return this.input;
		      case 4:
		        switch (side)
		        {
		        case 0:
		          return this.top;
		        case 1:
		          return this.top;
		        case 4:
		          return this.output;
		        }
		        return this.input;
		      case 5:
		        switch (side)
		        {
		        case 0:
		          return this.top;
		        case 1:
		          return this.top;
		        case 5:
		          return this.output;
		        }
		        return this.input;
			}
			return this.input;
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata){
	    switch (side){
	    case 0:
	      return this.top;
	    case 1:
	      return this.top;
	    case 3:
	      return this.output;
	    }
	    return this.input;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister){
		this.top = iconRegister.registerIcon("akutoengine:umfsu_top");
		this.output = iconRegister.registerIcon("akutoengine:umfsu_output");
		this.input = iconRegister.registerIcon("akutoengine:umfsu_input");
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess iBlockAccess, int x, int y, int z, int side){
		TileEntity tile = iBlockAccess.getTileEntity(x, y, z);
		if((tile instanceof TileEntityElectricBlock)){
			return ((TileEntityElectricBlock)tile).isEmittingRedstone() ? 15 : 0;
		}
		return isProvidingStrongPower(iBlockAccess, x, y, z, side);
	}

	@Override
	public boolean canProvidePower(){
		return true;
	}

	@Override
	public boolean isNormalCube(IBlockAccess iBlockAccess, int i, int j, int k){
		return false;
	}

	@Override
	public boolean isBlockSolid(IBlockAccess iBlockAccess, int i, int j, int k, int side){
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase eBase, ItemStack itemStack){
		TileEntity tile = world.getTileEntity(x, y, z);
	    if ((tile instanceof TileEntityElectricBlock))
	    {
	      TileEntityElectricBlock electricBlock = (TileEntityElectricBlock)tile;
	      NBTTagCompound nbt = StackUtil.getOrCreateNbtData(itemStack);
	      electricBlock.energy = nbt.getDouble("energy");
	      if (eBase == null) {
	        electricBlock.setFacing(convertIntegerToShort(1));
	      } else {
	        electricBlock.setFacing(convertIntegerToShort(BlockPistonBase.determineOrientation(world, x, y, z, eBase)));
	      }
	    }
	}

	private static short convertIntegerToShort(int integer_n){
	    return new Integer(integer_n).shortValue();
	}

	@Override
	public boolean hasComparatorInputOverride(){
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int side){
		TileEntity tile = world.getTileEntity(x, y, z);
		if((tile instanceof TileEntityElectricBlock)){
		      TileEntityElectricBlock teb = (TileEntityElectricBlock)tile;
		      return new Long(Math.round(Util.map(teb.energy, teb.maxStorage, 15.0D))).intValue();
		}
		return getComparatorInputOverride(world, x, y, z, side);
	}

	@Override
	public final boolean hasTileEntity(int metadata){
	    return true;
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z){
	    return false;
	}

	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection forgeDirection){
		if(forgeDirection == ForgeDirection.UNKNOWN){
			return false;
		}
		TileEntity tile = world.getTileEntity(x, y, z);
		if((tile instanceof IWrenchable)){
			IWrenchable te = (IWrenchable)tile;
			short newFacing = convertIntegerToShort(ForgeDirection.getOrientation(te.getFacing()).getRotation(forgeDirection).ordinal());
			if(te.wrenchCanSetFacing(null, newFacing)){
				te.setFacing(newFacing);
			}
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createTileEntity(World world, int mete){
		return new TileEntityUmfsu();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int var6, float var7, float var8, float var9){
		String EquippedItemID;
		String ItemID;
		if(player.getHeldItem() != null){
			EquippedItemID = player.getCurrentEquippedItem().toString();
			EquippedItemID = EquippedItemID.substring(0, EquippedItemID.lastIndexOf("@"));
			ItemID = Ic2Items.wrench.toString();
			ItemID = ItemID.substring(0, ItemID.lastIndexOf("@"));
			if(EquippedItemID.equals(ItemID)){
				return true;
			}
			ItemID = Ic2Items.electricWrench.toString();
			ItemID = ItemID.substring(0, ItemID.lastIndexOf("@"));
			if(EquippedItemID.equals(ItemID)){
				return true;
			}
		}
		if(!player.isSneaking()){
			player.openGui(Akuto2Core.instance, 0, world, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List stackList){
		ItemStack zeroStack = new ItemStack(this, 1, 0);
	    StackUtil.getOrCreateNbtData(zeroStack).setInteger("energy", 0);
	    stackList.add(zeroStack);
	    ItemStack fullStack = new ItemStack(this, 1, 1);
	    StackUtil.getOrCreateNbtData(fullStack).setInteger("energy", 200000000);
	    stackList.add(fullStack);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6){
		TileEntity tile = world.getTileEntity(x, y, z);
		if((tile instanceof IInventory)){
			IInventory iInventory = (IInventory)tile;
			for(int j1 = 0; j1 < iInventory.getSizeInventory(); j1++){
				ItemStack itemStack = iInventory.getStackInSlot(j1);
				if(itemStack != null){
					float f = world.rand.nextFloat() * 0.8F + 0.1F;
					float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
					float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
					while(itemStack.stackSize > 0){
						int k1 = world.rand.nextInt(21) + 10;
						if(k1 > itemStack.stackSize){
							k1 = itemStack.stackSize;
						}
						itemStack.stackSize -= k1;
						EntityItem entityitem = new EntityItem(world, x + f, y + f1, z + f2, new ItemStack(itemStack.getItem(), k1, itemStack.getItemDamage()));
						if (itemStack.hasTagCompound()) {
				              entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
				            }
				            entityitem.motionX = (world.rand.nextGaussian() * 0.05000000074505806D);
				            entityitem.motionY = (world.rand.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D);
				            entityitem.motionZ = (world.rand.nextGaussian() * 0.05000000074505806D);
				            world.spawnEntityInWorld(entityitem);
					}
				}
			}
			world.func_147453_f(x, y, z, block);
		}
		super.breakBlock(world, x, y, z, block, par6);
	}
}
