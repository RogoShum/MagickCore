package com.rogoshum.magickcore.common.item;

import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.api.mana.IManaContextItem;
import com.rogoshum.magickcore.api.render.IEasyRender;
import com.rogoshum.magickcore.client.RenderHelper;
import com.rogoshum.magickcore.client.event.RenderEvent;
import com.rogoshum.magickcore.common.lib.LibContext;
import com.rogoshum.magickcore.common.magick.MagickElement;
import com.rogoshum.magickcore.common.magick.MagickReleaseHelper;
import com.rogoshum.magickcore.common.magick.context.MagickContext;
import com.rogoshum.magickcore.common.extradata.entity.EntityStateData;
import com.rogoshum.magickcore.common.extradata.item.ItemManaData;
import com.rogoshum.magickcore.common.extradata.ExtraDataUtil;
import com.rogoshum.magickcore.common.magick.context.child.TraceContext;
import com.rogoshum.magickcore.proxy.ClientProxy;
import com.rogoshum.magickcore.proxy.IProxy;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SpiritCrystalStaffItem extends ManaItem implements IManaContextItem {
    public SpiritCrystalStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean releaseMagick(LivingEntity playerIn, EntityStateData state, ItemStack stack) {
        ItemManaData data = ExtraDataUtil.itemManaData(stack);
        MagickContext magickContext = MagickContext.create(playerIn.world, data.spellContext());
        MagickElement element = data.spellContext().element;
        MagickContext context = magickContext.caster(playerIn).victim(playerIn).element(element);
        if(context.containChild(LibContext.TRACE)) {
            TraceContext traceContext = context.getChild(LibContext.TRACE);
            traceContext.entity = MagickReleaseHelper.getEntityLookedAt(playerIn);
        }

        return MagickReleaseHelper.releaseMagick(context);
    }
}
