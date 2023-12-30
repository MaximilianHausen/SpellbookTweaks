package de.totodev.spellbooktweaks.mixin;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.armor.UpgradeTypes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

/**
 * Changes the mana armor upgrade to improve mana regeneration instead of max mana
 */
@Mixin(UpgradeTypes.class)
public class UpgradeTypesMixin {
    // This would be much easier with @ModifyArgs, but that seems to be broken on Forge 1.17+ (https://github.com/SpongePowered/Mixin/issues/584)
    // Why is the bytecode signature of this constructor so wierd?

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Lio/redspace/ironsspellbooks/item/armor/UpgradeTypes;<init>(Ljava/lang/String;ILjava/lang/String;Lnet/minecraft/world/entity/ai/attributes/Attribute;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;F)V"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/UpgradeTypes;MANA:Lio/redspace/ironsspellbooks/item/armor/UpgradeTypes;", shift = At.Shift.BY, by = -1)),
            index = 3,
            remap = false
    )
    private static Attribute manaUpgrade3(Attribute par3) {
        return AttributeRegistry.MANA_REGEN.get();
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Lio/redspace/ironsspellbooks/item/armor/UpgradeTypes;<init>(Ljava/lang/String;ILjava/lang/String;Lnet/minecraft/world/entity/ai/attributes/Attribute;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;F)V"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/UpgradeTypes;MANA:Lio/redspace/ironsspellbooks/item/armor/UpgradeTypes;", shift = At.Shift.BY, by = -1)),
            index = 4,
            remap = false
    )
    private static AttributeModifier.Operation manaUpgrade4(AttributeModifier.Operation par4) {
        return AttributeModifier.Operation.ADDITION;
    }

    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", ordinal = 0, target = "Lio/redspace/ironsspellbooks/item/armor/UpgradeTypes;<init>(Ljava/lang/String;ILjava/lang/String;Lnet/minecraft/world/entity/ai/attributes/Attribute;Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;F)V"),
            slice = @Slice(from = @At(value = "FIELD", target = "Lio/redspace/ironsspellbooks/item/armor/UpgradeTypes;MANA:Lio/redspace/ironsspellbooks/item/armor/UpgradeTypes;", shift = At.Shift.BY, by = -1)),
            index = 5,
            remap = false
    )
    private static float manaUpgrade5(float par5) {
        return 0.5F;
    }
}
