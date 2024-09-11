package de.totodev.spellbooktweaks;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Contains any additional attributes. Not to be confused with {@link io.redspace.ironsspellbooks.api.registry.AttributeRegistry}.
 */
@Mod.EventBusSubscriber(modid = SpellbookTweaksMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributeRegistry {
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, SpellbookTweaksMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final RegistryObject<Attribute> SPELL_PROFICIENCY = ATTRIBUTES.register("spell_proficiency", () -> new RangedAttribute("attribute.spellbooktweaks.spell_proficiency", 0.0, 0.0, 4.0).setSyncable(true));
    public static final RegistryObject<Attribute> FIRE_SCHOOL_PROFICIENCY = newSchoolProficiencyAttribute("fire");
    public static final RegistryObject<Attribute> ICE_SCHOOL_PROFICIENCY = newSchoolProficiencyAttribute("ice");
    public static final RegistryObject<Attribute> LIGHTNING_SCHOOL_PROFICIENCY = newSchoolProficiencyAttribute("lightning");
    public static final RegistryObject<Attribute> HOLY_SCHOOL_PROFICIENCY = newSchoolProficiencyAttribute("holy");
    public static final RegistryObject<Attribute> ENDER_SCHOOL_PROFICIENCY = newSchoolProficiencyAttribute("ender");
    public static final RegistryObject<Attribute> BLOOD_SCHOOL_PROFICIENCY = newSchoolProficiencyAttribute("blood");
    public static final RegistryObject<Attribute> EVOCATION_SCHOOL_PROFICIENCY = newSchoolProficiencyAttribute("evocation");
    public static final RegistryObject<Attribute> NATURE_SCHOOL_PROFICIENCY = newSchoolProficiencyAttribute("nature");

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> {
            e.add(entity, SPELL_PROFICIENCY.get());
            e.add(entity, FIRE_SCHOOL_PROFICIENCY.get());
            e.add(entity, ICE_SCHOOL_PROFICIENCY.get());
            e.add(entity, LIGHTNING_SCHOOL_PROFICIENCY.get());
            e.add(entity, HOLY_SCHOOL_PROFICIENCY.get());
            e.add(entity, ENDER_SCHOOL_PROFICIENCY.get());
            e.add(entity, BLOOD_SCHOOL_PROFICIENCY.get());
            e.add(entity, EVOCATION_SCHOOL_PROFICIENCY.get());
            e.add(entity, NATURE_SCHOOL_PROFICIENCY.get());
        });
    }

    private static RegistryObject<Attribute> newSchoolProficiencyAttribute(String id) {
        return ATTRIBUTES.register(
                id + "_school_proficiency", () -> new RangedAttribute("attribute.spellbooktweaks." + id + "_school_proficiency", 0.0, 0.0, 4.0).setSyncable(true)
        );
    }
}
