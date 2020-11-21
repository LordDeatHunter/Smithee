package wraith.smithee.items.tool_parts;

import net.minecraft.item.ToolMaterial;
import wraith.smithee.properties.Properties;

import java.util.HashMap;

public class Part {

    public ToolMaterial material;
    public String materialName;
    public String partType;
    public String toolType;

    public Part(String materialName, String partType, String toolType, ToolMaterial material) {
        this.material = material;
        this.materialName = materialName;
        this.partType = partType;
        this.toolType = toolType;
    }

    public static final HashMap<String, HashMap<String, Properties>> PROPERTIES = new HashMap<String, HashMap<String, Properties>>(){{
        /*
            WOOD(0, 59, 2.0F, 0.0F)
            STONE(1, 131, 4.0F, 1.0)
            IRON(2, 250, 6.0F, 2.0F)
            DIAMOND(3, 1561, 8.0F, 3.0F)
            GOLD(0, 32, 12.0F, 0.0F)
            NETHERITE(4, 2031, 9.0F, 4.0F)
         */
        put("head", new HashMap<String, Properties>(){{
            put("wooden", new Properties(0, 20, 0.3F, 0.5F));
            //92 ->
            put("stone", new Properties(1, 15, 1.26F, 0.8F));
            //211
            put("iron", new Properties(2, 36, 1.26F, 0.8F));
            //1522
            //-7
            //1992
        }});
        put("binding", new HashMap<String, Properties>(){{
            put("wooden", new Properties(0, 15, 0.45F, -0.20F));
            put("stone", new Properties(0, 55, 0.84F, -0.2F));
            put("iron", new Properties(0, 52, 1.26F, 0.8F));
        }});
        put("handle", new HashMap<String, Properties>(){{
            put("wooden", new Properties(0, 24, 1.25F, -0.30F));
            put("stone", new Properties(0, 22, 1.9F, 0.4F));
            put("iron", new Properties(0, 162, 1.26F, 0.8F));
        }});
    }};

}
