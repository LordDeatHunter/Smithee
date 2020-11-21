package wraith.smithee.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.registry.Registry;
import wraith.smithee.Utils;
import wraith.smithee.blocks.ToolStationBlock;

import java.util.HashMap;

public class BlockRegistry {

    public static HashMap<String, Block> BLOCKS = new HashMap<String, Block>() {{
        put("oak_tool_station", new ToolStationBlock(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("dark_oak_tool_station", new ToolStationBlock(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("spruce_tool_station", new ToolStationBlock(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("birch_tool_station", new ToolStationBlock(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
        put("jungle_tool_station", new ToolStationBlock(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).strength(2f, 2f)));
    }};

    public static void registerBlocks() {
        for(String id : BLOCKS.keySet()) {
            Registry.register(Registry.BLOCK, Utils.ID(id), BLOCKS.get(id));
        }
    }

}
