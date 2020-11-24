package wraith.smithee.properties;

public class Property {

    public float miningSpeed;
    public int miningLevel;
    public int durability;
    public float attackDamage;
    public float attackSpeed;

    public Property(float miningSpeed, int miningLevel, int durability, float attackDamage, float attackSpeed) {
        this.miningSpeed = miningSpeed;
        this.miningLevel = miningLevel;
        this.durability = durability;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

}
