package xyz.eclipseisoffline.randommod.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeature.Spike;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndSpikeFeature.class)
public abstract class EndSpikeFeatureMixin extends Feature<EndSpikeFeatureConfig> {

    public EndSpikeFeatureMixin(Codec<EndSpikeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Redirect(method = "generateSpike", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/EndSpikeFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", ordinal = 0))
    public void obsidianAirCheck(EndSpikeFeature instance, ModifiableWorld modifiableWorld,
            BlockPos blockPos, BlockState blockState,
            ServerWorldAccess world, Random random, EndSpikeFeatureConfig config, Spike spike) {
        BlockState currentState = world.getBlockState(blockPos);
        if (currentState.isAir()) {
            setBlockState(modifiableWorld, blockPos, blockState);
        }
    }

    @Redirect(method = "generateSpike", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/EndSpikeFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", ordinal = 1))
    public void cancelSetToAir(EndSpikeFeature instance, ModifiableWorld modifiableWorld,
            BlockPos blockPos, BlockState blockState) {
    }
}
