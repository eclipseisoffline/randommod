package xyz.eclipseisoffline.randommod;

import java.util.Random;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class RandomMod implements ModInitializer {

    private static final String BODY_PART_KEY = "BodyPartName";
    private static final String[] BODY_PARTS = new String[]{
            "Esophagus", "Kidney", "Arms", "Brain", "Ears", "Eyes", "Eyeballs",
            "Stomach", "Nose", "Feet", "Toes", "Thumb", "Fingers", "Intestines", "Right leg",
            "Left leg", "Liver", "Mouth", "Pancreas", "Shoulder", "Right shoulder", "Left shoulder",
            "Skin", "Muscles", "Teeth", "Tongue", "Lips", "Pharynx", "Duodenum", "Jejunum", "Ileum",
            "Cecum", "Appendix", "Pharynx", "Larynx", "Hands", "Lungs", "Trachea", "Bronchi",
            "Arteries", "Veins", "Capillaries", "Bones", "Spleen", "Interstitium", "Thymus",
            "Lymph nodes", "Lymphatic vessels", "Cerebrum", "Spinal cord", "Nerves", "Ganglia",
            "Taste buds", "Mammary glands", "Femur", "Trichocytes", "Keratinocytes", "Neurons",
            "Schwann cells", "Satellite glial cells", "Chromaffin cells", "Glumus cells",
            "Melanocytes", "Nevus cells", "Merkel cells", "Astrocytes", "Ependymocytes",
            "Oligodendrocytes", "Oligodendrocyte progrenitor cells", "Pnealocytes",
            "Myosattelite cells", "Tendon cells", "Cardiac muscle cells", "Fibrocytes",
            "Endothelial cells", "Mesangial cells", "Juxtaglomerular cells", "Macula densa cells",
            "Telocytes", "Podocytes", "Kidney proximal tubule brush border cells",
            "Lymphoids", "Myeloids", "Endothelial progenitor cells",
            "Endothelial colony forming cells",
            "Endothelial stem cells", "Angioblasts", "Pericytes", "Mural cells",
            "Mesothelial cells",
            "Pneumocytes", "Club cells", "Goblet cells", "Pulmonary neuroendocrine cells",
            "Enteroendocrine cells", "G cells", "Delta cells", "Enterochromaffin-like cells",
            "Gastric chief cells", "Parietal cells", "Foveolar cells", "S cells",
            "Cholecystokinins", "Paneth cells", "Tuft cells", "Microfold cells", "Hepatocytes",
            "Hepatic stellate cells", "Centroacinar cells", "Pancreatic stellate cells",
            "Alpha cells", "Beta cells", "Epsilon cells", "Follicular cells",
            "Parafollicular cells",
            "Parathyroid chief cells", "Oxyphil cells", "Urothelial cells"
    };

    @Override
    public void onInitialize() {
        System.out.println("Registered " + BODY_PARTS.length + " body parts");
        Random random = new Random();

        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
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
            );
            dispatcher.register(
                    CommandManager.literal("demo")
                            .requires(source -> source.hasPermissionLevel(2))
                            .then(CommandManager.argument("player", EntityArgumentType.player())
                                    .then(CommandManager.literal("end")
                                            .executes(context -> {
                                                ServerPlayerEntity player = EntityArgumentType.getPlayer(
                                                        context, "player");
                                                player.networkHandler.sendPacket(
                                                        new GameStateChangeS2CPacket(
                                                                GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN,
                                                                104));
                                                context.getSource().sendFeedback(() -> Text.literal(
                                                                "Showing demo end screen to ")
                                                        .append(player.getDisplayName()), true);
                                                return 0;
                                            })
                                    )
                                    .executes(context -> {
                                        ServerPlayerEntity player = EntityArgumentType.getPlayer(
                                                context, "player");
                                        player.networkHandler.sendPacket(
                                                new GameStateChangeS2CPacket(
                                                        GameStateChangeS2CPacket.DEMO_MESSAGE_SHOWN,
                                                        0));
                                        context.getSource().sendFeedback(
                                                () -> Text.literal("Showing demo screen to ")
                                                        .append(player.getDisplayName()), true);
                                        return 0;
                                    })
                            )
            );
        }));

        /* disabled to increase performance
        ServerTickEvents.END_SERVER_TICK.register((server -> {
            List<ServerPlayerEntity> playersOnline = server.getPlayerManager().getPlayerList();

            for (ServerPlayerEntity player : playersOnline) {
                Map<Item, List<ItemStack>> stacks = new HashMap<>();
                Map<Item, List<Integer>> slots = new HashMap<>();
                Map<Item, String> names = new HashMap<>();
                for (int slot = 0; slot < player.getInventory().size(); slot++) {
                    ItemStack stack = player.getInventory().getStack(slot);
                    if (!stack.isFood()) {
                        continue;
                    }

                    if (stacks.containsKey(stack.getItem())) {
                        stacks.get(stack.getItem()).add(stack);
                        slots.get(stack.getItem()).add(slot);
                    } else {
                        stacks.put(stack.getItem(), new ArrayList<>(List.of(stack)));
                        slots.put(stack.getItem(), new ArrayList<>(List.of(slot)));
                        names.put(stack.getItem(), getRandomFoodName(server, random));
                    }

                    if (stack.hasCustomName()
                            && stack.getOrCreateNbt().getString(BODY_PART_KEY).equals(stack.getName().getString())) {
                        names.put(stack.getItem(), stack.getName().getString());
                    }
                }

                List<Integer> slotsToReorder = new ArrayList<>();
                for (Entry<Item, List<ItemStack>> stacksToName : stacks.entrySet()) {
                    String name = names.get(stacksToName.getKey());
                    for (int i = 0; i < stacksToName.getValue().size(); i++) {
                        ItemStack stack = stacksToName.getValue().get(i);
                        int slot = slots.get(stacksToName.getKey()).get(i);
                        if (!stack.hasCustomName()
                                || (stack.getName().getString().equals(stack.getOrCreateNbt().getString(BODY_PART_KEY))
                                && !stack.getName().getString().equals(name))) {
                            stack.setCustomName(Text.of(name));
                            stack.getOrCreateNbt().putString(BODY_PART_KEY, name);
                            slotsToReorder.add(slot);
                        }
                    }
                }

                for (int slot : slotsToReorder) {
                    ItemStack stack = player.getInventory().getStack(slot);
                    player.getInventory().removeStack(slot);
                    player.getInventory().insertStack(stack);
                }
            }
        }));
        */
    }

    private String getRandomFoodName(MinecraftServer server, Random random) {
        String randomName = getRandomPlayerName(server, random) + "'s";
        String randomBodyPart = BODY_PARTS[random.nextInt(BODY_PARTS.length)];
        return randomName + " " + randomBodyPart;
    }

    private String getRandomPlayerName(MinecraftServer server, Random random) {
        if (server.getPlayerManager().isWhitelistEnabled()) {
            String[] whitelisted = server.getPlayerManager().getWhitelistedNames();
            return whitelisted[random.nextInt(whitelisted.length)];
        }

        String[] online = server.getPlayerNames();
        return online[random.nextInt(online.length)];
    }
}
