package com.rogoshum.magickcore.entity.base;

import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.api.entity.ILightSourceEntity;
import com.rogoshum.magickcore.api.entity.IManaEntity;
import com.rogoshum.magickcore.api.event.EntityEvents;
import com.rogoshum.magickcore.lib.LibContext;
import com.rogoshum.magickcore.magick.Color;
import com.rogoshum.magickcore.magick.MagickReleaseHelper;
import com.rogoshum.magickcore.magick.context.child.TraceContext;
import com.rogoshum.magickcore.magick.context.SpellContext;
import com.rogoshum.magickcore.magick.context.child.PositionContext;
import com.rogoshum.magickcore.tool.EntityLightSourceHandler;
import com.rogoshum.magickcore.client.particle.LitParticle;
import com.rogoshum.magickcore.client.particle.TrailParticle;
import com.rogoshum.magickcore.enums.EnumApplyType;
import com.rogoshum.magickcore.magick.context.MagickContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class ManaProjectileEntity extends ThrowableEntity implements IManaEntity, ILightSourceEntity, IEntityAdditionalSpawnData {
    public boolean cansee;
    private final SpellContext spellContext = SpellContext.create();
    private static final DataParameter<Optional<UUID>> dataUUID = EntityDataManager.createKey(ManaProjectileEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Float> HEIGHT = EntityDataManager.createKey(ManaProjectileEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> WIDTH = EntityDataManager.createKey(ManaProjectileEntity.class, DataSerializers.FLOAT);
    public Entity victim;

    public ManaProjectileEntity(EntityType<? extends ThrowableEntity> type, World worldIn) {
        super(type, worldIn);
        this.dataManager.register(dataUUID, Optional.of(MagickCore.emptyUUID));
        this.dataManager.register(HEIGHT, this.getType().getHeight());
        this.dataManager.register(WIDTH, this.getType().getWidth());
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (HEIGHT.equals(key) || WIDTH.equals(key)) {
            this.recalculateSize();
        }
        super.notifyDataManagerChange(key);
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return EntitySize.flexible(this.getDataManager().get(WIDTH), this.getDataManager().get(HEIGHT));
    }

    public void setHeight(float height) {
        this.getDataManager().set(HEIGHT, height);
    }
    public void setWidth(float width) {
        this.getDataManager().set(WIDTH, width);
    }

    @Override
    protected float getEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return sizeIn.height * -0.5f;
    }

    @Nonnull
    @Override
    public List<Entity> findEntity(@Nullable Predicate<Entity> predicate) {
        List<Entity> entities = new ArrayList<>();
        if(victim != null && (predicate == null || predicate.test(victim)))
            entities.add(victim);
        return entities;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        CompoundNBT addition = new CompoundNBT();
        writeAdditional(addition);
        buffer.writeCompoundTag(addition);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if(id == 3)
            this.remove();
        else
            super.handleStatusUpdate(id);
    }

    @Override
    public SpellContext spellContext() {
        return spellContext;
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        readAdditional(additionalData.readCompoundTag());
    }

    @Override
    public boolean isImmuneToFire() {
        return true;
    }

    @Override
    public void forceFireTicks(int ticks) {
    }

    @Override
    public int getFireTimer() {
        return 0;
    }

    @Override
    public void setShooter(Entity entityIn) {
        super.setShooter(entityIn);
        if (entityIn != null)
            this.setOwnerUUID(entityIn.getUniqueID());
    }

    @Override
    public void setOwner(Entity entityIn) {
        this.setShooter(entityIn);
    }

    @Override
    public Entity getOwner() {
        return this.func_234616_v_();
    }

    @Nullable
    @Override
    public Entity func_234616_v_() {
        Entity entity = super.func_234616_v_();

        if (entity == null && this.world.isRemote) {
            ArrayList<Entity> list = new ArrayList<>();
            ((ClientWorld) this.world).getAllEntities().forEach((list::add));

            for (Entity e : list) {
                if (e.getUniqueID().equals(getOwnerUUID()))
                    return e;
            }
        }
        return entity;
    }

    @Override
    public void tick() {
        victim = null;
        super.tick();
        /*if (this.homePos == null)
            this.homePos = this.getPositionVec();
        else if (this.homePos.subtract(this.getPositionVec()).length() > this.getRange())
            this.remove();

         */
        if (!world.isRemote)
            makeSound();

        traceTarget();
        float height = getType().getHeight() + spellContext().range * 0.1f;
        if(getHeight() != height)
            this.setHeight(height);
        float width = getType().getWidth() + spellContext().range * 0.1f;
        if(getWidth() != width)
            this.setWidth(width);
        if(this.world.isRemote) {
            MagickCore.proxy.addTask(this::doClientTask);
        } else
            MagickCore.proxy.addTask(this::doServerTask);
    }

    protected void doClientTask() {
        applyParticle();
    }

    protected void doServerTask() {

    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        EntityLightSourceHandler.addLightSource(this);
    }

    @Override
    public float getSourceLight() {
        return 7;
    }

    @Override
    public boolean alive() {
        return isAlive();
    }

    @Override
    public Vector3d positionVec() {
        return getPositionVec();
    }

    @Override
    public World world() {
        return getEntityWorld();
    }

    @Override
    public float eyeHeight() {
        return this.getHeight() / 2;
    }

    @Override
    public Color getColor() {
        return this.spellContext().element.color();
    }

    protected void makeSound() {
        if (this.ticksExisted == 1) {
            this.playSound(SoundEvents.ENTITY_ENDER_PEARL_THROW, 1.5F, 1.0F + this.rand.nextFloat());
        }
    }

    @Override
    protected void registerData() {

    }

    @Override
    public void setOwnerUUID(UUID uuid) {
        this.getDataManager().set(dataUUID, Optional.of(uuid));
    }

    public UUID getOwnerUUID() {
        Optional<UUID> uuid = this.getDataManager().get(dataUUID);
        return uuid.orElse(MagickCore.emptyUUID);
    }

    protected void traceTarget() {
        if (!this.spellContext().containChild(LibContext.TRACE) || this.world.isRemote) return;
        TraceContext traceContext = spellContext().getChild(LibContext.TRACE);
        Entity entity = traceContext.entity;
        if(entity == null && traceContext.uuid != MagickCore.emptyUUID) {
            entity = ((ServerWorld) this.world).getEntityByUuid(traceContext.uuid);
            traceContext.entity = entity;
        } else if(entity != null) {
            Vector3d goal = new Vector3d(entity.getPosX(), entity.getPosY() + entity.getHeight() / 1.5f, entity.getPosZ());
            Vector3d self = new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ());

            Vector3d motion = goal.subtract(self).normalize().scale(this.getMotion().length() * 0.06);
            this.setMotion(motion.add(this.getMotion()));
        }
    }

    protected void onImpact(RayTraceResult result) {
        if(result.getType() == RayTraceResult.Type.ENTITY) {
            this.victim = ((EntityRayTraceResult)result).getEntity();
        }
        super.onImpact(result);
    }

    protected void applyParticle() {
        if (this.world.isRemote() && this.spellContext().element != null) {
            LitParticle par = new LitParticle(this.world, MagickCore.proxy.getElementRender(spellContext().element.type()).getParticleTexture()
                    , new Vector3d(MagickCore.getNegativeToOne() * this.getWidth() + this.getPosX()
                    , MagickCore.getNegativeToOne() * this.getWidth() + this.getPosY() + this.getHeight() / 2
                    , MagickCore.getNegativeToOne() * this.getWidth() + this.getPosZ())
                    , 0.1f, 0.1f, 1.0f, 40, MagickCore.proxy.getElementRender(spellContext().element.type()));
            par.setGlow();
            MagickCore.addMagickParticle(par);

            for (int i = 0; i < 2; ++i) {
                LitParticle litPar = new LitParticle(this.world, MagickCore.proxy.getElementRender(spellContext().element.type()).getMistTexture()
                        , new Vector3d(MagickCore.getNegativeToOne() * this.getWidth() / 2 + this.getPosX()
                        , MagickCore.getNegativeToOne() * this.getWidth() / 2 + this.getPosY() + this.getHeight() / 2
                        , MagickCore.getNegativeToOne() * this.getWidth() / 2 + this.getPosZ())
                        , this.getWidth() + (this.rand.nextFloat() * this.getWidth()), this.getWidth() + (this.rand.nextFloat() * this.getWidth()), 0.8f, spellContext().element.getRenderer().getParticleRenderTick(), spellContext().element.getRenderer());
                litPar.setGlow();
                litPar.setParticleGravity(0f);
                litPar.setShakeLimit(15.0f);
                litPar.setLimitScale();
                MagickCore.addMagickParticle(litPar);
            }
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.hasUniqueId("Owner")) {
            UUID ownerUUID = compound.getUniqueId("Owner");
            this.setOwnerUUID(ownerUUID);
        }
        spellContext().deserialize(compound);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (this.getOwner() != null) {
            compound.putUniqueId("Owner", this.getOwner().getUniqueID());
        }
        spellContext().serialize(compound);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult p_213868_1_) {
        EntityEvents.HitEntityEvent event = new EntityEvents.HitEntityEvent(this, p_213868_1_.getEntity());
        MinecraftForge.EVENT_BUS.post(event);

        if (!this.world.isRemote) {
            this.remove();
        }
        super.onEntityHit(p_213868_1_);
    }

    @Override
    protected void func_230299_a_(BlockRayTraceResult p_230299_1_) {
        BlockState blockstate = this.world.getBlockState(p_230299_1_.getPos());
        blockstate.onProjectileCollision(this.world, blockstate, p_230299_1_, this);
        MagickContext context = MagickContext.create(world, spellContext().postContext).<MagickContext>applyType(EnumApplyType.HIT_BLOCK).saveMana().caster(this.func_234616_v_()).projectile(this);
        PositionContext positionContext = PositionContext.create(Vector3d.copy(p_230299_1_.getPos()));
        context.addChild(positionContext);
        MagickReleaseHelper.releaseMagick(context);

        if (!this.world.isRemote) {
            this.remove();
        }
        super.func_230299_a_(p_230299_1_);
    }

    @Override
    public void remove() {
        victim = this;
        releaseMagick();
        if (!this.world.isRemote) {
            this.playSound(SoundEvents.ENTITY_ENDER_EYE_DEATH, 1.5F, 1.0F + this.rand.nextFloat());
        }
        if (this.world.isRemote()) {
            for (int c = 0; c < 15; ++c) {
                LitParticle par = new LitParticle(this.world, spellContext().element.getRenderer().getParticleTexture()
                        , new Vector3d(MagickCore.getNegativeToOne() * this.getWidth() + this.getPosX()
                        , MagickCore.getNegativeToOne() * this.getWidth() + this.getPosY() + this.getHeight() / 2
                        , MagickCore.getNegativeToOne() * this.getWidth() + this.getPosZ())
                        , 0.125f, 0.125f, MagickCore.rand.nextFloat(), 80, spellContext().element.getRenderer());
                par.setGlow();
                par.setShakeLimit(15.0f);
                par.addMotion(MagickCore.getNegativeToOne() / 10, MagickCore.getNegativeToOne() / 10, MagickCore.getNegativeToOne() / 10);
                MagickCore.addMagickParticle(par);
            }
            for (int i = 0; i < 5; ++i) {
                LitParticle litPar = new LitParticle(this.world, spellContext().element.getRenderer().getMistTexture()
                        , new Vector3d(MagickCore.getNegativeToOne() * this.getWidth() / 2 + this.getPosX()
                        , MagickCore.getNegativeToOne() * this.getWidth() / 2 + this.getPosY() + this.getHeight() / 2
                        , MagickCore.getNegativeToOne() * this.getWidth() / 2 + this.getPosZ())
                        , this.getWidth() + (this.rand.nextFloat() * this.getWidth()), this.getWidth() + (this.rand.nextFloat() * this.getWidth()), 0.5f * MagickCore.rand.nextFloat(), spellContext().element.getRenderer().getParticleRenderTick(), spellContext().element.getRenderer());
                litPar.setGlow();
                litPar.setParticleGravity(0f);
                litPar.setShakeLimit(15.0f);
                litPar.addMotion(MagickCore.getNegativeToOne() / 15, MagickCore.getNegativeToOne() / 15, MagickCore.getNegativeToOne() / 15);
                MagickCore.addMagickParticle(litPar);
            }
        }
        super.remove();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.005F;
    }
}