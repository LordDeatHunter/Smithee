package wraith.smithee.properties;

import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import wraith.smithee.items.tools.BaseSmitheeItem;
import wraith.smithee.mixin.TextColorInvoker;
import wraith.smithee.recipes.EmbossModifiers;
import wraith.smithee.recipes.EmbossRecipe;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Modifier {

    public int level;
    public String type;
    public HashSet<String> modifiers;

    public Modifier(String type, int level, HashSet<String> modifiers) {
        this.level = level;
        this.type = type;
        this.modifiers = modifiers;
    }

    public static final HashMap<String, String> MODIFIER_TEXT = new HashMap<String, String>(){{
        put("lapis_lazuli", "Luck");
        put("redstone", "Haste");
        put("quartz", "Sharp");
        put("silky_jewel", "Silky");
        put("mending_moss", "Mending Moss");
    }};

    public static final HashMap<String, Integer> MODIFIER_COLOR = new HashMap<String, Integer>(){{
        put("lapis_lazuli", 0x219EBC);
        put("redstone", 0xD62828);
        put("quartz", 0xCAF0F8);
        put("silky_jewel", 0xF9C74F);
        put("mending_moss", 0xA7C957);
    }};

    public static HashSet<Text> getTooltip(ItemStack stack) {
        HashSet<Text> tooltips = new HashSet<>();
        if (!(stack.getItem() instanceof BaseSmitheeItem) || !stack.hasTag() || !stack.getTag().contains("SmitheeProperties") || !stack.getSubTag("SmitheeProperties").contains("Modifiers")) {
            return tooltips;
        }

        for (String modifier : stack.getSubTag("SmitheeProperties").getCompound("Modifiers").getCompound("Slots").getKeys()) {
            int level = stack.getSubTag("SmitheeProperties").getCompound("Modifiers").getCompound("Slots").getInt(modifier);
            tooltips.add(new LiteralText(MODIFIER_TEXT.get(modifier) + " " + Utils.toRoman(level)).setStyle(Style.EMPTY.withColor(TextColorInvoker.init(MODIFIER_COLOR.get(modifier)))));
        }


        return tooltips;
    }


    public static HashMap<String, Modifier> evaluateModifiers(ItemStack stack) {
        HashMap<String, Modifier> result = new HashMap<>();
        if (!(stack.getItem() instanceof BaseSmitheeItem) || !stack.hasTag() || !stack.getTag().contains("SmitheeProperties") || !stack.getSubTag("SmitheeProperties").contains("Modifiers")) {
            return result;
        }
        Set<String> modifiers = stack.getSubTag("SmitheeProperties").getCompound("Modifiers").getCompound("Slots").getKeys();
        for (String modifier : modifiers) {
            HashMap<String, EmbossRecipe> registry = ItemRegistry.EMBOSS_RECIPES;
            if (!registry.containsKey(modifier)) {
                continue;
            }
            EmbossRecipe recipe = ItemRegistry.EMBOSS_RECIPES.get(modifier);
            String toolType = ((BaseSmitheeItem)stack.getItem()).getToolType();
            HashSet<String> gives  = new HashSet<>();
            for (EmbossModifiers embossModifiers : recipe.modifiers) {
                if (embossModifiers.givesTo.contains(toolType)) {
                    gives.add(embossModifiers.gives);
                }
            }
            int level = stack.getSubTag("SmitheeProperties").getCompound("Modifiers").getCompound("Slots").getInt(modifier);
            Modifier mod = new Modifier(recipe.type, level, gives);
            result.put(Utils.capitalize(modifier.split("_")), mod);
        }
        return result;
    }

}
