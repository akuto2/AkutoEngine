package akuto2.akutoengine.player;

import java.lang.ref.WeakReference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerStats implements IExtendedEntityProperties{
	public WeakReference<EntityPlayer> player;
	public boolean fillerManual;

	public PlayerStats() {

	}

	public PlayerStats(EntityPlayer player) {
		this.player = new WeakReference(player);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("fillerManual", fillerManual);
		compound.setTag("AkutoEngine", tag);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound)compound.getTag("AkutoEngine");
		fillerManual = properties.getBoolean("fillerManual");
	}

	@Override
	public void init(Entity entity, World world) {
		player = new WeakReference((EntityPlayer)entity);
	}

	public void copyFrom(PlayerStats stats) {
		fillerManual = stats.fillerManual;
	}

	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties("AkutoEngine", new PlayerStats(player));
	}

	public static PlayerStats get(EntityPlayer player) {
		return (PlayerStats)player.getExtendedProperties("AkutoEngine");
	}
}
