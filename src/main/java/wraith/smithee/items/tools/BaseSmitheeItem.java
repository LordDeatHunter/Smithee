package wraith.smithee.items.tools;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public interface BaseSmitheeItem  {

    String getToolType();

    static long getLevel(long experience) {
        long xp = 100;
        long level = 0;
        while (xp < experience) {
            xp += calculateXp(xp, level);
            ++level;
        }
        return level;
    }

    static double calculateXp(long xp, long level) {
        return level < 15 ? Math.pow(Math.pow(xp, 0.3D) * 3, 2) + 100 : xp * 15;
    }

    static long getNeededExperience(long level) {
        long xp = 100;
        long lvl = 0;
        while (lvl < level) {
            xp += calculateXp(xp, lvl);
            ++lvl;
        }
        return xp;
    }

    static String getProgressString(long experience, long level) {
        long xp = getNeededExperience(level);
        double collected = (double)experience/(double)xp;
        StringBuilder filled = new StringBuilder();
        for (int i = 0; i < collected * 10; ++i) {
            filled.append("▓");
        }
        StringBuilder nonfilled = new StringBuilder();
        for (int i = 0; i < 10 - (collected * 10); ++i) {
            nonfilled.append("▓");
        }
        return "§1[§b" + filled + "§3" + nonfilled + "§1]";
    }

    default TypedActionResult<ItemStack> boostXp(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!user.isSneaking()) {
            return TypedActionResult.pass(stack);
        }
        CompoundTag tag = stack.getSubTag("SmitheeProperties");
        if (tag == null) {
            return TypedActionResult.fail(stack);
        }
        if (!tag.contains("Experience")) {
            tag.putInt("Experience", 0);
        }
        if (!tag.contains("Level")) {
            tag.putInt("Level", 0);
        }
        long curLevel = tag.getLong("Level");
        if (curLevel >= 25) {
            return TypedActionResult.fail(stack);
        }
        long vanillaxp = user.totalExperience;
        long lvl = 0;
        while (lvl < user.experienceLevel) {
            vanillaxp += getVanillaExperienceRequirement(lvl);
            ++lvl;
        }

        long xp = tag.getLong("Experience");
        long fraction = vanillaxp;
        if (vanillaxp >= 100) {
            fraction = vanillaxp / 5;
        }
        xp += fraction;
        user.addExperience((int) -fraction);
        tag.putLong("Experience", xp);
        tag.putLong("Level", getLevel(xp));
        if (tag.getLong("Level") != curLevel) {
            CompoundTag modifiers = tag.getCompound("Modifiers");
            int diff = (int) (tag.getLong("Level") - curLevel);
            modifiers.putInt("EnchantmentSlots", modifiers.getInt("EnchantmentSlots") + diff);
            tag.put("Modifiers", modifiers);
        }

        return TypedActionResult.success(stack, world.isClient());
    }

    static long getVanillaExperienceRequirement(long level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else {
            return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
        }
    }


    default String getBindingType() {
        return "binding";
    }
}