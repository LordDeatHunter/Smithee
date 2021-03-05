package wraith.smithee.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class ChiselingTableScreen extends HandledScreen<ScreenHandler> {

    private static final Identifier TEXTURE = Utils.ID("textures/gui/chiseling_table.png");
    public final ChiselingTableScreenHandler handler;
    private final int toolRows = 3;
    private final int toolCols = 4;
    private final int partAmount = 4;
    protected float toolScrollAmount;
    protected float partScrollAmount;
    protected int toolScrollOffset;
    protected int partScrollOffset;
    private int clickPos = -1;

    public ChiselingTableScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.handler = (ChiselingTableScreenHandler) handler;
        this.backgroundWidth = 176;
        this.backgroundHeight = 219;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        this.titleX += 20;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.renderBackground(matrices);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(TEXTURE);
        this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        int k = (int) (41.0F * this.toolScrollAmount);
        this.drawTexture(matrices, x + 123, y + 18 + k, 235 + (this.shouldToolsScroll() ? 0 : 8), 0, 8, 11);
        k = (int) (41.0F * this.partScrollAmount);
        this.drawTexture(matrices, x + 48 + k, y + 100, 224, (this.shouldPartsScroll() ? 0 : 8), 11, 8);

        this.renderButton(matrices, mouseX, mouseY, this.x + 16, this.y + 83, 20, 20, 3);

        if (this.handler.slots.get(0).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 18, y + 31, this.backgroundWidth, 0, 16, 16);
        }
        boolean renderCost = true;
        if (this.handler.slots.get(1).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 18, y + 56, this.backgroundWidth + 16, 0, 16, 16);
            renderCost = false;
        }

        if (this.handler.slots.get(2).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 143, y + 52, this.backgroundWidth + 32, 0, 16, 16);
        }

        int xOffset = this.x + 48;
        this.renderToolsBackground(matrices, mouseX, mouseY, xOffset, this.y + 18, this.toolScrollOffset + toolRows * toolCols);
        this.renderPartsBackground(matrices, mouseX, mouseY, xOffset, this.y + 79, this.partScrollOffset + partAmount);

        this.renderToolIcons(xOffset, this.y + 18, this.toolScrollOffset + toolRows * toolCols);
        this.renderPartIcons(xOffset, this.y + 79, this.partScrollOffset + partAmount);

        if (renderCost) {
            String key = this.handler.getSelectedPartAsString();

            if (ItemRegistry.TOOL_PART_RECIPES.containsKey(this.handler.slots.get(1).getStack().getItem()) && ItemRegistry.TOOL_PART_RECIPES.get(this.handler.slots.get(1).getStack().getItem()).containsKey(key)) {
                int amount = ItemRegistry.TOOL_PART_RECIPES.get(this.handler.slots.get(1).getStack().getItem()).get(key).requiredAmount;
                int chiselLevel = ItemRegistry.TOOL_PART_RECIPES.get(this.handler.slots.get(1).getStack().getItem()).get(key).chiselingLevel;
                this.textRenderer.draw(matrices, "Cost: " + amount, x + 8, y + 114, 0x000000);
                this.textRenderer.draw(matrices, "Chisel Level: " + chiselLevel, x + 84, y + 114, 0x000000);
            }
        }
    }

    private void renderButton(MatrixStack matrices, int mouseX, int mouseY, int buttonX, int buttonY, int width, int height, int id) {
        int U = this.backgroundWidth;
        if (id == clickPos) {
            U += 20;
        } else if (mouseX >= buttonX && mouseX < buttonX + width && mouseY >= buttonY && mouseY < buttonY + height) {
            U += 40;
        }
        this.drawTexture(matrices, buttonX, buttonY, U, 16, width, height);
    }

    private boolean shouldToolsScroll() {
        return ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.keySet().size() > toolRows * toolCols;
    }

    private int getMaxToolsScroll() {
        return (ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.keySet().size() + 4 - 1) / 4 - 3;
    }

    private int getMaxPartsScroll() {
        ArrayList<String> list = new ArrayList<>(ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.keySet());
        Collections.sort(list);
        return ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.get(list.get(getSelectedTool())).size() - 4;
    }

    private boolean shouldPartsScroll() {
        ArrayList<String> list = new ArrayList<>(ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.keySet());
        Collections.sort(list);
        return ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.get(list.get(getSelectedPart())).size() > partAmount;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    protected void renderToolsBackground(MatrixStack matrixStack, int mouseX, int mouseY, int x, int y, int amountWithOffset) {
        ArrayList<String> list = new ArrayList<>(ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.keySet());
        Collections.sort(list);
        for (int n = this.toolScrollOffset; n < amountWithOffset && n < list.size(); ++n) {
            int amount = n - this.toolScrollOffset;
            int buttonXOffset = x + amount % toolCols * 18;
            int row = amount / toolCols;
            int buttonYOffset = y + row * 18;
            int U = this.backgroundWidth;
            if (n == getSelectedTool()) {
                U += 18;
            } else if (mouseX >= buttonXOffset && mouseY >= buttonYOffset && mouseX < buttonXOffset + 18 && mouseY < buttonYOffset + 18) {
                U += 36;
            }
            this.drawTexture(matrixStack, buttonXOffset, buttonYOffset, U, 36, 18, 18);
        }
    }

    private void renderToolIcons(int x, int y, int scrollOffset) {
        ArrayList<String> list = new ArrayList<>(ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.keySet());
        Collections.sort(list);
        for (int n = this.toolScrollOffset; n < scrollOffset && n < list.size(); ++n) {
            int amount = n - this.toolScrollOffset;
            int itemXOffset = x + amount % toolCols * 18;
            int row = amount / toolCols;
            int itemYOffset = y + row * 18 + 2;
            Item item = Registry.ITEM.get(Utils.ID("base_smithee_" + list.get(n)));
            this.client.getItemRenderer().renderInGuiWithOverrides(new ItemStack(item), itemXOffset + 1, itemYOffset - 1);
        }
    }

    protected void renderPartsBackground(MatrixStack matrixStack, int mouseX, int mouseY, int x, int y, int amountWithOffset) {
        ArrayList<String> list = new ArrayList<>(ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.keySet());
        Collections.sort(list);
        String tool = list.get(getSelectedTool());
        ArrayList<String> parts = new ArrayList<>(ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.get(tool));
        Collections.sort(parts);
        for (int n = this.partScrollOffset; n < amountWithOffset && n < parts.size(); ++n) {
            int amount = n - this.partScrollOffset;
            int buttonXOffset = x + amount % 4 * 18;
            int U = this.backgroundWidth;
            if (n == getSelectedPart()) {
                U += 18;
            } else if (mouseX >= buttonXOffset && mouseY >= y && mouseX < buttonXOffset + 18 && mouseY < y + 18) {
                U += 36;
            }
            this.drawTexture(matrixStack, buttonXOffset, y, U, 36, 18, 18);
        }
    }

    private void renderPartIcons(int x, int y, int scrollOffset) {
        ArrayList<String> list = new ArrayList<>(ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.keySet());
        Collections.sort(list);
        String tool = list.get(getSelectedTool());
        ArrayList<String> parts = new ArrayList<>(ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.get(tool));
        Collections.sort(parts);

        for (int n = this.partScrollOffset; n < scrollOffset && n < parts.size(); ++n) {
            int amount = n - this.partScrollOffset;
            int itemXOffset = x + amount % 4 * 18;
            Item item = Registry.ITEM.get(Utils.ID("base_smithee_" + parts.get(n)));
            this.client.getItemRenderer().renderInGuiWithOverrides(new ItemStack(item), itemXOffset + 1, y);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int i = this.x + 48;
        int j = this.y + 18;
        int k = this.toolScrollOffset + toolRows * toolCols;
        ArrayList<String> list = new ArrayList<>(ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.keySet());
        Collections.sort(list);

        for (int l = this.toolScrollOffset; l < k && l - this.toolScrollOffset < list.size(); ++l) {
            int m = l - this.toolScrollOffset;
            double buttonWidth = mouseX - (double) (i + m % 4 * 18);
            double buttonHeight = mouseY - (double) (j + m / 4 * 18);
            if (18 >= buttonWidth && 0 < buttonWidth && 18 >= buttonHeight && 0 < buttonHeight) {
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                handler.setSelectedTool(l);
                handler.setSelectedPart(0);
                this.client.interactionManager.clickButton((this.handler).syncId, l * 2);
                return true;
            }
        }
        i = this.x + 48;
        j = this.y + 79;
        k = this.partScrollOffset + 4;

        String tool = list.get(getSelectedTool());
        ArrayList<String> parts = new ArrayList<>(ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.get(tool));

        for (int l = this.partScrollOffset; l < k && l - this.partScrollOffset < parts.size(); ++l) {
            int m = l - this.partScrollOffset;
            double buttonWidth = mouseX - (double) (i + m % 4 * 18);
            double buttonHeight = mouseY - j;
            if (18 >= buttonWidth && 0 < buttonWidth && 18 >= buttonHeight && 0 < buttonHeight) {
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                handler.setSelectedPart(l);
                this.client.interactionManager.clickButton((this.handler).syncId, l * 2 + 1);
                return true;
            }
        }

        int buttonX = this.x + 123;
        int buttonY = this.y + 18;
        if (mouseX >= buttonX && mouseX < buttonX + 8 && mouseY >= buttonY && mouseY < buttonY + 54) {
            this.clickPos = 1;
            return true;
        }
        buttonX = this.x + 48;
        buttonY = this.y + 100;
        if (mouseX >= buttonX && mouseX < buttonX + 72 && mouseY >= buttonY && mouseY < buttonY + 8) {
            this.clickPos = 2;
        }

        buttonX = this.x + 16;
        buttonY = this.y + 83;
        if (mouseX >= buttonX && mouseX < buttonX + 20 && mouseY >= buttonY && mouseY < buttonY + 20) {
            this.clickPos = 3;
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.client.interactionManager.clickButton((this.handler).syncId, -1);
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.shouldToolsScroll()) {
            int i = this.getMaxToolsScroll();
            this.toolScrollAmount = (float) ((double) this.toolScrollAmount - amount / (double) i);
            this.toolScrollAmount = MathHelper.clamp(this.toolScrollAmount, 0.0F, 1.0F);
            this.toolScrollOffset = (int) ((double) (this.toolScrollAmount * (float) i) + 0.5D) * 4;
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.clickPos = -1;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.clickPos == 1 && this.shouldToolsScroll()) {
            int i = this.y + 18;
            int j = i + 54;
            this.toolScrollAmount = ((float) mouseY - (float) i - 5.5F) / ((float) (j - i) - 11.0F);
            this.toolScrollAmount = MathHelper.clamp(this.toolScrollAmount, 0.0F, 1.0F);
            this.toolScrollOffset = (int) ((double) (this.toolScrollAmount * (float) this.getMaxToolsScroll()) + 0.5D) * this.toolCols;
            return true;
        } else if (this.clickPos == 2 && this.shouldPartsScroll()) {
            int i = this.x + 48;
            int j = i + 80;
            this.partScrollAmount = ((float) mouseX - (float) i - 5.5F) / ((float) (j - i) - 11.0F);
            this.partScrollAmount = MathHelper.clamp(this.partScrollAmount, 0.0F, 1.0F);
            this.partScrollOffset = (int) ((double) (this.partScrollAmount * (float) this.getMaxPartsScroll()) + 0.5D) * this.partAmount;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    private int getSelectedTool() {
        return handler.getSelectedTool();
    }

    private int getSelectedPart() {
        return handler.getSelectedPart();
    }

}
