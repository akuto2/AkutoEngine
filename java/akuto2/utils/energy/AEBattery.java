package akuto2.utils.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * MJ・RF・FEを管理するためのバッテリー
 */
public class AEBattery implements INBTSerializable<NBTTagCompound>, IEnergyStorage{
	// 内部的にはMJ換算で持っておく
	private long energy = 0L;
	private long maxEnergy;
	private long maxReceive;
	private long maxExtract;

	public AEBattery(long maxEnergy, long maxReceive, long maxExtract) {
		this.maxEnergy = maxEnergy;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setLong("energy", energy);
		compound.setLong("maxEnergy", maxEnergy);
		compound.setLong("maxReceive", maxReceive);
		compound.setLong("maxExtract", maxExtract);
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if(nbt.hasKey("energy") && nbt.hasKey("maxEnergy") && nbt.hasKey("maxReceive") && nbt.hasKey("maxExtract")) {
			energy = nbt.getLong("energy");
			maxEnergy = nbt.getLong("maxEnergy");
			maxReceive = nbt.getLong("maxReceive");
			maxExtract = nbt.getLong("maxExtract");
		}
	}

	// RFとFE用
	public int addEnergy(int minEnergy, int maxEnergy, boolean simulate) {
		int amountReceived = Math.min(maxEnergy, EnergyUtils.changeMJToRF(this.maxEnergy - this.energy));

		if(amountReceived < minEnergy) {
			return 0;
		} else {
			if(!simulate) {
				energy += EnergyUtils.changeRFToMJ(amountReceived);
			}

			return amountReceived;
		}
	}

	public int useEnergy(int minEnergy, int maxEnergy, boolean simulate) {
		int amountExtracted = Math.min(maxEnergy, EnergyUtils.changeMJToRF(energy));

		if(amountExtracted < minEnergy) {
			return 0;
		} else {
			if(!simulate) {
				energy -= EnergyUtils.changeRFToMJ(amountExtracted);
			}

			return amountExtracted;
		}
	}

	@Override
	public int receiveEnergy(int energy, boolean simulate) {
		return addEnergy(0, Math.min(energy, EnergyUtils.changeMJToRF(this.maxReceive)), simulate);
	}

	@Override
	public int extractEnergy(int energy, boolean simulate) {
		return useEnergy(0, Math.min(energy, EnergyUtils.changeMJToRF(this.maxExtract)), simulate);
	}

	public void clearEnergy() {
		energy = 0L;
	}

	@Override
	public int getEnergyStored() {
		return EnergyUtils.changeMJToRF(energy);
	}

	@Override
	public int getMaxEnergyStored() {
		return EnergyUtils.changeMJToRF(maxEnergy);
	}

	public int getMaxReceived() {
		return EnergyUtils.changeMJToRF(maxReceive);
	}

	public int getMaxExtract() {
		return EnergyUtils.changeMJToRF(maxExtract);
	}

	// MJ用

	/**
	 * MJ換算でエネルギーの受け取り
	 */
	public long addMJ(long mj, boolean simulate) {
		if(isFull()) {
			return mj;
		}

		if(maxEnergy - energy < mj) {
			long receivedEnergy = maxEnergy - energy;
			if(!simulate) {
				energy += receivedEnergy;
			}
			return mj - receivedEnergy;
		}
		else {
			if(!simulate) {
				energy += mj;
			}
			return 0L;
		}
	}

	public long extractMJ(long mj, boolean simulate) {
		return EnergyUtils.changeRFToMJ(useEnergy(0, Math.min(EnergyUtils.changeMJToRF(mj), EnergyUtils.changeMJToRF(this.maxReceive)), simulate));
	}

	/**
	 * MJ換算の現在の容量を取得する
	 */
	public long getMjStored() {
		return energy;
	}

	/**
	 * MJ換算の最大容量を取得する
	 */
	public long getMjCapacity() {
		return maxEnergy;
	}

	public boolean isFull() {
		return energy >= maxEnergy;
	}

	public void setPower(long maxEnergy, long maxReceive, long maxExtract) {
		this.maxEnergy = maxEnergy;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	@Override
	public boolean canExtract() {
		if(maxExtract > 0 && getMjStored() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canReceive() {
		if(maxReceive > 0 && !isFull()) {
			return true;
		}
		return false;
	}
}
