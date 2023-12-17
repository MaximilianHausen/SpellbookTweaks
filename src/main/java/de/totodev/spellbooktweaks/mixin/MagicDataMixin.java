package de.totodev.spellbooktweaks.mixin;

import de.totodev.spellbooktweaks.ChangeReserveManaEvent;
import de.totodev.spellbooktweaks.IReserveManaData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(
            method = "saveNBTData",
            at = @At("HEAD"),
            remap = false
    )
    public void saveNBTData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("reserveMana", (int) this.spellbookTweaks$reserveMana);
    }

    @Inject(
            method = "loadNBTData",
            at = @At("HEAD"),
            remap = false
    )
    public void loadNBTData(CompoundTag compound, CallbackInfo ci) {
        this.spellbookTweaks$reserveMana = (float) compound.getInt("reserveMana");
    }
}
