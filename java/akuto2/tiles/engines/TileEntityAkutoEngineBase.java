package akuto2.tiles.engines;

import akuto2.utils.energy.EnergyUtils;
import buildcraft.api.enums.EnumPowerStage;
import buildcraft.api.mj.IMjConnector;
import buildcraft.api.mj.IMjReceiver;
import buildcraft.api.mj.MjAPI;
import buildcraft.lib.engine.EngineConnector;
import buildcraft.lib.engine.TileEngineBase_BC8;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityAkutoEngineBase extends TileEngineBase_BC8{
	public long level;
	private float progress;
	private float lastProgress;
	private int progressPart;
	public static final int[] powerlevel = {1, 8, 32, 128, 512, 2048, 8192, 32768, 1000000};

	public TileEntityAkutoEngineBase(int meta) {
		super();
		initAkutoEngine(meta);
	}

	public void initAkutoEngine(int meta) {
		level = EnergyUtils.changeRFToMJ(powerlevel[meta]);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		compound.setFloat("ae_progress", progress);
		compound.setInteger("ae_progressPart", progressPart);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		progress = compound.getFloat("ae_progress");
		progressPart = compound.getInteger("ae_progressPart");
		return compound;
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

		if(progressPart != 0) {
			progress = (float)(progress + getPistonSpeed());
			if(progress > 0.5F && progressPart == 1) {
				progressPart = 2;
				sendPower();
			}
			else if(progress >= 1.0F) {
				progress = 0.0F;
				progressPart = 0;
			}
		}
		else if(!isRedstonePowered && isActive()) {
			if(getPowerToExtract(false) > 0L) {
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
			power = level;
		}
		else {
			power = 0L;
		}
	}

	private long getPowerToExtract(boolean doExtract) {
		TileEntity tile = getTileBuffer(currentDirection).getTile();
		if(tile == null || getClass() == tile.getClass())
			return 0L;

		IMjReceiver receiver = getReceiverToPower(tile, currentDirection);
		if(receiver == null)
			return 0L;

		return extractPower(0L, receiver.getPowerRequested(), doExtract);
	}

	private void sendPower() {
		TileEntity tile = getTileBuffer(currentDirection).getTile();
		if(tile == null)
			return;

		if(getClass() == tile.getClass())
			return;

		IMjReceiver receiver = getReceiverToPower(tile, currentDirection);
		if(receiver != null) {
			long extracted = getPowerToExtract(true);
			if(extracted > 0L) {
				long excess = receiver.receivePower(extracted, false);
				extractPower(extracted - excess, extracted - excess, true);
			}
		}
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
}
