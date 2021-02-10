package akuto2.akutoengine.blocks;

import java.util.ArrayList;

import akuto2.akutoengine.tiles.TileTankEX;
import buildcraft.factory.BlockTank;
import buildcraft.factory.TileTank;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class BlockTankEX extends BlockTank {
	private IIcon sideBase;
	private IIcon side;
	private IIcon bottom;
	private IIcon top;
	private int currentPass = 0;
	NBTTagCompound worktank = null;

	@Override
	public TileEntity createTileEntity(World world, int meta){
		TileTankEX tankEX = new TileTankEX();
		tankEX.tank.setCapacity(2100000000);
		return tankEX;
	}

	@Override
	public int getRenderBlockPass(){
		return 1;
	}

	@Override
	public boolean canRenderInPass(int pass){
		currentPass = pass;
		return (pass == 0) || (pass == 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register){
		super.registerBlockIcons(register);
		sideBase = register.registerIcon("akutoengine:tank_side_base");
		side = register.registerIcon("akutoengine:tank_side");
		bottom = register.registerIcon("akutoengine:tank_bottom");
		top = register.registerIcon("akutoengine:tank_top");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		switch(side){
		case 0:
		case 1:
			return top;
		}
		return currentPass == 0 ? sideBase : bottom;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess iBlockAccess, int i, int j, int k, int l){
		switch(l){
		case 0:
		case 1:
			return top;
		}
		if((iBlockAccess.getBlock(i, j - 1, k) instanceof BlockTank)){
			return currentPass == 0 ? sideBase : side;
		}
		return currentPass == 0 ? sideBase : bottom;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune){
		ArrayList<ItemStack> itemStacks = super.getDrops(world, x, y, z, meta, fortune);
		NBTTagCompound tagCompound;
		FluidStack stack;
		if(itemStacks != null){
			if(worktank != null){
				for(ItemStack item : itemStacks){
					if(item.stackTagCompound == null){
						item.setTagCompound(new NBTTagCompound());
					}
					tagCompound = item.getTagCompound();
					tagCompound.setTag("tank", worktank);
				}
				worktank = null;
			}
			else{
				TileEntity tileEntity = world.getTileEntity(x, y, z);
				if((tileEntity != null) && (tileEntity instanceof TileTank)){
					stack = ((TileTank)tileEntity).tank.getFluid();
					if(stack != null){
						for(ItemStack item : itemStacks){
							if(item.stackTagCompound == null){
								item.setTagCompound(new NBTTagCompound());
							}
							tagCompound = item.getTagCompound();
							tagCompound.setTag("tank", stack.writeToNBT(new NBTTagCompound()));
						}
					}
				}
			}
		}
		return itemStacks;
	}

	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest){
		if(willHarvest){
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if((tileEntity != null) && (tileEntity instanceof TileTank)){
				FluidStack fluidStack = ((TileTank)tileEntity).tank.getFluid();
				if((fluidStack != null) && (0 < fluidStack.amount)){
					worktank = fluidStack.writeToNBT(new NBTTagCompound());
				}
			}
		}
		else{
			worktank = null;
		}
		return super.removedByPlayer(world, player, x, y, z, willHarvest);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase eBase, ItemStack stack){
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if((tileEntity != null) && (tileEntity instanceof TileTank)){
			TileTank tank = (TileTank)tileEntity;
			if(stack.hasTagCompound()){
				NBTTagCompound tag = stack.getTagCompound();
				FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("tank"));
				tank.tank.setFluid(fluidStack);
			}
		}
		super.onBlockPlacedBy(world, x, y, z, eBase, stack);
	}
}
