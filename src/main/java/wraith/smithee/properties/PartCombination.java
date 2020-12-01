package wraith.smithee.properties;

import java.util.HashMap;
import java.util.HashSet;

public class PartCombination {

    public static HashSet<PartCombination> COMBINATIONS = new HashSet<>();

    public HashMap<String, HashSet<String>> includes;
    public HashMap<String, HashSet<String>> excludes;
    public HashMap<String, String> stats;

    public PartCombination(HashMap<String, HashSet<String>> includes, HashMap<String, HashSet<String>> excludes, HashMap<String, String> stats) {
        this.includes = includes;
        this.excludes = excludes;
        this.stats = stats;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PartCombination)) {
            return false;
        }

        PartCombination combination = (PartCombination) o;

        return combination.includes.equals(this.includes) && combination.excludes.equals(this.excludes) && combination.stats.equals(this.stats);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + includes.hashCode();
        result = 31 * result + excludes.hashCode();
        result = 31 * result + stats.hashCode();
        return result;
    }

}
