package wraith.smithee.properties;

public class Properties {

    public final float miningSpeed;
    public final int miningLevel;
    public final int durability;
    public final float attackDamage;

    public Properties(int miningLevel, int durability, float miningSpeed, float attackDamage) {
        this.miningSpeed = miningSpeed;
        this.miningLevel = miningLevel;
        this.durability = durability;
        this.attackDamage = attackDamage;
    }

}
