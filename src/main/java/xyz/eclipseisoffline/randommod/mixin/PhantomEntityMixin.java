package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.mob.PhantomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PhantomEntity.class)
public class PhantomEntityMixin {

    @Redirect(method = "setPhantomSize", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"))
    public int cancelClamp(int value, int min, int max) {
        return value;
    }
}
