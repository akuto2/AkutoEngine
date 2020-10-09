package akuto2.tiles.engines;

import akuto2.utils.energy.AEBattery;
import akuto2.utils.energy.EnergyUtils;
import buildcraft.api.enums.EnumPowerStage;
import buildcraft.api.mj.IMjConnector;
import buildcraft.api.mj.IMjReceiver;
import buildcraft.api.mj.MjAPI;
import buildcraft.lib.block.VanillaRotationHandlers;
import buildcraft.lib.engine.EngineConnector;
import buildcraft.lib.engine.TileEngineBase_BC8;
import buildcraft.lib.misc.collect.OrderedEnumMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAkutoEngineBase extends TileEngineBase_BC8 {
	public long level;
	private float progress;
	private float lastProgress;
	private int progressPart;
	private AEBattery battery;
	public static final int[] powerlevel = {1, 8, 32, 128, 512, 2048, 8192, 32768, 1000000};

	public TileEntityAkutoEngineBase(int meta) {
		super();
		initAkutoEngine(meta);
	}

	public void initAkutoEngine(int meta) {
		level = EnergyUtils.changeRFToMJ(powerlevel[meta]);
		battery = new AEBattery(level, 0, level);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		progress = compound.getFloat("ae_progress");
		progressPart = compound.getInteger("ae_progressPart");
		battery.deserializeNBT(compound.getCompoundTag("battery"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setFloat("ae_progress", progress);
		compound.setInteger("ae_progressPart", progressPart);
		compound.setTag("battery", battery.serializeNBT());
		return compound;
	}

	private boolean isFacingReceiver(EnumFacing dir) {
		TileEntity neightbour = world.getTileEntity(getPos().offset(dir));
		if(neightbour == null)
			return false;
		IMjConnector other = neightbour.getCapability(MjAPI.CAP_CONNECTOR, dir.getOpposite());
		if(other != null)
			return mjConnector.canConnect(other) && other.canConnect(mjConnector);

		IEnergyStorage otherStorage = neightbour.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite());
		if(otherStorage != null)
			return true;
		return false;
	}

	@Override
	public EnumActionResult attemptRotation() {
		OrderedEnumMap<EnumFacing> possible = VanillaRotationHandlers.ROTATE_FACING;
        EnumFacing current = currentDirection;
        for (int i = 0; i < 6; i++) {
            current = possible.next(current);
            if (isFacingReceiver(current)) {
                if (currentDirection != current) {
                    currentDirection = current;
                    sendNetworkUpdate(NET_RENDER_DATA);
                    redrawBlock();
                    world.notifyNeighborsRespectDebug(getPos(), getBlockType(), true);
                    return EnumActionResult.SUCCESS;
                }
                return EnumActionResult.FAIL;
            }
        }
        return EnumActionResult.FAIL;
	}

	@Override
	public void rotateIfInvalid() {
		if(currentDirection != null && !isFacingReceiver(currentDirection))
			return;

		attemptRotation();
		if(currentDirection == null) {
			currentDirection = EnumFacing.UP;
		}
	}

	@Override
	public void update() {
		if(world.isRemote) {
			lastProgress = progress;

			if(isPumping) {
				progress = (float)(progress + getPistonSpeed());

				if(progress >= 1.0F) {
					progress = 0.0F;
				}
			}
			else if(progress > 0.0F) {
				progress -= 0.01F;
			}
			clientModelData.tick();
			return;
		}

		updateHeatLevel();
		getPowerStage();
		engineUpdate();

		TileEntity tile = getTileBuffer(currentDirection).getTile();
		if(progressPart != 0) {
			progress = (float)(progress + getPistonSpeed());
			if(progress > 0.5F && progressPart == 1) {
				progressPart = 2;
				IMjReceiver receiver = getReceiverToPower(tile, currentDirection);
				if(receiver != null) {
					sendPower();
				}
			}
			else if(progress >= 1.0F) {
				progress = 0.0F;
				progressPart = 0;
			}
		}
		else if(!isRedstonePowered && isActive()) {
			if(getPowerToExtractMJ(false) > 0L) {
				progressPart = 1;
				setPumping(true);
			}
			else {
				setPumping(false);
			}
		}
		else {
			setPumping(false);
		}

		IEnergyStorage storage = getEnergyStorage(tile, currentDirection);
		if(storage != null) {
			sendPower();
		}

		markChunkDirty();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getProgressClient(float partialTicks) {
		float last = this.lastProgress;
		float now = this.progress;
		if ((double)last > 0.5D && (double)now < 0.5D) {
			++now;
		}
		float interp = last * (1.0F - partialTicks) + now * partialTicks;
		return interp % 1.0F;
	}

	@Override
	protected EnumPowerStage computePowerStage() {
		return EnumPowerStage.RED;
	}

	@Override
	protected void engineUpdate() {
		super.engineUpdate();

		if(!isRedstonePowered) {
			battery.addMJ(level, false);
		}
		else {
			battery.clearEnergy();
		}
	}

	@Override
	public long extractPower(long min, long max, boolean doExtract) {
		if(battery.getMjStored() < min) {
			return 0;
		}

		long actualMax;

		if(max > maxPowerExtracted()) {
			actualMax = maxPowerExtracted();
		}
		else {
			actualMax = max;
		}

		if(actualMax < min) {
			return 0;
		}

		long extracted;
		if(battery.getMjStored() >= actualMax) {
			extracted = actualMax;

			if(doExtract) {
				battery.extractMJ(actualMax, false);
			}
		}
		else {
			extracted = battery.getMjStored();

			if(doExtract) {
				battery.clearEnergy();
			}
		}

		return extracted;
	}

	private int extractPowerFE(int min, int max, boolean doExtract) {
		if(battery.getEnergyStored() < min) {
			return 0;
		}

		int actualMax;

		if(max > EnergyUtils.changeMJToRF(maxPowerExtracted())) {
			actualMax = EnergyUtils.changeMJToRF(maxPowerExtracted());
		}
		else {
			actualMax = max;
		}

		if(actualMax < min) {
			return 0;
		}

		int extracted;
		if(battery.getEnergyStored() >= actualMax) {
			extracted = actualMax;

			if(doExtract) {
				battery.extractEnergy(actualMax, false);
			}
		}
		else {
			extracted = battery.getEnergyStored();

			if(doExtract) {
				battery.clearEnergy();
			}
		}

		return extracted;
	}

	private long getPowerToExtractMJ(boolean doExtract) {
		TileEntity tile = getTileBuffer(currentDirection).getTile();
		if(tile == null || getClass() == tile.getClass())
			return 0L;

		IMjReceiver receiver = getReceiverToPower(tile, currentDirection);

		if(receiver != null)
			return extractPower(0L, receiver.getPowerRequested(), doExtract);

		IEnergyStorage storage = getEnergyStorage(tile, currentDirection);
		if(storage != null)
			return EnergyUtils.changeRFToMJ(extractPowerFE(0, storage.getMaxEnergyStored() - storage.getEnergyStored(), doExtract));

		return 0L;
	}

	private void sendPower() {
		TileEntity tile = getTileBuffer(currentDirection).getTile();
		if(tile == null)
			return;

		if(getClass() == tile.getClass())
			return;

		IMjReceiver receiver = getReceiverToPower(tile, currentDirection);
		IEnergyStorage storage = getEnergyStorage(tile, currentDirection);
		if(receiver != null) {
			long extracted = getPowerToExtractMJ(true);
			if(extracted > 0L) {
				long excess = receiver.receivePower(extracted, false);
				extractPower(extracted - excess, extracted - excess, true);
			}
		}
		else if(storage != null) {
			int extracted = EnergyUtils.changeMJToRF(getPowerToExtractMJ(true));
			if(extracted > 0) {
				int excess = storage.receiveEnergy(extracted, false);
				extractPowerFE(extracted - excess, extracted - excess, true);
			}
		}
	}

	private IEnergyStorage getEnergyStorage(TileEntity tile, EnumFacing facing) {
		if(tile == null) {
			return null;
		}

		IEnergyStorage storage = (IEnergyStorage)tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
		if(storage != null && storage.canReceive()) {
			return storage;
		}
		return null;
	}

	@Override
	protected IMjConnector createConnector() {
		return new EngineConnector(false);
	}

	@Override
	public float explosionRange() {
		return 0;
	}

	@Override
	public long getCurrentOutput() {
		return level * MjAPI.MJ;
	}

	@Override
	public long getMaxPower() {
		return MjAPI.MJ;
	}

	@Override
	public long maxPowerExtracted() {
		return 10000L * getCurrentOutput();
	}

	@Override
	public long maxPowerReceived() {
		return 0;
	}

	@Override
	public boolean isBurning() {
		return !isRedstonePowered;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY && currentDirection == facing) {
			return CapabilityEnergy.ENERGY.cast(battery);
		}
		return super.getCapability(capability, facing);
	}
}
