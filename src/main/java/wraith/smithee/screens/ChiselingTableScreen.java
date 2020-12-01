package wraith.smithee.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wraith.smithee.utils.Utils;

public class ChiselingTableScreen extends HandledScreen<ScreenHandler> {

    private static final Identifier TEXTURE = Utils.ID("textures/gui/chiseling_table.png");
    public final ChiselingTableScreenHandler handler;
    private boolean mouseClicked = false;

    public ChiselingTableScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.handler = (ChiselingTableScreenHandler) handler;
        this.backgroundWidth = 176;
        this.backgroundHeight = 205;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        this.titleX += 20;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(TEXTURE);
        int x = this.x;
        int y = this.y;
        this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        //Chisel
        if (this.handler.slots.get(0).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 17, y + 44, 176 + 48, 0, 16, 16);
        }

        //Head
        if (this.handler.slots.get(4).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 109, y + 20, 176 + 32, 0, 16, 16);
        }
        //Binding
        if (this.handler.slots.get(5).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 109, y + 44, 176 + 16, 0, 16, 16);
        }
        //Handle
        if (this.handler.slots.get(6).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 109, y + 68, 176, 0, 16, 16);
        }

        //Shards
        if (this.handler.slots.get(7).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 130, y + 20, 176 + 64, 0, 16, 16);
        }
        if (this.handler.slots.get(8).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 130, y + 44, 176 + 64, 0, 16, 16);
        }
        if (this.handler.slots.get(9).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 130, y + 68, 176 + 64, 0, 16, 16);
        }


        drawButton(matrices, x, y, 78, 18, 196, 16, mouseX, mouseY);
        drawButton(matrices, x, y, 78, 42, 196, 16, mouseX, mouseY);
        drawButton(matrices, x, y, 78, 66, 196, 16, mouseX, mouseY);

        drawButton(matrices, x, y, 8, 90, 196, 36, mouseX, mouseY, 0);
        drawButton(matrices, x, y, 43, 90, 196, 56, mouseX, mouseY, 1);
        drawButton(matrices, x, y, 78, 90, 196, 76, mouseX, mouseY, 2);
        drawButton(matrices, x, y, 113, 90, 196, 96, mouseX, mouseY, 3);
        drawButton(matrices, x, y, 148, 90, 196, 116, mouseX, mouseY, 4);

    }

    public void drawButton(MatrixStack matrices, int x, int y, int xm, int ym, int u, int v, int mouseX, int mouseY, int index) {
        if (handler.getPage() == index) {
            u -= 20;
        }
        else if (mouseX >= x + xm && mouseY >= y + ym && mouseX < x + xm + 20 && mouseY < y + ym + 20) {
            u += mouseClicked ? -20 : 20;
        }

        this.drawTexture(matrices, x + xm, y + ym, u, v, 20, 20);
    }

    public void drawButton(MatrixStack matrices, int x, int y, int xm, int ym, int u, int v, int mouseX, int mouseY) {
        if (mouseX >= x + xm && mouseY >= y + ym && mouseX < x + xm + 20 && mouseY < y + ym + 20) {
            u += mouseClicked ? -20 : 20;
        }
        this.drawTexture(matrices, x + xm, y + ym, u, v, 20, 20);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = true;
        boolean but1 = mouseX >= x + 78 && mouseY >= y + 13 && mouseX < x + 78 + 20 && mouseY < y + 13 + 20;
        boolean but2 = mouseX >= x + 78 && mouseY >= y + 37 && mouseX < x + 78 + 20 && mouseY < y + 37 + 20;
        boolean but3 = mouseX >= x + 78 && mouseY >= y + 61 && mouseX < x + 78 + 20 && mouseY < y + 61 + 20;

        boolean tool1 = mouseX >= x + 8 && mouseY >= y + 85 && mouseX < x + 8 + 20 && mouseY < y + 85 + 20;
        boolean tool2 = mouseX >= x + 43 && mouseY >= y + 85 && mouseX < x + 43 + 20 && mouseY < y + 85 + 20;
        boolean tool3 = mouseX >= x + 78 && mouseY >= y + 85 && mouseX < x + 78 + 20 && mouseY < y + 85 + 20;
        boolean tool4 = mouseX >= x + 113 && mouseY >= y + 85 && mouseX < x + 113 + 20 && mouseY < y + 85 + 20;
        boolean tool5 = mouseX >= x + 148 && mouseY >= y + 85 && mouseX < x + 148 + 20 && mouseY < y + 85 + 20;

        if (but1 || but2 || but3) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            int id = but1 ? 0 : but2 ? 1 : 2;
            MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, id);
        } else if (tool1 || tool2 || tool3 || tool4 || tool5) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            int id = 3;
            id += tool1 ? 0 : tool2 ? 1 : tool3 ? 2 : tool4 ? 3 : 4;
            MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, id);
            this.handler.setPage(id - 3);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
