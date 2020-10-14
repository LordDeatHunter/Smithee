package wraith.smithee.registry;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import wraith.smithee.Utils;
import wraith.smithee.blocks.ToolStationBlockEntity;

import java.util.HashMap;

public class BlockEntityRegistry {

    public static HashMap<String, BlockEntityType<? extends BlockEntity>> BLOCK_ENTITIES = new HashMap<String, BlockEntityType<? extends BlockEntity>>() {{
        put("tool_station", BlockEntityType.Builder.create(ToolStationBlockEntity::new, BlockRegistry.BLOCKS.get("tool_station")).build(null));
    }};

    public static void registerBlockEntities() {
        for (String id : BLOCK_ENTITIES.keySet()) {
            Registry.register(Registry.BLOCK_ENTITY_TYPE, Utils.ID(id), BLOCK_ENTITIES.get(id));
        }
    }

}
