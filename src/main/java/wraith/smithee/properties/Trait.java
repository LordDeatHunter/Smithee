package wraith.smithee.properties;

public class Trait {

    String traitName;
    int minLevel;
    int maxLevel;
    String proc;
    double chance;

    public Trait(String traitName, int minLevel, int maxLevel, String proc, double chance) {
        this.traitName = traitName;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.proc = proc;
        this.chance = chance;
    }
}
