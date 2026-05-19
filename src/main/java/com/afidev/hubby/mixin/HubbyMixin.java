package com.afidev.hubby.mixin;

import com.afidev.hubby.Hubby;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class HubbyMixin {
	@Inject(at = @At("HEAD"), method = "loadLevel")
	private void hubby$init(CallbackInfo info) {
		Hubby.LOGGER.info("Hubby loaded successfully");
	}
}
