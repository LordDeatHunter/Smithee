package wraith.smithee.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import wraith.smithee.Smithee;
import wraith.smithee.utils.Utils;

import java.util.Objects;

public class AssemblyTableScreen extends HandledScreen<ScreenHandler> {

    private static final Identifier TEXTURE = Utils.ID("textures/gui/assembly_table.png");
    public final AssemblyTableScreenHandler handler;
    private boolean ignoreTypedCharacter;
    private TextFieldWidget nameInputField;
    private boolean mouseClicked = false;
    private int clickPos = -1;

    public AssemblyTableScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.handler = (AssemblyTableScreenHandler) handler;
        this.backgroundWidth = 176;
        this.backgroundHeight = 184;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        this.titleX += 20;
    }

    @Override
    public void tick() {
        if (this.nameInputField != null) {
            this.nameInputField.tick();
        }
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
        this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        //Hande
        if (this.handler.slots.get(0).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 35, y + 57, 176, 0, 16, 16);
        }
        //Binding
        if (this.handler.slots.get(1).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 47, y + 37, 176 + 16, 0, 16, 16);
        }
        //Head
        if (this.handler.slots.get(2).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 59, y + 17, 176 + 32, 0, 16, 16);
        }
        //Output
        if (this.handler.slots.get(3).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 109, y + 39, 176, 16, 24, 24);
        }
        //Embossment
        if (this.handler.slots.get(4).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 113, y + 11, 176 + 48, 0, 16, 16);
        }

        drawButton(matrices, x, y, 76, 42, 196, 40, mouseX, mouseY, 0, 20, 0);
        drawButton(matrices, x, y, 116, 28, 210, 16, mouseX, mouseY, 0, 10, 1);

        this.nameInputField.render(matrices, mouseX, mouseY, delta);

    }

    public void drawButton(MatrixStack matrices, int x, int y, int xm, int ym, int u, int v, int mouseX, int mouseY, int index, int size, int buttonType) {
        if (mouseX >= x + xm && mouseY >= y + ym && mouseX < x + xm + size && mouseY < y + ym + size && this.clickPos == -1) {
            u += mouseClicked ? -size : size;
        }
        this.drawTexture(matrices, x + xm, y + ym, u, v, size, size);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        this.ignoreTypedCharacter = false;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
    @Override
    protected void onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType) {
        super.onMouseClick(slot, invSlot, clickData, actionType);
        this.nameInputField.setCursorToEnd();
        this.nameInputField.setSelectionEnd(0);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.ignoreTypedCharacter = false;
        if (InputUtil.fromKeyCode(keyCode, scanCode).method_30103().isPresent() && this.handleHotbarKeyPressed(keyCode, scanCode)) {
            this.ignoreTypedCharacter = true;
            return true;
        } else {
            String string = this.nameInputField.getText();
            if (this.nameInputField.keyPressed(keyCode, scanCode, modifiers)) {
                if (!Objects.equals(string, this.nameInputField.getText())) {
                    this.rename();
                }

                return true;
            } else {
                return this.nameInputField.isFocused() && this.nameInputField.isVisible() && keyCode != 256 || super.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        if (this.ignoreTypedCharacter) {
            return false;
        } else {
            String string = this.nameInputField.getText();
            if (this.nameInputField.charTyped(chr, keyCode)) {
                if (!Objects.equals(string, this.nameInputField.getText())) {
                    this.rename();
                }
                return true;
            } else {
                return false;
            }
        }
    }
    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String string = this.nameInputField.getText();
        this.init(client, width, height);
        this.nameInputField.setText(string);
        if (!this.nameInputField.getText().isEmpty()) {
            this.rename();
        }
    }

    private void rename() {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        CompoundTag tag = new CompoundTag();

        ItemStack stack = handler.slots.get(3).getStack();
        if (!stack.isEmpty() && stack.getSubTag("SmitheeProperties") != null) {
            stack.getSubTag("SmitheeProperties").putString("CustomName", this.nameInputField.getText());
        }
        tag.putString("CustomName", this.nameInputField.getText());
        data.writeCompoundTag(tag);
        ClientPlayNetworking.send(Utils.ID("assembly_table.rename_tool"), data);
    }

    @Override
    protected void init() {
        super.init();
        this.nameInputField = new TextFieldWidget(this.textRenderer, this.x + 31, this.y + 80, 115, 9, new TranslatableText("smithee.assembly.rename"));
        this.nameInputField.setMaxLength(32);
        this.nameInputField.setHasBorder(false);
        this.nameInputField.setEditableColor(0xffffff);
        this.nameInputField.setVisible(true);
        this.nameInputField.setFocusUnlocked(true);
        this.nameInputField.setText("");
        this.children.add(this.nameInputField);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = true;

        boolean craft = mouseX >= x + 76 && mouseY >= y + 42 && mouseX < x + 76 + 20 && mouseY < y + 42 + 20;
        boolean improve = mouseX >= x + 116 && mouseY >= y + 28 && mouseX < x + 116 + 10 && mouseY < y + 28 + 10;

        if (this.clickPos == -1) {
            this.clickPos = craft ? 0 : improve ? 1 : -1;
        }

        if (craft) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, 0);
        } else if (improve) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            MinecraftClient.getInstance().interactionManager.clickButton(this.handler.syncId, 1);
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
