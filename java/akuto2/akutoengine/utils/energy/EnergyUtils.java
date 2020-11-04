package akuto2.akutoengine.utils.energy;

import buildcraft.api.mj.MjAPI;

public class EnergyUtils {
	/**
	 * MJ換算のものをRFに変える
	 */
	public static int changeMJToRF(long mj) {
		return (int)(mj / MjAPI.MJ);
	}

	/**
	 * RF換算のものをMJに変える
	 */
	public static long changeRFToMJ(int rf) {
		return rf * MjAPI.MJ;
	}
}
