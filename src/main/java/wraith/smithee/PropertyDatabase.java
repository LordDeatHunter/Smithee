package wraith.smithee;

import java.util.HashMap;

public class PropertyDatabase {

    public static HashMap<String, HashMap<String, CustomToolMaterial>> PROPERTY_DATABASE = new HashMap<String, HashMap<String, CustomToolMaterial>>(){{
        put("wooden", new HashMap<String, CustomToolMaterial>() {{
            put("head", new CustomToolMaterial(1, ));
        }});

    }};

}
