package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.eclipseisoffline.randommod.NoDestroyExplosionBehavior;

@Mixin(WitherSkull.class)
public abstract class WitherSkullEntityMixin extends AbstractHurtingProjectile {

    protected WitherSkullEntityMixin(
            EntityType<? extends AbstractHurtingProjectile> entityType,
            Level world) {
        super(entityType, world);
    }

    @Redirect(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)V"))
    public void cancelExplosion(Level instance, Entity entity, double x, double y, double z,
                                float power, boolean createFire, ExplosionInteraction explosionSourceType) {
        instance.explode(entity, Explosion.getDefaultDamageSource(instance, entity),
                new NoDestroyExplosionBehavior(entity), x, y, z,
                power, false, explosionSourceType);
    }
}
