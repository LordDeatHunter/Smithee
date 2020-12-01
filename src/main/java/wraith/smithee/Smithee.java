package wraith.smithee;

import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wraith.smithee.recipes.RecipesGenerator;
import wraith.smithee.registry.BlockEntityRegistry;
import wraith.smithee.registry.BlockRegistry;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.registry.ScreenHandlerRegistry;
import wraith.smithee.screens.AssemblyTableScreenHandler;
import wraith.smithee.utils.JsonParser;
import wraith.smithee.utils.Utils;

public class Smithee implements ModInitializer {

    public static final String MOD_ID = "smithee";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.info("[Smithee] is loading.");

        JsonObject json = Config.loadConfig();

        if (!json.has("regenerate_material_list") || json.get("regenerate_material_list").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/items", "items", json.has("replace_material_list_when_regenerating") && json.get("replace_material_list_when_regenerating").getAsBoolean());
        }

        ItemRegistry.addMaterials();

        if (!json.has("regenerate_deleted_stat_files") || json.get("regenerate_deleted_stat_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/stats", "stats", json.has("replace_old_stat_files_when_regenerating") && json.get("replace_old_stat_files_when_regenerating").getAsBoolean());
        }
        ItemRegistry.generateProperties();

        ItemRegistry.addItems();
        ItemRegistry.registerItems();
        BlockRegistry.registerBlocks();
        BlockEntityRegistry.addBlockEntities();
        BlockEntityRegistry.registerBlockEntities();
        ScreenHandlerRegistry.registerScreenHandlers();

        if (!json.has("regenerate_deleted_recipe_files") || json.get("regenerate_deleted_recipe_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/recipes", "recipes", json.has("replace_old_recipe_files_when_regenerating") && json.get("replace_old_recipe_files_when_regenerating").getAsBoolean());
        }
        if (!json.has("regenerate_deleted_smithing_files") || json.get("regenerate_deleted_smithing_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/smithing", "smithing", json.has("replace_old_smithing_files_when_regenerating") && json.get("replace_old_smithing_files_when_regenerating").getAsBoolean());
        }
        if (!json.has("regenerate_deleted_combination_files") || json.get("regenerate_deleted_combination_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/combinations", "combinations", json.has("replace_old_combination_files_when_regenerating") && json.get("replace_old_combination_files_when_regenerating").getAsBoolean());
        }
        JsonParser.parseCombinations();

        ItemRegistry.generateRecipes();
        RecipesGenerator.generateRecipes();
        registerPacketHandlers();
        LOGGER.info("[Smithee] has successfully been loaded.");
    }

    private void registerPacketHandlers() {
        ServerSidePacketRegistry.INSTANCE.register(new Identifier(MOD_ID, "rename_tool_assembly"), (packetContext, attachedData) -> {
            CompoundTag tag = attachedData.readCompoundTag();
            if (packetContext.getPlayer().currentScreenHandler instanceof AssemblyTableScreenHandler && !packetContext.getPlayer().currentScreenHandler.slots.get(3).getStack().isEmpty()) {
                packetContext.getPlayer().currentScreenHandler.slots.get(3).getStack().getSubTag("SmitheeProperties").putString("CustomName", tag.getString("tool_name"));
            }
        });
    }

}
