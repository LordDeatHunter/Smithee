package wraith.smithee.mod_support;

import com.github.chainmailstudios.astromine.foundations.registry.AstromineFoundationsToolMaterials;
import wraith.smithee.registry.ItemRegistry;

public class AstromineSupport {

    public static void load() {
        ItemRegistry.MATERIALS.put("copper", AstromineFoundationsToolMaterials.COPPER);
        ItemRegistry.MATERIALS.put("tin", AstromineFoundationsToolMaterials.TIN);
        ItemRegistry.MATERIALS.put("silver", AstromineFoundationsToolMaterials.SILVER);
        ItemRegistry.MATERIALS.put("lead", AstromineFoundationsToolMaterials.LEAD);
        ItemRegistry.MATERIALS.put("bronze", AstromineFoundationsToolMaterials.BRONZE);
        ItemRegistry.MATERIALS.put("steel", AstromineFoundationsToolMaterials.STEEL);
        ItemRegistry.MATERIALS.put("electrum", AstromineFoundationsToolMaterials.ELECTRUM);
        ItemRegistry.MATERIALS.put("rose_gold", AstromineFoundationsToolMaterials.ROSE_GOLD);
        ItemRegistry.MATERIALS.put("sterling_silver", AstromineFoundationsToolMaterials.STERLING_SILVER);
        ItemRegistry.MATERIALS.put("fools_gold", AstromineFoundationsToolMaterials.FOOLS_GOLD);
        ItemRegistry.MATERIALS.put("metite", AstromineFoundationsToolMaterials.METITE);
        ItemRegistry.MATERIALS.put("asterite", AstromineFoundationsToolMaterials.ASTERITE);
        ItemRegistry.MATERIALS.put("stellum", AstromineFoundationsToolMaterials.STELLUM);
        ItemRegistry.MATERIALS.put("galaxium", AstromineFoundationsToolMaterials.GALAXIUM);
        ItemRegistry.MATERIALS.put("univite", AstromineFoundationsToolMaterials.UNIVITE);
        ItemRegistry.MATERIALS.put("lunum", AstromineFoundationsToolMaterials.LUNUM);
        ItemRegistry.MATERIALS.put("meteoric_steel", AstromineFoundationsToolMaterials.METEORIC_STEEL);
    }

}
