package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.Npc;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.Merchant;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MerchantEntity.class)
public abstract class MerchantEntityMixin extends PassiveEntity implements InventoryOwner, Npc, Merchant {

    protected MerchantEntityMixin(EntityType<? extends PassiveEntity> entityType,
            World world) {
        super(entityType, world);
    }

    @Inject(method = "canBeLeashedBy", at = @At("HEAD"), cancellable = true)
    public void canLeash(PlayerEntity player, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(true);
    }
}
