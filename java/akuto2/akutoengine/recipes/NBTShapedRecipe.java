package akuto2.akutoengine.recipes;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;

/**
 * BCのゲートのようにNBTデータでアイテムを判断するものを使うレシピ用のクラス
 */
public class NBTShapedRecipe extends ShapedRecipes{
	public NBTShapedRecipe(ItemStack output, Object... inputs) {
		this(3, 3, getItemStackArray(inputs), output);
	}

	public NBTShapedRecipe(int width, int height, ItemStack[] inputs, ItemStack output) {
		super(width, height, inputs, output);
	}

	public static ItemStack[] getItemStackArray(Object... objects) {
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		if(objects[i] instanceof String[]) {
			String[] strings = (String[])objects[i++];
			for(int l = 0; l < strings.length; l++) {
				String s1 = strings[l];
				k++;
				j = s1.length();
				s = s + s1;
			}
		}
		else {
			while(objects[i] instanceof String) {
				String s1 = (String)objects[i++];
				k++;
				j = s1.length();
				s = s + s1;
			}
		}
		HashMap hashMap;
		for(hashMap = new HashMap(); i < objects.length; i += 2) {
			Character c = (Character)objects[i];
			ItemStack stack = null;
			if(objects[i + 1] instanceof Item) {
				stack = new ItemStack((Item)objects[i + 1]);
			}
			else if(objects[i + 1] instanceof Block) {
				stack = new ItemStack((Block)objects[i + 1]);
			}
			else if(objects[i + 1] instanceof ItemStack) {
				stack = (ItemStack)objects[i + 1];
			}
			hashMap.put(c, stack);
		}

		ItemStack[] stacks = new ItemStack[j * k];
		for(int i1 = 0; i1 < j * k; i1++) {
			char c = s.charAt(i1);
			if(hashMap.containsKey(Character.valueOf(c))) {
				stacks[i1] = ((ItemStack)hashMap.get(Character.valueOf(c))).copy();
			}
			else {
				 stacks[i1] = null;
			}
		}

		return stacks;
	}

	@Override
	public boolean matches(InventoryCrafting inventoryCrafting, World world) {
		for(int i = 0; i <= 3 - recipeWidth; i++) {
			for(int j = 0; j <= 3 - recipeHeight; j++) {
				if(checkMatches(inventoryCrafting, i, j, true)) {
					return true;
				}
				if(checkMatches(inventoryCrafting, i, j, false)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkMatches(InventoryCrafting inventoryCrafting, int x, int y, boolean mirrored) {
		for(int i = 0; i < recipeWidth; i++) {
			for(int j = 0; j < recipeHeight; j++) {
				int k = i - x;
				int l = j - y;
				ItemStack stack = null;
				if((k >= 0) && (l >= 0) && (k < recipeWidth) && (l < recipeHeight)) {
					if(mirrored) {
						stack = recipeItems[recipeWidth - k - 1 + l * recipeWidth];
					}
					else {
						stack = recipeItems[k + l * recipeWidth];
					}
				}
				ItemStack stack2 = inventoryCrafting.getStackInRowAndColumn(i, j);
				if((stack != null) || (stack2 != null)) {
					if(stack == null || stack2 == null) {
						return false;
					}

					if(!stack.isItemEqual(stack2) && !ItemStack.areItemStackTagsEqual(stack, stack2)) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
