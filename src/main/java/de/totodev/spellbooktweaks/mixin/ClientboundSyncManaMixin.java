package de.totodev.spellbooktweaks.mixin;

import de.totodev.spellbooktweaks.ClientReserveManaData;
import de.totodev.spellbooktweaks.IReserveManaData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.network.ClientboundSyncMana;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundSyncMana.class)
public class ClientboundSyncManaMixin {
    @Unique
    private int spellbookTweaks$playerReserveMana = 0;
    @Shadow(remap = false)
    private MagicData playerMagicData;

    @Inject(
            method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V",
            at = @At(value = "RETURN"),
            remap = false
    )
    void constructor(FriendlyByteBuf buf, CallbackInfo ci) {
        spellbookTweaks$playerReserveMana = buf.readInt();
    }

    @Inject(
            method = "toBytes",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/network/FriendlyByteBuf;writeInt(I)Lio/netty/buffer/ByteBuf;"),
            remap = false
    )
    public void toBytes(FriendlyByteBuf buf, CallbackInfo ci) {
        buf.writeInt(((IReserveManaData) playerMagicData).spellbookTweaks$getReserveMana());
    }

    @Inject(
            method = "lambda$handle$0",
            at = @At(value = "RETURN"),
            remap = false
    )
    public void handle(CallbackInfo ci) {
        ClientReserveManaData.setReserveMana(spellbookTweaks$playerReserveMana);
    }
}
