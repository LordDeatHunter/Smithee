package wraith.smithee;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import wraith.smithee.registry.ItemRegistry;

public class ItemGroups {

    public static final ItemGroup SMITHEE_PARTS = FabricItemGroupBuilder.create(Utils.ID("parts")).icon(() -> new ItemStack(ItemRegistry.ITEM_REGISTRY.get("diamond_pickaxe_head"))).build();

}
