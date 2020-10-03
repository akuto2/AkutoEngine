package akuto2.utils.energy;

import buildcraft.api.mj.IMjConnector;
import buildcraft.api.mj.IMjReceiver;

public class AEBatteryReceiver implements IMjReceiver{
	private final AEBattery battery;

	public AEBatteryReceiver(AEBattery battery) {
		this.battery = battery;
	}

	public void setBattery(long maxEnergy, long maxReceive, long maxExtract) {
		battery.setPower(maxEnergy, maxReceive, maxExtract);
	}

	@Override
	public boolean canConnect(IMjConnector var1) {
		return true;
	}

	@Override
	public long getPowerRequested() {
		return battery.getMjCapacity() - battery.getMjStored();
	}

	@Override
	public long receivePower(long energy, boolean simulate) {
		return battery.addMJ(energy, simulate);
	}

}
