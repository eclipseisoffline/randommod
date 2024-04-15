package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.world.WanderingTraderManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WanderingTraderManager.class)
public class WanderingTraderManagerMixin {

    // 3 minute instead of 20
    @Unique
    private static final int MODIFIED_SPAWN_DELAY = 180 * 20;
    @Shadow
    @Final
    public static int DEFAULT_SPAWN_DELAY;
    @Shadow
    private int spawnDelay;

    @Redirect(method = "*", at = @At(value = "FIELD", target = "Lnet/minecraft/world/WanderingTraderManager;spawnDelay:I", opcode = Opcodes.PUTFIELD))
    public void setSpawnDelay(WanderingTraderManager manager, int spawnDelay) {
        if (spawnDelay == DEFAULT_SPAWN_DELAY) {
            spawnDelay = MODIFIED_SPAWN_DELAY;
        }
        this.spawnDelay = spawnDelay;
    }
}
