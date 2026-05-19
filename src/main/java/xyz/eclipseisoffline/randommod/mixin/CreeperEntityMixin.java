package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.eclipseisoffline.randommod.NoDestroyExplosionBehavior;

@Mixin(Creeper.class)
public abstract class CreeperEntityMixin extends Monster {

    protected CreeperEntityMixin(EntityType<? extends Monster> entityType,
                                 Level world) {
        super(entityType, world);
    }

    @Redirect(method = "explodeCreeper", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;createExplosion(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)V"))
    public void setExplosionBehavior(ServerLevel instance, Entity entity, double x, double y, double z, float power, ExplosionInteraction explosionSourceType) {
        instance.explode(this, Explosion.getDefaultDamageSource(instance, entity), new NoDestroyExplosionBehavior(entity), this.getX(), this.getY(), this.getZ(),
                power, false, explosionSourceType);
    }
}
