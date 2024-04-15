package xyz.eclipseisoffline.randommod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class RandomMod implements ModInitializer {

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment)-> dispatcher.register(
                CommandManager.literal("head").executes(context -> {
                    if (!context.getSource().isExecutedByPlayer()) {
                        return 1;
                    }

                    ServerPlayerEntity player = context.getSource().getPlayer();
                    assert player != null;
                    ItemStack head = player.getEquippedStack(EquipmentSlot.HEAD);
                    if (!head.isEmpty()) {
                        context.getSource().sendError(Text.of("Empty your head slot first!"));
                        return 1;
                    }

                    if (player.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
                        context.getSource().sendError(Text.of("Select an item by holding it!"));
                        return 1;
                    }

                    player.equipStack(EquipmentSlot.HEAD,
                            player.getStackInHand(Hand.MAIN_HAND).copyWithCount(1));
                    if (!player.getAbilities().creativeMode) {
                        player.getStackInHand(Hand.MAIN_HAND).decrement(1);
                    }
                    return 0;
                })
        ));
    }
}
