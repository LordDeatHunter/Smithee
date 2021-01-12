package wraith.smithee.support;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import wraith.smithee.ItemGroups;
import wraith.smithee.items.tools.BaseSmitheeHarvestScythe;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.registry.ItemRegistry;

import java.util.HashSet;

public class HarvestScythes {

    public static void addTag(CompoundTag tag) {
        CompoundTag subtag = tag.getCompound("SmitheeProperties");
        int mineLevel = subtag.getInt("MiningLevel");
        boolean circleHarvest = mineLevel % 2 == 0;
        int harvestRange = (int) (Math.floor(mineLevel/2.0f)+1);

        CompoundTag scytheTag = new CompoundTag();
        scytheTag.putInt("HarvestRadius", harvestRange);
        scytheTag.putBoolean("CircleHarvest", circleHarvest);
        tag.put("HarvestScytheProperties", scytheTag);
    }

    public static void addItemRegistryValues() {
        ItemRegistry.ITEMS.put("base_smithee_harvest_scythe", new BaseSmitheeHarvestScythe(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("harvest_scythe");
        ItemRegistry.BASE_RECIPE_VALUES.put("harvest_scythe_head", 27);
        HashSet<String> recipe = new HashSet<>();
        recipe.add("harvest_scythe_head");
        recipe.add("binding");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("harvest_scythe", recipe);
    }

}
