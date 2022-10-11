package com.rogoshum.magickcore.registry.elementmap;

import com.rogoshum.magickcore.api.event.ElementEvent;
import com.rogoshum.magickcore.enums.EnumApplyType;
import com.rogoshum.magickcore.magick.context.MagickContext;
import com.rogoshum.magickcore.registry.ElementMap;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Function;

public class ElementFunctions extends ElementMap<EnumApplyType, Function<MagickContext, Boolean>> {
    private ElementFunctions(){}

    public static ElementFunctions create(){
        return new ElementFunctions();
    }

    public boolean applyElementFunction(MagickContext context) {
        if(elementMap.containsKey(context.applyType)) {
            ElementEvent.ElementFunctionApply event = new ElementEvent.ElementFunctionApply(context);
            MinecraftForge.EVENT_BUS.post(event);
            if(!event.isCanceled())
                return elementMap.get(context.applyType).apply(event.getMagickContext());
        }

        return false;
    }
}
