package com.afidev.hubby;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.Objects;

import static net.minecraft.commands.Commands.literal;

public class HubCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("hub")
                .requires(source -> source.getEntity() instanceof ServerPlayer)
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player != null) {
                        if (HubbyConfig.isHubSet()) {
                            HubbyConfig.Location hub = HubbyConfig.getHub();
                            ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(hub.dimension));
                            ServerLevel targetWorld = Objects.requireNonNull(player.getServer()).getLevel(dimensionKey);
                            if (targetWorld == null) {
                                context.getSource().sendFailure(TextUtils.parseColor(HubbyConfig.getConfigData().getMessages().cannot_find_dimension));
                                return 0;
                            }
                            player.teleportTo(targetWorld, hub.x, hub.y, hub.z, hub.yaw, hub.pitch);
                            context.getSource().sendSuccess(() -> TextUtils.parseColor(HubbyConfig.getConfigData().getMessages().teleported_to_hub), false);
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
