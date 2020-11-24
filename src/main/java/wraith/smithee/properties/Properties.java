package wraith.smithee.properties;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

import java.util.HashMap;
import java.util.HashSet;

public class Properties {

    public HashMap<HashSet<String>, HashMap<String, String>> combinations = new HashMap<>();;
    public HashMap<String, Trait> traits = new HashMap<>();
    public HashMap<String, Property> partProperties = new HashMap<>();
    public HashMap<String, String> fullToolProperties = new HashMap<>();

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
        Property property = new Property(miningSpeed, miningLevel, durability, attackDamage, attackSpeed);

        HashSet<String> headBinding = new HashSet<String>(){{add("head");add("binding");}};
        HashSet<String> headHandle = new HashSet<String>(){{add("head");add("handle");}};
        HashSet<String> handleBinding = new HashSet<String>(){{add("handle");add("binding");}};
        if (headPart.equals(bindingPart) && headProperties.combinations.containsKey(headBinding)) {
            changeProperties(headProperties.combinations.get(headBinding), property);
        } else if (bindingPart.equals(handlePart) && bindingProperties.combinations.containsKey(handleBinding)) {
            changeProperties(bindingProperties.combinations.get(handleBinding), property);
        } else if (handlePart.equals(headPart) && handleProperties.combinations.containsKey(headHandle)) {
            changeProperties(handleProperties.combinations.get(headHandle), property);
        }
        if (headPart.equals(bindingPart) && bindingPart.equals(handlePart) && handlePart.equals(headPart)) {
            if (!headProperties.fullToolProperties.isEmpty()) {
                changeProperties(headProperties.fullToolProperties, property);
            }
            if (!bindingProperties.fullToolProperties.isEmpty()) {
                changeProperties(headProperties.fullToolProperties, property);
            }
            if (!handleProperties.fullToolProperties.isEmpty()) {
                changeProperties(headProperties.fullToolProperties, property);
            }
        }
        return property;
    }

    private static void changeProperties(HashMap<String, String> properties, Property property) {
        if (properties.containsKey("miningLevel")) {
            property.miningLevel = (int) Math.max(Utils.evaluateExpression(replaceVariables(properties.get("miningLevel"), property.miningLevel)), 0);
        }
        else if (properties.containsKey("durability")) {
            property.durability = (int) Math.max(Utils.evaluateExpression(replaceVariables(properties.get("durability"), property.durability)), 1);
        }
        else if (properties.containsKey("miningSpeed")) {
            property.miningSpeed = (float) Math.max(Utils.evaluateExpression(replaceVariables(properties.get("miningSpeed"), property.miningSpeed)), 0.5);
        }
        else if (properties.containsKey("attackDamage")) {
            property.attackDamage = (float) Math.max(Utils.evaluateExpression(replaceVariables(properties.get("attackDamage"), property.attackDamage)), 0.5);
        }
        else if (properties.containsKey("attackSpeed")) {
            property.attackSpeed = (float) Math.max(Utils.evaluateExpression(replaceVariables(properties.get("attackSpeed"), property.attackSpeed)), 0.5);
        }
    }

    private static String replaceVariables(String expression, double value) {
         return expression.replace("base", String.valueOf(value));
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
}
