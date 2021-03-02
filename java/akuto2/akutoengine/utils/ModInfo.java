package akuto2.akutoengine.utils;

import net.minecraftforge.fml.common.ModMetadata;

public class ModInfo {
	public static void registerInfo(ModMetadata meta) {
		meta.modId = "akutoengine";
		meta.name = "AkutoEngine";
		meta.description = "Add Engine and Many Machines";
		meta.version = "2.0.11";
		meta.url = "https://minecraft.curseforge.com/projects/akuto-engine";
		meta.updateJSON = "https://raw.githubusercontent.com/akuto2/akutoengine/master/AkutoEngine-Update.json";
		meta.authorList.add("akuto2");
		meta.autogenerated = false;
	}
}
