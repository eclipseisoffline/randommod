package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GoatHornItem.class)
public class GoatHornItemMixin {

    @Redirect(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ItemCooldownManager;set(Lnet/minecraft/item/Item;I)V"))
    public void cancelCooldown(ItemCooldownManager instance, Item item, int duration) {
        instance.set(item, 0);
    }
}
