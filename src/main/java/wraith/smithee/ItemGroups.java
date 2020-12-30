package wraith.smithee;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

public class ItemGroups {

    public static final ItemGroup SMITHEE_PARTS = FabricItemGroupBuilder.create(Utils.ID("parts")).icon(() -> new ItemStack(ItemRegistry.ITEMS.get("parts_creative_icon"))).build();
    public static final ItemGroup SMITHEE_BLOCKS = FabricItemGroupBuilder.create(Utils.ID("blocks")).icon(() -> new ItemStack(ItemRegistry.ITEMS.get("oak_assembly_table"))).build();
    public static final ItemGroup SMITHEE_ITEMS = FabricItemGroupBuilder.create(Utils.ID("items")).icon(() -> new ItemStack(ItemRegistry.ITEMS.get("items_creative_icon"))).build();

}
