package xyz.eclipseisoffline.randommod.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.EndSpikeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndSpikeFeature.class)
public abstract class EndSpikeFeatureMixin implements Feature {

    @WrapOperation(method = "placeSpike", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/EndSpikeFeature;setBlock(Lnet/minecraft/world/level/LevelWriter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", ordinal = 0))
    public void obsidianAirCheck(EndSpikeFeature instance, LevelWriter levelWriter, BlockPos blockPos, BlockState blockState, Operation<Void> original,
                                 @Local(argsOnly = true, name = "level") ServerLevelAccessor level) {
        BlockState currentState = level.getBlockState(blockPos);
        if (currentState.isAir()) {
            original.call(instance, levelWriter, blockPos, blockState);
        }
    }

    @Redirect(method = "placeSpike", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/EndSpikeFeature;setBlock(Lnet/minecraft/world/level/LevelWriter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", ordinal = 1))
    public void cancelSetToAir(EndSpikeFeature instance, LevelWriter levelWriter, BlockPos blockPos, BlockState blockState) {}
}
