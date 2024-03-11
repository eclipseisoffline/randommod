package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodComponents.class)
public class FoodComponentsMixin {

    @Shadow
    @Final
    @Mutable
    public static FoodComponent GLOW_BERRIES;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyGlowBerries(CallbackInfo callbackInfo) {
        GLOW_BERRIES = new FoodComponent.Builder().hunger(2)
                .saturationModifier(0.1f).statusEffect(
                        new StatusEffectInstance(StatusEffects.GLOWING, 300, 0, false, false, true),
                        1.0F).build();
    }
}
