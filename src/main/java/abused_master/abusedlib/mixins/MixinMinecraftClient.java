package abused_master.abusedlib.mixins;

import abused_master.abusedlib.eventhandler.events.PlayerEvents;
import abused_master.abusedlib.eventhandler.EventRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Shadow
    int attackCooldown;

    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void doAttack(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        PlayerEvents.PlayerLeftClick playerLeftClick = new PlayerEvents.PlayerLeftClick(client.player, client.player.getActiveHand());

        if (attackCooldown <= 0) {
            if (client.hitResult != null) {
                if (client.hitResult.type == HitResult.Type.NONE) {
                    EventRegistry.runEvent(playerLeftClick);
                    if (playerLeftClick.isCanceled()) {
                        ci.cancel();
                    }
                }
            }
        }
    }
}
