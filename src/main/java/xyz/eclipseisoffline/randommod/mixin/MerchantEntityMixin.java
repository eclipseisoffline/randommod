package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractVillager.class)
public abstract class MerchantEntityMixin extends AgeableMob implements InventoryCarrier, Npc,
        Merchant {

    protected MerchantEntityMixin(EntityType<? extends AgeableMob> entityType,
            Level world) {
        super(entityType, world);
    }

    @Inject(method = "canBeLeashed", at = @At("HEAD"), cancellable = true)
    public void canLeash(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(true);
    }
}
