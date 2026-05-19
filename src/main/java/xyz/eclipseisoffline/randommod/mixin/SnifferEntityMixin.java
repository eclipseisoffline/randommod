package xyz.eclipseisoffline.randommod.mixin;

import java.util.function.BiConsumer;
import javax.crypto.Mac;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.randommod.JukeboxListener;
import xyz.eclipseisoffline.randommod.SnifferJukeboxEventListener;

@Mixin(SnifferEntity.class)
public abstract class SnifferEntityMixin extends AnimalEntity implements JukeboxListener {

    @Shadow protected abstract SnifferEntity setState(SnifferEntity.State state);

    @Unique
    private EntityGameEventHandler<SnifferJukeboxEventListener> jukeboxEventHandler;
    @Unique
    private boolean isDancing = false;

    protected SnifferEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void initEventHandler(EntityType<? extends AnimalEntity> entityType, World world, CallbackInfo callbackInfo) {
        jukeboxEventHandler = new EntityGameEventHandler<>(new SnifferJukeboxEventListener(
                new EntityPositionSource(this, getStandingEyeHeight()),
                GameEvent.JUKEBOX_PLAY.value().notificationRadius(), this));
    }

    @Inject(method = "startState", at = @At("HEAD"), cancellable = true)
    public void cancelStateChangeWhenDancing(SnifferEntity.State state,
            CallbackInfoReturnable<SnifferEntity> callbackInfoReturnable) {
        if (isDancing) {
            callbackInfoReturnable.setReturnValue((SnifferEntity) (Object) this);
        }
    }

    @Inject(method = "canTryToDig", at = @At("TAIL"), cancellable = true)
    public void checkDancing(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (isDancing) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method = "canDig", at = @At("TAIL"), cancellable = true)
    public void checkDancingForDig(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (isDancing) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Override
    public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
        if (getEntityWorld() instanceof ServerWorld serverWorld) {
            callback.accept(jukeboxEventHandler, serverWorld);
        }
    }

    @Override
    public void randommod$updateJukeboxPos(BlockPos jukeboxPos, boolean playing) {
        if (playing && !isDancing) {
            setState(SnifferEntity.State.FEELING_HAPPY);
            playSound(SoundEvents.ENTITY_SNIFFER_HAPPY, 1.0F, 1.0F);
        }
        isDancing = playing;
    }
}
