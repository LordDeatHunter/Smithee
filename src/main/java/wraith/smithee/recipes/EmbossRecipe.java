package wraith.smithee.recipes;

import java.util.ArrayList;
import java.util.HashSet;

public class EmbossRecipe {

    public boolean stackable;
    public String type;
    public ArrayList<EmbossModifiers> modifiers;
    public HashSet<String> incompatible;

    public EmbossRecipe(String type, boolean stackable, ArrayList<EmbossModifiers> modifiers, HashSet<String> incompatible) {
        this.type = type;
        this.stackable = stackable;
        this.modifiers = modifiers;
        this.incompatible = incompatible;
    }
}
