package com.afidev.hubby;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import static net.minecraft.commands.Commands.literal;


public class SpawnCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("spawn")
                .requires(source -> source.getEntity() instanceof ServerPlayer)
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player != null) {
                        if (HubbyConfig.isSpawnSet()) {
                            HubbyConfig.Location spawn = HubbyConfig.getSpawn();
                            ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(spawn.dimension));
                            ServerLevel targetWorld = player.getServer().getLevel(dimensionKey);
                            if (targetWorld == null) {
                                context.getSource().sendFailure(TextUtils.parseColor(HubbyConfig.getConfigData().getMessages().cannot_find_dimension));
                                return 0;
                            }
                            player.teleportTo(targetWorld, spawn.x, spawn.y, spawn.z, spawn.yaw, spawn.pitch);
                            context.getSource().sendSuccess(() -> TextUtils.parseColor(HubbyConfig.getConfigData().getMessages().teleported_to_spawn), false);
                        } else {
                            context.getSource().sendFailure(TextUtils.parseColor(HubbyConfig.getConfigData().getMessages().hub_not_set));
                            return 0;
                        }
                    } else {
                        context.getSource().sendFailure(TextUtils.parseColor(HubbyConfig.getConfigData().getMessages().player_only));
                        return 0;
                    }
                    return 1;
                }));
    }
}
