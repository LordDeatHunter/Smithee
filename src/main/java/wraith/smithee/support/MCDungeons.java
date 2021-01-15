package wraith.smithee.support;

import net.minecraft.item.Item;
import wraith.smithee.ItemGroups;
import wraith.smithee.items.tools.*;
import wraith.smithee.properties.ToolPartRecipe;
import wraith.smithee.registry.ItemRegistry;

import java.util.HashSet;

public class MCDungeons {

    public static void addItemRegistryValues() {
        HashSet<String> recipe;
        ItemRegistry.ITEMS.put("base_smithee_broadsword", new BaseSmitheeBroadsword(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("broadsword");
        ItemRegistry.BASE_RECIPE_VALUES.put("broadsword_head", 108);
        ItemRegistry.BASE_RECIPE_VALUES.put("broadsword_guard", 36);
        ItemRegistry.BINDING_TYPES.add("broadsword_guard");
        recipe = new HashSet<>();
        recipe.add("broadsword_head");
        recipe.add("broadsword_guard");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("broadsword", recipe);

        ItemRegistry.ITEMS.put("base_smithee_spear", new BaseSmitheeSpear(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("spear");
        ItemRegistry.BASE_RECIPE_VALUES.put("spear_head", 54);
        recipe = new HashSet<>();
        recipe.add("spear_head");
        recipe.add("binding");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("spear", recipe);

        ItemRegistry.ITEMS.put("base_smithee_glaive", new BaseSmitheeGlaive(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("glaive");
        ItemRegistry.BASE_RECIPE_VALUES.put("glaive_head", 18);
        recipe = new HashSet<>();
        recipe.add("glaive_head");
        recipe.add("binding");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("glaive", recipe);

        ItemRegistry.ITEMS.put("base_smithee_cutlass", new BaseSmitheeCutlass(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("cutlass");
        ItemRegistry.BASE_RECIPE_VALUES.put("cutlass_head", 189);
        recipe = new HashSet<>();
        recipe.add("cutlass_head");
        recipe.add("sword_guard");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("cutlass", recipe);

        ItemRegistry.ITEMS.put("base_smithee_claymore", new BaseSmitheeClaymore(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("claymore");
        ItemRegistry.BASE_RECIPE_VALUES.put("claymore_head", 254);
        recipe = new HashSet<>();
        recipe.add("claymore_head");
        recipe.add("sword_guard");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("claymore", recipe);

        ItemRegistry.ITEMS.put("base_smithee_battle_scythe", new BaseSmitheeBattleScythe(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("battle_scythe");
        ItemRegistry.BASE_RECIPE_VALUES.put("battle_scythe_head", 144);
        recipe = new HashSet<>();
        recipe.add("battle_scythe_head");
        recipe.add("binding");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("battle_scythe", recipe);

        ItemRegistry.ITEMS.put("base_smithee_katana", new BaseSmitheeKatana(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("katana");
        ItemRegistry.BASE_RECIPE_VALUES.put("katana_head", 63);
        recipe = new HashSet<>();
        recipe.add("katana_head");
        recipe.add("sword_guard");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("katana", recipe);

        ItemRegistry.ITEMS.put("base_smithee_battle_staff", new BaseSmitheeBattleStaff(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("battle_staff");
        ItemRegistry.BASE_RECIPE_VALUES.put("battle_staff_head", 18);
        recipe = new HashSet<>();
        recipe.add("battle_staff_head");
        recipe.add("binding");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("battle_staff", recipe);

        ItemRegistry.ITEMS.put("base_smithee_dagger", new BaseSmitheeDagger(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("dagger");
        ItemRegistry.BASE_RECIPE_VALUES.put("dagger_head", 8);
        recipe = new HashSet<>();
        recipe.add("dagger_head");
        recipe.add("sword_guard");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("dagger", recipe);

        ItemRegistry.ITEMS.put("base_smithee_rapier", new BaseSmitheeRapier(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("rapier");
        ItemRegistry.BASE_RECIPE_VALUES.put("rapier_head", 12);
        recipe = new HashSet<>();
        recipe.add("rapier_head");
        recipe.add("sword_guard");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("rapier", recipe);

        ItemRegistry.ITEMS.put("base_smithee_sickle", new BaseSmitheeSickle(new Item.Settings().group(ItemGroups.SMITHEE_PARTS)));
        ItemRegistry.TOOL_TYPES.add("sickle");
        ItemRegistry.BASE_RECIPE_VALUES.put("sickle_head", 27);
        recipe = new HashSet<>();
        recipe.add("sickle_head");
        recipe.add("binding");
        recipe.add("handle");
        ToolPartRecipe.TOOL_ASSEMBLY_RECIPES.put("sickle", recipe);
    }
}
