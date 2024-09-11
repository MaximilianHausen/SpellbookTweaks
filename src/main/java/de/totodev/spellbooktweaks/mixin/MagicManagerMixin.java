package de.totodev.spellbooktweaks.mixin;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MagicManager.class)
public abstract class MagicManagerMixin {
    /**
     * @author SpellbookTweaks
     * @reason To correctly modify the increment value, a lot of other local variables are needed, which (afaik) can't capture with @ModifyVariable.
     */
    @Overwrite(remap = false)
    public boolean regenPlayerMana(ServerPlayer serverPlayer, MagicData playerMagicData) {
        int playerMaxMana = (int) serverPlayer.getAttributeValue(AttributeRegistry.MAX_MANA.get());
        var mana = playerMagicData.getMana();
        if (mana != playerMaxMana) {
            float playerManaRegenMultiplier = (float) serverPlayer.getAttributeValue(AttributeRegistry.MANA_REGEN.get());
            var increment = (1 + (playerMaxMana - 100) * 0.005f) * playerManaRegenMultiplier;
            playerMagicData.setMana(Mth.clamp(playerMagicData.getMana() + increment, 0, playerMaxMana));
            return true;
        } else {
            return false;
        }
    }
}
