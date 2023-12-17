package de.totodev.spellbooktweaks;

import java.lang.reflect.Field;

public class ClientReserveManaData {
    static final IReserveManaData playerReserveManaData;

    static {
        try {
            Field clientDataField = Class.forName("io.redspace.ironsspellbooks.player.ClientMagicData").getDeclaredField("playerMagicData");
            clientDataField.setAccessible(true);
            playerReserveManaData = (IReserveManaData) clientDataField.get(null);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException("Could not get client-side MagicData", e);
        }
    }

    public static int getReserveMana() {
        return playerReserveManaData.spellbookTweaks$getReserveMana();
    }

    public static void setReserveMana(float mana) {
        playerReserveManaData.spellbookTweaks$setReserveMana(mana);
    }
}
