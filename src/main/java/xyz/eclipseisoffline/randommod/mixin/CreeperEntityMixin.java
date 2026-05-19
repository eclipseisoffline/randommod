package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.World.ExplosionSourceType;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.eclipseisoffline.randommod.NoDestroyExplosionBehavior;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType,
                                 World world) {
        super(entityType, world);
    }

    @Redirect(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)V"))
    public void setExplosionBehavior(ServerWorld instance, Entity entity, double x, double y, double z, float power, ExplosionSourceType explosionSourceType) {
        instance.createExplosion(this, Explosion.createDamageSource(instance, entity), new NoDestroyExplosionBehavior(entity), this.getX(), this.getY(), this.getZ(),
                power, false, explosionSourceType);
    }
}
