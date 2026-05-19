package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World.ExplosionSourceType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.entity.boss.dragon.EnderDragonSpawnState$3")
public abstract class EnderDragonSpawnStateSummoningPillarsMixin {

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)V"))
    public void cancelExplosion(ServerWorld instance, Entity entity, double x, double y, double z,
                                float power, ExplosionSourceType explosionSourceType) {}
}
