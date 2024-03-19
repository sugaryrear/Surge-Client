package com.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

public enum PlayerRights {

    PLAYER(0, "000000"),
    STAFF(11, "004080"),
    MODERATOR(1, "#0000ff", STAFF),
    ADMINISTRATOR(2, "F5FF0F", MODERATOR),
    OWNER(3, "F5FF0F", ADMINISTRATOR),
    UNKNOWN(4, "F5FF0F"),
    SAPPHIRE_DONATOR(5, "B60818"),
    EMERALD_DONATOR(6, "118120", SAPPHIRE_DONATOR),//regular
    RUBY_DONATOR(8, "9E6405", EMERALD_DONATOR),//extreme
    DIAMOND_DONATOR(9, "9E6405", RUBY_DONATOR),
    ONYX_DONATOR(10, "9E6405", DIAMOND_DONATOR),

    ZENYTE_DONATOR(15, "437100", ONYX_DONATOR),

    GLACYTE_INVESTOR(21, "437100", ZENYTE_DONATOR),

    YOUTUBER(12, "FE0018"),
    IRONMAN(13, "3A3A3A"),
    ULTIMATE_IRONMAN(14, "717070"),
    GAME_DEVELOPER(36, "544FBB", OWNER),//16th ordinal
    OSRS(23, "437100"),
    GRINDMAN(25, "437100"),
    HC_IRONMAN(10, "60201f"),
    GRINDMAN_IRONMAN(0, "60201f"),//"rogue hardcore ironman AKA standard mode?"
    GRINDMAN_HARDCORE_IRONMAN(27, "60201f"),
    GROUP_IRONMAN(28, "60201f"),
    HC_GROUP_IRONMAN(30, "60201f", GROUP_IRONMAN)
    ;

    /**
     * The level of rights that define this
     */
    private final int rightsId;

    /**
     * The rights inherited by this right
     */
    private final List<PlayerRights> inherited;

    /**
     * The color associated with the right
     */
    private final String color;

    /**
     * Creates a new right with a value to differentiate it between the others
     *
     * @param right the right required
     * @param color a color thats used to represent the players name when displayed
     * @param inherited the right or rights inherited with this level of right
     */
    PlayerRights(int right, String color, PlayerRights... inherited) {
        this.rightsId = right;
        this.inherited = Arrays.asList(inherited);
        this.color = color;
    }

    public boolean isStaffPosition() {
        return this == STAFF || this == ADMINISTRATOR || this == MODERATOR || this == OWNER || this == GAME_DEVELOPER;
    }

    public int spriteId() {
        return rightsId - 1;
    }

    public int crownId() {
        return rightsId;
    }

    public boolean hasCrown() {
        return this != PlayerRights.PLAYER;
    }

    public int getRightsId() {
        return rightsId;
    }

    public static final EnumSet[] DISPLAY_GROUPS = {
            EnumSet.of(STAFF, MODERATOR, ADMINISTRATOR, OWNER, GAME_DEVELOPER, UNKNOWN, SAPPHIRE_DONATOR, EMERALD_DONATOR,
                    RUBY_DONATOR, DIAMOND_DONATOR, ONYX_DONATOR, ZENYTE_DONATOR, GLACYTE_INVESTOR, YOUTUBER),
            EnumSet.of(IRONMAN, ULTIMATE_IRONMAN, OSRS, HC_IRONMAN, GRINDMAN,
                    GRINDMAN_HARDCORE_IRONMAN, GRINDMAN_IRONMAN, GROUP_IRONMAN, HC_GROUP_IRONMAN),
    };

    public static PlayerRights forRightsValue(int rightsValue) {
        Optional<PlayerRights> rights = Arrays.stream(PlayerRights.values()).filter(right -> right.getRightsId() == rightsValue).findFirst();
        if (rights.isPresent()) {
            return rights.get();
        } else {
            System.err.println("No rights for value " + rightsValue);
            return PlayerRights.PLAYER;
        }
    }

    public static List<PlayerRights> getDisplayedRights(PlayerRights[] set) {
        List<PlayerRights> rights = new ArrayList<>();

        for (PlayerRights right : set) {
            if (DISPLAY_GROUPS[0].contains(right)) {
                rights.add(right);
                break; // Only displaying one crown from this group!
            }
        }

        for (PlayerRights right : set) {
            if (DISPLAY_GROUPS[1].contains(right)) {
                if (rights.size() < 2) {
                    rights.add(right);
                }
            }
        }

        return rights;
    }

    public static PlayerRights[] ordinalsToArray(int[] ordinals) {
        PlayerRights[] rights = new PlayerRights[ordinals.length];
        for (int index = 0; index < ordinals.length; index++) {
            rights[index] = PlayerRights.values()[ordinals[index]];
        }
        return rights;
    }

    public static Pair<Integer, PlayerRights[]> readRightsFromPacket(Buffer inStream) {
        int rightsAmount = inStream.get_unsignedbyte();
        int[] ordinals = new int[rightsAmount];
        for (int right = 0; right < rightsAmount; right++) {
            ordinals[right] = inStream.get_unsignedbyte();
        }
        return Pair.of(rightsAmount, PlayerRights.ordinalsToArray(ordinals));
    }

    public static boolean hasRightsOtherThan(PlayerRights[] rights, PlayerRights playerRight) {
        return Arrays.stream(rights).anyMatch(right -> right != playerRight);
    }

    public static boolean hasRights(PlayerRights[] rights, PlayerRights playerRights) {
        return Arrays.stream(rights).anyMatch(right -> right == playerRights);
    }

    public static boolean hasRightsLevel(PlayerRights[] rights, int rightsId) {
        return Arrays.stream(rights).anyMatch(right -> right.getRightsId() >= rightsId);
    }

    public static boolean hasRightsBetween(PlayerRights[] rights, int low, int high) {
        return Arrays.stream(rights).anyMatch(right -> right.getRightsId() > low && right.getRightsId() < high);
    }

    public static String buildCrownString(List<PlayerRights> rights) {
        return buildCrownString(rights.toArray(new PlayerRights[0]));
    }

    public static String buildCrownString(PlayerRights[] rights) {
        StringBuilder builder = new StringBuilder();
        for (PlayerRights right : rights) {
            if (right.hasCrown()) {
                builder.append("@cr" + right.crownId() + "@");
            }
        }
        return builder.toString();
    }

}
