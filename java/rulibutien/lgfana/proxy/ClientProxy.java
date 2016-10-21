package rulibutien.lgfana.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

import static rulibutien.lgfana.common.Lgfana.MODID;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void onInitGuiEvent(final InitGuiEvent.Post event) {

        if (event.gui instanceof GuiMainMenu) {
            event.buttonList.add(new MuteButton(30, event.gui.width / 2 + 105, event.gui.height / 4 + 132, event.gui));
        }

        if (event.gui instanceof GuiScreenOptionsSounds) {
            event.buttonList.add(new MuteButton(30, event.gui.width / 2 + 105, event.gui.height / 6 + 168, event.gui));
        }

        if (event.gui instanceof GuiIngameMenu) {
            event.buttonList.add(new MuteButton(30, event.gui.width / 2 + 105, event.gui.height / 4 + 105, event.gui));
        }

    }

}

class MuteButton extends GuiButton {

    private GuiScreen gui;
    private boolean on;
    private ResourceLocation textures;

    MuteButton(int id, int x, int y, GuiScreen gui) {
        super(id, x, y, 20, 20, "");
        this.gui = gui;
        this.on = this.gui.mc.gameSettings.getSoundLevel(SoundCategory.MASTER) != 0F;
        this.textures = new ResourceLocation(MODID + ":textures/gui/widgets" + this.on + ".png");
        this.drawButton(this.gui.mc, this.xPosition, this.yPosition);
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(this.textures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
            int k = 106;
            if (flag) k += this.height;
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, k, this.width, this.height);
        }
    }

    @Override
    public void mouseReleased(int x, int y) {

        this.on = this.gui.mc.gameSettings.getSoundLevel(SoundCategory.MASTER) != 0F;

        float level = this.on ? 0.0F : 1.0F;
        Minecraft.getMinecraft().gameSettings.setSoundLevel(SoundCategory.MASTER, level);

        this.on = !this.on;

        this.textures = new ResourceLocation(MODID + ":textures/gui/widgets" + this.on + ".png");
        this.drawButton(this.gui.mc, this.xPosition, this.yPosition);

    }

}

