package akuto2.utils;

import net.minecraft.item.ItemStack;

public class Utils {
	public static int FILLEREX_GUI = 0;

	public static final long[] MAX_KLEIN_EX_EMC = new long[] { 409600000L, 2048000000L };

	public static long getKleinStarEXMaxEmc(ItemStack stack) {
		return MAX_KLEIN_EX_EMC[stack.getItemDamage()];
	}
}
