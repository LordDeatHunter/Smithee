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
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

public class ChiselingTableScreen extends HandledScreen<ScreenHandler> {

    private static final Identifier TEXTURE = Utils.ID("textures/gui/chiseling_table.png");
    public final ChiselingTableScreenHandler handler;
    private boolean mouseClicked = false;
    private int clickPos = -1;

    public ChiselingTableScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.handler = (ChiselingTableScreenHandler) handler;
        this.backgroundWidth = 176 + 40;
        this.backgroundHeight = 171;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        this.playerInventoryTitleX += 20;
        this.titleX += 40;
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
        int x = this.x + 20;
        this.drawTexture(matrices, x, y, 20, 0, this.backgroundWidth - 40, this.backgroundHeight);

        //Chisel
        if (this.handler.slots.get(0).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 61, y + 41, 196, 0, 16, 16);
        }
        //Material
        boolean hasMaterial = true;
        if (this.handler.slots.get(1).getStack().isEmpty()) {
            hasMaterial = false;
            this.drawTexture(matrices, x + 95, y + 41, 196 + 16, 0, 16, 16);
        }
        //Output
        if (this.handler.slots.get(2).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 78, y + 66, 196 + 32, 0, 16, 16);
        }

        //Craft
        drawButton(matrices, x, y, 76, 16, 216, 16, mouseX, mouseY, 0, 0);
        //Tools
        drawButton(matrices, x, y, -20, 2, 216, 36, mouseX, mouseY, 0, 1);
        drawButton(matrices, x, y, -20, 22, 216, 56, mouseX, mouseY, 1, 1);
        drawButton(matrices, x, y, -20, 42, 216, 76, mouseX, mouseY, 2, 1);
        drawButton(matrices, x, y, -20, 62, 216, 96, mouseX, mouseY, 3, 1);
        drawButton(matrices, x, y, -20, 82, 216, 116, mouseX, mouseY, 4, 1);
        drawButton(matrices, x, y, -20, 102, 216, 216, mouseX, mouseY, 5, 1);
        //Parts
        if (this.handler.getToolPage() != 5) {
            drawButton(matrices, x, y, backgroundWidth - 40, 2, 216, 136, mouseX, mouseY, 0, 2);
            drawButton(matrices, x, y, backgroundWidth - 40, 22, 216, 156, mouseX, mouseY, 1, 2);
            drawButton(matrices, x, y, backgroundWidth - 40, 42, 216, 196, mouseX, mouseY, 2, 2);
        }

        if (hasMaterial) {
            String part = this.handler.partPageToString();
            String key = this.handler.toolPageToString() + "_" + part;
            if (!"head".equals(part)) {
                key = part;
            }
            if (ItemRegistry.TOOL_PART_RECIPES.containsKey(this.handler.slots.get(1).getStack().getItem()) && ItemRegistry.TOOL_PART_RECIPES.get(this.handler.slots.get(1).getStack().getItem()).containsKey(key)) {
                int amount = ItemRegistry.TOOL_PART_RECIPES.get(this.handler.slots.get(1).getStack().getItem()).get(key).requiredAmount;
                this.textRenderer.draw(matrices, "Cost: " + amount, x + 100, y + 70, 0x000000);
            }
        }
    }

    public void drawButton(MatrixStack matrices, int x, int y, int xm, int ym, int u, int v, int mouseX, int mouseY, int index, int buttonType) {
        if ((buttonType == 1 && handler.getToolPage() == index) || (buttonType == 2 && handler.getPartPage() == index)) {
            u -= 20;
        }
        else if (mouseX >= x + xm && mouseY >= y + ym && mouseX < x + xm + 20 && mouseY < y + ym + 20) {
            int testIndex;
            if (buttonType == 0) {
                testIndex = 8;
            } else if (buttonType == 2) {
                testIndex = index + 5;
            } else {
                testIndex = index;
            }
            if (this.clickPos == -1 || this.clickPos == testIndex) {
                u += mouseClicked ? -20 : 20;
            }
        }
        boolean renderSwordGuard = index == 1 && buttonType == 2 && "sword".equals(handler.toolPageToString());
        this.drawTexture(matrices, x + xm, y + ym, u, v + (renderSwordGuard?20:0), 20, 20);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = true;

        int x = this.x + 20;

        boolean tool1 = mouseX >= x - 20 && mouseY >= y + 2   && mouseX < x && mouseY < y + 22       && this.handler.getToolPage() != 0;
        boolean tool2 = mouseX >= x - 20 && mouseY >= y + 22  && mouseX < x && mouseY < y + 22  + 20 && this.handler.getToolPage() != 1;
        boolean tool3 = mouseX >= x - 20 && mouseY >= y + 42  && mouseX < x && mouseY < y + 42  + 20 && this.handler.getToolPage() != 2;
        boolean tool4 = mouseX >= x - 20 && mouseY >= y + 62  && mouseX < x && mouseY < y + 62  + 20 && this.handler.getToolPage() != 3;
        boolean tool5 = mouseX >= x - 20 && mouseY >= y + 82  && mouseX < x && mouseY < y + 82  + 20 && this.handler.getToolPage() != 4;
        boolean tool6 = mouseX >= x - 20 && mouseY >= y + 102 && mouseX < x && mouseY < y + 102 + 20 && this.handler.getToolPage() != 5;

        this.clickPos = tool1 ? 0 : tool2 ? 1 : tool3 ? 2 : tool4 ? 3 : tool5 ? 4 : tool6 ? 5 : -1;

        boolean part1 = mouseX >= x + backgroundWidth - 40 && mouseY >= y + 2  && mouseX < x + backgroundWidth - 20 && mouseY < y + 22 && this.handler.getPartPage() != 0 && this.handler.getToolPage() != 5;
        boolean part2 = mouseX >= x + backgroundWidth - 40 && mouseY >= y + 22 && mouseX < x + backgroundWidth - 20 && mouseY < y + 42 && this.handler.getPartPage() != 1 && this.handler.getToolPage() != 5;
        boolean part3 = mouseX >= x + backgroundWidth - 40 && mouseY >= y + 42 && mouseX < x + backgroundWidth - 20 && mouseY < y + 62 && this.handler.getPartPage() != 2 && this.handler.getToolPage() != 5;

        if (this.clickPos == -1) {
            this.clickPos = part1 ? 5 : part2 ? 6 : part3 ? 7 : -1;
        }

        boolean chisel = mouseX >= x + 76 && mouseY >= y + 16 && mouseX < x + 76 + 20 && mouseY < y + 16 + 20;

        if (this.clickPos == -1 && chisel) {
            this.clickPos = 8;
        }

        if (tool1 || tool2 || tool3 || tool4 || tool5 || tool6) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            int id = tool1 ? 0 : tool2 ? 1 : tool3 ? 2 : tool4 ? 3 : tool5 ? 4 : 5;
            MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, id);
            this.handler.setToolPage(id);
        } else if (part1 || part2 || part3) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            int id = 6 + (part1 ? 0 : part2 ? 1 : 2);
            MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, id);
            this.handler.setPartPage(id - 6);
        } else if (chisel) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, 9);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.clickPos = -1;
        this.mouseClicked = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
