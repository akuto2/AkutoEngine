package Akuto2Mod.TileEntity;

import java.lang.ref.WeakReference;

import buildcraft.api.core.IInvSlot;
import buildcraft.core.lib.gui.ContainerDummy;
import buildcraft.core.lib.inventory.InvUtils;
import buildcraft.core.lib.inventory.InventoryConcatenator;
import buildcraft.core.lib.inventory.InventoryIterator;
import buildcraft.core.lib.inventory.SimpleInventory;
import buildcraft.core.lib.inventory.StackHelper;
import buildcraft.core.lib.utils.CraftingUtils;
import buildcraft.core.lib.utils.Utils;
import buildcraft.core.proxy.CoreProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;

public class TileAutoWorkBench extends TileEntity implements ISidedInventory{
	public static final int SLOT_RESULT = 9;
	private static final int [] SLOTS = Utils.createSlotArray(0, 10);

	public LocalInventoryCrafting craftMatrix = new LocalInventoryCrafting();
	private SimpleInventory resultInv = new SimpleInventory(1, "AutoWorkBench", 64);
	private SimpleInventory inputInv = new SimpleInventory(9, "AutoWorkBench", 64) {
		@Override
		public void setInventorySlotContents(int slodId, ItemStack stack) {
			super.setInventorySlotContents(slodId, stack);
			if(craftMatrix.isInputMissing && getStackInSlot(slodId) != null) {
				craftMatrix.isInputMissing = false;
			}
		}

		@Override
		public void markDirty() {
			super.markDirty();
			craftMatrix.isInputMissing = false;
		}
	};

	private IInventory inv = InventoryConcatenator.make().add(inputInv).add(resultInv).add(craftMatrix);

	private SlotCrafting craftSlot;
	private InventoryCraftResult craftResult = new InventoryCraftResult();

	private int[] bindings = new int[9];
	private int[] bindingCounts = new int[9];

	private boolean scheduledCacheRebuild = false;

	public class LocalInventoryCrafting extends InventoryCrafting{
		public IRecipe currentRecipe;
		public boolean useBindings;
		public boolean isOutputJammed;
		public boolean isInputMissing;

		public LocalInventoryCrafting() {
			super(new ContainerDummy(), 3, 3);
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			if(useBindings) {
				if((slot >= 0) && (slot < 9) && (bindings[slot] >= 0)) {
					return inputInv.getStackInSlot(bindings[slot]);
				}
				else {
					return null;
				}
			}
			else {
				return super.getStackInSlot(slot);
			}
		}

		public ItemStack getRecipeOutput() {
			currentRecipe = findRecipe();
			if(currentRecipe == null) {
				return null;
			}
			ItemStack result = currentRecipe.getCraftingResult(this);
			if(result != null) {
				result = result.copy();
			}
			return result;
		}

		private IRecipe findRecipe() {
			for(IInvSlot slot : InventoryIterator.getIterable(this, ForgeDirection.UP)) {
				ItemStack stack = slot.getStackInSlot();
				if(stack == null) {
					continue;
				}
				if(stack.getItem().hasContainerItem(stack)) {
					return null;
				}
			}

			return CraftingUtils.findMatchingRecipe(craftMatrix, worldObj);
		}

		public void rebuildCache() {
			currentRecipe = findRecipe();

			ItemStack result = getRecipeOutput();
			ItemStack resultInfo = resultInv.getStackInSlot(0);

			if(resultInfo != null && !StackHelper.canStacksMerge(resultInfo, result) || resultInfo.stackSize + result.stackSize > resultInfo.getMaxStackSize()) {
				isOutputJammed = true;
			}
			else {
				isOutputJammed = false;
			}
		}

		@Override
		public void setInventorySlotContents(int slot, ItemStack stack) {
			if(useBindings) {
				if(slot >= 0 && slot < 9 && bindings[slot] >= 0) {
					inputInv.setInventorySlotContents(bindings[slot], stack);
				}
				return;
			}
			super.setInventorySlotContents(slot, stack);
			scheduledCacheRebuild = true;
		}

		@Override
		public void markDirty() {
			super.markDirty();
			scheduledCacheRebuild = true;
		}

		@Override
		public ItemStack decrStackSize(int slot, int amount) {
			if(useBindings) {
				if(slot >= 0 && slot < 9 && bindings[slot] >= 0) {
					return inputInv.decrStackSize(bindings[slot], amount);
				}
				else {
					return null;
				}
			}
			scheduledCacheRebuild = true;
			return decrStackSize(slot, amount);
		}

		public void setUseBindings(boolean use) {
			useBindings = use;
		}
	}

	public WeakReference<EntityPlayer> getInternalPlayer(){
		return CoreProxy.proxy.getBuildCraftPlayer((WorldServer)worldObj, xCoord, yCoord + 1, zCoord);
	}

	@Override
	public void markDirty() {
		super.markDirty();
		inv.markDirty();
	}

	@Override
	public int getSizeInventory() {
		return 10;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		return inv.decrStackSize(slot, count);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv.setInventorySlotContents(slot, stack);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return inv.getStackInSlot(slot);
	}

	@Override
	public String getInventoryName() {
		return "";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		resultInv.readFromNBT(compound);
		if(compound.hasKey("input")) {
			InvUtils.readInvFromNBT(inputInv, "input", compound);
			InvUtils.readInvFromNBT(craftMatrix, "matrix", compound);
		}
		else {
			InvUtils.readInvFromNBT(inputInv, "matrix", compound);
			for(int i = 0; i < 9; i++) {
				ItemStack inputStack = inputInv.getStackInSlot(i);
				if(inputStack != null) {
					ItemStack matrixStack = inputStack.copy();
					matrixStack.stackSize = 1;
					craftMatrix.setInventorySlotContents(i, matrixStack);
				}
			}
		}

		craftMatrix.rebuildCache();
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		resultInv.writeToNBT(compound);
		InvUtils.writeInvToNBT(inputInv, "input", compound);
		InvUtils.writeInvToNBT(craftMatrix, "matrix", compound);
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if(worldObj.isRemote) {
			return;
		}

		if(scheduledCacheRebuild) {
			craftMatrix.rebuildCache();
			scheduledCacheRebuild = false;
		}

		if(craftMatrix.isOutputJammed || craftMatrix.isInputMissing || craftMatrix.currentRecipe == null) {
			return;
		}

		if(craftSlot == null) {
			craftSlot = new SlotCrafting(getInternalPlayer().get(), craftMatrix, craftResult, 0, 0, 0);
		}

		updateCrafting();
	}

	private void updateCrafting() {
		for(int i = 0; i < 9; i++) {
			bindingCounts[i] = 0;
		}
		for(int i = 0; i < 9; i++) {
			ItemStack comparedStack = craftMatrix.getStackInSlot(i);
			if(comparedStack == null || comparedStack.getItem() == null) {
				bindings[i] = -1;
				continue;
			}

			if(bindings[i] == -1 || !StackHelper.isMatchingItem(inputInv.getStackInSlot(bindings[i]), comparedStack, true, true)){
				boolean found = false;
				for(int j = 0; j < 9; j++) {
					if(j == bindings[i]) {
						continue;
					}

					ItemStack inputInvStack = inputInv.getStackInSlot(j);

					if(StackHelper.isMatchingItem(inputInvStack, comparedStack, true , false) && inputInvStack.stackSize > bindingCounts[j]) {
						found = true;
						bindings[i] = j;
						bindingCounts[j]++;
						break;
					}
				}
				if(!found) {
					craftMatrix.isInputMissing = true;
					return;
				}
			}
			else {
				bindingCounts[bindings[i]]++;
			}
		}

		for(int i = 0; i < 9; i++) {
			if(bindingCounts[i] > 0) {
				ItemStack stack = inputInv.getStackInSlot(i);
				if(stack != null && stack.stackSize < bindingCounts[i]) {
					for(int j = 0; j < 9; j++) {
						if(bindings[j] == i) {
							bindings[j] = -1;
						}
					}
					return;
				}
			}
		}

		craftMatrix.setUseBindings(true);
		ItemStack result = craftMatrix.getRecipeOutput();

		if(result != null && result.stackSize > 0) {
			ItemStack resultInfo = resultInv.getStackInSlot(0);

			craftSlot.onPickupFromSlot(getInternalPlayer().get(), result);

			if(resultInfo == null) {
				resultInv.setInventorySlotContents(0, result);
			}
			else {
				resultInfo.stackSize += result.stackSize;
			}
		}

		craftMatrix.setUseBindings(false);
		craftMatrix.rebuildCache();
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {

	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if(slot == SLOT_RESULT) {
			return false;
		}
		if(stack.getItem().hasContainerItem(stack)) {
			return false;
		}
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int slot) {
		return SLOTS;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if(slot >= 9) {
			return false;
		}
		ItemStack slotStack = inv.getStackInSlot(slot);
		if(StackHelper.canStacksMerge(stack, slotStack)) {
			return true;
		}
		for(int i = 0; i < 9; i++) {
			ItemStack inputStack = craftMatrix.getStackInSlot(i);
			if(inputStack != null && StackHelper.isMatchingItem(inputStack, stack, true, false)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return slot == SLOT_RESULT;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}
}
