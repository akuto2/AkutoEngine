package Akuto2Mod.TileEntity.Engine;

import Akuto2Mod.Akuto2Core;
import buildcraft.api.power.IEngine;
import buildcraft.api.transport.IPipeTile.PipeType;
import buildcraft.core.lib.engines.TileEngineBase;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

public class TileAutoEngineCore extends TileEngineBase{
	public int power;
	public int no;
	public int level;
	public double stageRed;
	public static final int[] powerlevel = {1, 8, 32, 128, 512, 2048, 8192, 32768, 1000000};
	public static final float[] pistonSpeed = {};
	private boolean isPumping;
	private boolean checkOrientation;

	public TileAutoEngineCore(int meta) {
		this.initAutoEngine(meta);
	}


	public void initAutoEngine(int meta){
		level = powerlevel[meta];
		no = meta;
		power = MathHelper.ceiling_float_int(level * 0.5f);
		this.stageRed = (250.0D * this.level);
	}

	@Override
    public ResourceLocation getBaseTexture() {
        return (ResourceLocation)Akuto2Core.RESOURCE_LOCATION_LIST.get(this.no);
    }

	@Override
    public ResourceLocation getChamberTexture() {
        return super.getChamberTexture();
    }

	public ResourceLocation getTrunkTexture() {
		return TRUNK_RED_TEXTURE;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
	    if (this.worldObj.isRemote)
	    {
	      if (this.progressPart != 0)
	      {
	        this.progress += getPistonSpeed();
	        if (this.progress > 1.0F)
	        {
	          this.progressPart = 0;
	          this.progress = 0.0F;
	        }
	      }
	      else if (this.isPumping)
	      {
	        this.progressPart = 1;
	      }
	      return;
	    }
	    if (this.checkOrientation)
	    {
	      this.checkOrientation = false;
	      if (!isOrientationValid()) {
	        switchOrientation(true);
	      }
	    }
	    updateHeat();
	    getEnergyStage();
	    if (getEnergyStage() == EnergyStage.OVERHEAT)
	    {
	      this.energy = Math.max(this.energy - 50, 0);
	      return;
	    }
	    engineUpdate();

	    Object tile = getEnergyProvider(this.orientation);
	    if (this.progressPart != 0)
	    {
	      this.progress += getPistonSpeed();
	      if ((this.progress > 0.5D) && (this.progressPart == 1))
	      {
	        this.progressPart = 2;
	      }
	      else if (this.progress >= 1.0F)
	      {
	        this.progress = 0.0F;
	        this.progressPart = 0;
	      }
	    }
	    else if ((!this.isRedstonePowered) && (isActive()))
	    {
	      if (isPoweredTile(tile, this.orientation))
	      {
	        this.progressPart = 1;
	        setPumping(true);
	        if (getPowerToExtract() > 0)
	        {
	          this.progressPart = 1;
	          setPumping(true);
	        }
	        else
	        {
	          setPumping(false);
	        }
	      }
	      else
	      {
	        setPumping(false);
	      }
	    }
	    else
	    {
	      setPumping(false);
	    }
	    burn();
	    if (this.isRedstonePowered) {
	      this.currentOutput = 0;
	    } else if ((!this.isRedstonePowered) && (isActive())) {
	      sendPower();
	    }
	}

	private int getPowerToExtract()
	  {
	    Object tile = getEnergyProvider(this.orientation);
	    if ((tile instanceof IEngine))
	    {
	      IEngine engine = (IEngine)tile;

	      int maxEnergy = engine.receiveEnergyFromEngine(this.orientation.getOpposite(), this.energy, true);

	      return extractEnergy(maxEnergy, false);
	    }
	    if ((tile instanceof IEnergyHandler))
	    {
	      IEnergyHandler handler = (IEnergyHandler)tile;

	      int maxEnergy = handler.receiveEnergy(this.orientation.getOpposite(), this.energy, true);

	      return extractEnergy(maxEnergy, false);
	    }
	    if ((tile instanceof IEnergyReceiver))
	    {
	      IEnergyReceiver handler = (IEnergyReceiver)tile;

	      int maxEnergy = handler.receiveEnergy(this.orientation.getOpposite(), this.energy, true);

	      return extractEnergy(maxEnergy, false);
	    }
	    return 0;
	 }

	//public int minEnergyReceived() {
	//	return 0;
	//}

	//public int maxEnergyReceived() {
	//	return 50 * level;
	//}

	/*@Override
	public float getHeatLevel() {
		return this.energy >= this.getMaxEnergy() - this.stageRed ?
				(int)(this.getMaxEnergy() - this.stageRed) : (int)this.energy;
	}*/

	@Override
	protected EnergyStage computeEnergyStage() {
		return EnergyStage.RED;
	}

	@Override
	public float getPistonSpeed() {
		if (!worldObj.isRemote)
			return Math.max(0.10f * getHeatLevel(), 0.01f);
		else {
			switch (getEnergyStage()) {
				case RED:
					return 0.10F;
				default:
					return 0;
			}
		}
	}

	@Override
	public void engineUpdate() {
		super.engineUpdate();
		//if (isRedstonePowered && energy > power * 20)
		//{
		//	energy -= power * 20;
		//}
		//	energy += power;
//		if (isRedstonePowered/* && energy > power * 20*/) {
//        energy -= power * 20;
//      }
		if(!isRedstonePowered){
			energy = level;
		}
		if(isRedstonePowered){
			energy = 0;
		}
	}


	@Override
	public ConnectOverride overridePipeConnection(PipeType type, ForgeDirection with) {
		 return type == PipeType.POWER?ConnectOverride.CONNECT:ConnectOverride.DISCONNECT;
	}

	@Override
    public int getMaxEnergy() {
        return 100 * level * 20;
    }

	//public int getCurrentOutput() {
	//	return power;
	//}

	/*public int maxEnergyExtracted() {
		return level / 2;
	}*/


	@Override
	public boolean isBurning() {
		return false;
	}


	@Override
	public int getIdealOutput() {
		return 0;
	}
}
