package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin extends Block {

    public FarmlandBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    public void cancelSetToDirt(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}
