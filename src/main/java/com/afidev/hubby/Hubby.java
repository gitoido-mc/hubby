package com.afidev.hubby;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hubby implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("hubby");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Hubby");
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            SetHubCommand.register(dispatcher);
            HubCommand.register(dispatcher);
            SetSpawnCommand.register(dispatcher);
            SpawnCommand.register(dispatcher);
            SetLobbyCommand.register(dispatcher);
            LobbyCommand.register(dispatcher);
        });
        HubbyConfig.load();
        LOGGER.info("Hubby config loaded");
    }

    public static boolean hasPermission(ServerPlayer player, String permission) {
        try {
            User user = LuckPermsProvider.get().getUserManager().getUser(player.getUUID());
            if (user != null) {
                return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
            }
        } catch (Exception e) {
            player.sendSystemMessage(Component.literal("Error when checking permissions."), false);
        }
        return false;
    }
}

