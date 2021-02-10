package akuto2.akutoengine.gui;

import ic2.core.ContainerFullInv;
import ic2.core.block.invslot.InvSlotArmor;
import ic2.core.block.wiring.TileEntityElectricBlock;
import ic2.core.slot.SlotInvSlot;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public class ContainerUmfsUnit extends ContainerFullInv<TileEntityElectricBlock>{
	public ContainerUmfsUnit(EntityPlayer player, TileEntityElectricBlock tileentity){
		super(player, tileentity, 196);
		for(int col = 0; col < 4; col++){
			addSlotToContainer(new InvSlotArmor(player.inventory, col, 8 + col * 18, 84));
		}
		addSlotToContainer(new SlotInvSlot(tileentity.chargeSlot, 0, 56, 17));
		addSlotToContainer(new SlotInvSlot(tileentity.dischargeSlot, 0, 56, 53));
	}

	@Override
	public List<String> getNetworkedFields(){
		List<String> ret = super.getNetworkedFields();

		ret.add("energy");
		ret.add("redstoneMode");
		ret.add("chargeSlot");
		ret.add("dischargeSlot");

		return ret;
	}
}
