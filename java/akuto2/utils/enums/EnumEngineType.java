package akuto2.utils.enums;

import buildcraft.api.core.IEngineType;
import net.minecraft.util.IStringSerializable;

public enum EnumEngineType implements IStringSerializable, IEngineType{
	X1("1"),
	X8("8"),
	X32("32"),
	X128("128"),
	X512("512"),
	X2048("2048"),
	SUPER("super"),
	SUPER2("super2"),
	FINAL("final");

	public final String name;
	public final String resourceLocation;

	public static final EnumEngineType[] VALUES = values();

	EnumEngineType(String name) {
		this.name = name;
		resourceLocation = "akutoengine:models/item/engine/akutoengine" + name;
	}

	@Override
	public String getItemModelLocation() {
		return resourceLocation;
	}

	@Override
	public String getName() {
		return name;
	}

	public static EnumEngineType fromMeta(int meta) {
		if(meta < 0 || meta >= VALUES.length) {
			meta = 0;
		}

		return VALUES[meta];
	}
}
