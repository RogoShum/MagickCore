package com.rogoshum.magickcore.init;

import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.magick.ManaCapacity;
import com.rogoshum.magickcore.magick.context.SpellContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModDataSerializers {
    public static final DeferredRegister<DataSerializerEntry> DATA_SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, MagickCore.MOD_ID);
    public static final IDataSerializer<ManaCapacity> MANA_CAPACITY = new IDataSerializer<ManaCapacity>() {
        @Override
        public void write(PacketBuffer buf, ManaCapacity value) {
            CompoundNBT tag = new CompoundNBT();
            value.serialize(tag);
            buf.writeCompoundTag(tag);
        }

        @Override
        public ManaCapacity read(PacketBuffer buf) {
            CompoundNBT tag = buf.readCompoundTag();
            ManaCapacity capacity = ManaCapacity.create(tag);
            if(capacity == null)
                return new ManaCapacity(0);
            return capacity;
        }

        @Override
        public ManaCapacity copyValue(ManaCapacity value) {
            CompoundNBT tag = new CompoundNBT();
            value.serialize(tag);
            ManaCapacity capacity = ManaCapacity.create(tag);
            if(capacity == null)
                return new ManaCapacity(0);
            return capacity;
        }
    };

    public static final IDataSerializer<SpellContext> SPELL_CONTEXT = new IDataSerializer<SpellContext>() {
        @Override
        public void write(PacketBuffer buf, SpellContext value) {
            CompoundNBT tag = new CompoundNBT();
            value.serialize(tag);
            buf.writeCompoundTag(tag);
        }

        @Override
        public SpellContext read(PacketBuffer buf) {
            CompoundNBT tag = buf.readCompoundTag();
            return SpellContext.create(tag);
        }

        @Override
        public SpellContext copyValue(SpellContext value) {
            CompoundNBT tag = new CompoundNBT();
            value.serialize(tag);
            return SpellContext.create(tag);
        }
    };

    private static final RegistryObject<DataSerializerEntry> MANA_CAPACITY_REGISTRY = DATA_SERIALIZERS.register("mana_capacity", () -> new DataSerializerEntry(MANA_CAPACITY));
    private static final RegistryObject<DataSerializerEntry> SPELL_CONTEXT_REGISTRY = DATA_SERIALIZERS.register("spell_context", () -> new DataSerializerEntry(SPELL_CONTEXT));
}