package akuto2.utils.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumWorkType implements IStringSerializable{
	On("on"),
	Off("off");

	String name;

	private EnumWorkType(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
