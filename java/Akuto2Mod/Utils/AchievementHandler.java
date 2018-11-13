package Akuto2Mod.Utils;

import Akuto2Mod.Akuto2Core;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementHandler {
	public static Achievement[] akutoEngineAchivement;

	public static Achievement getAkutoEngine;
	public static Achievement getSuperEngine;
	public static Achievement getFiller;
	public static Achievement getTank;
	public static Achievement getPump;

	public static Achievement getFeather;
	public static Achievement getLeather;

	public static void addAchivement() {
		getAkutoEngine = new Achievement("getAkutoEngine", "getAkutoEngine", 0, 0, new ItemStack(Akuto2Core.engineBlock, 0), null).registerStat();
		getSuperEngine = new Achievement("getSuperEngine", "getSuperEngine", 2, 0, new ItemStack(Akuto2Core.engineBlock, 6), getAkutoEngine).registerStat().setSpecial();
		getFiller = new Achievement("getFiller", "getFiller", -2, 2, new ItemStack(Akuto2Core.fillerEX), null).registerStat();
		getTank = new Achievement("getTank", "getTank", 0, 2, new ItemStack(Akuto2Core.TankEX), null).registerStat();
		getPump = new Achievement("getPump", "getPump", 2, 2, new ItemStack(Akuto2Core.pumpEX, 0), null).registerStat();

		getFeather = new Achievement("getFeather", "getFeather", 0, -2, new ItemStack(Akuto2Core.coreElementary1), null).registerStat().setSpecial();
		getLeather = new Achievement("getLeather", "getLeather", 2, -2, new ItemStack(Akuto2Core.coreElementary2), null).registerStat().setSpecial();
		akutoEngineAchivement = new Achievement[] {getAkutoEngine, getSuperEngine, getFiller, getTank, getPump, getFeather, getLeather};

		AchievementPage.registerAchievementPage(new AchievementPage("AkutoEngine", akutoEngineAchivement));
	}
}
