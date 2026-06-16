package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level.ExplosionInteraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.level.dimension.end.DragonRespawnStage$3")
public abstract class EnderDragonSpawnStateSummoningPillarsMixin {

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;explode(Lnet/minecraft/world/entity/Entity;DDDFLnet/minecraft/world/level/Level$ExplosionInteraction;)V"))
    public void cancelExplosion(ServerLevel instance, Entity entity, double x, double y, double z,
                                float power, ExplosionInteraction explosionSourceType) {}
}
