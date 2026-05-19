package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragon.class)
public abstract class EnderDragonEntityMixin extends Mob implements Enemy {

    protected EnderDragonEntityMixin(EntityType<? extends Mob> entityType,
            Level world) {
        super(entityType, world);
    }

    @Inject(method = "checkWalls", at = @At("HEAD"), cancellable = true)
    public void cancelDestroyBlocks(ServerLevel world, AABB box, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(true);
    }
}
