package xyz.eclipseisoffline.randommod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
