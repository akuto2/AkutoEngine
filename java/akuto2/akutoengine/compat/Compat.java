package akuto2.akutoengine.compat;

import akuto2.akutoengine.compat.ce.CompactEngine;
import akuto2.akutoengine.compat.ic2.IndustrialCraft2;
import akuto2.akutoengine.compat.infinity.Infinity;
import akuto2.akutoengine.compat.pe.ProjectE;
import akuto2.akutoengine.compat.storagebox.StorageBox;
import cpw.mods.fml.common.Loader;

public class Compat {
	public static boolean ce = false;
	public static boolean ic2 = false;
	public static boolean pe = false;
	public static boolean infinity = false;
	public static boolean sb = false;

	public static void census() {
		ce = Loader.isModLoaded("CompactEngine");
		ic2 = Loader.isModLoaded("IC2");
		pe = Loader.isModLoaded("ProjectE");
		infinity = Loader.isModLoaded("InfinityChest");
		sb = Loader.isModLoaded("net.minecraft.storagebox.mod_StorageBox");
	}

	public static void pre() {
		census();

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
		census();

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
		if(sb) {
			StorageBox.init();
		}
	}

	public static void postInit() {
		census();

		if(sb) {
			StorageBox.post();
		}
	}
}
