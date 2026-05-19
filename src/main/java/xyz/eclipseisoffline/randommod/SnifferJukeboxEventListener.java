package xyz.eclipseisoffline.randommod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

public class SnifferJukeboxEventListener implements GameEventListener {
    private final PositionSource positionSource;
    private final int range;
    private final JukeboxListener jukeboxListener;

    public SnifferJukeboxEventListener(PositionSource positionSource, int range, JukeboxListener jukeboxListener) {
        this.positionSource = positionSource;
        this.range = range;
        this.jukeboxListener = jukeboxListener;
    }

    @Override
    public PositionSource getListenerSource() {
        return this.positionSource;
    }

    @Override
    public int getListenerRadius() {
        return this.range;
    }

    @Override
    public boolean handleGameEvent(ServerLevel world, Holder<GameEvent> event, GameEvent.Context emitter, Vec3 emitterPos) {
        if (event.is(GameEvent.JUKEBOX_PLAY)) {
            jukeboxListener.randommod$updateJukeboxPos(BlockPos.containing(emitterPos), true);
            return true;
        }
        if (event.is(GameEvent.JUKEBOX_STOP_PLAY)) {
            jukeboxListener.randommod$updateJukeboxPos(BlockPos.containing(emitterPos), false);
            return true;
        }
        return false;
    }
}
