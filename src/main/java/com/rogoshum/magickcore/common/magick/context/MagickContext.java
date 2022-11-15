package com.rogoshum.magickcore.common.magick.context;

import com.rogoshum.magickcore.common.util.ToolTipHelper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class MagickContext extends SpellContext{
    public final World world;
    public Entity caster, projectile, victim;
    public boolean noCost = false;

    public MagickContext(World world) {
        this.world = world;
    }

    public static MagickContext create(World world, SpellContext spellContext) {
        MagickContext context = new MagickContext(world);
        if(spellContext != null)
            context.copy(spellContext);
        return context;
    }

    public MagickContext caster(Entity caster) {
        this.caster = caster;
        return this;
    }

    public MagickContext noCost() {
        this.noCost = true;
        return this;
    }

    public MagickContext projectile(Entity projectile) {
        this.projectile = projectile;
        return this;
    }

    public MagickContext victim(Entity victim) {
        this.victim = victim;
        return this;
    }

    public static MagickContext create(World world) {
        return new MagickContext(world);
    }

    @Override
    public String toString() {
        ToolTipHelper toolTip = new ToolTipHelper();
        if(world != null)
            toolTip.nextTrans("World", world, ToolTipHelper.PINK, ToolTipHelper.GREY);
        if(caster != null)
            toolTip.nextTrans("Caster", caster, ToolTipHelper.PINK, ToolTipHelper.GREY);
        if(projectile != null)
            toolTip.nextTrans("Projectile", projectile, ToolTipHelper.PINK, ToolTipHelper.GREY);
        if(victim != null)
            toolTip.nextTrans("Victim", victim, ToolTipHelper.PINK, ToolTipHelper.GREY);
        if(noCost)
            toolTip.nextTrans("noCost", ToolTipHelper.PINK);

        toolTip.builder.append(getString(1));
        return super.toString();
    }
}