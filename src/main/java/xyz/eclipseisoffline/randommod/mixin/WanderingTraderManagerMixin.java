package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.world.WanderingTraderManager;
import net.minecraft.world.spawner.SpecialSpawner;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WanderingTraderManager.class)
public abstract class WanderingTraderManagerMixin implements SpecialSpawner {

    // 3 minutes instead of 20
    @Unique
    private static final int MODIFIED_SPAWN_DELAY = 180 * 20;
    @Shadow
    private int spawnDelay;

    @Redirect(method = "*", at = @At(value = "FIELD", target = "Lnet/minecraft/world/WanderingTraderManager;spawnDelay:I", opcode = Opcodes.PUTFIELD))
    public void setSpawnDelay(WanderingTraderManager manager, int spawnDelay) {
        if (spawnDelay == WanderingTraderManager.DEFAULT_SPAWN_DELAY) {
            spawnDelay = MODIFIED_SPAWN_DELAY;
        }
        this.spawnDelay = spawnDelay;
    }
}
