package de.totodev.spellbooktweaks.mixin;

import io.redspace.ironsspellbooks.item.curios.AffinityRing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AffinityRing.class)
public class AffinityRingMixin {
    @ModifyArg(
            method = "appendHoverText",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"),
            index = 0
    )
    private String appendHoverText(String pKey) {
        return "tooltip.spellbooktweaks.ignore_proficiency";
    }
}
