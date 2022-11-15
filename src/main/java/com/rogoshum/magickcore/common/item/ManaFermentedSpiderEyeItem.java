package com.rogoshum.magickcore.common.item;

import com.rogoshum.magickcore.common.api.mana.ISpellContext;
import com.rogoshum.magickcore.common.api.enums.ApplyType;
import com.rogoshum.magickcore.common.api.mana.IManaMaterial;
import com.rogoshum.magickcore.common.lib.LibItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ManaFermentedSpiderEyeItem extends BaseItem implements IManaMaterial {
    public ManaFermentedSpiderEyeItem() {
        super(properties());
    }

    @Override
    public boolean disappearAfterRead() {
        return false;
    }

    @Override
    public int getManaNeed(ItemStack stack) {
        return 400;
    }

    @Override
    public boolean upgradeManaItem(ItemStack stack, ISpellContext data) {
        data.spellContext().applyType(ApplyType.DE_BUFF);
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent(LibItem.MANA_SPIDER_EYE));
    }
}