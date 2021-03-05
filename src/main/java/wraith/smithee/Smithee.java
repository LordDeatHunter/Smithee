package wraith.smithee;

import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wraith.smithee.recipes.RecipesGenerator;
import wraith.smithee.registry.*;
import wraith.smithee.screens.AssemblyTableScreenHandler;
import wraith.smithee.support.HarvestScythes;
import wraith.smithee.support.MCDungeons;
import wraith.smithee.utils.JsonParser;
import wraith.smithee.utils.Utils;

public class Smithee implements ModInitializer {

    public static final String MOD_ID = "smithee";
    public static final Logger LOGGER = LogManager.getLogger();
    public static boolean DISABLE_TOOLS = false;

    @Override
    public void onInitialize() {
        LOGGER.info("[Smithee] is loading.");

        if (FabricLoader.getInstance().isModLoaded("harvest_scythes")) {
            HarvestScythes.addItemRegistryValues();
        }
        if (FabricLoader.getInstance().isModLoaded("mcdw")) {
            MCDungeons.addItemRegistryValues();
        }

        JsonObject json = Config.loadConfig();

        if (json.has("disable_vanilla_tools") && json.get("disable_vanilla_tools").getAsBoolean()) {
            DISABLE_TOOLS = true;
            ItemRegistry.setDisabledItems();
        }

        Config.createMaterials(json.has("replace_material_list_when_regenerating") && json.get("replace_material_list_when_regenerating").getAsBoolean());

        Utils.saveFilesFromJar("configs/templates", "templates", true);
        if (!json.has("regenerate_deleted_palettes") || json.get("regenerate_deleted_palettes").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/palettes", "palettes", json.has("replace_old_palettes_when_regenerating") && json.get("replace_old_palettes_when_regenerating").getAsBoolean());
        }
        if (!json.has("regenerate_deleted_texture_files") || json.get("regenerate_deleted_texture_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/textures", "textures", json.has("replace_old_texture_files_when_regenerating") && json.get("replace_old_texture_files_when_regenerating").getAsBoolean());
        }
        if (!json.has("regenerate_deleted_shard_files") || json.get("regenerate_deleted_shard_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/shards", "shards", json.has("replace_old_shard_files_when_regenerating") && json.get("replace_old_shard_files_when_regenerating").getAsBoolean());
        }
        if (!json.has("regenerate_deleted_model_files") || json.get("regenerate_deleted_model_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/models", "models", json.has("replace_old_model_files_when_regenerating") && json.get("replace_old_model_files_when_regenerating").getAsBoolean());
        }
        if (!json.has("regenerate_deleted_stat_files") || json.get("regenerate_deleted_stat_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/stats", "stats", json.has("replace_old_stat_files_when_regenerating") && json.get("replace_old_stat_files_when_regenerating").getAsBoolean());
        }
        if (!json.has("regenerate_deleted_chisel_files") || json.get("regenerate_deleted_chisel_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/chisels", "chisels", json.has("replace_old_chisel_files_when_regenerating") && json.get("replace_old_chisel_files_when_regenerating").getAsBoolean());
        }
        if (!json.has("regenerate_deleted_modifier_files") || json.get("regenerate_deleted_modifier_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/modifiers", "modifiers", json.has("replace_old_modifier_files_when_regenerating") && json.get("replace_old_modifier_files_when_regenerating").getAsBoolean());
        }
        if (!json.has("regenerate_deleted_recipe_files") || json.get("regenerate_deleted_recipe_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/recipes", "recipes", json.has("replace_old_recipe_files_when_regenerating") && json.get("replace_old_recipe_files_when_regenerating").getAsBoolean());
        }
        if (!json.has("regenerate_deleted_smithing_files") || json.get("regenerate_deleted_smithing_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/smithing", "smithing", json.has("replace_old_smithing_files_when_regenerating") && json.get("replace_old_smithing_files_when_regenerating").getAsBoolean());
        }
        if (!json.has("regenerate_deleted_combination_files") || json.get("regenerate_deleted_combination_files").getAsBoolean()) {
            Utils.saveFilesFromJar("configs/combinations", "combinations", json.has("replace_old_combination_files_when_regenerating") && json.get("replace_old_combination_files_when_regenerating").getAsBoolean());
        }

        ItemRegistry.generateModels();
        ItemRegistry.generateShards();

        ItemRegistry.addMaterials();

        ItemRegistry.generateChiselingStats();
        ItemRegistry.generateProperties();

        ItemRegistry.generateModifiers();

        BlockRegistry.registerBYG();
        BlockRegistry.registerBlocks();
        BlockEntityRegistry.createAssemblyTables();
        ItemRegistry.addItems();
        ItemRegistry.registerItems();
        BlockEntityRegistry.addBlockEntities();
        BlockEntityRegistry.registerBlockEntities();
        ScreenHandlerRegistry.registerScreenHandlers();
        StatusEffectRegistry.registerStatusEffects();

        JsonParser.parseCombinations();

        RecipesGenerator.generateRecipes();
        registerPacketHandlers();
        registerEvents();
        LOGGER.info("[Smithee] has successfully loaded.");
    }

    private void registerPacketHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(Utils.ID("assembly_table.rename_tool"), (server, player, networkHandler, packet, sender) -> {
            String name = packet.readCompoundTag().getString("CustomName");
            if (player.currentScreenHandler instanceof AssemblyTableScreenHandler) {
                ItemStack stack = player.currentScreenHandler.slots.get(3).getStack();
                stack.getOrCreateSubTag("SmitheeProperties").putString("CustomName", name);
            }
        });
    }

    private void registerEvents() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            ItemRegistry.generateRecipes();
        });
    }

}
