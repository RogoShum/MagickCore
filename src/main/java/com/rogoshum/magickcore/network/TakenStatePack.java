package com.rogoshum.magickcore.network;

import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.capability.IElementAnimalState;
import com.rogoshum.magickcore.capability.ITakenState;
import com.rogoshum.magickcore.init.ModElements;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class TakenStatePack extends EntityPack{
    private final UUID uuid;
    private final int time;

    public TakenStatePack(PacketBuffer buffer) {
        super(buffer);
        uuid = buffer.readUniqueId();
        time = buffer.readInt();
    }

    public TakenStatePack(int id, int time, UUID uuid) {
        super(id);
        this.uuid = uuid;
        this.time = time;
    }

    public void toBytes(PacketBuffer buf) {
        super.toBytes(buf);
        buf.writeUniqueId(uuid);
        buf.writeInt(time);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> ctx) {
        if(ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) return;
        Entity entity = Minecraft.getInstance().world.getEntityByID(this.id);
        if(entity == null || entity.removed)
            return;
        ITakenState state = entity.getCapability(MagickCore.takenState).orElse(null);
        if(state != null) {
            state.setTime(this.time);
            state.setOwner(this.uuid);
        }
    }
}
