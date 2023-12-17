package de.totodev.spellbooktweaks.mixin;

import de.totodev.spellbooktweaks.ChangeReserveManaEvent;
import de.totodev.spellbooktweaks.IReserveManaData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MagicData.class)
public class MagicDataMixin implements IReserveManaData {
    @Unique
    private float spellbookTweaks$reserveMana;
    @Shadow(remap = false)
    private ServerPlayer serverPlayer = null;

    @Unique
    public int spellbookTweaks$getReserveMana() {
        return (int) spellbookTweaks$reserveMana;
    }

    @Unique
    public void spellbookTweaks$setReserveMana(float mana) {
        //Event does will not get posted if the server player is null
        ChangeReserveManaEvent e = new ChangeReserveManaEvent(this.serverPlayer, (MagicData) (Object) this, this.spellbookTweaks$reserveMana, mana);
        if (this.serverPlayer == null || !MinecraftForge.EVENT_BUS.post(e)) {
            this.spellbookTweaks$reserveMana = e.getNewMana();
        }
    }

    @Unique
    public void spellbookTweaks$addReserveMana(float mana) {
        spellbookTweaks$setReserveMana(this.spellbookTweaks$reserveMana + mana);
    }
}
