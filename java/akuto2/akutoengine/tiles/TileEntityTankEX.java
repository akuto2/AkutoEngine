package akuto2.akutoengine.tiles;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import buildcraft.api.core.IFluidFilter;
import buildcraft.api.core.IFluidHandlerAdv;
import buildcraft.lib.fluid.FluidSmoother;
import buildcraft.lib.fluid.FluidSmoother.FluidStackInterp;
import buildcraft.lib.fluid.Tank;
import buildcraft.lib.fluid.TankManager;
import buildcraft.lib.misc.AdvancementUtil;
import buildcraft.lib.misc.CapUtil;
import buildcraft.lib.misc.FluidUtilBC;
import buildcraft.lib.misc.data.IdAllocator;
import buildcraft.lib.net.PacketBufferBC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityTankEX extends TileBuildCraftEX implements ITickable, IFluidHandlerAdv {
	public static final IdAllocator IDS;
	public static final int NET_FLUID_DELTA;
	private static final ResourceLocation ADVANCEMENT_STORE_FLUIDS = new ResourceLocation("buildcraftfactory:fluid_storage");

	private static boolean isPlayerInteracting = false;

	public final Tank tank;
	public final FluidSmoother smoothedTank;

	protected TankManager tankManager = new TankManager();
	private int lastComparatorLevel;

	static {
		IDS = TileBuildCraftEX.IDS.makeChild("tank");

		NET_FLUID_DELTA = IDS.allocId("FLUID_DELTA");
	}

	public TileEntityTankEX() {
		this(2100000 * Fluid.BUCKET_VOLUME);
	}

	protected TileEntityTankEX(int capacity) {
		this(new Tank("tank", capacity, null));
	}

	protected TileEntityTankEX(Tank tank) {
		tank.setTileEntity(this);
		this.tank = tank;
		tankManager.add(tank);
		smoothedTank = new FluidSmoother(w -> createAndSendMessage(NET_FLUID_DELTA, w), tank);
	}

	public void onPlacedBy(EntityLivingBase placer, ItemStack stack) {
		if(!placer.world.isRemote) {
			isPlayerInteracting = true;
			balanceTankFluids();
			isPlayerInteracting = false;
		}
	}

	public void balanceTankFluids() {
		List<TileEntityTankEX> tanks = getTanks();
		FluidStack fluid = null;
		for(TileEntityTankEX tile : tanks) {
			FluidStack held = tile.tank.getFluid();
			if(held == null) {
				continue;
			}
			if(fluid == null) {
				fluid = held;
			} else if(!fluid.isFluidEqual(held)) {
				return;
			}
		}
		if(fluid == null) {
			return;
		}
		if(fluid.getFluid().isGaseous(fluid)) {
			Collections.reverse(tanks);
		}
		TileEntityTankEX prev = null;
		for(TileEntityTankEX tile : tanks) {
			if(prev != null) {
				FluidUtilBC.move(tile.tank, prev.tank);
			}

			prev = tile;
		}
	}

	@Override
	public void readPayload(int id, PacketBufferBC buffer, Side side, MessageContext ctx) throws IOException {
		super.readPayload(id, buffer, side, ctx);
		if(side == Side.CLIENT) {
			if(id == NET_RENDER_DATA) {
				readPayload(NET_FLUID_DELTA, buffer, side, ctx);
				smoothedTank.resetSmoothing(getWorld());
			}
			else if(id == NET_FLUID_DELTA) {
				smoothedTank.handleMessage(getWorld(), buffer);
			}
		}
	}

	@Override
	public void writePayload(int id, PacketBufferBC buffer, Side side) {
		super.writePayload(id, buffer, side);
		if(side == Side.SERVER) {
			if(id == NET_RENDER_DATA) {
				writePayload(NET_FLUID_DELTA, buffer, side);
			}
			else if(id == NET_FLUID_DELTA) {
				smoothedTank.writeInit(buffer);
			}
		}
	}

	public boolean onActivated(EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		int amountBefore = tank.getFluidAmount();
		isPlayerInteracting = true;
		boolean didChange = FluidUtilBC.onTankActivated(player, pos, hand, this);
		isPlayerInteracting = false;
		if(didChange && !player.world.isRemote && amountBefore < tank.getFluidAmount()) {
			AdvancementUtil.unlockAdvancement(player, ADVANCEMENT_STORE_FLUIDS);
		}
		return didChange;
	}

	@Override
	public IdAllocator getIdAllocator() {
		return IDS;
	}

	public int getComparatorLevel() {
		int amount = tank.getFluidAmount();
		int cap = tank.getCapacity();
		return amount * 14 / cap + (amount > 0 ? 1 : 0);
	}

	@Override
	public void update() {
		smoothedTank.tick(world);

		if(!world.isRemote) {
			int compLevel = getComparatorLevel();
			if(compLevel != lastComparatorLevel) {
				lastComparatorLevel = compLevel;
				markDirty();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public FluidStackInterp getFluidForRender(float parialTicks) {
		return smoothedTank.getFluidForRender(parialTicks);
	}

	public boolean canConnectTo(TileEntityTankEX other, EnumFacing direction) {
		return true;
	}

	public static boolean canTanksConnect(TileEntityTankEX from, TileEntityTankEX to, EnumFacing direction) {
		return from.canConnectTo(to, direction) && to.canConnectTo(from, direction.getOpposite());
	}

	private List<TileEntityTankEX> getTanks(){
		Deque<TileEntityTankEX> tanks = new ArrayDeque();
		tanks.add(this);
		TileEntityTankEX prevTank = this;
		while(true) {
			TileEntity abobe = prevTank.getNeighbourTile(EnumFacing.UP);
			if(!(abobe instanceof TileEntityTankEX)) {
				break;
			}
			TileEntityTankEX tankUp = (TileEntityTankEX)abobe;
			if(tankUp != null && canTanksConnect(prevTank, tankUp, EnumFacing.UP)) {
				tanks.addLast(tankUp);
			}
			else {
				break;
			}

			prevTank = tankUp;
		}
		prevTank = this;
		return new ArrayList(tanks);
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		List<TileEntityTankEX> tanks = getTanks();
		TileEntityTankEX bottom = tanks.get(0);
		TileEntityTankEX top = tanks.get(tanks.size() - 1);
		FluidStack total = bottom.tank.getFluid();
		if(total == null) {
			total = top.tank.getFluid();
		}
		int capacity = 0;
		if(total == null) {
			for(TileEntityTankEX tile : tanks) {
				capacity += tile.tank.getCapacity();
			}
		}
		else {
			total = total.copy();
			total.amount = 0;
			for(TileEntityTankEX tile : tanks) {
				FluidStack other = tile.tank.getFluid();
				if(other != null) {
					total.amount += other.amount;
				}
				capacity += tile.tank.getCapacity();
			}
		}
		return new IFluidTankProperties[] { new FluidTankProperties(total, capacity) };
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if(resource == null || resource.amount <= 0) {
			return 0;
		}
		int filled = 0;
		List<TileEntityTankEX> tanks = getTanks();
		for(TileEntityTankEX tile : tanks) {
			FluidStack current = tile.tank.getFluid();
			if(current != null && !current.isFluidEqual(resource)) {
				return 0;
			}
		}
		boolean gas = resource.getFluid().isGaseous(resource);
		if(gas) {
			Collections.reverse(tanks);
		}
		resource = resource.copy();
		for(TileEntityTankEX tile : tanks) {
			int tankFilled = tile.tank.fill(resource, doFill);
			if(tankFilled > 0) {
				if(isPlayerInteracting & doFill) {
					tile.sendNetworkUpdate(NET_RENDER_DATA);
				}
				resource.amount -= tankFilled;
				filled += tankFilled;
				if(resource.amount == 0) {
					break;
				}
			}
		}
		return filled;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if(resource == null) {
			return null;
		}
		return drain(resource::isFluidEqual, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return drain((fluid) -> true, maxDrain, doDrain);
	}

	@Override
	public FluidStack drain(IFluidFilter filter, int maxDrain, boolean doDrain) {
		if(maxDrain <= 0) {
			return null;
		}
		List<TileEntityTankEX> tanks = getTanks();
		boolean gas = false;
		for(TileEntityTankEX tile : tanks) {
			FluidStack fluid = tile.tank.getFluid();
			if(fluid != null) {
				gas = fluid.getFluid().isGaseous(fluid);
				break;
			}
		}
		if(!gas) {
			Collections.reverse(tanks);
		}
		FluidStack total = null;
		for(TileEntityTankEX tile : tanks) {
			int realMax = maxDrain - (total == null ? 0 : total.amount);
			if(realMax <= 0) {
				break;
			}
			FluidStack drained = tile.tank.drain(filter, realMax, doDrain);
			if(drained == null) continue;
			if(isPlayerInteracting & doDrain) {
				tile.sendNetworkUpdate(NET_RENDER_DATA);
			}
			if(total == null) {
				total = drained.copy();
				total.amount = 0;
			}
			total.amount += drained.amount;
		}
		return total;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapUtil.CAP_FLUIDS || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapUtil.CAP_FLUIDS) {
			return CapUtil.CAP_FLUIDS.cast(this);
		}
		return super.getCapability(capability, facing);
	}
}
