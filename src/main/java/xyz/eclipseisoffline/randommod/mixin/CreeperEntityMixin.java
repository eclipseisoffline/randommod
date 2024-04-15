package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.eclipseisoffline.randommod.NoDestroyExplosionBehavior;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity implements SkinOverlayOwner {

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType,
            World world) {
        super(entityType, world);
    }

    @Redirect(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;"))
    public Explosion setExplosionBehavior(World instance, Entity entity, double x, double y,
            double z,
            float power, ExplosionSourceType explosionSourceType) {
        return instance.createExplosion(entity, Explosion.createDamageSource(instance, entity),
                new NoDestroyExplosionBehavior(entity), x, y, z,
                power, false, explosionSourceType,
                ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER,
                SoundEvents.ENTITY_GENERIC_EXPLODE);
    }
}
