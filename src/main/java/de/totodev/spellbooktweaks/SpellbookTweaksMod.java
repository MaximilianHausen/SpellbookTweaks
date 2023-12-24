package de.totodev.spellbooktweaks;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

import static io.redspace.ironsspellbooks.api.registry.AttributeRegistry.MANA_REGEN;

@Mod(SpellbookTweaksMod.MOD_ID)
public class SpellbookTweaksMod {
    public static final String MOD_ID = "spellbooktweaks";

    public SpellbookTweaksMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        AttributeRegistry.register(modEventBus);

        modEventBus.addListener(SpellbookTweaksMod::onAttributeModification);
    }

    private static void onAttributeModification(EntityAttributeModificationEvent event) {
        try {
            // fieldName = "defaultValue"
            Field f = ObfuscationReflectionHelper.findField(Attribute.class, "f_22076_");
            f.set(MANA_REGEN.get(), 3);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
