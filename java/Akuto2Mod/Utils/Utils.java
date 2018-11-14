package Akuto2Mod.Utils;

import net.minecraft.init.Blocks;

public class Utils {
	// Fillerのモジュールの数
	public static int fillerModuleMaxItem = 12;
	public static Object[] fillerRecipeItem = {'b', Blocks.brick_block, 'g', Blocks.glass};
	public static int ELECTRIC_BLOCK_GUI_ID = 0;
	public static int FILLER_EX_GUI_ID = 1;
	public static int AUTO_WORKBENCH_GUI_ID = 2;
	public static int EMC_BUILDER_GUI_ID = 3;
	// EMCコンテナ―の種類
	public static int emcContainerType = 4;
	// EMCコンテナ―の容量
	public static int[] emcContainerTank = new int[] {10000, 80000, 640000, 2560000};
}
