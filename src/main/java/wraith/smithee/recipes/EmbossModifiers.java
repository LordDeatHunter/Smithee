package wraith.smithee.recipes;

import java.util.HashSet;

public class EmbossModifiers {

    public String gives;
    public HashSet<String> givesTo;

    public EmbossModifiers(String gives, HashSet<String> givesTo) {
        this.gives = gives;
        this.givesTo = givesTo;
    }
}
