package wraith.smithee.properties;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

import java.util.HashMap;
import java.util.HashSet;

public class Properties {

    //ToolPart --> Traits
    public HashMap<String, HashSet<Trait>> traits = new HashMap<>();
    public HashMap<String, Property> partProperties = new HashMap<>();

    public static Property getProperties(ItemStack stack) {
        CompoundTag tag = stack.getSubTag("Parts");

        String headPart = tag.getString("HeadPart");
        String bindingPart = tag.getString("BindingPart");
        String handlePart = tag.getString("HandlePart");

        Properties headProperties = ItemRegistry.PROPERTIES.get(headPart);
        Properties bindingProperties = ItemRegistry.PROPERTIES.get(bindingPart);
        Properties handleProperties = ItemRegistry.PROPERTIES.get(handlePart);

        int miningLevel = Math.max(headProperties.partProperties.get("head").miningLevel + bindingProperties.partProperties.get("binding").miningLevel + handleProperties.partProperties.get("handle").miningLevel, 0);
        int durability = Math.max(headProperties.partProperties.get("head").durability + bindingProperties.partProperties.get("binding").durability + handleProperties.partProperties.get("handle").durability, 1);
        float miningSpeed = Math.max(headProperties.partProperties.get("head").miningSpeed + bindingProperties.partProperties.get("binding").miningSpeed + handleProperties.partProperties.get("handle").miningSpeed, 0.5f);
        float attackDamage = Math.max(headProperties.partProperties.get("head").attackDamage + bindingProperties.partProperties.get("binding").attackDamage + handleProperties.partProperties.get("handle").attackDamage, 0.5f);
        float attackSpeed = Math.max(headProperties.partProperties.get("head").attackSpeed + bindingProperties.partProperties.get("binding").attackSpeed + handleProperties.partProperties.get("handle").attackSpeed, 0.5f);

        PartCombination partCombination = null;
        for (PartCombination combination : PartCombination.COMBINATIONS) {
            if ((combination.includes.get("head").isEmpty() || combination.includes.get("head").contains(headPart)) &&
                (combination.includes.get("binding").isEmpty() || combination.includes.get("binding").contains(bindingPart)) &&
                (combination.includes.get("handle").isEmpty() || combination.includes.get("handle").contains(handlePart)) &&
                (!combination.excludes.get("head").contains(headPart)) &&
                (!combination.excludes.get("binding").contains(bindingPart)) &&
                (!combination.excludes.get("handle").contains(handlePart))) {
                    partCombination = combination;
                    break;
            }
        }
        if (partCombination != null) {
            if (partCombination.stats.containsKey("mining_level")) {
                miningLevel = (int) Utils.evaluateExpression(partCombination.stats.get("mining_level").replace("base", String.valueOf(miningLevel)));
            } else if (partCombination.stats.containsKey("durability")) {
                durability = (int) Utils.evaluateExpression(partCombination.stats.get("durability").replace("base", String.valueOf(durability)));
            } else if (partCombination.stats.containsKey("mining_speed")) {
                miningSpeed = (int) Utils.evaluateExpression(partCombination.stats.get("mining_speed").replace("base", String.valueOf(miningSpeed)));
            } else if (partCombination.stats.containsKey("attack_damage")) {
                attackDamage = (int) Utils.evaluateExpression(partCombination.stats.get("attack_damage").replace("base", String.valueOf(attackDamage)));
            } else if (partCombination.stats.containsKey("attack_speed")) {
                attackSpeed = (int) Utils.evaluateExpression(partCombination.stats.get("attack_speed").replace("base", String.valueOf(attackSpeed)));
            }
        }

        return new Property(miningSpeed, miningLevel, durability, attackDamage, attackSpeed);
    }

    public static void setProperties(ItemStack itemStack, Property properties) {
        CompoundTag tag = itemStack.getOrCreateSubTag("SmitheeProperties");

        tag.putInt("MiningLevel", properties.miningLevel);
        tag.putFloat("MiningSpeed", properties.miningSpeed);
        tag.putFloat("AttackSpeed", properties.attackSpeed);
        tag.putFloat("AttackDamage", properties.attackDamage);
        tag.putInt("Durability", properties.durability);

        itemStack.putSubTag("SmitheeProperties", tag);
    }

    public static float getExtraDamage(String toolType) {
        switch (toolType) {
            case "pickaxe":
                return 1;
            case "axe":
                return 6;
            case "sword":
                return 3;
            case "shovel":
                return 1.5f;
            default:
                return 0;
        }
    }

    public static float getExtraAttackSpeed(String toolType) {
        switch (toolType) {
            case "pickaxe":
                return 0.2f;
            case "axe":
                return -0.2f;
            case "sword":
                return 0.6f;
            default:
                return 0;
        }
    }

}
