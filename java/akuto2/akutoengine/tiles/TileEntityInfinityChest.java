package akuto2.akutoengine.tiles;

import java.math.BigDecimal;
import java.math.BigInteger;

import akuto2.akutoengine.gui.ContainerInfinityChest;
import akuto2.akutoengine.packet.ItemCountMessage;
import akuto2.akutoengine.packet.PacketHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lib.utils.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityInfinityChest extends TileEntity implements IInventory{
	private ItemStack contents;
	private ItemStack[] inventory = new ItemStack[getSizeInventory()];
	private BigInteger count = BigInteger.ZERO;
	private ContainerInfinityChest container;
	public static final BigInteger INT_MAX = BigInteger.valueOf(Integer.MAX_VALUE);

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		contents = readStackFromNBT(compound);
		count = readCountFromNBT(compound);
		updateInv();
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		setStackNBT(compound);
	}

	public static ItemStack readStackFromNBT(NBTTagCompound compound) {
		if(compound == null || !compound.hasKey("item")) {
			return null;
		}
		NBTTagCompound stackCompound = compound.getCompoundTag("item");
		ItemStack stack = ItemStack.loadItemStackFromNBT(stackCompound);
		return stack;
	}

	public static BigInteger readCountFromNBT(NBTTagCompound compound) {
		if(compound == null || !compound.hasKey("count")) {
			return BigInteger.ZERO;
		}
		BigDecimal decimal = BigDecimal.ZERO;
		BigInteger size = BigInteger.ZERO;
		try {
			decimal = new BigDecimal(compound.getString("count"));
		}
		catch(NumberFormatException e) {
			LogHelper.logError("TileEntityInfinityChest loading problem.");
		}

		if(decimal != BigDecimal.ZERO) {
			try {
				size = decimal.toBigIntegerExact();
			}
			catch(ArithmeticException e) {
				LogHelper.logError("TileEntityInfinityChest loading problem.");
			}
		}
		return size;
	}

	public NBTTagCompound setStackNBT(NBTTagCompound compound) {
		if(contents != null) {
			NBTTagCompound compound2 = new NBTTagCompound();
			compound.setTag("item", contents.writeToNBT(compound2));
			compound.setString("count", count.toString());
		}
		return compound;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, compound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
	}

	public void updateInv() {
		ItemStack insert = getStackInSlot(0);
		if(insert != null) {
			if(isItemValidForSlot(0, insert)) {
				addStack(insert);
			}
		}

		if(contents != null) {
			ItemStack out = getStackInSlot(1);
			boolean outFlag = out == null || stacksEquals(contents, out);
			int outStackSize = out != null ? out.stackSize : 0;
			if(outFlag && outStackSize < contents.getMaxStackSize() && checkInteger(count, 0)) {
				int sub = contents.getMaxStackSize() - outStackSize;
				if(checkInteger(count, sub)) {
					ItemStack stack = copyAmount(contents, contents.getMaxStackSize());
					inventory[1] = stack;
				}
				else {
					ItemStack stack = copyAmount(contents, count.intValueExact());
					inventory[1] = stack;
				}
			}
		}
		else {
			inventory[1] = null;
		}

		if(container != null) {
			container.changeSlot();
		}

		if(worldObj != null && !worldObj.isRemote) {
			PacketHandler.sendToPoint(new ItemCountMessage(this, getCount()));
		}
	}


	public BigInteger addStack(ItemStack insertStack) {
		return addStack(insertStack, BigInteger.valueOf(insertStack.stackSize));
	}

	public BigInteger addStack(ItemStack insertStack, BigInteger add) {
		if(contents != null && insertStack != null && !stacksEquals(contents, insertStack)) {
			return add;
		}
		count = count.add(add);
		if(contents == null) {
			contents = copyAmount(insertStack, 1);
		}
		inventory[0] = null;
		return BigInteger.ZERO;
	}

	public BigInteger decrStack(ItemStack stack, BigInteger limit) {
		if(contents != null && stack != null && !stacksEquals(contents, stack)) {
			return BigInteger.ZERO;
		}
		return decrStack(BigInteger.valueOf(stack.stackSize), limit);
	}

	public BigInteger decrStack(BigInteger subs, BigInteger limit) {
		if(subs.compareTo(count) >= 0) {
			subs = count;
		}
		else if(subs.compareTo(count) < 0) {
			if(limit.compareTo(count) >= 0) {
				subs = count;
			}
			else if(limit.compareTo(count) < 0) {
				subs = limit;
			}
		}
		count = count.subtract(subs);
		if(count.signum() <= 0) {
			contents = null;
			count = BigInteger.ZERO;
		}
		updateInv();
		return subs;
	}

	public void setStack(ItemStack stack, BigInteger size, boolean isBlockSet) {
		if(isBlockSet) {
			contents = copyAmount(stack, 1);
			count = size;
			markDirty();
		}
		else {
			contents = stack;
			count = size;
		}
	}

	public ItemStack getStack() {
		return getStack(INT_MAX.min(count).intValueExact());
	}

	public ItemStack getStack(int amount) {
		return copyAmount(contents, amount);
	}

	public BigInteger getCount() {
		return count;
	}

	@SideOnly(Side.CLIENT)
	public void setCount(BigInteger count) {
		this.count = count;
	}

	@SideOnly(Side.CLIENT)
	public void setContents(ItemStack contents) {
		this.contents = contents;
	}

	public void setContainer(ContainerInfinityChest container) {
		this.container = container;
	}

	public boolean hasStack() {
		if(contents != null && contents.stackSize <= 0 && count.compareTo(BigInteger.valueOf(0)) <= 0) {
			contents = null;
		}
		return (contents != null);
	}

	private ItemStack copyAmount(ItemStack stack, int amount) {
		if(stack == null) {
			return null;
		}
		ItemStack copyStack = stack.copy();
		copyStack.stackSize = amount;
		return copyStack;
	}

	private boolean contentsEquals(ItemStack stack) {
		if(contents == null) {
			return false;
		}
		return stacksEquals(contents, stack);
	}

	private boolean stacksEquals(ItemStack stack, ItemStack stack2) {
		LogHelper.logInfo("Stack Check");
		return (stack.getItem() == stack2.getItem()) && (stack.getItemDamage() == stack2.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, stack2);
	}

	private boolean checkInteger(BigInteger bigInteger, int i) {
		return bigInteger.compareTo(BigInteger.valueOf(i)) > 0;
	}

	@Override
	public int getSizeInventory() {
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int count) {
		if(inventory[slot] != null) {
			ItemStack stack;

			if(inventory[slot].stackSize <= count) {
				stack = inventory[slot];
				if(slot == 1) {
					decrStack(BigInteger.valueOf(count), BigInteger.valueOf(count));
				}
				inventory[slot] = null;
				return stack;
			}
			else {
				stack = inventory[slot].splitStack(count);
				if(slot == 1) {
					decrStack(BigInteger.valueOf(count), BigInteger.valueOf(count));
				}
				if(inventory[slot] != null && inventory[slot].stackSize == 0) {
					inventory[slot] = null;
				}
				return stack;
			}
		}
		else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(inventory[slot] != null) {
			ItemStack stack = inventory[slot];
			inventory[slot] = null;
			return stack;
		}
		else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
		markDirty();
	}

	@Override
	public void markDirty() {
		super.markDirty();
		updateInv();
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
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) != this ? false : player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {

	}

	@Override
	public void closeInventory() {
		setContainer(null);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if(slot == 0) {
			return contents == null || stacksEquals(contents, stack);
		}
		return false;
	}
}
