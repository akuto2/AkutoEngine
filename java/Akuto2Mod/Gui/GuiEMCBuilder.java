package Akuto2Mod.Gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import Akuto2Mod.TileEntity.TileEMCBuilder;
import Akuto2Mod.Utils.EMCWorldSave;
import buildcraft.BuildCraftCore;
import buildcraft.builders.gui.SlotBuilderRequirement;
import buildcraft.core.blueprints.RequirementItemStack;
import buildcraft.core.lib.fluids.Tank;
import buildcraft.core.lib.gui.AdvancedSlot;
import buildcraft.core.lib.gui.GuiAdvancedInterface;
import buildcraft.core.lib.network.Packet;
import buildcraft.core.lib.network.command.CommandWriter;
import buildcraft.core.lib.network.command.PacketCommand;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiEMCBuilder extends GuiAdvancedInterface{
	private static final ResourceLocation REGULAR_TEXTURE = new ResourceLocation("akutoengine:textures/gui/emcbuilder.png");
	private static final ResourceLocation BLUEPRINT_TEXTURE = new ResourceLocation("akutoengine:textures/gui/emcbuilder_blueprint.png");
	private TileEMCBuilder emcBuilder;
	private GuiButton selectedButton;
	private static EMCWorldSave worldData;
	private GuiButton selectButton;

	public GuiEMCBuilder(InventoryPlayer player, TileEMCBuilder emcBuilder) {
		super(new ContainerEMCBuilder(player, emcBuilder), emcBuilder, BLUEPRINT_TEXTURE);
		this.emcBuilder = emcBuilder;
		worldData = this.emcBuilder.getWorldSaveData(this.emcBuilder.getWorldObj());
		xSize = 256;
		ySize = 225;

		resetNullSlots(24);
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 4; j++) {
				slots.set(i * 4 + j, new SlotBuilderRequirement(this, 179 + j * 18, 18 + i * 18));
			}
		}
	}

	@Override
	public void initGui() {
		super.initGui();
		for(int i = 0; i < 4; i++) {
			buttonList.add(new EMCBuilderEraseButton(i, guiLeft + 178 + 18 * i, guiTop + 197, 18, 18));
		}
	}

	private ContainerEMCBuilder getContainerEMCBuilder() {
		return (ContainerEMCBuilder)getContainer();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if(emcBuilder.isBuildingBlueprint()) {
			mc.renderEngine.bindTexture(REGULAR_TEXTURE);
			drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, ySize);
		}
		else {
			mc.renderEngine.bindTexture(BLUEPRINT_TEXTURE);
			drawTexturedModalRect(guiLeft + 19, guiTop, 0, 0, 87, ySize);
		}
		List<RequirementItemStack> needs = emcBuilder.getNeededItems();
		if(needs != null) {
			if(needs.size() > slots.size()) {
				getContainerEMCBuilder().scrollbarWidget.hidden = false;
				getContainerEMCBuilder().scrollbarWidget.setLength((needs.size() - slots.size() + 3) / 4);
			}
			else {
				getContainerEMCBuilder().scrollbarWidget.hidden = true;
			}

			int offset = getContainerEMCBuilder().scrollbarWidget.getPosition() * 4;
			for(int i = 0; i < slots.size(); i++) {
				int ts = offset * i;
				if(ts >= needs.size()) {
					((SlotBuilderRequirement)slots.get(i)).stack = null;
				}
				else {
					((SlotBuilderRequirement)slots.get(i)).stack = needs.get(ts);
				}
			}
			for(GuiButton b : (List<GuiButton>)buttonList) {
				b.visible = true;
			}
		}
		else {
			getContainerEMCBuilder().scrollbarWidget.hidden = true;
			for(AdvancedSlot slot : slots) {
				((SlotBuilderRequirement)slot).stack = null;
			}
			for(GuiButton b : (List<GuiButton>)buttonList) {
				b.visible = false;
			}
		}

		drawWidgets(x, y);
		if(emcBuilder.isBuildingBlueprint()) {
			for(int i = 0; i < emcBuilder.fluidTanks.length; i++) {
				Tank tank = emcBuilder.fluidTanks[i];
				if(tank.getFluid() != null && (tank.getFluid()).amount > 0) {
					drawFluid(tank.getFluid(), guiLeft + 179 + 18 * i, guiTop + 145, 16, 47, tank.getCapacity());
				}
			}
			mc.renderEngine.bindTexture(BLUEPRINT_TEXTURE);
			for(int i = 0; i < emcBuilder.fluidTanks.length; i++) {
				Tank tank = emcBuilder.fluidTanks[i];
				if(tank.getFluid() != null && (tank.getFluid()).amount > 0) {
					drawTexturedModalRect(guiLeft + 179 + 18 * i, guiTop + 145, 0, 54, 16, 47);
				}
			}
		}
	}

	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int eventType) {
		super.mouseMovedOrUp(mouseX, mouseY, eventType);
	}

	private class EMCBuilderEraseButton extends GuiButton{
		private boolean clicked;

		public EMCBuilderEraseButton(int id, int x, int y, int width, int height) {
			super(id, x, y, width, height, null);
		}

		@Override
		public boolean mousePressed(Minecraft mc, int x, int y) {
			if(mousePressed(mc, x, y)) {
				GuiEMCBuilder.this.selectedButton = this;
				clicked = true;
				BuildCraftCore.instance.sendToServer((Packet)new PacketCommand(GuiEMCBuilder.this.emcBuilder, "eraseFluidTank", new CommandWriter() {
					@Override
					public void write(ByteBuf data) {
						data.writeInt(GuiEMCBuilder.EMCBuilderEraseButton.this.id);
					}
				}));
				return true;
			}
			return false;
		}

		@Override
		public void mouseReleased(int x, int y) {
			super.mouseReleased(x, y);
			clicked = false;
		}

		@Override
		public void drawButton(Minecraft mc, int x, int y) {
			if(!visible) {
				return;
			}
			field_146123_n = (x >= xPosition && y >= yPosition && x < xPosition + width && y < yPosition + height);
			mc.renderEngine.bindTexture(GuiEMCBuilder.BLUEPRINT_TEXTURE);
			drawTexturedModalRect(xPosition, yPosition, 0, (clicked ? 1 : (field_146123_n ? 2 : 0)) * 18, 18, 18);
			mouseDragged(mc, x, y);
		}
	}
}
