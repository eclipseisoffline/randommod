package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin extends Block {

    public FarmlandBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onLandedUpon", at = @At("HEAD"), cancellable = true)
    public void cancelSetToDirt(World world, BlockState state, BlockPos pos, Entity entity,
            float fallDistance, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}
