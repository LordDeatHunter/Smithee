package wraith.smithee;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.CompoundTag;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenRegistry;
import wraith.smithee.utils.Utils;

import java.util.HashMap;
import java.util.HashSet;

public class SmitheeClient implements ClientModInitializer {

    public static final HashSet<String> RENDERING_TOOL_PARTS = new HashSet<>();

    @Override
    public void onInitializeClient() {
        ScreenRegistry.registerScreens();
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            for (String material : ItemRegistry.MATERIALS) {
                for (String tool : ItemRegistry.TOOL_TYPES) {
                    String id;

                    id = material + "_" + tool + "_head";
                    RENDERING_TOOL_PARTS.add(id);
                    RENDERING_TOOL_PARTS.add(material + "_broken_" + tool + "_head");
                    out.accept(Utils.inventoryModelID(id));
                    out.accept(Utils.inventoryModelID(material + "_broken_" + tool + "_head"));

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
        ClientPlayNetworking.registerGlobalReceiver(Utils.ID("connect_packet"), (client, networkHandler, data, sender) -> {
            if (MinecraftClient.getInstance().isInSingleplayer()) {
                return;
            }
            CompoundTag tag = data.readCompoundTag();
            /*
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
                    ItemRegistry.REMAINS.get(id).put(new Identifier(item), remainsTag.getInt(item));
                }
            }
             */

            ItemRegistry.DISABLED_ITEMS.clear();
            Smithee.DISABLE_TOOLS = tag.getBoolean("disable_tools");
            if (Smithee.DISABLE_TOOLS){
                ItemRegistry.setDisabledItems();
            }
            CompoundTag subtag;
            String[] contents;
            int i;
            subtag = tag.getCompound("shards");
            contents = new String[subtag.getSize()];
            i = 0;
            for (String name : subtag.getKeys()) {
                contents[i] = subtag.getString(name);
                ++i;
            }
            ItemRegistry.generateShards(contents);

            ItemRegistry.addMaterials(tag.getString("materials"), tag.getString("emboss_materials"));

            i = 0;
            subtag = tag.getCompound("chisels");
            contents = new String[subtag.getSize()];
            for (String name : subtag.getKeys()) {
                contents[i] = subtag.getString(name);
                ++i;
            }
            ItemRegistry.generateChiselingStats(contents);

            subtag = tag.getCompound("stats");
            HashMap<String, String> contentsMap = new HashMap<>();
            for (String name : subtag.getKeys()) {
                contentsMap.put(name, subtag.getString(name));
            }
            ItemRegistry.generateProperties(contentsMap);

            subtag = tag.getCompound("modifiers");
            contents = new String[subtag.getSize()];
            i = 0;
            for (String name : subtag.getKeys()) {
                contents[i] = subtag.getString(name);
                ++i;
            }
            ItemRegistry.generateModifiers(contents);

            subtag = tag.getCompound("recipes");
            contents = new String[subtag.getSize()];
            i = 0;
            for (String name : subtag.getKeys()) {
                contents[i] = subtag.getString(name);
                ++i;
            }
            ItemRegistry.generateRecipes(contents);
            //ItemRegistry.addItems();
            //ItemRegistry.registerNewItems();
        });
    }

}
