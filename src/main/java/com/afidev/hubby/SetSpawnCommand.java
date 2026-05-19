package com.afidev.hubby;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import static com.afidev.hubby.Hubby.hasPermission;
import static net.minecraft.commands.Commands.literal;

public class SetSpawnCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("setspawn")
                .requires(source -> source.hasPermission(2) && source.getEntity() instanceof ServerPlayer)
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player == null) {
                        context.getSource().sendFailure(TextUtils.parseColor(HubbyConfig.getConfigData().getMessages().player_only));
                        return 0;
                    }
                    if (hasPermission(player, "hubby.setspawn")) {
                        Vec3 pos = player.position();
                        float yaw = player.getYRot();
                        float pitch = player.getXRot();
                        String dimension = player.level().toString();
                        HubbyConfig.setSpawn(pos.x, pos.y, pos.z, yaw, pitch, dimension);
                        context.getSource().sendSuccess(() -> TextUtils.parseColor(HubbyConfig.getConfigData().getMessages().spawn_set_confirmation), true);
                        return 1;
                    } else {
                        context.getSource().sendFailure(TextUtils.parseColor(HubbyConfig.getConfigData().getMessages().no_permission));
                        return 0;
                    }
                }));
    }
}
