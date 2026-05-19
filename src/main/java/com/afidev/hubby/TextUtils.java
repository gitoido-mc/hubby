package com.afidev.hubby;

import net.minecraft.network.chat.Component;

public class TextUtils {

    public static Component parseColor(String message) {
        message = message.replaceAll("&([0-9a-fk-or])", "§$1");
        return Component.literal(message);
    }
}
