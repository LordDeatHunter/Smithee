package wraith.smithee.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.registry.Registry;
import wraith.smithee.blocks.AssemblyTable;
import wraith.smithee.blocks.ChiselingTable;
import wraith.smithee.blocks.DisassemblyTable;
import wraith.smithee.utils.Utils;

import java.util.HashMap;

public class BlockRegistry {

    public static HashMap<String, Block> BLOCKS = new HashMap<String, Block>() {{
        put("oak_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("dark_oak_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("spruce_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("birch_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("jungle_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("acacia_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));

        put("stone_disassembly_table", new DisassemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("cobblestone_disassembly_table", new DisassemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("mossy_cobblestone_disassembly_table", new DisassemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("diorite_disassembly_table", new DisassemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("andesite_disassembly_table", new DisassemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("granite_disassembly_table", new DisassemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("netherrack_disassembly_table", new DisassemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));

        put("oak_chiseling_table", new ChiselingTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("dark_oak_chiseling_table", new ChiselingTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("spruce_chiseling_table", new ChiselingTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("birch_chiseling_table", new ChiselingTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("jungle_chiseling_table", new ChiselingTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("acacia_chiseling_table", new ChiselingTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
    }};

    public static void registerBYG() {
        BLOCKS.put("aspen_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("baobab_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("blue_enchanted_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("bulbis_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("cherry_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("cika_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("cypress_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("ebony_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("embur_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("ether_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("fir_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("glacial_oak_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("green_enchanted_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("holly_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("jacaranda_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("lament_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("mahogany_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("mangrove_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("maple_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("nightshade_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("palm_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("pine_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("rainbow_eucalyptus_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("redwood_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("skyris_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("sythian_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("willow_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("witch_hazel_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        BLOCKS.put("zelkova_assembly_table", new AssemblyTable(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
    }

    public static void registerBlocks() {
        for (String id : BLOCKS.keySet()) {
            Registry.register(Registry.BLOCK, Utils.ID(id), BLOCKS.get(id));
        }
    }

}
