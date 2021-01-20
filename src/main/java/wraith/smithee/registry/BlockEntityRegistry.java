package wraith.smithee.registry;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import wraith.smithee.blocks.ChiselingTableBlockEntity;
import wraith.smithee.utils.Utils;
import wraith.smithee.blocks.AassemblyTableBlockEntity;
import wraith.smithee.blocks.DisassemblyTableBlockEntity;

import java.util.HashMap;
import java.util.HashSet;

public class BlockEntityRegistry {

    public static HashMap<String, BlockEntityType<? extends BlockEntity>> BLOCK_ENTITIES = new HashMap<>();
    public static final HashMap<String, Block> assemblyTableMap = new HashMap<>();

    public static void createAssemblyTables() {
        assemblyTableMap.put("oak_assembly_table", BlockRegistry.BLOCKS.get("oak_assembly_table"));
        assemblyTableMap.put("dark_oak_assembly_table", BlockRegistry.BLOCKS.get("dark_oak_assembly_table"));
        assemblyTableMap.put("spruce_assembly_table", BlockRegistry.BLOCKS.get("spruce_assembly_table"));
        assemblyTableMap.put("birch_assembly_table", BlockRegistry.BLOCKS.get("birch_assembly_table"));
        assemblyTableMap.put("jungle_assembly_table", BlockRegistry.BLOCKS.get("jungle_assembly_table"));
        assemblyTableMap.put("acacia_assembly_table", BlockRegistry.BLOCKS.get("acacia_assembly_table"));
        if (FabricLoader.getInstance().isModLoaded("byg")) {
            assemblyTableMap.put("aspen_assembly_table", BlockRegistry.BLOCKS.get("aspen_assembly_table"));
            assemblyTableMap.put("baobab_assembly_table", BlockRegistry.BLOCKS.get("baobab_assembly_table"));
            assemblyTableMap.put("blue_enchanted_assembly_table", BlockRegistry.BLOCKS.get("blue_enchanted_assembly_table"));
            assemblyTableMap.put("bulbis_assembly_table", BlockRegistry.BLOCKS.get("bulbis_assembly_table"));
            assemblyTableMap.put("cherry_assembly_table", BlockRegistry.BLOCKS.get("cherry_assembly_table"));
            assemblyTableMap.put("cika_assembly_table", BlockRegistry.BLOCKS.get("cika_assembly_table"));
            assemblyTableMap.put("cypress_assembly_table", BlockRegistry.BLOCKS.get("cypress_assembly_table"));
            assemblyTableMap.put("ebony_assembly_table", BlockRegistry.BLOCKS.get("ebony_assembly_table"));
            assemblyTableMap.put("embur_assembly_table", BlockRegistry.BLOCKS.get("embur_assembly_table"));
            assemblyTableMap.put("ether_assembly_table", BlockRegistry.BLOCKS.get("ether_assembly_table"));
            assemblyTableMap.put("fir_assembly_table", BlockRegistry.BLOCKS.get("fir_assembly_table"));
            assemblyTableMap.put("glacial_oak_assembly_table", BlockRegistry.BLOCKS.get("glacial_oak_assembly_table"));
            assemblyTableMap.put("green_enchanted_assembly_table", BlockRegistry.BLOCKS.get("green_enchanted_assembly_table"));
            assemblyTableMap.put("holly_assembly_table", BlockRegistry.BLOCKS.get("holly_assembly_table"));
            assemblyTableMap.put("jacaranda_assembly_table", BlockRegistry.BLOCKS.get("jacaranda_assembly_table"));
            assemblyTableMap.put("lament_assembly_table", BlockRegistry.BLOCKS.get("lament_assembly_table"));
            assemblyTableMap.put("mahogany_assembly_table", BlockRegistry.BLOCKS.get("mahogany_assembly_table"));
            assemblyTableMap.put("mangrove_assembly_table", BlockRegistry.BLOCKS.get("mangrove_assembly_table"));
            assemblyTableMap.put("maple_assembly_table", BlockRegistry.BLOCKS.get("maple_assembly_table"));
            assemblyTableMap.put("nightshade_assembly_table", BlockRegistry.BLOCKS.get("nightshade_assembly_table"));
            assemblyTableMap.put("palm_assembly_table", BlockRegistry.BLOCKS.get("palm_assembly_table"));
            assemblyTableMap.put("pine_assembly_table", BlockRegistry.BLOCKS.get("pine_assembly_table"));
            assemblyTableMap.put("rainbow_eucalyptus_assembly_table", BlockRegistry.BLOCKS.get("rainbow_eucalyptus_assembly_table"));
            assemblyTableMap.put("redwood_assembly_table", BlockRegistry.BLOCKS.get("redwood_assembly_table"));
            assemblyTableMap.put("skyris_assembly_table", BlockRegistry.BLOCKS.get("skyris_assembly_table"));
            assemblyTableMap.put("sythian_assembly_table", BlockRegistry.BLOCKS.get("sythian_assembly_table"));
            assemblyTableMap.put("willow_assembly_table", BlockRegistry.BLOCKS.get("willow_assembly_table"));
            assemblyTableMap.put("witch_hazel_assembly_table", BlockRegistry.BLOCKS.get("witch_hazel_assembly_table"));
            assemblyTableMap.put("zelkova_assembly_table", BlockRegistry.BLOCKS.get("zelkova_assembly_table"));
        }
    }

    public static void addBlockEntities() {
        Block[] assemblyTables = new Block[assemblyTableMap.size()];
        int i = 0;
        for (Block table : assemblyTableMap.values()) {
            assemblyTables[i++] = table;
        }

        BLOCK_ENTITIES.put("assembly_table", BlockEntityType.Builder.create(AassemblyTableBlockEntity::new, assemblyTables).build(null));
        BLOCK_ENTITIES.put("disassembly_table", BlockEntityType.Builder.create(DisassemblyTableBlockEntity::new,
                BlockRegistry.BLOCKS.get("stone_disassembly_table"),
                BlockRegistry.BLOCKS.get("cobblestone_disassembly_table"),
                BlockRegistry.BLOCKS.get("mossy_cobblestone_disassembly_table"),
                BlockRegistry.BLOCKS.get("diorite_disassembly_table"),
                BlockRegistry.BLOCKS.get("andesite_disassembly_table"),
                BlockRegistry.BLOCKS.get("granite_disassembly_table")
        ).build(null));
        BLOCK_ENTITIES.put("chiseling_table", BlockEntityType.Builder.create(ChiselingTableBlockEntity::new,
                BlockRegistry.BLOCKS.get("oak_chiseling_table"),
                BlockRegistry.BLOCKS.get("dark_oak_chiseling_table"),
                BlockRegistry.BLOCKS.get("spruce_chiseling_table"),
                BlockRegistry.BLOCKS.get("birch_chiseling_table"),
                BlockRegistry.BLOCKS.get("jungle_chiseling_table"),
                BlockRegistry.BLOCKS.get("acacia_chiseling_table")
        ).build(null));
    }

    public static void registerBlockEntities() {
        for (String id : BLOCK_ENTITIES.keySet()) {
            Registry.register(Registry.BLOCK_ENTITY_TYPE, Utils.ID(id), BLOCK_ENTITIES.get(id));
        }
    }

}
