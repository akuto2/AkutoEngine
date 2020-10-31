package akuto2.akutoengine.utils.energy;

import buildcraft.api.mj.MjAPI;

public class EnergyUtils {
	/**
	 * MJ換算のものをRFに変える(MJで割って10倍する)
	 */
	public static int changeMJToRF(long mj) {
		return (int)((mj / MjAPI.MJ) * 10);
	}

	/**
	 * RF換算のものをMJに変える
	 */
	public static long changeRFToMJ(int rf) {
		return ((rf / 10) * MjAPI.MJ);
	}
}
