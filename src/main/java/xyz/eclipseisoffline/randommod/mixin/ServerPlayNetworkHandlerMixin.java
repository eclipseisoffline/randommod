package xyz.eclipseisoffline.randommod.mixin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Redirect(method = "method_44900", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/message/SignedMessage;getContent()Lnet/minecraft/text/Text;"))
    public Text returnScrambledContent(SignedMessage instance) {
        Text chat = instance.getContent();
        if (player.hasStatusEffect(StatusEffects.NAUSEA)) {
            String message = chat.getString();
            List<String> words = new ArrayList<>(List.of(message.split(" ")));
            Collections.reverse(words);
            List<String> newWords = new ArrayList<>();
            for (String word : words) {
                newWords.add(new StringBuilder(word).reverse().toString());
            }
            chat = Text.of(String.join(" ", newWords));
        }
        return chat;
    }
}
