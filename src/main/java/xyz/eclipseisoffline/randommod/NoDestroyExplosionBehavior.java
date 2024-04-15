package xyz.eclipseisoffline.randommod;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.Explosion;

public class NoDestroyExplosionBehavior extends EntityExplosionBehavior {

    public NoDestroyExplosionBehavior(Entity entity) {
        super(entity);
    }

    @Override
    public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos,
            BlockState state, float power) {
        return false;
    }
}
