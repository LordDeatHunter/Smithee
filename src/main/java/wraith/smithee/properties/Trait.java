package wraith.smithee.properties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
import wraith.smithee.items.tools.BaseSmitheeTool;
import wraith.smithee.mixin.TextColorInvoker;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Trait {

    public String traitName;
    public int minLevel;
    public int maxLevel;
    public double chance;

    public static final HashMap<String, Text> TRAIT_TEXT = new HashMap<String, Text>(){{
        put("ecological", new LiteralText("Ecological").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x866526))));
        put("midas_touch", new LiteralText("Midas Touch").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0xe9b115))));
        put("brittle", new LiteralText("Brittle").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x6c6c6c))));
        put("lucky", new LiteralText("Lucky").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x33ebcb))));
        put("magnetic", new LiteralText("Magnetic").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x33ebcb))));
        put("superheated", new LiteralText("SuperHeated").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x652828))));
    }};

    public Trait(String traitName, int minLevel, int maxLevel, double chance) {
        this.traitName = traitName;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.chance = chance;
    }

    public static HashSet<Text> getTooltip(ItemStack stack) {
        HashSet<Text> tooltips = new HashSet<>();
        if (!(stack.getItem() instanceof BaseSmitheeTool) || !stack.hasTag() || !stack.getTag().contains("Parts")) {
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

    public static void evaluateBrittle(Trait trait, ItemStack stack) {
        if (!"brittle".equals(trait.traitName) || stack == ItemStack.EMPTY || trait.chance == 0 || Utils.getRandomDoubleInRange(0, 100) > trait.chance * 100) {
            return;
        }
        Utils.damage(stack, Utils.getRandomIntInRange(trait.minLevel, trait.maxLevel));
    }

    public static void evaluateMediasTouch(Trait trait, World world, BlockPos pos, @Nullable BlockEntity blockEntity) {
        if (!"midas_touch".equals(trait.traitName) || trait.chance == 0 || Utils.getRandomDoubleInRange(0, 100) > trait.chance * 100) {
            return;
        }
        Block.dropStack(world, pos, new ItemStack(Items.GOLD_NUGGET, Utils.getRandomIntInRange(trait.minLevel, trait.maxLevel)));
    }

    public static int evaluateFortune(String material, String part) {
        HashSet<Trait> traits = ItemRegistry.PROPERTIES.get(material).traits.get(part);
        for (Trait trait : traits) {
            if ("lucky".equals(trait.traitName) && trait.chance != 0 && Utils.getRandomDoubleInRange(0, 100) <= trait.chance * 100) {
                return Utils.getRandomIntInRange(trait.minLevel, trait.maxLevel);
            }
        }
        return 0;
    }

    public static void evaluateEcological(Trait trait, ItemStack stack) {
        if (!"ecological".equals(trait.traitName) || trait.chance == 0 || Utils.getRandomDoubleInRange(0, 100) > trait.chance * 100) {
            return;
        }
        Utils.repair(stack, 1);
    }

    public static void evaluateMagnetic(Trait trait, BlockState state, World world, BlockPos pos, @Nullable BlockEntity blockEntity, PlayerEntity player, ItemStack stack) {
        if (!"magnetic".equals(trait.traitName) || trait.chance == 0 || Utils.getRandomDoubleInRange(0, 100) > trait.chance * 100) {
            return;
        }
        List<ItemStack> drops = Block.getDroppedStacks(state, (ServerWorld)world, pos, blockEntity, player, stack);
        for(ItemStack drop : drops) {
            player.inventory.offerOrDrop(world, drop);
        }
    }

    public static boolean evaluateSuperheated(String material, String part) {
        HashSet<Trait> traits = ItemRegistry.PROPERTIES.get(material).traits.get(part);
        for (Trait trait : traits) {
            if ("superheated".equals(trait.traitName) && trait.chance != 0 && Utils.getRandomDoubleInRange(0, 100) <= trait.chance * 100) {
                return true;
            }
        }
        return false;
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
