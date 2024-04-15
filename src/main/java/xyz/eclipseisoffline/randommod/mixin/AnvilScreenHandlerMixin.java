package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    @Unique
    private static final int LEVEL_LIMIT = 1000;

    @Shadow @Final private Property levelCost;

    public AnvilScreenHandlerMixin(
            @Nullable ScreenHandlerType<?> type,
            int syncId, PlayerInventory playerInventory,
            ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/Property;set(I)V", ordinal = 6))
    public void cancelSetLevelCost(Property instance, int i) {}

    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/Property;get()I", ordinal = 1))
    public int modifyLevelCostLimit(Property instance) {
        int levelCost = instance.get();
        if (levelCost >= 40 && levelCost < LEVEL_LIMIT) {
            levelCost = 39;
        }
        return levelCost;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (player instanceof ServerPlayerEntity) {
            if (levelCost.get() >= LEVEL_LIMIT) {
                ((ServerPlayerEntity) player).networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(player.getAbilities()));
            } else if (levelCost.get() >= 40) {
                NbtCompound abilityCompound = new NbtCompound();
                player.getAbilities().writeNbt(abilityCompound);
                PlayerAbilities tempAbilities = new PlayerAbilities();
                tempAbilities.readNbt(abilityCompound);
                tempAbilities.creativeMode = true;
                ((ServerPlayerEntity) player).networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(tempAbilities));
            } else {
                ((ServerPlayerEntity) player).networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(player.getAbilities()));
            }
        }
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (player instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) player).networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(player.getAbilities()));
        }
    }
}
