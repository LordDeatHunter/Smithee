package wraith.smithee.properties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.smithee.items.tools.BaseSmitheeItem;
import wraith.smithee.mixin.TextColorInvoker;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

import java.util.*;

public class Trait {

    public enum Traits {
        ECOLOGICAL,
        MIDAS_TOUCH,
        BRITTLE,
        MAGNETIC,
        SUPERHEATED,
        SHARP,
        CHILLING,
        ADAMANT,
        AQUADYNAMIC,
    }

    public String traitName;
    public int minLevel;
    public int maxLevel;
    public double chance;

    // TODO: ItemRegistry.PROPERTIES get is used often. Make it a private function called getTraitsOf

    public static final HashMap<String, Text> TRAIT_TEXT = new HashMap<String, Text>(){{
        put("ecological",  new LiteralText("Ecological") .setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x866526))));
        put("midas_touch", new LiteralText("Midas Touch").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0xe9b115))));
        put("brittle",     new LiteralText("Brittle")    .setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x6c6c6c))));
        put("magnetic",    new LiteralText("Magnetic")   .setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x33ebcb))));
        put("superheated", new LiteralText("SuperHeated").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x6A040F))));//0x652828))));
        put("sharp",       new LiteralText("Sharp")      .setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x87EFCC))));
        put("chilling",    new LiteralText("Chilling")   .setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x08CDFD))));
        put("adamant",     new LiteralText("Adamant")    .setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0xBF0026))));
        put("aquadynamic", new LiteralText("Aquadynamic").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0xA2D2FF))));
    }};

    public Trait(String traitName, int minLevel, int maxLevel, double chance) {
        this.traitName = traitName;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.chance = chance;
    }

    private static String convertNBTToConfig(String string) {
        switch (string) {
            case "HeadPart":
                return "head";
            case "BindingPart":
                return "binding";
            case "HandlePart":
                return "handle";
            default:
                return "";
        }
    }

    public static HashSet<Text> getTooltip(ItemStack stack) {
        HashSet<Text> tooltips = new HashSet<>();
        if (!(stack.getItem() instanceof BaseSmitheeItem) || !stack.hasTag() || !stack.getTag().contains("Parts")) {
            return tooltips;
        }
        CompoundTag tag = stack.getTag().getCompound("Parts");
        String head = tag.getString("HeadPart");
        String binding = tag.getString("BindingPart");
        String handle = tag.getString("HandlePart");

        for (Trait trait : ItemRegistry.PROPERTIES.get(head).traits.get("head")) {
            tooltips.add(TRAIT_TEXT.get(trait.traitName));
        }
        for (Trait trait : ItemRegistry.PROPERTIES.get(binding).traits.get("binding")) {
            tooltips.add(TRAIT_TEXT.get(trait.traitName));
        }
        for (Trait trait : ItemRegistry.PROPERTIES.get(handle).traits.get("handle")) {
            tooltips.add(TRAIT_TEXT.get(trait.traitName));
        }
        return tooltips;
    }

    public static boolean hasTrait(ItemStack stack, Trait trait) {
        if (!(stack.getItem() instanceof BaseSmitheeItem) || !stack.hasTag() || !stack.getTag().contains("Parts")) {
            return false;
        }
        CompoundTag tag = stack.getTag().getCompound("Parts");

        for (String part : tag.getKeys()) {
            if (ItemRegistry.PROPERTIES.get(tag.getString(part)).traits.get(convertNBTToConfig(part)).contains(trait)) { // FIXME this is *ugly*
                return true;
            }
        }
        return false;
    }

    public static boolean hasTrait(ItemStack stack, String traitName) {
        if (!(stack.getItem() instanceof BaseSmitheeItem) || !stack.hasTag() || !stack.getTag().contains("Parts")) {
            return false;
        }
        CompoundTag tag = stack.getTag().getCompound("Parts");

        for (String part : tag.getKeys()) {
            System.out.println(part);
            System.out.println(ItemRegistry.PROPERTIES.get(tag.getString(part)).traits);
            if (ItemRegistry.PROPERTIES.get(tag.getString(part)).traits.get(convertNBTToConfig(part)).stream().anyMatch(t -> t.traitName.equals(traitName))) { // FIXME this is *ugly*
                return true;
            }
        }
        return false;
    }

    public static boolean hasTrait(ItemStack stack, Traits trait) {
        return hasTrait(stack, trait.name().toLowerCase());
    }

    public static int getTraitLevel(ItemStack stack, Trait trait) {
        if (!hasTrait(stack, trait)) {
            return 0;
        }
        CompoundTag tag = stack.getTag().getCompound("Parts");
        int traitLevel = 0;

        for (String part : tag.getKeys()) {
            if (ItemRegistry.PROPERTIES.get(tag.getString(part)).traits.get(convertNBTToConfig(part)).contains(trait)) {
                traitLevel++;
            }
        }
        return traitLevel;
    }

    public static ItemStack getMidas(ItemStack stack) {
        if (!Trait.hasTrait(stack, Traits.MIDAS_TOUCH)) {
            return ItemStack.EMPTY;
        }
        CompoundTag tag = stack.getTag().getCompound("Parts");

        ItemStack nuggets = new ItemStack(Items.GOLD_NUGGET, 0);

        for (String part : tag.getKeys()) {
            HashSet<Trait> traits = ItemRegistry.PROPERTIES.get(tag.getString(part)).traits.get(convertNBTToConfig(part));
            traits.forEach(t -> {
                if (t.traitName.equals("midas_touch")) {
                    nuggets.setCount(nuggets.getCount() + Utils.getRandomIntInRange(t.minLevel, t.maxLevel));
                }
            });
        }
        return nuggets;
    }

    public static float getSharpnessAddition(ItemStack stack) {
        if (!Trait.hasTrait(stack, Traits.SHARP)) {
            return 0;
        }
        CompoundTag tag = stack.getTag().getCompound("Parts");

        float[] f = {0};
        for (String part : tag.getKeys()) {
            HashSet<Trait> traits = ItemRegistry.PROPERTIES.get(tag.getString(part)).traits.get(convertNBTToConfig(part));
            traits.forEach(t -> {
                if (t.traitName.equals("sharp")) {
                    f[0] += (float) Utils.getRandomDoubleInRange(t.minLevel, t.maxLevel);
                }
            });
        }
        return f[0];
        //FIXME IF POSSIBLE janky array shit?
    }
    //FIXME this one too
    public static int getFrostbiteEffectDuration(ItemStack stack) {
        if (!Trait.hasTrait(stack, Traits.CHILLING)) {
            return 0;
        }
        CompoundTag tag = stack.getTag().getCompound("Parts");

        int[] dur = {0};
        for (String part : tag.getKeys()) {
            HashSet<Trait> traits = ItemRegistry.PROPERTIES.get(tag.getString(part)).traits.get(convertNBTToConfig(part));
            traits.forEach(t -> {
                if (t.traitName.equals("chilling")) {
                    dur[0] += (float) Utils.getRandomIntInRange(t.minLevel, t.maxLevel);
                }
            });
        }
        return dur[0];
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Trait)) {
            return false;
        }

        Trait trait = (Trait) o;

        return trait.traitName.equals(this.traitName) && trait.minLevel == this.minLevel && trait.maxLevel == this.maxLevel && trait.chance == this.chance;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + traitName.hashCode();
        result = 31 * result + minLevel;
        result = 31 * result + maxLevel;
        result = (int) (31 * result + chance);
        return result;
    }

}
