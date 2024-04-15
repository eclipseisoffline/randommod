package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.ProtectionEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProtectionEnchantment.class)
public abstract class ProtectionEnchantmentMixin extends Enchantment {

    public ProtectionEnchantmentMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "canAccept", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public void canAcceptOtherProtectionEnchantments(Enchantment other,
            CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(true);
    }
}
