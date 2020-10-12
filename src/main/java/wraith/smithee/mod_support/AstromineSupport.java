package wraith.smithee.mod_support;

import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.RepairType;

public class AstromineSupport {

    public static void load() {
        ItemRegistry.MATERIALS.put("copper", new RepairType("tag", "c", "copper_ingots"));
        ItemRegistry.MATERIALS.put("tin", new RepairType("tag", "c", "tin_ingots"));
        ItemRegistry.MATERIALS.put("silver", new RepairType("tag", "c", "silver_ingots"));
        ItemRegistry.MATERIALS.put("lead", new RepairType("tag", "c", "lead_ingots"));
        ItemRegistry.MATERIALS.put("bronze", new RepairType("tag", "c", "bronze_ingots"));
        ItemRegistry.MATERIALS.put("steel", new RepairType("tag", "c", "steel_ingots"));
        ItemRegistry.MATERIALS.put("electrum", new RepairType("tag", "c", "electrum_ingots"));
        ItemRegistry.MATERIALS.put("rose_gold", new RepairType("tag", "c", "rose_gold_ingots"));
        ItemRegistry.MATERIALS.put("sterling_silver", new RepairType("tag", "c", "sterling_silver_ingots"));
        ItemRegistry.MATERIALS.put("fools_gold", new RepairType("tag", "c", "fools_gold_ingots"));
        ItemRegistry.MATERIALS.put("metite", new RepairType("tag", "c", "metite_ingots"));
        ItemRegistry.MATERIALS.put("asterite", new RepairType("tag", "c", "asterite_ingots"));
        ItemRegistry.MATERIALS.put("stellum", new RepairType("tag", "c", "stellum_ingots"));
        ItemRegistry.MATERIALS.put("galaxium", new RepairType("tag", "c", "galaxium_ingots"));
        ItemRegistry.MATERIALS.put("univite", new RepairType("tag", "c", "univite_ingots"));
        ItemRegistry.MATERIALS.put("lunum", new RepairType("tag", "c", "lunum_ingots"));
        ItemRegistry.MATERIALS.put("meteoric_steel", new RepairType("tag", "c", "meteoric_steel_ingots"));
    }

}
