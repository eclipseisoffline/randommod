package xyz.eclipseisoffline.randommod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class RandomMod implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> dispatcher.register(
                        Commands.literal("head").executes(context -> {
                            if (!context.getSource().isPlayer()) {
                                return 1;
                            }

                            ServerPlayer player = context.getSource().getPlayer();
                            assert player != null;
                            ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
                            if (!head.isEmpty()) {
                                context.getSource()
                                        .sendFailure(Component.nullToEmpty("Empty your head slot first!"));
                                return 1;
                            }

                            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                                context.getSource()
                                        .sendFailure(Component.nullToEmpty("Select an item by holding it!"));
                                return 1;
                            }

                            player.setItemSlot(EquipmentSlot.HEAD,
                                    player.getItemInHand(InteractionHand.MAIN_HAND).copyWithCount(1));
                            if (!player.getAbilities().instabuild) {
                                player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                            }
                            return 0;
                        })
                ));
    }
}
