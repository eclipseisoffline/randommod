package xyz.eclipseisoffline.randommod.mixin;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements Ownable {

    @Shadow
    public abstract ItemStack getStack();

    @Shadow
    private @Nullable UUID throwerUuid;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void tickInVoid() {
        if (!getEntityWorld().isClient && throwerUuid != null && getStack().hasEnchantments()) {
            assert getEntityWorld().getServer() != null;
            ServerPlayerEntity player = getEntityWorld().getServer().getPlayerManager()
                    .getPlayer(throwerUuid);
            if (player != null) {
                setPosition(player.getPos());
            }
        }
    }
}
