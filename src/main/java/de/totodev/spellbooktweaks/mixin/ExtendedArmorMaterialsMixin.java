package de.totodev.spellbooktweaks.mixin;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.ExtendedArmorMaterials;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Map;

/**
 * Changes armor attribute modifiers
 */
@Mixin(ExtendedArmorMaterials.class)
public class ExtendedArmorMaterialsMixin {
    // General armor
    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;TARNISHED:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> tarnished(K k1, V v1, K k2, V v2, K k3, V v3) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 100.0, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MANA_REGEN.get(),
                new AttributeModifier("Mana Regen", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL),
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier("minus damage", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL)
        );
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;WANDERING_MAGICIAN:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> wanderingMagician(K k1, V v1) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.25, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 25.0, AttributeModifier.Operation.ADDITION)
        );
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;PUMPKIN:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> pumpkin(K k1, V v1) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 50.0, AttributeModifier.Operation.ADDITION)
        );
    }

    // Specialized armor
    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;PYROMANCER:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> pyro(K k1, V v1, K k2, V v2, K k3, V v3) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                de.totodev.spellbooktweaks.AttributeRegistry.FIRE_SCHOOL_PROFICIENCY.get(),
                new AttributeModifier("Fire Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 100.0, AttributeModifier.Operation.ADDITION)
        );
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;ARCHEVOKER:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> evocation(K k1, V v1, K k2, V v2, K k3, V v3) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                de.totodev.spellbooktweaks.AttributeRegistry.EVOCATION_SCHOOL_PROFICIENCY.get(),
                new AttributeModifier("Evocation Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 100.0, AttributeModifier.Operation.ADDITION)
        );
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;CULTIST:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> blood(K k1, V v1, K k2, V v2, K k3, V v3) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                de.totodev.spellbooktweaks.AttributeRegistry.BLOOD_SCHOOL_PROFICIENCY.get(),
                new AttributeModifier("Blood Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 100.0, AttributeModifier.Operation.ADDITION)
        );
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;PRIEST:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> holy(K k1, V v1, K k2, V v2, K k3, V v3) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                de.totodev.spellbooktweaks.AttributeRegistry.HOLY_SCHOOL_PROFICIENCY.get(),
                new AttributeModifier("Holy Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 100.0, AttributeModifier.Operation.ADDITION)
        );
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;CRYOMANCER:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> ice(K k1, V v1, K k2, V v2, K k3, V v3) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                de.totodev.spellbooktweaks.AttributeRegistry.ICE_SCHOOL_PROFICIENCY.get(),
                new AttributeModifier("Ice Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 100.0, AttributeModifier.Operation.ADDITION)
        );
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;SHADOWWALKER:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> ender(K k1, V v1, K k2, V v2, K k3, V v3) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                de.totodev.spellbooktweaks.AttributeRegistry.ENDER_SCHOOL_PROFICIENCY.get(),
                new AttributeModifier("Ender Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 100.0, AttributeModifier.Operation.ADDITION)
        );
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;PLAGUED:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> nature(K k1, V v1, K k2, V v2, K k3, V v3) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                de.totodev.spellbooktweaks.AttributeRegistry.NATURE_SCHOOL_PROFICIENCY.get(),
                new AttributeModifier("Nature Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 100.0, AttributeModifier.Operation.ADDITION)
        );
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;ELECTROMANCER:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> lightning(K k1, V v1, K k2, V v2, K k3, V v3) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                de.totodev.spellbooktweaks.AttributeRegistry.LIGHTNING_SCHOOL_PROFICIENCY.get(),
                new AttributeModifier("Lightning Proficiency", 0.5, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 100.0, AttributeModifier.Operation.ADDITION)
        );
    }

    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Ljava/util/Map;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;NETHERITE_BATTLEMAGE:Lio/redspace/ironsspellbooks/item/armor/ExtendedArmorMaterials;", shift = At.Shift.BY, by = -2)),
            remap = false
    )
    @Coerce
    private static <K, V> Map<Attribute, AttributeModifier> battlemage(K k1, V v1, K k2, V v2) {
        return Map.of(
                de.totodev.spellbooktweaks.AttributeRegistry.SPELL_PROFICIENCY.get(),
                new AttributeModifier("Spell Proficiency", 0.75, AttributeModifier.Operation.ADDITION),
                AttributeRegistry.MAX_MANA.get(),
                new AttributeModifier("Max Mana", 100.0, AttributeModifier.Operation.ADDITION)
        );
    }
}
