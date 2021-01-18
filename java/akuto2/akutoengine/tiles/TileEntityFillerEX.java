package akuto2.akutoengine.tiles;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Nonnull;

import akuto2.akutoengine.blocks.BlockFillerEX;
import akuto2.akutoengine.patterns.FillerPatternCore;
import akuto2.akutoengine.patterns.FillerPatternRecipe;
import akuto2.akutoengine.utils.energy.AEBattery;
import akuto2.akutoengine.utils.energy.AEBatteryReceiver;
import akuto2.akutoengine.utils.energy.EnergyUtils;
import akuto2.akutoengine.utils.enums.EnumWorkType;
import buildcraft.api.core.BuildCraftAPI;
import buildcraft.api.core.IAreaProvider;
import buildcraft.api.mj.MjAPI;
import buildcraft.api.mj.MjCapabilityHelper;
import buildcraft.api.tiles.IControllable;
import buildcraft.lib.misc.BoundingBoxUtil;
import buildcraft.lib.misc.NBTUtilBC;
import buildcraft.lib.misc.data.Box;
import buildcraft.lib.misc.data.ModelVariableData;
import buildcraft.lib.net.PacketBufferBC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityFillerEX extends TileBuildCraftEX implements IInventory, IControllable {
	public static final int NET_BOX;
	public NonNullList<ItemStack> craft = NonNullList.withSize(9, ItemStack.EMPTY);
	public NonNullList<ItemStack> container = NonNullList.withSize(27, ItemStack.EMPTY);
	public int currentSlot;
	public Box box = new Box();
	public boolean markerBox;
	public boolean done;
	public int workEnergy;
	public FillerPatternCore currentPattern;
	public EntityPlayer player;
	public int cx, cy, cz;	// 現在の位置
	public int sx, sy, sz;	// 開始位置
	public int ex, ey, ez;	// 終了位置
	public int mx, my, mz, now;
	public final ModelVariableData clientModelData = new ModelVariableData();
	private AEBattery battery;
	private MjCapabilityHelper mjCaps;
	private AEBatteryReceiver receiver;
	private boolean finished;
	private Mode mode;

	static {
		NET_BOX = IDS.allocId("BOX");
	}

	public TileEntityFillerEX() {
		setBattery(new AEBattery(1000L * MjAPI.MJ, 1000L * MjAPI.MJ, 0));;
		done = true;
		mode = Mode.ON;
		markerBox = false;
	}

	/**
	 * 稼働エネルギー量を設定する
	 * @param work 動くのに必要なエネルギー量
	 * @param max 受け取るエネルギー量 * work
	 */
	public void setPower(int work, int max) {
		setBattery(new AEBattery(EnergyUtils.changeRFToMJ(work * max), EnergyUtils.changeRFToMJ(work * max), 0));
		workEnergy = work;
	}

	/**
	 * 初期位置を設定する
	 */
	public void initTargetPosition() {
		if(box.isInitialized()) {
			sx = (int)box.min().getX();
			sy = (int)box.min().getY();
			sz = (int)box.min().getZ();
			ex = (int)box.max().getX();
			ey = (int)box.max().getY();
			ez = (int)box.max().getZ();
			cx = sx;
			cy = sy;
			cz = sz;
			now = 0;
		}
	}

	/**
	 * 初期位置と向きを設定する
	 */
	public void initRotationPosition() {
		if(box.isInitialized()) {
			sy = (int)box.min().getY();
			ey = (int)box.max().getY();
			int bsx = (int)box.min().getX();
			int bsz = (int)box.min().getZ();
			int bex = (int)box.max().getX();
			int bez = (int)box.max().getZ();
			if(bsx >= pos.getX()) {
				sx = bsx;
				ex = bex;
				mx = 1;
			}
			else {
				sx = bex;
				ex = bsx;
				mx = -1;
			}
			if(bsz >= pos.getZ()) {
				sz = bsz;
				ez = bez;
				mz = 1;
			}
			else {
				sz = bez;
				ez = bsz;
				mz = -1;
			}
			now = 0;
			cx = sx;
			cy = sy;
			cz = sz;
		}
	}

	@Override
	public void initialize() {
		super.initialize();
		if(!world.isRemote) {
			IBlockState blockState = world.getBlockState(pos);
			BlockPos offsetPos = pos.offset(blockState.getValue(BlockFillerEX.FACING).getOpposite());
			TileEntity tile = world.getTileEntity(offsetPos);
			if(tile instanceof IAreaProvider) {
				IAreaProvider provider = (IAreaProvider)tile;
				box.reset();
				box.setMin(provider.min());
				box.setMax(provider.max());
				provider.removeFromWorld();
				markerBox = true;
			}
			sendNetworkUpdate(NET_RENDER_DATA);
		}
		computeRecipe();
	}

	@Override
	public void update() {
		super.update();
		if(world.isRemote) {
			clientModelData.tick();
		}
		if(done) {
			if(mode == Mode.LOOP) {
				done = false;
			}
			else {
				return;
			}
		}
		sendNetworkUpdate(NET_RENDER_DATA);
		if(getBattery().getEnergyStored() > workEnergy) {
			doWork();
		}
	}

	/**
	 * フィラーを動作させる
	 */
	public void doWork() {
		if(world.isRemote || mode == Mode.OFF)
			return;

		if(getBattery().useEnergy(workEnergy, workEnergy, false) < workEnergy)
			return;

		if(box.isInitialized() && currentPattern != null) {
			SetWorkMode(EnumWorkType.On);
			ItemStack stack = ItemStack.EMPTY;
			currentSlot = 9;
			int j = 9;
			while(true) {
				if(j >= getSizeInventory())
					break;

				if(!getStackInSlot(j).isEmpty() && getStackInSlot(j).getCount() > 0) {
					stack = container.get(j - 9);
					currentSlot = j;
					break;
				}
				j++;
			}

			done = currentPattern.iteratePattern(this, box, stack);
			if(!stack.isEmpty() && stack.getCount() <= 0) {
				container.set(currentSlot - 9, ItemStack.EMPTY);
			}

			if(done) {
				SetWorkMode(EnumWorkType.Off);
				markDirty();
				sendNetworkUpdate(NET_RENDER_DATA);
			}

			if(getBattery().getEnergyStored() > workEnergy) {
				doWork();
			}
		}
	}

	public void SetWorkMode(EnumWorkType work) {
		IBlockState state = world.getBlockState(pos);

		if(state != state.withProperty(BlockFillerEX.WORK, work)) {
			world.setBlockState(pos, state.withProperty(BlockFillerEX.WORK, work));
			validate();
			world.setTileEntity(pos, this);
		}
	}

	/**
	 * レシピからパターンを読み取り設定するメソッド
	 */
	public void computeRecipe() {
		if(world.isRemote)
			return;

		FillerPatternCore fillerPattern = FillerPatternRecipe.findMatchingRecipe(craft);
		if(fillerPattern == currentPattern) {
			return;
		}
		player = (EntityPlayer)BuildCraftAPI.fakePlayerProvider.getBuildCraftPlayer((WorldServer)world);
		if(fillerPattern != null) {
			fillerPattern.initialize(this);
		}

		currentPattern = fillerPattern;
		if(currentPattern == null) {
			done = mode != Mode.LOOP;
		}
		else {
			done = false;
		}

		markDirty();
		sendNetworkUpdate(NET_RENDER_DATA);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		for(int x = 0; x < craft.size(); x++) {
			craft.set(x, new ItemStack(compound.getCompoundTag("Craft" + x)));
		}
		for(int x = 0; x < container.size(); x++) {
			container.set(x, new ItemStack(compound.getCompoundTag("Items" + x)));
		}
		battery.deserializeNBT(compound.getCompoundTag("battery"));
		mode = Optional.ofNullable(NBTUtilBC.readEnum(compound.getTag("mode"), Mode.class)).orElse(Mode.ON);
		box.initialize(compound.getCompoundTag("box"));
		markerBox = compound.getBoolean("markerBox");
		done = compound.getBoolean("done");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		for(int x = 0; x < craft.size(); x++) {
			if(!craft.get(x).isEmpty()) {
				NBTTagCompound crafttag = new NBTTagCompound();
				craft.get(x).writeToNBT(crafttag);
				compound.setTag("Craft" + x, crafttag);
			}
			else {
				compound.removeTag("Craft" + x);
			}
		}
		for(int x = 0; x < container.size(); x++) {
			if(!container.get(x).isEmpty()) {
				NBTTagCompound containertag = new NBTTagCompound();
				container.get(x).writeToNBT(containertag);
				compound.setTag("Items" + x, containertag);
			}
			else {
				compound.removeTag("Items" + x);
			}
		}
		compound.setTag("battery", battery.serializeNBT());
		compound.setTag("mode", NBTUtilBC.writeEnum(mode));
		compound.setTag("box", box.writeToNBT());
		compound.setBoolean("markerBox", markerBox);
		compound.setBoolean("done", done);
		return compound;
	}

	@Override
	public void readPayload(int id, PacketBufferBC buffer, Side side, MessageContext ctx) throws IOException {
		super.readPayload(id, buffer, side, ctx);
		if(side == Side.CLIENT) {
			if(id == NET_RENDER_DATA) {
				readPayload(NET_BOX, buffer, side, ctx);
			}
			else if(id == NET_BOX) {
				box.readData(buffer);
				markerBox = buffer.readBoolean();
			}
		}
	}

	@Override
	public void writePayload(int id, PacketBufferBC buffer, Side side) {
		super.writePayload(id, buffer, side);
		if(side == Side.SERVER) {
			if(id == NET_RENDER_DATA) {
				writePayload(NET_BOX, buffer, side);
			}
			else if(id == NET_BOX) {
				box.writeData(buffer);
				buffer.writeBoolean(markerBox);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasFastRenderer() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return BoundingBoxUtil.makeFrom(pos, box);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return Double.MAX_VALUE;
	}

	private void setBattery(AEBattery battery) {
		receiver = new AEBatteryReceiver(battery);
		this.battery = battery;
	}

	public boolean hasWork() {
		return !done && mode != Mode.OFF;
	}

	public AEBattery getBattery() {
		return battery;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing));
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == MjAPI.CAP_CONNECTOR) {
			return MjAPI.CAP_CONNECTOR.cast(receiver);
		}
		if(capability == MjAPI.CAP_RECEIVER) {
			return MjAPI.CAP_RECEIVER.cast(receiver);
		}
		if(capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(battery);
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 36;
	}

	@Override
	public boolean isEmpty() {
		for(ItemStack stack : craft) {
			if(!stack.isEmpty()) {
				return false;
			}
		}

		for(ItemStack stack : container) {
			if(!stack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void setInventorySlotContents(int index,@Nonnull ItemStack stack) {
		if(index < craft.size()) {
			craft.set(index, stack);
		}
		else if(index - 9 < container.size()) {
			container.set(index - 9, stack);
		}
		computeRecipe();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if(index < craft.size()) {
			return craft.get(index);
		}
		else if(index - 9 < container.size()) {
			return container.get(index - 9);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if(index < craft.size() && !craft.get(index).isEmpty()) {
			if(craft.get(index).getCount() <= count) {
				ItemStack stack = craft.get(index);
				craft.set(index, ItemStack.EMPTY);
				computeRecipe();
				return stack;
			}
			ItemStack split = craft.get(index).splitStack(count);
			if(craft.get(index).getCount() <= 0) {
				craft.set(index, ItemStack.EMPTY);
			}
			return split;
		}
		else if(index - 9 < container.size() && !container.get(index - 9).isEmpty()) {
			if(container.get(index - 9).getCount() <= count) {
				ItemStack stack = container.get(index - 9);
				container.set(index - 9, ItemStack.EMPTY);
				return stack;
			}
			ItemStack split = container.get(index - 9).splitStack(count);
			if(container.get(index - 9).getCount() <= 0) {
				container.set(index - 9, ItemStack.EMPTY);
			}
			computeRecipe();
			return split;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if(index < craft.size()) {
			ItemStack stackCopy = craft.get(index);
			craft.set(index, ItemStack.EMPTY);
			computeRecipe();
			return stackCopy;
		}
		else if(index - 9 < container.size()) {
			ItemStack stackCopy = container.get(index - 9);
			container.set(index - 9, ItemStack.EMPTY);
			return stackCopy;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) == this && player.getDistanceSq(pos) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index,@Nonnull ItemStack stack) {
		if(index < 9 || index - 9 > 27) {
			return false;
		}
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {

	}

	@Override
	public Mode getControlMode() {
		return mode;
	}

	@Override
	public void setControlMode(Mode mode) {
		if(this.mode == Mode.OFF && mode != Mode.OFF)
			finished = false;
		this.mode = mode;
	}
}
