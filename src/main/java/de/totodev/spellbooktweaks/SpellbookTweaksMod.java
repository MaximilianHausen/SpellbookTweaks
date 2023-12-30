package de.totodev.spellbooktweaks;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SpellbookTweaksMod.MOD_ID)
public class SpellbookTweaksMod {
    public static final String MOD_ID = "spellbooktweaks";

    public SpellbookTweaksMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        AttributeRegistry.register(modEventBus);
    }
}
