package com.rogoshum.magickcore.recipes;

import com.rogoshum.magickcore.api.IItemContainer;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class CopyTagContainer extends NBTRecipeContainer{
    private final String item;
    private final int count;

    public static CopyTagContainer create(String item, IItemContainer... containers)
    {
        return new CopyTagContainer(item, 1, containers);
    }

    public static CopyTagContainer create(String item, int count, IItemContainer... containers)
    {
        return new CopyTagContainer(item, count, containers);
    }

    private CopyTagContainer(String item, int count, IItemContainer... containers)
    {
        super(containers);
        this.item = item;
        this.count = count;
    }

    @Override
    public ItemStack getResultStack(CraftingInventory inv) {
        ItemStack stack = new ItemStack(Registry.ITEM.getOrDefault(new ResourceLocation(item)));

        CompoundNBT tag = new CompoundNBT();
        List<IItemContainer> containers =  this.getContainers();
        for(IItemContainer container : containers)
        {
            for (int i = 0; i < inv.getSizeInventory(); ++i)
            {
                ItemStack itemStack = inv.getStackInSlot(i);
                if(container.hasKey() && container.matches(itemStack))
                {
                    for (String key : container.getKeys())
                    {
                        INBT nbt = itemStack.getTag().get(key);
                        tag.put(key, nbt);
                    }
                }
            }
        }

        stack.setTag(tag);
        stack.setCount(count);
        return stack;
    }
}