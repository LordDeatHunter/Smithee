package wraith.smithee;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenRegistry;
import wraith.smithee.utils.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SmitheeClient implements ClientModInitializer {

    public static final HashSet<String> RENDERING_TOOL_PARTS = new HashSet<>();

    @Override
    public void onInitializeClient() {
        ScreenRegistry.registerScreens();
        ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
            for (String material : ItemRegistry.MATERIALS) {
                for (String tool : ItemRegistry.TOOL_TYPES) {
                    String id;

                    id = material + "_" + tool + "_head";
                    RENDERING_TOOL_PARTS.add(id);
                    out.accept(Utils.inventoryModelID(id));

                    id = material + "_" + tool + "_binding";
                    RENDERING_TOOL_PARTS.add(id);
                    out.accept(Utils.inventoryModelID(id));

                    id = material + "_" + tool + "_handle";
                    RENDERING_TOOL_PARTS.add(id);
                    out.accept(Utils.inventoryModelID(id));
                }
            }
        });
        ModelLoadingRegistry.INSTANCE.registerVariantProvider(rm -> new ItemModelProvider());
        registerPacketHandlers();
    }

    private void registerPacketHandlers() {
        ClientSidePacketRegistry.INSTANCE.register(Utils.ID("connect_packet"), (packetContext, attachedData) -> {
            CompoundTag tag = attachedData.readCompoundTag();
            ItemRegistry.TOOL_PART_RECIPES.clear();
            Set<String> ids = tag.getCompound("recipes").getKeys();

            for (String id : ids) {
                Item item = Registry.ITEM.get(new Identifier(id));
                ItemRegistry.TOOL_PART_RECIPES.put(item, new HashMap<>());
                CompoundTag recipeTag = tag.getCompound("recipes").getCompound(id);
                Set<String> parts = recipeTag.getKeys();
                for (String part : parts) {
                    CompoundTag partTag = recipeTag.getCompound(part);
                    int reqAmount = partTag.getInt("requiredAmount");
                    String outMaterial = partTag.getString("outputMaterial");
                    int chiselLvl = partTag.getInt("chiselingLevel");
                    ItemRegistry.TOOL_PART_RECIPES.get(item).put(part, new ToolPartRecipe(outMaterial, reqAmount, chiselLvl));
                }
            }

            ItemRegistry.REMAINS.clear();
            ids = tag.getCompound("remains").getKeys();
            for (String id : ids) {
                ItemRegistry.REMAINS.put(id, new HashMap<>());
                CompoundTag remainsTag = tag.getCompound("remains").getCompound(id);
                Set<String> items = remainsTag.getKeys();
                for (String item : items) {
                    ItemRegistry.REMAINS.get(id).put(Registry.ITEM.get(new Identifier(item)), remainsTag.getInt(item));
                }
            }
        });
    }

}
