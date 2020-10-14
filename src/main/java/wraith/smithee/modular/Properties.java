package wraith.smithee.modular;

import java.util.HashMap;

public class Properties {
      /*
            Properties are responsible for dictating a tool's internal capabilities
            example: durability, mining speed, mining level, attack damage, etc.
            These can be modified (changed or intensified depending on the priority of the new material that's being used).
            Modifications are only there to change base stats of a tool, not to add more effects (separate system)
            Modifications can be as simple as polishing a blade (which would add extra damage) or reinforcing it with a stronger material (increasing its durability)
            Some Effects have synergies with Properties
     */

    public static HashMap<String, String> PROPERTIES = new HashMap<String, String>() {{
        put("iron", "staorec");
    }};

}
