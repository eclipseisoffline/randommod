package xyz.eclipseisoffline.randommod.mixin;

import java.util.function.BiConsumer;
import javax.crypto.Mac;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.eclipseisoffline.randommod.JukeboxListener;
import xyz.eclipseisoffline.randommod.SnifferJukeboxEventListener;

@Mixin(Sniffer.class)
public abstract class SnifferEntityMixin extends Animal implements JukeboxListener {

    @Shadow protected abstract Sniffer setState(Sniffer.State state);

    @Unique
    private DynamicGameEventListener<SnifferJukeboxEventListener> jukeboxEventHandler;
    @Unique
    private boolean isDancing = false;

    protected SnifferEntityMixin(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void initEventHandler(EntityType<? extends Animal> entityType, Level world, CallbackInfo callbackInfo) {
        jukeboxEventHandler = new DynamicGameEventListener<>(new SnifferJukeboxEventListener(
                new EntityPositionSource(this, getEyeHeight()),
                GameEvent.JUKEBOX_PLAY.value().notificationRadius(), this));
    }

    @Inject(method = "transitionTo", at = @At("HEAD"), cancellable = true)
    public void cancelStateChangeWhenDancing(Sniffer.State state,
            CallbackInfoReturnable<Sniffer> callbackInfoReturnable) {
        if (isDancing) {
            callbackInfoReturnable.setReturnValue((Sniffer) (Object) this);
        }
    }

    @Inject(method = "canSniff", at = @At("TAIL"), cancellable = true)
    public void checkDancing(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (isDancing) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method = "canDig()Z", at = @At("TAIL"), cancellable = true)
    public void checkDancingForDig(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (isDancing) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> callback) {
        if (level() instanceof ServerLevel serverWorld) {
            callback.accept(jukeboxEventHandler, serverWorld);
        }
    }

    @Override
    public void randommod$updateJukeboxPos(BlockPos jukeboxPos, boolean playing) {
        if (playing && !isDancing) {
            setState(Sniffer.State.FEELING_HAPPY);
            playSound(SoundEvents.SNIFFER_HAPPY, 1.0F, 1.0F);
        }
        isDancing = playing;
    }
}
