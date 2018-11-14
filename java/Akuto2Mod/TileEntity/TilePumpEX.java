package Akuto2Mod.TileEntity;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

<<<<<<< HEAD
=======
import Akuto2Mod.Utils.WorldHelper;
import Akuto2Mod.Utils.Blocks.AdjacentLiquids;
>>>>>>> mc1.7.10
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftFactory;
import buildcraft.api.core.BlockIndex;
import buildcraft.api.core.SafeTimeTracker;
<<<<<<< HEAD
import buildcraft.api.tiles.IHasWork;
=======
>>>>>>> mc1.7.10
import buildcraft.core.lib.EntityBlock;
import buildcraft.core.lib.RFBattery;
import buildcraft.core.lib.TileBuffer;
import buildcraft.core.lib.block.TileBuildCraft;
import buildcraft.core.lib.fluids.SingleUseTank;
import buildcraft.core.lib.fluids.TankUtils;
import buildcraft.core.lib.utils.BlockUtils;
import buildcraft.core.lib.utils.Utils;
import buildcraft.core.proxy.CoreProxy;
import buildcraft.factory.FactoryProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
<<<<<<< HEAD
=======
import net.minecraft.init.Blocks;
>>>>>>> mc1.7.10
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

<<<<<<< HEAD
public class TilePumpEX extends TileBuildCraft implements IHasWork{

	private int capacity = 1000000;
	public int internalLiquid;
=======
public class TilePumpEX extends TileBuildCraft{

	private int capacity = 1000000;
	public int internalLiquid = 0;
>>>>>>> mc1.7.10
	private EntityBlock tube;
	private double tubeY = Double.NaN;
	private int aimY = 0;
	private int type;
<<<<<<< HEAD
=======
	private double tubeSpeed = 0.1D;
>>>>>>> mc1.7.10
	private TreeMap<Integer, Deque<BlockIndex>> pumpLayerQueues = new TreeMap();
	private boolean powered = false;
	private SafeTimeTracker timer = new SafeTimeTracker(512L);
	private SafeTimeTracker updateTracker = new SafeTimeTracker(Math.max(16, BuildCraftCore.updateFactor));
	private int tick = Utils.RANDOM.nextInt(32);
<<<<<<< HEAD
	public boolean doWork = false;
=======
	public boolean liquidsInit = false;
	public boolean doWork = false;
	private AdjacentLiquids pumpableBlocks;
>>>>>>> mc1.7.10
	private int numFluidBlocksFound = 0;
	public SingleUseTank tank = new SingleUseTank("tank", capacity, this);
	public static Fluid WATER = FluidRegistry.WATER;
	private int minUseEnergy[] = {10, 25, 25, 25, 25};
	private FluidStack fluidStack;
	private boolean initialized = false;

	public void initTilePumpEX() {
<<<<<<< HEAD
		this.type = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		setProviderConfigure(type);
		initialized = true;
	}

	public void setProviderConfigure(int type) {
=======
		type = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		setProviderConfigure(type);
		initialized = true;
		createTube();
	}

	public void setAim(int y, Block block) {
		liquidsInit = true;
		fluidStack = BlockUtils.drainBlock(block, worldObj, xCoord, y, zCoord, false);
		aimY = y;
		pumpableBlocks.setPosition(worldObj, xCoord, y, zCoord);
		pumpableBlocks.setTarget(block, 0);
		if(type >= 2) {
			pumpableBlocks.ice = Blocks.ice;
		}
		if(type == 1) {
			pumpableBlocks.setRange(0);
		}
		else {
			pumpableBlocks.setRange(64);
		}
	}

	/**
	 * エネルギー設定メソッド
	 * @param type ポンプのType
	 */
	public void setProviderConfigure(int type) {
		System.out.println("setProvider");
		pumpableBlocks = new AdjacentLiquids(this, type);
>>>>>>> mc1.7.10
		if(type == 0) {
			setBattery(new RFBattery(10, 10, 0));
		}
		if(type == 1) {
			setBattery(new RFBattery(25, 25, 0));
		}
		if(type == 2) {
			setBattery(new RFBattery(100, 100, 0));
		}
		if(type == 3) {
			setBattery(new RFBattery(1600, 1600, 0));
		}
		if(type == 4) {
			setBattery(new RFBattery(51200, 51200, 0));
		}
	}

<<<<<<< HEAD
=======
	protected void pumpInitialize() {
		for(int y = yCoord - 1; y > 0; y--) {
			Block block = worldObj.getBlock(xCoord, y, zCoord);
			if(isPumpableFluid(xCoord, y, zCoord)) {
				Block underBlock = worldObj.getBlock(xCoord, y - 1, zCoord);
				if(!isPumpableFluid(xCoord, y - 1, zCoord)) {
					setAim(y, block);
					return;
				}
				if(y == 1) {
					setAim(0, underBlock);
					return;
				}
			}
		}
	}

	/**
	 * チューブの位置設定メソッド
	 */
>>>>>>> mc1.7.10
	protected void setTubePosition() {
		if(tube != null) {
			tube.iSize = 0.5D;
			tube.kSize = 0.5D;
			tube.jSize = yCoord - tube.posY;
			tube.setPosition(xCoord + 0.25F, tubeY, zCoord + 0.25F);
		}
	}

<<<<<<< HEAD
=======
	/**
	 * チューブの作成メソッド
	 */
>>>>>>> mc1.7.10
	protected void createTube() {
		if(tube == null) {
			tube = FactoryProxy.proxy.newPumpTube(worldObj);
			if(!Double.isNaN(tubeY)) {
				tube.posY = tubeY;
			}
			else {
				tube.posY = yCoord;
			}
			tubeY = tube.posY;
			if(aimY == 0) {
				aimY = yCoord;
			}
			setTubePosition();

			worldObj.spawnEntityInWorld(tube);
			if(!worldObj.isRemote) {
				sendNetworkUpdate();
			}
		}
	}

<<<<<<< HEAD
=======
	/**
	 * チューブの削除メソッド
	 */
>>>>>>> mc1.7.10
	protected void destroyTube() {
		if(tube != null) {
			CoreProxy.proxy.removeEntity(tube);
			tube = null;
			tubeY = 0.0D;
			aimY = 0;
		}
	}

	public void onNeighborBlockChange(Block block) {
		boolean p = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
		if(powered != p) {
			powered = p;
			if(!worldObj.isRemote) {
				sendNetworkUpdate();
			}
		}
	}

<<<<<<< HEAD
=======
	/**
	 * 隣接しているタンクに水を移すメソッド
	 */
>>>>>>> mc1.7.10
	private void pushToConsumers() {
		if(cache == null) {
			cache = TileBuffer.makeBuffer(worldObj, xCoord, yCoord, zCoord, false);
		}
		TankUtils.pushFluidToConsumers(tank, tank.getFluidAmount(), cache);
	}

<<<<<<< HEAD
=======
	/**
	 * ポンプの実際の稼働メソッド
	 * @param index ブロック情報
	 */
>>>>>>> mc1.7.10
	private void onAction(BlockIndex index) {
		while(getBattery().useEnergy(minUseEnergy[type], minUseEnergy[type], false) > 0) {
			if((isFluidAllowed(fluidStack.getFluid())) && (tank.fill(fluidStack, false) == fluidStack.amount)) {
				if(type == 1) {

				}
<<<<<<< HEAD
				else if((fluidStack.getFluid() != FluidRegistry.WATER) || (BuildCraftCore.consumeWaterSources) || (numFluidBlocksFound > 0)) {
					index = getNextIndexToPump(true);
					BlockUtils.drainBlock(worldObj, index.x, index.y, index.z, true);
				}
				tank.fill(fluidStack, true);
				pushToConsumers();
				fluidStack = index != null ? BlockUtils.drainBlock(worldObj, index.x, index.y, index.z, false) : null;
=======
				else if((numFluidBlocksFound > 0)) {
					index = getNextIndexToPump(true);
					//BlockUtils.drainBlock(worldObj, nowIndex.x, nowIndex.y, nowIndex.z, true);
					WorldHelper.setBlockToAir(worldObj, index.x, index.y, index.z);
					worldObj.markBlockForUpdate(index.x, index.y, index.z);
				}
				tank.fill(fluidStack, true);
				pushToConsumers();
				fluidStack = (index != null ? BlockUtils.drainBlock(worldObj, index.x, index.y, index.z, false) : null);
>>>>>>> mc1.7.10
				if(fluidStack == null) {
					return;
				}
			}
		}
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(!initialized) {
			initTilePumpEX();
		}
		if(powered) {
			pumpLayerQueues.clear();
			destroyTube();
		}
		else {
			createTube();
		}
		if(worldObj.isRemote) {
			return;
		}
<<<<<<< HEAD
		if(updateTracker.markTimeIfDelay(worldObj)) {
			sendNetworkUpdate();
		}
		if(powered) {
			return;
		}
		if(tube.posY - aimY > 0.01D) {
			tubeY = tube.posY - 0.01D;
=======
		if(tube.posY - aimY > tubeSpeed + 0.4D) {
			tubeY = (tube.posY - tubeSpeed);
>>>>>>> mc1.7.10
			setTubePosition();
			sendNetworkUpdate();
			return;
		}
<<<<<<< HEAD
		tick += 1;

=======
>>>>>>> mc1.7.10
		BlockIndex index = getNextIndexToPump(false);

		fluidStack = index != null ? BlockUtils.drainBlock(worldObj, index.x, index.y, index.z, false) : null;

		if(fluidStack != null) {
			onAction(index);
		}
		rebuildQueue();
		if(getNextIndexToPump(false) == null) {
			for(int y = yCoord - 1; y > 0; y--){
				if(isPumpableFluid(xCoord, y, zCoord)) {
					aimY = y;
					return;
				}
				if(isBlocked(xCoord, y, zCoord)) {
					return;
				}
			}
		}
	}

	private boolean isBlocked(int x, int y, int z) {
		Material mat = BlockUtils.getBlock(worldObj, x, y, z).getMaterial();
		return mat.blocksMovement();
	}

	private boolean isPumpableFluid(int x, int y, int z) {
		Fluid fluid = BlockUtils.getFluid(BlockUtils.getBlock(worldObj, x, y, z));
		if(fluid == null) {
			return false;
		}
		if(!isFluidAllowed(fluid)) {
			return false;
		}
		return (tank.getAcceptedFluid() == null) || (tank.getAcceptedFluid() == fluid);
	}

	private BlockIndex getNextIndexToPump(boolean remove) {
		if(pumpLayerQueues.isEmpty()) {
			if(timer.markTimeIfDelay(worldObj)) {
				rebuildQueue();
			}
			return null;
		}
		Deque<BlockIndex> topLayer = (Deque)pumpLayerQueues.lastEntry().getValue();
		if(topLayer != null) {
			if(topLayer.isEmpty()) {
				pumpLayerQueues.pollLastEntry();
			}
			if(remove) {
				BlockIndex index = (BlockIndex)topLayer.pollLast();
				return index;
			}
			return topLayer.peekLast();
		}
		return null;
	}

	public void rebuildQueue() {
		numFluidBlocksFound = 0;
		pumpLayerQueues.clear();
		int x = xCoord;
		int y = aimY;
		int z = zCoord;
		Fluid pumpingFluid = BlockUtils.getFluid(BlockUtils.getBlock(worldObj, x, y, z));
		if(pumpingFluid == null) {
			return;
		}
		if((pumpingFluid != tank.getAcceptedFluid()) && (tank.getAcceptedFluid() != null)) {
			return;
		}
		Set<BlockIndex> visitedBlocks = new HashSet();
		Deque<BlockIndex> fluidsFound = new LinkedList();

		queueForPumping(x, y, z, visitedBlocks, fluidsFound, pumpingFluid);
		while(!fluidsFound.isEmpty()) {
			Deque<BlockIndex> fluidsToExpand = fluidsFound;
			fluidsFound = new LinkedList();
			for(BlockIndex index : fluidsToExpand) {
				queueForPumping(index.x, index.y + 1, index.z, visitedBlocks, fluidsFound, pumpingFluid);
				queueForPumping(index.x + 1, index.y, index.z, visitedBlocks, fluidsFound, pumpingFluid);
				queueForPumping(index.x - 1, index.y, index.z, visitedBlocks, fluidsFound, pumpingFluid);
				queueForPumping(index.x, index.y, index.z + 1, visitedBlocks, fluidsFound, pumpingFluid);
				queueForPumping(index.x, index.y, index.z - 1, visitedBlocks, fluidsFound, pumpingFluid);
<<<<<<< HEAD
				if((pumpingFluid == FluidRegistry.WATER) && (numFluidBlocksFound >= 9)) {
					return;
				}
=======
>>>>>>> mc1.7.10
			}
		}
	}

	public void queueForPumping(int x, int y, int z, Set<BlockIndex> visitedBlocks, Deque<BlockIndex> fluidsFound, Fluid pumpingFluid) {
		BlockIndex index = new BlockIndex(x, y, z);
		if(visitedBlocks.add(index)) {
			if((x - xCoord) * (x - xCoord) + (z - zCoord) * (z - zCoord) > 4096) {
				return;
			}
			Block block = BlockUtils.getBlock(worldObj, x, y, z);
			if(BlockUtils.getFluid(block) == pumpingFluid) {
				fluidsFound.add(index);
			}
			if(canDrainBlock(block, x, y, z, pumpingFluid)) {
				getLayerQueue(y).add(index);
				numFluidBlocksFound += 1;
			}
		}
	}

	private boolean canDrainBlock(Block block, int x, int y, int z, Fluid fluid) {
		if(!isFluidAllowed(fluid)) {
			return false;
		}
		FluidStack fluidStack = BlockUtils.drainBlock(worldObj, x, y, z, false);
		if((fluidStack == null) || (fluidStack.amount <= 0)) {
			return false;
		}
		return fluidStack.getFluid() == fluid;
	}

	private boolean isFluidAllowed(Fluid fluid) {
		return BuildCraftFactory.pumpDimensionList.isFluidAllowed(fluid, worldObj.provider.dimensionId);
	}

	private Deque<BlockIndex> getLayerQueue(int layer){
		Deque<BlockIndex> pumpQueue = (Deque)pumpLayerQueues.get(Integer.valueOf(layer));
		if(pumpQueue == null) {
			pumpQueue = new LinkedList();
			pumpLayerQueues.put(Integer.valueOf(layer), pumpQueue);
		}
		return pumpQueue;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

		tank.readFromNBT(nbt);

		powered = nbt.getBoolean("powered");

		aimY = nbt.getInteger("aimY");
		tubeY = nbt.getFloat("tubeY");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		tank.writeToNBT(nbt);

		nbt.setBoolean("powered", powered);

		nbt.setInteger("aimY", aimY);

		if(tube != null) {
			nbt.setFloat("tubeY", (float)tube.posY);
		}
		else {
			nbt.setFloat("tubeY", yCoord);
		}
	}

	@Override
	public void readData(ByteBuf stream) {
		aimY = stream.readShort();
		tubeY = stream.readFloat();
		powered = stream.readBoolean();

		setTubePosition();
	}

	@Override
	public void writeData(ByteBuf stream) {
		stream.writeShort(aimY);
		stream.writeFloat((float)tubeY);
		stream.writeBoolean(powered);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		destroy();
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if(tube != null) {
			CoreProxy.proxy.removeEntity(tube);
			tube = null;
		}
	}

	@Override
	public void validate() {
		super.validate();
	}

	@Override
	public void destroy() {
		pumpLayerQueues.clear();
		destroyTube();
	}
<<<<<<< HEAD

	@Override
	public boolean hasWork() {
		BlockIndex next = getNextIndexToPump(false);
		if(next != null) {
			return isPumpableFluid(next.x, next.y, next.z);
		}
		return false;
	}
=======
>>>>>>> mc1.7.10
}
