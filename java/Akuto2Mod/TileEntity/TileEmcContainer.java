package Akuto2Mod.TileEntity;

import Akuto2Mod.EMCHandler;
import buildcraft.core.proxy.CoreProxy;
import moze_intel.projecte.api.tile.IEmcAcceptor;
import moze_intel.projecte.gameObjs.container.inventory.TransmutationInventory;
import moze_intel.projecte.gameObjs.tiles.TileEmc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEmcContainer extends TileEmc implements ISidedInventory, IEmcAcceptor{
	// 錬成版の最大保持Emc量
	private static double maxTransEmc = 1.073741824E9D;
	// スロット
	private ItemStack[] containerItems;
	private EntityPlayer player;
	private TransmutationInventory transmutationInventory;

	public TileEmcContainer() {
		player = (EntityPlayer)CoreProxy.proxy.getBuildCraftPlayer((WorldServer)worldObj).get();
		transmutationInventory = new TransmutationInventory(player);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(canChangeEmc()) {

		}
		if(!transmutationInventory.hasMaxedEmc()) {
			double addEmc = (maxTransEmc - transmutationInventory.emc) < currentEMC ? (maxTransEmc - transmutationInventory.emc) : currentEMC;
			transmutationInventory.addEmc(addEmc);
			currentEMC -= addEmc;
		}
	}

	public boolean canChangeEmc() {
		if(containerItems[0] == null) {
			return false;
		}
		return EMCHandler.getEMC(containerItems[0]) != 0;
	}

	@Override
	public int getSizeInventory() {
		return containerItems.length;
	}

	@Override
	public ItemStack getStackInSlot(int meta) {
		return containerItems[meta];
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if(containerItems[par1] != null) {
			ItemStack stack;
			if(containerItems[par1].stackSize <= par2) {
				stack = containerItems[par1];
				containerItems[par1] = null;
				return stack;
			}
			else {
				stack = containerItems[par1].splitStack(par2);
				if(containerItems[par1].stackSize == 0) {
					containerItems[par1] = null;
				}
				return stack;
			}
		}
		else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
	}

	@Override
	public String getInventoryName() {
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return true;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return null;
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
		return false;
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		return false;
	}

	@Override
	public double getMaximumEmc() {
		return maximumEMC;
	}

	@Override
	public double getStoredEmc() {
		return currentEMC;
	}

	@Override
	public double acceptEMC(ForgeDirection side, double toAccept) {
		if ((this.worldObj.getTileEntity(this.xCoord + side.offsetX, this.yCoord + side.offsetY, this.zCoord + side.offsetZ) instanceof TileEmcContainer)) {
		      return 0.0D;
		}
		double toAdd = Math.min(this.maximumEMC - this.currentEMC, toAccept);
	    this.currentEMC += toAdd;
	    return toAdd;
	}
}
