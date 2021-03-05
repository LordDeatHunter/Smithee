package wraith.smithee.properties;

import java.util.HashMap;
import java.util.HashSet;

public class ToolPartRecipe {

    public static HashMap<String, HashSet<String>> TOOL_ASSEMBLY_RECIPES = new HashMap<String, HashSet<String>>() {{
        put("pickaxe", new HashSet<String>() {{
            add("pickaxe_head");
            add("binding");
            add("handle");
        }});
        put("axe", new HashSet<String>() {{
            add("axe_head");
            add("binding");
            add("handle");
        }});
        put("shovel", new HashSet<String>() {{
            add("shovel_head");
            add("binding");
            add("handle");
        }});
        put("hoe", new HashSet<String>() {{
            add("hoe_head");
            add("binding");
            add("handle");
        }});
        put("sword", new HashSet<String>() {{
            add("sword_head");
            add("sword_guard");
            add("handle");
        }});
        put("whetstone", new HashSet<String>() {{
            add("whetstone");
        }});
        put("shard", new HashSet<String>() {{
            add("shard");
        }});
        put("embossment", new HashSet<String>() {{
            add("embossment");
        }});
    }};
    public int requiredAmount;
    public String outputMaterial;
    public int chiselingLevel;

    public ToolPartRecipe(String outputMaterial, int requiredAmount, int chiselingLevel) {
        this.outputMaterial = outputMaterial;
        this.requiredAmount = requiredAmount;
        this.chiselingLevel = chiselingLevel;
    }

}
