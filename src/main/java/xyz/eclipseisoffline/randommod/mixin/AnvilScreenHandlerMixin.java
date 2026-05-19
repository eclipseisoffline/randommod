package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilMenu.class)
public abstract class AnvilScreenHandlerMixin extends ItemCombinerMenu {

    @Unique
    private static final int LEVEL_LIMIT = 1000;

    @Shadow
    @Final
    private DataSlot cost;

    public AnvilScreenHandlerMixin(@Nullable MenuType<?> type, int syncId, Inventory playerInventory, ContainerLevelAccess context, ItemCombinerMenuSlotDefinition forgingSlotsManager) {
        super(type, syncId, playerInventory, context, forgingSlotsManager);
    }

    @Redirect(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;set(I)V", ordinal = 6))
    public void cancelSetLevelCost(DataSlot instance, int i) {
    }

    @Redirect(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;get()I", ordinal = 1))
    public int modifyLevelCostLimit(DataSlot instance) {
        int levelCost = instance.get();
        if (levelCost >= 40 && levelCost < LEVEL_LIMIT) {
            levelCost = 39;
        }
        return levelCost;
    }

    @Override
    public void slotsChanged(Container inventory) {
        super.slotsChanged(inventory);
        if (player instanceof ServerPlayer) {
            if (cost.get() >= LEVEL_LIMIT) {
                ((ServerPlayer) player).connection.send(
                        new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
            } else if (cost.get() >= 40) {
                Abilities.Packed packedAbilities = player.getAbilities().pack();
                Abilities tempAbilities = new Abilities();
                tempAbilities.apply(packedAbilities);
                tempAbilities.instabuild = true;
                ((ServerPlayer) player).connection.send(
                        new ClientboundPlayerAbilitiesPacket(tempAbilities));
            } else {
                ((ServerPlayer) player).connection.send(
                        new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
            }
        }
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (player instanceof ServerPlayer) {
            ((ServerPlayer) player).connection.send(
                    new ClientboundPlayerAbilitiesPacket(player.getAbilities()));
        }
    }
}
