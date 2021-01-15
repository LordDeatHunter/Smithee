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

public class DisassemblyTableScreen extends HandledScreen<ScreenHandler> {

    private static final Identifier TEXTURE = Utils.ID("textures/gui/disassembly_table.png");
    public final DisassemblyTableScreenHandler handler;
    private boolean mouseClicked = false;

    public DisassemblyTableScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.handler = (DisassemblyTableScreenHandler) handler;
        this.backgroundWidth = 176;
        this.backgroundHeight = 176;
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

        //Handle
        if (this.handler.slots.get(3).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 109, y + 63, 176, 0, 16, 16);
        }
        //Binding
        if (this.handler.slots.get(2).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 51, y + 63, 176 + 16, 0, 16, 16);
        }
        //Head
        if (this.handler.slots.get(1).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 80, y + 19, 176 + 32, 0, 16, 16);
        }

        int s = 196;
        if (mouseX >= x + 16 && mouseY >= y + 61 && mouseX < x + 16 + 20 && mouseY < y + 61 + 20) {
            s += mouseClicked ? -20 : 20;
        }
        this.drawTexture(matrices, x + 16, y + 61, s, 16, 20, 20);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = true;
        if (mouseX >= x + 16 && mouseY >= y + 61 && mouseX < x + 16 + 20 && mouseY < y + 61 + 20) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.client.interactionManager.clickButton(this.handler.syncId, 0);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
