package de.totodev.spellbooktweaks.mixin;

import de.totodev.spellbooktweaks.IReserveManaData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.server.level.ServerPlayer;
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
    public void regenPlayerMana(ServerPlayer serverPlayer, MagicData magicData) {
        var reserveManaData = (IReserveManaData) magicData;

        int mana = magicData.getMana();
        int maxMana = (int) serverPlayer.getAttributeValue(MAX_MANA.get());
        float manaRegen = (float) serverPlayer.getAttributeValue(MANA_REGEN.get());

        int reserveMana = reserveManaData.spellbookTweaks$getReserveMana();
        int maxReserveMana = (int) serverPlayer.getAttributeValue(MAX_RESERVE_MANA.get());
        float reserveManaRegen = (float) serverPlayer.getAttributeValue(RESERVE_MANA_REGEN.get());

        // Regen reserve
        if (reserveMana < maxReserveMana) {
            if (reserveMana + reserveManaRegen < maxReserveMana) {
                reserveManaData.spellbookTweaks$addReserveMana(reserveManaRegen);
            } else {
                reserveManaData.spellbookTweaks$setReserveMana(maxReserveMana);
            }
            reserveMana = reserveManaData.spellbookTweaks$getReserveMana();
        }

        // Transfer
        if (mana < maxMana && reserveMana > manaRegen) {
            if (mana + manaRegen < maxMana) {
                magicData.addMana(manaRegen);
                reserveManaData.spellbookTweaks$addReserveMana(-manaRegen);
            } else {
                magicData.setMana(maxMana);
                reserveManaData.spellbookTweaks$addReserveMana(-manaRegen);
            }
        }
    }
}
