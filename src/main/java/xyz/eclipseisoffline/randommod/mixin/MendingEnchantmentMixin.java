package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.MendingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MendingEnchantment.class)
public class MendingEnchantmentMixin extends Enchantment {

    public MendingEnchantmentMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "isTreasure", at = @At("HEAD"), cancellable = true)
    public void isNotTreasure(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(false);
    }
}
