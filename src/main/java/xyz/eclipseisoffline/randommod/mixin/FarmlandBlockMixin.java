package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin {

    @Shadow
    public static void setToDirt(@Nullable Entity entity, BlockState state, World world,
            BlockPos pos) {}

    /* leather boots requirement, players only
    @Redirect(method = "onLandedUpon", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FarmlandBlock;setToDirt(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
    public void cancelSetToDirtWithLeatherBoots(Entity entity, BlockState state, World world,
            BlockPos pos) {
        if (entity instanceof PlayerEntity player) {
            if (player.getEquippedStack(EquipmentSlot.FEET).getItem().equals(Items.LEATHER_BOOTS)) {
                return;
            }
        }
        setToDirt(entity, state, world, pos);
    }
    */

    /* Always */
    @Inject(method = "onLandedUpon", at = @At("HEAD"), cancellable = true)
    public void cancelSetToDirt(World world, BlockState state, BlockPos pos, Entity entity,
            float fallDistance, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}
