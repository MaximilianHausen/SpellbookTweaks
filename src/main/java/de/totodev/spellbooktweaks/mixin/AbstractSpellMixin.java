package de.totodev.spellbooktweaks.mixin;

import io.redspace.ironsspellbooks.api.item.curios.AffinityData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

import static de.totodev.spellbooktweaks.AttributeRegistry.*;

/**
 * Checks spell proficiency before casting a spell
 */
@Mixin(AbstractSpell.class)
public abstract class AbstractSpellMixin {
    @Shadow(remap = false)
    public abstract SpellRarity getRarity(int level);

    @Shadow(remap = false)
    public abstract MutableComponent getDisplayName(Player player);

    @Shadow(remap = false)
    public abstract SchoolType getSchoolType();

    @Redirect(
            method = "getLevelFor",
            at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"),
            remap = false
    )
    private int getLevelForIgnoreAffinity(List self) {
        return 0;
    }

    @Inject(
            method = "canBeCastedBy(ILio/redspace/ironsspellbooks/api/spells/CastSource;Lio/redspace/ironsspellbooks/api/magic/MagicData;Lnet/minecraft/world/entity/player/Player;)Lio/redspace/ironsspellbooks/api/spells/CastResult;",
            at = @At(value = "HEAD"),
            cancellable = true,
            remap = false
    )
    public void canBeCastedBy(int spellLevel, CastSource castSource, MagicData playerMagicData, Player player, CallbackInfoReturnable<CastResult> cir) {
        String schoolName = getSchoolType().getId().getPath();
        double exactProficiency = spellbookTweaks$getSchoolProficiency(player, schoolName);
        int proficiency = (int) exactProficiency;

        // Skip proficiency check when wearing a relevant affinity ring
        if (!CuriosApi.getCuriosHelper().findCurios(player, itemStack -> AffinityData.hasAffinityData(itemStack) && AffinityData.getAffinityData(itemStack).getSpell().equals(this)).isEmpty()) {
            return;
        }

        SpellRarity spellRarity = getRarity(spellLevel);
        if (proficiency < spellRarity.getValue()) {
            // Format manually to remove the trailing 0 on whole numbers
            String prettyProficiency = exactProficiency == proficiency ? String.valueOf(proficiency) : String.valueOf(exactProficiency);
            cir.setReturnValue(new CastResult(
                    CastResult.Type.FAILURE,
                    Component.translatable("ui.spellbooktweaks.cast_error_rarity", schoolName.equals("eldritch") ? "spell" : schoolName, this.getDisplayName(player), prettyProficiency, spellRarity.getValue()).withStyle(ChatFormatting.RED)
            ));
        }
    }

    @Unique
    private double spellbookTweaks$getSchoolProficiency(Player player, String regPath) {
        double proficiency = switch (regPath) {
            case "fire" -> player.getAttributeValue(FIRE_SCHOOL_PROFICIENCY.get());
            case "ice" -> player.getAttributeValue(ICE_SCHOOL_PROFICIENCY.get());
            case "lightning" -> player.getAttributeValue(LIGHTNING_SCHOOL_PROFICIENCY.get());
            case "holy" -> player.getAttributeValue(HOLY_SCHOOL_PROFICIENCY.get());
            case "ender" -> player.getAttributeValue(ENDER_SCHOOL_PROFICIENCY.get());
            case "blood" -> player.getAttributeValue(BLOOD_SCHOOL_PROFICIENCY.get());
            case "evocation" -> player.getAttributeValue(EVOCATION_SCHOOL_PROFICIENCY.get());
            case "nature" -> player.getAttributeValue(NATURE_SCHOOL_PROFICIENCY.get());
            case "eldritch" -> {
                double maxProficiency = 0;
                maxProficiency = Math.max(maxProficiency, player.getAttributeValue(FIRE_SCHOOL_PROFICIENCY.get()));
                maxProficiency = Math.max(maxProficiency, player.getAttributeValue(ICE_SCHOOL_PROFICIENCY.get()));
                maxProficiency = Math.max(maxProficiency, player.getAttributeValue(LIGHTNING_SCHOOL_PROFICIENCY.get()));
                maxProficiency = Math.max(maxProficiency, player.getAttributeValue(HOLY_SCHOOL_PROFICIENCY.get()));
                maxProficiency = Math.max(maxProficiency, player.getAttributeValue(ENDER_SCHOOL_PROFICIENCY.get()));
                maxProficiency = Math.max(maxProficiency, player.getAttributeValue(BLOOD_SCHOOL_PROFICIENCY.get()));
                maxProficiency = Math.max(maxProficiency, player.getAttributeValue(EVOCATION_SCHOOL_PROFICIENCY.get()));
                maxProficiency = Math.max(maxProficiency, player.getAttributeValue(NATURE_SCHOOL_PROFICIENCY.get()));
                yield maxProficiency;
            }
            default -> 0;
        };

        proficiency += player.getAttributeValue(SPELL_PROFICIENCY.get());

        return proficiency;
    }
}
