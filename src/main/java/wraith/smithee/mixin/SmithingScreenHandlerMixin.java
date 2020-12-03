package wraith.smithee.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wraith.smithee.Smithee;
import wraith.smithee.recipes.RecipesGenerator;
import wraith.smithee.recipes.SmithingRecipe;
import wraith.smithee.registry.ItemRegistry;
import wraith.smithee.utils.Utils;

import java.util.HashSet;

@Mixin(SmithingScreenHandler.class)
public class SmithingScreenHandlerMixin {

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    public void updateResult(CallbackInfo ci) {
        ItemStack input = ((ForgingScreenHandlerAccessor) this).getInput().getStack(0);
        ItemStack addition = ((ForgingScreenHandlerAccessor) this).getInput().getStack(1);
        Identifier inputId = Registry.ITEM.getId(input.getItem());

        if (!inputId.getNamespace().equals(Smithee.MOD_ID)) {
            return;
        }
        String[] segments = inputId.getPath().split("_");

        HashSet<String> key = new HashSet<>();
        key.add(inputId.getPath().split("_")[0]);
        key.add(Registry.ITEM.getId(addition.getItem()).toString());

        if (!RecipesGenerator.RECIPES.containsKey(key)) {
            return;
        }
        SmithingRecipe recipe = RecipesGenerator.RECIPES.get(key);

        if (!segments[0].equals(recipe.inputMaterial)) {
            return;
        }

        if (addition.getCount() >= recipe.additionAmount) {
            ItemStack output = new ItemStack(ItemRegistry.ITEMS.get(recipe.outputMaterial + "_" + segments[1] + "_" + segments[2]));
            Utils.setDamage(output, (int) input.getTag().getDouble("PartDamage"));
            ((ForgingScreenHandlerAccessor) this).getOutput().setStack(0, output);
            ci.cancel();
        }
    }

    @Inject(method = "onTakeOutput", at = @At("HEAD"), cancellable = true)
    private void onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack input = ((ForgingScreenHandlerAccessor) this).getInput().getStack(0);
        ItemStack addition = ((ForgingScreenHandlerAccessor) this).getInput().getStack(1);
        Identifier inputId = Registry.ITEM.getId(input.getItem());
        if (!inputId.getNamespace().equals(Smithee.MOD_ID)) {
            return;
        }
        String[] segments = inputId.getPath().split("_");

        HashSet<String> key = new HashSet<>();
        key.add(inputId.getPath().split("_")[0]);
        key.add(Registry.ITEM.getId(addition.getItem()).toString());

        if (!RecipesGenerator.RECIPES.containsKey(key)) {
            return;
        }
        SmithingRecipe recipe = RecipesGenerator.RECIPES.get(key);

        if (!segments[0].equals(recipe.inputMaterial)) {
            return;
        }

        if (addition.getCount() >= recipe.additionAmount) {
            stack.onCraft(player.world, player, stack.getCount());
            input.decrement(1);
            addition.decrement(recipe.additionAmount);
            cir.setReturnValue(stack);
            cir.cancel();
        }
    }

    @Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
    private void canTakeOutput(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {
        ItemStack input = ((ForgingScreenHandlerAccessor) this).getInput().getStack(0);
        ItemStack addition = ((ForgingScreenHandlerAccessor) this).getInput().getStack(1);
        Identifier inputId = Registry.ITEM.getId(input.getItem());
        if (!inputId.getNamespace().equals(Smithee.MOD_ID)) {
            return;
        }
        String[] segments = inputId.getPath().split("_");

        HashSet<String> key = new HashSet<>();
        key.add(inputId.getPath().split("_")[0]);
        key.add(Registry.ITEM.getId(addition.getItem()).toString());

        if (!RecipesGenerator.RECIPES.containsKey(key)) {
            return;
        }
        SmithingRecipe recipe = RecipesGenerator.RECIPES.get(key);

        if (!segments[0].equals(recipe.inputMaterial)) {
            return;
        }

        if (addition.getCount() >= recipe.additionAmount) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

}