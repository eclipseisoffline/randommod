package xyz.eclipseisoffline.randommod.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTraderSpawner;
import net.minecraft.world.level.CustomSpawner;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WanderingTraderSpawner.class)
public abstract class WanderingTraderManagerMixin implements CustomSpawner {

    // 10 minutes instead of 20
    @Unique
    private static final int MODIFIED_SPAWN_DELAY = 600 * 20;

    @WrapOperation(method = "*", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/npc/wanderingtrader/WanderingTraderSpawner;tickDelay:I", opcode = Opcodes.PUTFIELD))
    public void setSpawnDelay(WanderingTraderSpawner manager, int spawnDelay, Operation<?> original) {
        if (spawnDelay == WanderingTraderSpawner.DEFAULT_SPAWN_DELAY) {
            spawnDelay = MODIFIED_SPAWN_DELAY;
        }
        original.call(manager, spawnDelay);
    }
}
