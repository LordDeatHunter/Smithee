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
        put("magnetic", new LiteralText("Magnetic").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x33ebcb))));
        put("superheated", new LiteralText("SuperHeated").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x652828))));
        put("sharp", new LiteralText("Sharp").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x87EFCC))));
        put("chilling", new LiteralText("Chilling").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0x08CDFD))));
        put("adamant", new LiteralText("Adamant").setStyle(Style.EMPTY.withColor(TextColorInvoker.init(0xBF0026))));
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

    public static HashMap<String, Object> evaluateTraits(ItemStack stack, World world, BlockPos pos, BlockState state, @Nullable BlockEntity entity, Entity player, String source) {
        HashMap<String, Object> returns = new HashMap<>();
        if (!(stack.getItem() instanceof BaseSmitheeTool) || !stack.hasTag() || !stack.getTag().contains("Parts")) {
            return returns;
        }

        CompoundTag tag = stack.getTag().getCompound("Parts");

        HashMap<String, String> parts = new HashMap<>();
        parts.put("head", tag.getString("HeadPart"));
        parts.put("binding", tag.getString("BindingPart"));
        parts.put("handle", tag.getString("HandlePart"));

        HashSet<Trait> evaluateOnce = new HashSet<>();
        HashSet<String> evaluateOnceString = new HashSet<>();
        evaluateOnceString.add("magnetic");
        for (String part : parts.keySet()) {
            HashSet<Trait> traits = ItemRegistry.PROPERTIES.get(parts.get(part)).traits.get(part);
            for (Trait trait : traits) {
                if (evaluateOnceString.contains(trait.traitName)){
                    evaluateOnce.add(trait);
                } else {
                    returns.putAll(Trait.evaluateTrait(trait, source, stack, state, world, pos, entity, player, returns));
                }
            }
        }
        for (Trait trait : evaluateOnce) {
            returns.putAll(evaluateTrait(trait, source, stack, state, world, pos, entity, player, returns));
        }
        return returns;
    }

    public static HashMap<String, Object> evaluateTrait(Trait trait, String source, ItemStack stack, BlockState state, World world, BlockPos pos, @Nullable BlockEntity entity, Entity player, HashMap<String, Object> variables) {
        HashMap<String, Object> returns = new HashMap<>();
        if (stack == ItemStack.EMPTY || trait.chance == 0 || Utils.getRandomDoubleInRange(0, 100) > trait.chance * 100) {
            return returns;
        }
        switch(trait.traitName) {
            case "midas_touch":
                if (!"Block#afterBreak".equals(source)) {
                    break;
                }
                returns.put("Cancel Exhaustion", false);
                returns.put("Cancel Drops", false);
                if (!world.isClient()) {
                    List<ItemStack> changedDrops = Block.getDroppedStacks(state, (ServerWorld) world, pos, entity, player, stack);
                    changedDrops.add(new ItemStack(Items.GOLD_NUGGET, Utils.getRandomIntInRange(trait.minLevel, trait.maxLevel)));
                    returns.put("Drops", changedDrops);
                }
                break;
            case "ecological":
                if (!"ItemStack#inventoryTick".equals(source)) {
                    break;
                }
                Utils.repair(stack, 1);
                break;
            case "superheated":
                if (!"ItemEntity#isFireImmune".equals(source)) {
                    break;
                }
                returns.put("Fire Immunity", true);
                break;
            case "brittle":
                if (!"Block#afterBreak".equals(source)) {
                    break;
                }
                Utils.damage(stack, Utils.getRandomIntInRange(trait.minLevel, trait.maxLevel));
                break;
            case "magnetic":
                if (!"Block#afterBreak".equals(source)) {
                    break;
                }
                returns.put("Cancel Exhaustion", false);
                returns.put("Cancel Drops", true);
                for (ItemStack drop : (List<ItemStack>)variables.getOrDefault("Drops", Block.getDroppedStacks(state, (ServerWorld) world, pos, entity, player, stack))) {
                    ((PlayerEntity)player).inventory.offerOrDrop(world, drop);
                }
                break;
            case "chilling":
                if (!"LivingEntity#damage".equals(source)) {
                    break;
                }
                returns.put("Damage Entity Effect Type", "frostbite");
                returns.put("Damage Entity Effect Duration", Utils.getRandomIntInRange(trait.minLevel, trait.maxLevel));
                break;
            case "adamant":
                if (!"ItemStack#damage".equals(source)) {
                    break;
                }
                returns.put("Cancel Item Damage", true);
                break;
            case "sharp":
                if (!"MobEntity#tryAttack".equals(source)) {
                    break;
                }
                returns.put("Attack Damage Amount", (float)Utils.getRandomDoubleInRange(trait.minLevel, trait.maxLevel));
                break;
        }
        return returns;
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
