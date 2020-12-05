package wraith.smithee.properties;

import java.util.HashSet;

public class ToolPartRecipe {

    public int requiredAmount;
    public String outputMaterial;
    public int chiselingLevel;

    public ToolPartRecipe(String outputMaterial, int requiredAmount, int chiselingLevel) {
        this.outputMaterial = outputMaterial;
        this.requiredAmount = requiredAmount;
        this.chiselingLevel = chiselingLevel;
    }

    public static final HashSet<HashSet<String>> TOOL_ASSEMBLY_RECIPES = new HashSet<HashSet<String>>(){{
        add(new HashSet<String>(){{
            add("pickaxe_head");
            add("binding");
            add("handle");
        }});
        add(new HashSet<String>(){{
            add("axe_head");
            add("binding");
            add("handle");
        }});
        add(new HashSet<String>(){{
            add("shovel_head");
            add("binding");
            add("handle");
        }});
        add(new HashSet<String>(){{
            add("hoe_head");
            add("binding");
            add("handle");
        }});
        add(new HashSet<String>(){{
            add("sword_head");
            add("sword_guard");
            add("handle");
        }});
    }};

}
