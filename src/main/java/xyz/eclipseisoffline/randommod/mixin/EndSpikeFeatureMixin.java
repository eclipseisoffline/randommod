package xyz.eclipseisoffline.randommod.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpikeFeature.class)
public abstract class EndSpikeFeatureMixin extends Feature<SpikeConfiguration> {

    public EndSpikeFeatureMixin(Codec<SpikeConfiguration> configCodec) {
        super(configCodec);
    }

    @WrapOperation(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/SpikeFeature;setBlock(Lnet/minecraft/world/level/LevelWriter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", ordinal = 0))
    public void obsidianAirCheck(SpikeFeature instance, LevelWriter levelWriter, BlockPos blockPos, BlockState state, Operation<Void> original,
                                 @Local(argsOnly = true, name = "context") FeaturePlaceContext<SpikeConfiguration> context) {
        BlockState currentState = context.level().getBlockState(blockPos);
        if (currentState.isAir()) {
            original.call(instance, levelWriter, blockPos, state);
        }
    }

    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/feature/SpikeFeature;setBlock(Lnet/minecraft/world/level/LevelWriter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", ordinal = 1))
    public void cancelSetToAir(SpikeFeature instance, LevelWriter modifiableWorld,
            BlockPos blockPos, BlockState blockState) {
    }
}
