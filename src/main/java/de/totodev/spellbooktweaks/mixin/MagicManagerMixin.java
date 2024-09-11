package de.totodev.spellbooktweaks.mixin;

import de.totodev.spellbooktweaks.IReserveManaData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static de.totodev.spellbooktweaks.AttributeRegistry.MAX_RESERVE_MANA;
import static de.totodev.spellbooktweaks.AttributeRegistry.RESERVE_MANA_REGEN;
import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.MANA_REGEN;
import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.MAX_MANA;

@Mixin(MagicManager.class)
public abstract class MagicManagerMixin {
    /**
     * @author SpellbookTweaks
     * @reason Completely replaces mana regeneration to implement reserve mana
     */
    @Overwrite(remap = false)
    public boolean regenPlayerMana(ServerPlayer serverPlayer, MagicData magicData) {
        var reserveManaData = (IReserveManaData) magicData;

        float mana = magicData.getMana();
        float maxMana = (float) serverPlayer.getAttributeValue(MAX_MANA.get());
        float manaRegen = (float) serverPlayer.getAttributeValue(MANA_REGEN.get());

        float reserveMana = reserveManaData.spellbookTweaks$getReserveMana();
        float maxReserveMana = (float) serverPlayer.getAttributeValue(MAX_RESERVE_MANA.get());
        float reserveManaRegen = (float) serverPlayer.getAttributeValue(RESERVE_MANA_REGEN.get());

        if (mana > maxMana) {
            magicData.setMana(maxMana);
            mana = maxMana;
        }
        if (reserveMana > maxReserveMana) {
            reserveManaData.spellbookTweaks$setReserveMana(maxReserveMana);
            reserveMana = maxReserveMana;
        }

        boolean hasChanged = false;

        // Regen reserve
        if (reserveMana < maxReserveMana) {
            reserveManaData.spellbookTweaks$setReserveMana(Mth.clamp(reserveMana + reserveManaRegen, 0.0F, maxReserveMana));
            reserveMana = reserveManaData.spellbookTweaks$getReserveMana();
            hasChanged = true;
        }

        // Transfer
        if (mana < maxMana && reserveMana > manaRegen) {
            magicData.setMana(Mth.clamp(mana + manaRegen, 0.0F, maxMana));
            hasChanged = true;
        }

        return hasChanged;
    }
}
