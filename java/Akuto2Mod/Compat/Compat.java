package Akuto2Mod.Compat;

import Akuto2Mod.Compat.CE.CompactEngine;
import Akuto2Mod.Compat.IC2.IndustrialCraft2;
import Akuto2Mod.Compat.Infinity.Infinity;
import Akuto2Mod.Compat.PE.ProjectE;
import cpw.mods.fml.common.Loader;

public class Compat {
	public static boolean ce = false;
	public static boolean ic2 = false;
	public static boolean pe = false;
	public static boolean infinity = false;

	public static void census() {
		ce = Loader.isModLoaded("CompactEngine");
		ic2 = Loader.isModLoaded("IC2");
		pe = Loader.isModLoaded("ProjectE");
		infinity = Loader.isModLoaded("InfinityChest");
	}

	public static void pre() {
		if(ic2) {
			IndustrialCraft2.preIC2();
		}
		if(pe) {
			ProjectE.prePE();
		}
		if(infinity) {
			Infinity.pre();
		}
	}

	public static void init() {
		if(ic2) {
			IndustrialCraft2.initIC2();
		}
		if(pe) {
			ProjectE.initPE();
		}
		if(ce) {
			CompactEngine.init();
		}
		if(infinity) {
			Infinity.init();
		}
	}
}
