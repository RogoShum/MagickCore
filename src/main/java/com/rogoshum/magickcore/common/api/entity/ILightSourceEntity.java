package com.rogoshum.magickcore.common.api.entity;

import com.rogoshum.magickcore.common.magick.Color;
import net.minecraft.world.World;

public interface ILightSourceEntity extends IPositionEntity{
    public float getSourceLight();

    public boolean alive();

    public World world();

    public float eyeHeight();

    public Color getColor();

    public boolean spawnGlowBlock();
}