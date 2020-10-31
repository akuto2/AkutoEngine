package akuto2.akutoengine.items;

import akuto2.akutoengine.utils.enums.EnumEngineType;
import buildcraft.lib.engine.BlockEngineBase_BC8;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockAkutoEngine extends ItemBlock{
	public ItemBlockAkutoEngine(BlockEngineBase_BC8 block) {
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + EnumEngineType.fromMeta(stack.getMetadata()).getName();
	}
}
