package com.rogoshum.magickcore.api.magick.context.child;

import com.rogoshum.magickcore.api.enums.ApplyType;
import net.minecraft.nbt.CompoundTag;

public abstract class ChildContext {
    public abstract void serialize(CompoundTag tag);
    public abstract void deserialize(CompoundTag tag);

    public abstract boolean valid();
    public abstract Type<?> getType();
    public abstract String getString(int tab);

    public ApplyType getLinkType() {
        return ApplyType.NONE;
    }

    public record Type<T extends ChildContext>(String name) {
    }
}