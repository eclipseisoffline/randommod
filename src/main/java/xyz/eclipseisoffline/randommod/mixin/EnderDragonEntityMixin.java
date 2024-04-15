package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends MobEntity implements Monster {

    protected EnderDragonEntityMixin(EntityType<? extends MobEntity> entityType,
            World world) {
        super(entityType, world);
    }

    @Inject(method = "destroyBlocks", at = @At("HEAD"), cancellable = true)
    public void cancelDestroyBlocks(Box box,
            CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(true);
    }
}
