package com.example.examplemod.mixin;

import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class ExampleMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "run", at = @At("HEAD"))
    public void run(CallbackInfo ci) {
        LOGGER.info("Hello from example mixin!");
    }
}
