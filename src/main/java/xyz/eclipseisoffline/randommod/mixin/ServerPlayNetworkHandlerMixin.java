package xyz.eclipseisoffline.randommod.mixin;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.PlayerAssociatedNetworkHandler;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandler implements
        ServerPlayPacketListener, PlayerAssociatedNetworkHandler, TickablePacketListener {

    @Shadow
    public ServerPlayerEntity player;

    public ServerPlayNetworkHandlerMixin(MinecraftServer server,
            ClientConnection connection,
            ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @Redirect(method = "method_44900", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/message/SignedMessage;getContent()Lnet/minecraft/text/Text;"))
    public Text returnScrambledContent(SignedMessage instance) {
        Text chat = instance.getContent();
        if (player.hasStatusEffect(StatusEffects.NAUSEA)) {
            String message = chat.getString();
            int words = message.split(" ").length;

            char[] messageChars = message.toCharArray();
            int messageLength = messageChars.length;

            for (int i = 0; i < words * 0.4; i++) {
                int firstPos = player.getWorld().random.nextInt(messageLength);
                int secondPos = player.getWorld().random.nextInt(messageLength);
                if (firstPos == secondPos) {
                    continue;
                }
                char firstChar = messageChars[firstPos];
                char secondChar = messageChars[secondPos];
                if (firstChar == ' ' || secondChar == ' ') {
                    continue;
                }

                messageChars[firstPos] = secondChar;
                messageChars[secondPos] = firstChar;
            }

            StringBuilder newMessage = new StringBuilder();
            for (char c : messageChars) {
                if (player.getWorld().random.nextInt(10) < 2) {
                    newMessage.append(String.valueOf(c).toUpperCase());
                } else {
                    newMessage.append(c);
                }
            }

            chat = Text.of(newMessage.toString());
        }
        return chat;
    }
}
