package xyz.eclipseisoffline.randommod;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;

public class NoDestroyExplosionBehavior extends EntityBasedExplosionDamageCalculator {

    public NoDestroyExplosionBehavior(Entity entity) {
        super(entity);
    }

    @Override
    public boolean shouldBlockExplode(Explosion explosion, BlockGetter world, BlockPos pos,
            BlockState state, float power) {
        return false;
    }
}
