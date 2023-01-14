package com.rogoshum.magickcore.common.recipe;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.common.util.NBTTagHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Container;
import net.minecraft.world.World;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MagickWorkbenchRecipe implements Recipe<Container> {
    public static final IRecipeType<MagickWorkbenchRecipe> MAGICK_WORKBENCH = Registry.register(Registry.RECIPE_TYPE, MagickCore.MOD_ID, new IRecipeType<MagickWorkbenchRecipe>() {
        public String toString() {
            return "magick_workbench";
        }
    });
    private final Ingredient ingredient;
    private final ItemStack recipeOutput;
    private final HashSet<String> keySet;
    private final ResourceLocation id;
    private final String group;

    public MagickWorkbenchRecipe(ResourceLocation idIn, String groupIn, Ingredient recipeItemsIn, ItemStack recipeOutputIn) {
        this.id = idIn;
        this.group = groupIn;
        this.ingredient = recipeItemsIn;
        this.recipeOutput = recipeOutputIn;
        HashSet<String> keys = new HashSet<>();
        if(recipeOutput.hasTag()) {
            CompoundNBT tag = recipeOutput.getTag();
            keys = NBTTagHelper.getNBTKeySet(tag);
        }
        keySet = keys;
    }

    @Override
    public IRecipeType<?> getType() {
        return MAGICK_WORKBENCH;
    }

    public ItemStack getResultItem() {
        return this.recipeOutput;
    }

    @Nonnull
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(ingredient);
    }

    @Nonnull
    public Ingredient getIngredient() {
        return ingredient;
    }

    public String getGroup() {
        return this.group;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canCraftInDimensions(int width, int height) {
        return width*height==1;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(Container inv, World worldIn) {
        if(inv.getContainerSize() < 1)
            return false;
        ItemStack stack = inv.getItem(0);
        return ingredient.test(stack);
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack assemble(Container inv) {
        if(inv.getContainerSize() < 1)
            return ItemStack.EMPTY;
        ItemStack stack = inv.getItem(0);
        HashSet<String> keys = new HashSet<>();
        if(stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            keys = NBTTagHelper.getNBTKeySet(tag);
        }
        if(keys.containsAll(this.keySet)) {
            ItemStack copy = this.getResultItem().copy();
            if(stack.hasTag())
                copy.setTag(stack.getTag().copy());
            return copy;
        }
        return this.getResultItem().copy();
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>  implements IRecipeSerializer<MagickWorkbenchRecipe> {
        private static final ResourceLocation NAME = new ResourceLocation(MagickCore.MOD_ID, "magick_workbench");
        public static final IRecipeSerializer<?> INSTANCE = new Serializer().setRegistryName(NAME);
        public MagickWorkbenchRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String s = JSONUtils.getAsString(json, "group", "");
            Ingredient ingredient = Ingredient.fromJson(JSONUtils.getAsJsonObject(json, "input"));
            ItemStack itemstack = NBTRecipe.deserializeItem(JSONUtils.getAsJsonObject(json, "result"));
            return new MagickWorkbenchRecipe(recipeId, s, ingredient, itemstack);
        }

        public MagickWorkbenchRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            String s = buffer.readUtf(32767);
            return new MagickWorkbenchRecipe(recipeId, s, Ingredient.fromNetwork(buffer), buffer.readItem());
        }

        public void toNetwork(PacketBuffer buffer, MagickWorkbenchRecipe recipe) {
            buffer.writeUtf(recipe.group);
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.recipeOutput);
        }
    }
}