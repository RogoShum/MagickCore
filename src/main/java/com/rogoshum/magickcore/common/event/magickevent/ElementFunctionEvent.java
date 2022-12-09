package com.rogoshum.magickcore.common.event.magickevent;

import com.rogoshum.magickcore.api.IConditionOnlyEntity;
import com.rogoshum.magickcore.api.entity.IManaEntity;
import com.rogoshum.magickcore.api.enums.ApplyType;
import com.rogoshum.magickcore.api.event.ElementEvent;
import com.rogoshum.magickcore.common.lib.LibConditions;
import com.rogoshum.magickcore.common.lib.LibContext;
import com.rogoshum.magickcore.common.magick.context.SpellContext;
import com.rogoshum.magickcore.common.magick.context.child.ConditionContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class ElementFunctionEvent {

    @SubscribeEvent
    public void applyFunction(ElementEvent.ElementFunctionApply event) {
        if(event.getMagickContext().victim instanceof ItemEntity && event.getMagickContext().applyType == ApplyType.ATTACK)
            event.setCanceled(true);

        if(event.getMagickContext().applyType == ApplyType.HIT_BLOCK) {
            Entity last = event.getMagickContext().projectile;
            AtomicBoolean entityOnly = new AtomicBoolean(false);
            if(last instanceof IManaEntity) {
                SpellContext spellContext = ((IManaEntity) last).spellContext();
                if(spellContext.containChild(LibContext.CONDITION)) {
                    ConditionContext condition = spellContext.getChild(LibContext.CONDITION);
                    condition.conditions.forEach(condition1 -> {
                        if(condition1 instanceof IConditionOnlyEntity)
                            entityOnly.set(true);
                    });
                }
            }
            if(entityOnly.get())
                event.setCanceled(true);
        }
    }
}