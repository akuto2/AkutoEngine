package akuto2.akutoengine.utils;

import cpw.mods.fml.common.ModMetadata;

public class ModInfo {

	public static void load(ModMetadata meta) {
		meta.modId = "AkutoEngine";
		meta.name = "AkutoEngine";
		meta.description = "Add Engine and Many Machines";
		meta.version = "1.4.3";
		meta.url = "https://minecraft.curseforge.com/projects/akuto-engine";
		meta.updateUrl = "https://raw.githubusercontent.com/akuto2/akutoengine/master/AkutoEngine-Update.json";
		meta.authorList.add("akuto2");
		meta.autogenerated = false;
	}
}
