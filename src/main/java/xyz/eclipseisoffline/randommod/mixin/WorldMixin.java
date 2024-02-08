package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.eclipseisoffline.randommod.CreeperExplosionBehavior;

@Mixin(World.class)
public class WorldMixin {

    @Redirect(method = "createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/sound/SoundEvent;)Lnet/minecraft/world/explosion/Explosion;"))
    public Explosion setCreeperBehavior(World instance, Entity entity, DamageSource damageSource,
            ExplosionBehavior behavior, double x, double y, double z, float power,
            boolean createFire, ExplosionSourceType explosionSourceType, ParticleEffect particle,
            ParticleEffect emitterParticle, SoundEvent soundEvent) {
        if (entity instanceof CreeperEntity) {
            behavior = new CreeperExplosionBehavior(entity);
        }
        return instance.createExplosion(entity, damageSource, behavior, x, y, z, power, createFire, explosionSourceType, particle, emitterParticle, soundEvent);
    }
}
