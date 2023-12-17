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

@Mod.EventBusSubscriber(modid = SpellbookTweaksMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributeRegistry {
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, SpellbookTweaksMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final RegistryObject<Attribute> MAX_RESERVE_MANA = ATTRIBUTES.register("max_reserve_mana", () -> (new RangedAttribute("attribute.spellbooktewaks.max_reserve_mana", 800.0D, 0.0D, 100000.0D).setSyncable(true)));
    public static final RegistryObject<Attribute> RESERVE_MANA_REGEN = ATTRIBUTES.register("reserve_mana_regen", () -> (new RangedAttribute("attribute.spellbooktewaks.reserve_mana_regen", 1.0D, 0.0D, 10.0D).setSyncable(true)));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> {
            e.add(entity, MAX_RESERVE_MANA.get());
            e.add(entity, RESERVE_MANA_REGEN.get());
        });
    }
}
