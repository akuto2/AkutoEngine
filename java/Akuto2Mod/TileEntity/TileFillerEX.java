package Akuto2Mod.TileEntity;

import Akuto2Mod.Pattern.FillerPatternCore;
import Akuto2Mod.Pattern.FillerPatternRecipe;
import buildcraft.api.core.IAreaProvider;
import buildcraft.api.tiles.IControllable;
import buildcraft.api.tiles.IHasWork;
import buildcraft.core.Box;
import buildcraft.core.LaserKind;
import buildcraft.core.internal.IBoxProvider;
import buildcraft.core.lib.RFBattery;
import buildcraft.core.lib.utils.LaserUtils;
import buildcraft.core.lib.utils.Utils;
import buildcraft.core.proxy.CoreProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;

public class TileFillerEX extends TileBuildCraftEX implements IInventory, IHasWork, IControllable, IBoxProvider{
	public ItemStack[] craft = new ItemStack[9];
	public ItemStack[] container = new ItemStack[27];
	public int currentSlot;
	public Box box = new Box();
	public boolean done;
	public int workEnergy;
	public static FillerPatternCore currentPattern;
	public int cx, cy, cz;
	public int sx, sy, sz;
	public int ex, ey, ez;
	public int mx, my, mz, now;
	public EntityPlayer player;
	public InventoryCrafting crafting;

	public TileFillerEX() {
		box.kind = Box.Kind.STRIPES;
		done = true;
		mode = Mode.Unknown;
		setBattery(new RFBattery(100, 100, 0));
	}

	/**
	 * エネルギーを設定するメソッド
	 * @param work 動くのに必要なエネルギー量
	 * @param max 受け取るエネルギー量*work*10される
	 */
	public void setPower(int work, int max) {
		setBattery(new RFBattery(work * max * 10, work * max * 10, 0));
		workEnergy = work;
	}

	/**
	 * 初期位置を設定するメソッド
	 */
	public void initTargetPosition() {
		sx = (int)box.pMin().x;
		sy = (int)box.pMin().y;
		sz = (int)box.pMin().z;
		ex = (int)box.pMax().x;
		ey = (int)box.pMax().y;
		ez = (int)box.pMax().z;
		cx = sx;
		cy = sy;
		cz = sz;
		now = 0;
	}

	/**
	 * 初期の向きを設定するメソッド
	 */
	public void initRotationPosition() {
		sy = (int)box.pMin().y;
		ey = (int)box.pMax().y;
		int bsx = (int)box.pMin().x;
		int bsz = (int)box.pMin().z;
		int bex = (int)box.pMax().x;
		int bez = (int)box.pMax().z;
		if(bsx >= xCoord) {
			sx = bsx;
			ex = bex;
			mx = 1;
		}else {
			sx = bex;
			ex = bsx;
			mx = -1;
		}
		if(bsz >= zCoord) {
			sz = bsz;
			ez = bez;
			mz = 1;
		}else {
			sz = bez;
			ez = bsz;
			mz = -1;
		}
		now = 0;
		cx = sx;
		cy = sy;
		cz = sz;
	}


	/**
	 * 初期化処理
	 */
	@Override
	public void initialize() {
		super.initialize();
		if(!worldObj.isRemote) {
			IAreaProvider a = Utils.getNearbyAreaProvider(worldObj, xCoord, yCoord, zCoord);
			if(a != null) {
				box.initialize(a);
				a.removeFromWorld();
				LaserUtils.createLaserBox(worldObj, box.xMin, box.yMin, box.zMin, box.xMax, box.yMax, box.zMax, LaserKind.Stripes);
			}
			sendNetworkUpdate();
		}
		computeRecipe();
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(done) {
			if(mode == Mode.Loop) {
				done = false;
			}
			else {
				return;
			}
		}
		if(getBattery().getEnergyStored() > workEnergy) {
			doWork();
		}
	}

	/**
	 * フィラーの動作メソッド
	 */
	public void doWork() {
		if(worldObj.isRemote) {
			return;
		}
		if(mode == Mode.Off) {
			return;
		}
		if(getBattery().useEnergy(workEnergy, workEnergy, false) < workEnergy) {
			return;
		}
		if(box.isInitialized() && currentPattern != null && !done) {
			ItemStack stack = null;
			currentSlot = 9;
			int j = 9;
			do {
				if(j >= getSizeInventory()) {
					break;
				}

				if(getStackInSlot(j) != null && getStackInSlot(j).stackSize > 0) {
					stack = container[j - 9];
					if(currentPattern.isSetBlock(stack)) {
						currentSlot = j;
						break;
					}
					else {
						stack = null;
					}
				}
				j++;
			}while(true);

			done = currentPattern.iteratePattern(this, box, stack);
			if(stack != null && stack.stackSize <= 0) {
				container[currentSlot - 9] = null;
			}

			if(done) {
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				sendNetworkUpdate();
			}

			if(getBattery().getEnergyStored() > workEnergy) {
				doWork();
			}
		}
	}

	/**
	 * レシピを読み取り設定するメソッド
	 */
	public void computeRecipe() {
		if(worldObj.isRemote) {
			return;
		}
		FillerPatternCore fillerPattern = FillerPatternRecipe.findMatchingRecipe(this);
		if(fillerPattern == currentPattern) {
			return;
		}
		player = (EntityPlayer)CoreProxy.proxy.getBuildCraftPlayer((WorldServer)worldObj).get();
		if(fillerPattern != null) {
			fillerPattern.initialize(this);
		}
		currentPattern = fillerPattern;
		if(currentPattern == null) {
			done = mode != Mode.Loop;
		}
		else {
			done = false;
		}

		if(worldObj != null) {
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		sendNetworkUpdate();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		for(int x = 0; x < craft.length; x++) {
			craft[x] = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Craft" + x));
		}
		for(int x = 0; x < container.length; x++) {
			container[x] = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Items" + x));
		}
		if(compound.hasKey("box")) {
			box.initialize(compound.getCompoundTag("box"));
		}
		done = compound.getBoolean("done");
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		for(int x = 0; x < craft.length; x++) {
			if(craft[x] != null) {
				NBTTagCompound crafttag = new NBTTagCompound();
				craft[x].writeToNBT(crafttag);
				compound.setTag("Craft" + x, crafttag);
			}
			else {
				compound.removeTag("Craft" + x);
			}
		}
		for(int x = 0; x < container.length; x++) {
			if(container[x] != null) {
				NBTTagCompound containertag = new NBTTagCompound();
				container[x].writeToNBT(containertag);
				compound.setTag("Items" + x, containertag);
			}
			else {
				compound.removeTag("Items" + x);
			}
		}
		if(box != null) {
			NBTTagCompound boxtag = new NBTTagCompound();
			box.writeToNBT(boxtag);
			compound.setTag("box", boxtag);
		}
		compound.setBoolean("done", done);
	}

	@Override
	public int getSizeInventory() {
		return 36;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if(slot < craft.length) {
			return craft[slot];
		}
		else if(slot - 9 < container.length) {
			return container[slot - 9];
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		if(slot < craft.length) {
			craft[slot] = stack;
		}
		else if(slot - 9 < container.length) {
			container[slot - 9] = stack;
		}
		computeRecipe();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public ItemStack decrStackSize(int slot, int decrement) {
		if(slot < craft.length && craft[slot] != null) {
			if(craft[slot].stackSize <= decrement) {
				ItemStack stack = craft[slot];
				craft[slot] = null;
				computeRecipe();
				return stack;
			}
			ItemStack split = craft[slot].splitStack(decrement);
			if(craft[slot].stackSize <= 0) {
				craft[slot] = null;
			}
			return split;
		}
		if(slot - 9 < container.length && container[slot - 9] != null) {
			if(container[slot - 9].stackSize <= decrement) {
				ItemStack stack = container[slot - 9];
				container[slot - 9] = null;
				return stack;
			}
			ItemStack split = container[slot - 9].splitStack(decrement);
			if(container[slot - 9].stackSize <= 0) {
				container[slot - 9] = null;
			}
			computeRecipe();
			return split;
		}
		return null;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	@Override
	public String getInventoryName() {
		return "container.fillerEX";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return (this.worldObj.getTileEntity(xCoord, yCoord, zCoord) == this) && (player.getDistanceSq(yCoord, yCoord, zCoord) <= 64.0D);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	@Override
	public void readData(ByteBuf data) {
		box.readData(data);
	}

	@Override
	public void writeData(ByteBuf data) {
		box.writeData(data);
	}

	@Override
	public Box getBox() {
		return box;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new Box(this).extendToEncompass(box).expand(50).getBoundingBox();
	}

	@Override
	public boolean hasWork() {
		return !done && mode != Mode.Off;
	}

	@Override
	public boolean acceptsControlMode(Mode mode) {
		return (mode == Mode.Loop) || (mode == Mode.On) || (mode == Mode.Off);
	}

	@Override
	public boolean isBuildingMaterialSlot(int arg0) {
		return false;
	}
}
