package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.eclipseisoffline.randommod.NoDestroyExplosionBehavior;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityMixin extends Entity {

    public EndCrystalEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;)V"))
    public void cancelExplosionInEnd(ServerWorld instance, Entity entity, DamageSource damageSource, ExplosionBehavior behavior,
                                     double x, double y, double z, float power, boolean fire, ExplosionSourceType explosionSourceType) {
        ExplosionBehavior explosionBehavior = behavior;
        if (instance.getEnderDragonFight() != null) {
            explosionBehavior = new NoDestroyExplosionBehavior(entity);
        }

        instance.createExplosion(entity, damageSource, explosionBehavior, x, y, z, power, fire, explosionSourceType);
    }
}
