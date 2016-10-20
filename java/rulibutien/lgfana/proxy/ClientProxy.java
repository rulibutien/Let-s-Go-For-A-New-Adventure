package rulibutien.lgfana.proxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.GameSettings;
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

            event.buttonList.add(new GuiButton(30, event.gui.width / 2 + 105, event.gui.height / 4 + 132, 20, 20, "") {

                float level = Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MASTER);
                private ResourceLocation textures = new ResourceLocation(MODID + ":textures/gui/widgets" + (level != 0F) + ".png");

                @Override
                public void drawButton(Minecraft mc, int x, int y) {

                    if (this.visible) {
                        mc.getTextureManager().bindTexture(textures);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        boolean flag = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
                        int k = 106;

                        if (flag) {
                            k += this.height;
                        }

                        this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, k, this.width, this.height);
                    }

                }

                @Override
                public void mouseReleased(int x, int y) {
                    GameSettings settings = Minecraft.getMinecraft().gameSettings;
                    float level = settings.getSoundLevel(SoundCategory.MASTER);
                    if (level == 0) {
                        textures = new ResourceLocation(MODID + ":textures/gui/widgets" + true + ".png");
                        this.drawButton(event.gui.mc, this.xPosition, this.yPosition);
                        settings.setSoundLevel(SoundCategory.MASTER, 1F);
                        Minecraft.getMinecraft().getSoundHandler().update();
                        settings.loadOptions();
                        Minecraft.getMinecraft().getSoundHandler().update();
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                    } else {
                        textures = new ResourceLocation(MODID + ":textures/gui/widgets" + false + ".png");
                        this.drawButton(event.gui.mc, this.xPosition, this.yPosition);
                        settings.setSoundLevel(SoundCategory.MASTER, 0F);
                    }
                }

            });

        }

    }


}