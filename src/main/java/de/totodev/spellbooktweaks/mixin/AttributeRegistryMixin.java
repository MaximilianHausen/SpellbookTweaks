package de.totodev.spellbooktweaks.mixin;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.function.Supplier;

/**
 * Changes default attributes
 */
@Mixin(AttributeRegistry.class)
public class AttributeRegistryMixin {
    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraftforge/registries/DeferredRegister;register(Ljava/lang/String;Ljava/util/function/Supplier;)Lnet/minecraftforge/registries/RegistryObject;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/api/registry/AttributeRegistry;MAX_MANA:Lnet/minecraftforge/registries/RegistryObject;", shift = At.Shift.BY, by = -1)),
            index = 1,
            remap = false
    )
    private static Supplier<Attribute> maxMana(Supplier<RangedAttribute> sup) {
        return () -> new RangedAttribute("attribute.irons_spellbooks.max_mana", 60.0D, 0.0D, 100.0D).setSyncable(true);
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraftforge/registries/DeferredRegister;register(Ljava/lang/String;Ljava/util/function/Supplier;)Lnet/minecraftforge/registries/RegistryObject;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/api/registry/AttributeRegistry;MANA_REGEN:Lnet/minecraftforge/registries/RegistryObject;", shift = At.Shift.BY, by = -1)),
            index = 1,
            remap = false
    )
    private static Supplier<Attribute> manaRegen(Supplier<RangedAttribute> sup) {
        return () -> new RangedAttribute("attribute.irons_spellbooks.mana_regen", 3.0D, 0.0D, 10.0D).setSyncable(true);
    }
}
