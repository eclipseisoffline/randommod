package xyz.eclipseisoffline.randommod;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;

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
    public PositionSource getPositionSource() {
        return this.positionSource;
    }

    @Override
    public int getRange() {
        return this.range;
    }

    @Override
    public boolean listen(ServerWorld world, RegistryEntry<GameEvent> event, GameEvent.Emitter emitter, Vec3d emitterPos) {
        if (event.matches(GameEvent.JUKEBOX_PLAY)) {
            jukeboxListener.randommod$updateJukeboxPos(BlockPos.ofFloored(emitterPos), true);
            return true;
        }
        if (event.matches(GameEvent.JUKEBOX_STOP_PLAY)) {
            jukeboxListener.randommod$updateJukeboxPos(BlockPos.ofFloored(emitterPos), false);
            return true;
        }
        return false;
    }
}
