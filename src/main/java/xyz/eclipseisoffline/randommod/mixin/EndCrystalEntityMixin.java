package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.eclipseisoffline.randommod.NoDestroyExplosionBehavior;

@Mixin(EndCrystal.class)
public abstract class EndCrystalEntityMixin extends Entity {

    public EndCrystalEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Redirect(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;createExplosion(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)V"))
    public void cancelExplosionInEnd(ServerLevel instance, Entity entity, DamageSource damageSource, ExplosionDamageCalculator behavior,
                                     double x, double y, double z, float power, boolean fire, ExplosionInteraction explosionSourceType) {
        ExplosionDamageCalculator explosionBehavior = behavior;
        if (instance.getDragonFight() != null) {
            explosionBehavior = new NoDestroyExplosionBehavior(entity);
        }

        instance.explode(entity, damageSource, explosionBehavior, x, y, z, power, fire, explosionSourceType);
    }
}
