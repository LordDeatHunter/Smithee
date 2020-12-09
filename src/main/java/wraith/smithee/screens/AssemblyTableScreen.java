package wraith.smithee.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import wraith.smithee.Smithee;
import wraith.smithee.items.tools.BaseSmitheeTool;
import wraith.smithee.utils.Utils;
import net.minecraft.item.ItemStack;

import java.util.Objects;

public class AssemblyTableScreen extends HandledScreen<ScreenHandler> {

    private static final Identifier TEXTURE = Utils.ID("textures/gui/assembly_table.png");
    public final AssemblyTableScreenHandler handler;
    private boolean ignoreTypedCharacter;
    private TextFieldWidget nameInputField;
    private boolean canType = false;
    private boolean hadTool = false;

    public AssemblyTableScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.handler = (AssemblyTableScreenHandler) handler;
        this.backgroundWidth = 176;
        this.backgroundHeight = 184;
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
    public void tick() {
        super.tick();
        ItemStack tool = handler.slots.get(3).getStack();
        if (tool.getItem() instanceof BaseSmitheeTool) {
            if (tool.hasTag() && tool.getTag().contains("SmitheeProperties") && tool.getTag().getCompound("SmitheeProperties").contains("CustomName")) {
                this.nameInputField.setText(tool.getTag().getCompound("SmitheeProperties").getString("CustomName"));
            } else {
                if (!hadTool) {
                    hadTool = true;
                    this.nameInputField.setText(tool.getName().asString());
                }
            }
            canType = true;
        } else {
            if (hadTool) {
                hadTool = false;
                this.nameInputField.setText("");
            }
            this.nameInputField.setSelected(false);
            canType = false;
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(TEXTURE);
        int x = this.x;
        int y = this.y;
        this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        //Hande
        if (this.handler.slots.get(0).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 43, y + 57, 176, 0, 16, 16);
        }
        //Binding
        if (this.handler.slots.get(1).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 55, y + 37, 176 + 16, 0, 16, 16);
        }
        //Head
        if (this.handler.slots.get(2).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 67, y + 17, 176 + 32, 0, 16, 16);
        }
        //Output
        if (this.handler.slots.get(3).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 109, y + 34, 176, 16, 24, 24);
        }
        //Embossment
        if (this.handler.slots.get(3).getStack().isEmpty()) {
            this.drawTexture(matrices, x + 113, y + 12, 176 + 48, 0, 24, 24);
        }
        this.nameInputField.render(matrices, mouseX, mouseY, delta);

    }
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        this.ignoreTypedCharacter = false;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
    @Override
    protected void onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType) {
        super.onMouseClick(slot, invSlot, clickData, actionType);
        if (canType) {
            this.nameInputField.setCursorToEnd();
            this.nameInputField.setSelectionEnd(0);
        }
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
        if (this.ignoreTypedCharacter || !canType) {
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
        if (canType) {
            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
            CompoundTag tag = new CompoundTag();
            tag.putString("tool_name", this.nameInputField.getText());
            data.writeCompoundTag(tag);
            ClientSidePacketRegistry.INSTANCE.sendToServer(new Identifier(Smithee.MOD_ID, "rename_tool_assembly"), data);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.nameInputField = new TextFieldWidget(this.textRenderer, this.x + 31, this.y + 80, 115, 9, new TranslatableText("smithee.assembly.rename"));
        this.nameInputField.setMaxLength(32);
        this.nameInputField.setHasBorder(false);
        this.nameInputField.setEditableColor(0xffffff);
        this.nameInputField.setVisible(true);
        this.nameInputField.setSelected(false);
        this.nameInputField.setFocusUnlocked(true);
        this.nameInputField.setText("");
        this.children.add(this.nameInputField);
    }


}
