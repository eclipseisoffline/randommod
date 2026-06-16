package xyz.eclipseisoffline.randommod.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.EnderEyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnderEyeItem.class)
public abstract class EnderEyeItemMixin extends Item  {

    public EnderEyeItemMixin(Properties properties) {
        super(properties);
    }

    @WrapOperation(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
    public boolean neverPortalFrame(BlockState instance, Object o, Operation<Boolean> original) {
        return false;
    }
}
