package de.totodev.spellbooktweaks.mixin;

import de.totodev.spellbooktweaks.AttributeRegistry;
import io.redspace.ironsspellbooks.item.curios.SimpleAttributeCurio;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.function.Supplier;

/**
 * Changes mana-related rings to affect reserve mana
 */
@Mixin(ItemRegistry.class)
public class ItemRegistryMixin {
    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraftforge/registries/DeferredRegister;register(Ljava/lang/String;Ljava/util/function/Supplier;)Lnet/minecraftforge/registries/RegistryObject;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/registries/ItemRegistry;MANA_RING:Lnet/minecraftforge/registries/RegistryObject;", shift = At.Shift.BY, by = -1)),
            index = 1,
            remap = false
    )
    private static Supplier<SimpleAttributeCurio> manaRing(Supplier<SimpleAttributeCurio> sup) {
        return () -> new SimpleAttributeCurio(
                ItemPropertiesHelper.equipment().stacksTo(1),
                AttributeRegistry.MAX_RESERVE_MANA.get(),
                new AttributeModifier("reserve_mana", 100.0, AttributeModifier.Operation.ADDITION)
        );
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraftforge/registries/DeferredRegister;register(Ljava/lang/String;Ljava/util/function/Supplier;)Lnet/minecraftforge/registries/RegistryObject;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/registries/ItemRegistry;SILVER_RING:Lnet/minecraftforge/registries/RegistryObject;", shift = At.Shift.BY, by = -1)),
            index = 1,
            remap = false
    )
    private static Supplier<SimpleAttributeCurio> silverRing(Supplier<SimpleAttributeCurio> sup) {
        return () -> new SimpleAttributeCurio(
                ItemPropertiesHelper.equipment().stacksTo(1),
                AttributeRegistry.MAX_RESERVE_MANA.get(),
                new AttributeModifier("reserve_mana", 50.0, AttributeModifier.Operation.ADDITION)
        );
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraftforge/registries/DeferredRegister;register(Ljava/lang/String;Ljava/util/function/Supplier;)Lnet/minecraftforge/registries/RegistryObject;"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/registries/ItemRegistry;AMETHYST_RESONANCE_NECKLACE:Lnet/minecraftforge/registries/RegistryObject;", shift = At.Shift.BY, by = -1)),
            index = 1,
            remap = false
    )
    private static Supplier<SimpleAttributeCurio> amethystResonanceCharm(Supplier<SimpleAttributeCurio> sup) {
        return () -> new SimpleAttributeCurio(
                ItemPropertiesHelper.equipment().stacksTo(1),
                AttributeRegistry.RESERVE_MANA_REGEN.get(),
                new AttributeModifier("reserve_mana_regen", 0.5, AttributeModifier.Operation.MULTIPLY_BASE)
        );
    }
}
