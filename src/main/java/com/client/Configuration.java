package com.client;

import java.time.LocalDateTime;

public class Configuration {

	/**
	 * Client version is a number that will tell the server whether
	 * the player has the most up-to-date client, otherwise they
	 * will receive an error on login to update their client.
	 */
	public static final int CLIENT_VERSION = 252;

	public static boolean production = true;///WHEN LIVE = TRUE DONT FORGET BEFORE SHADOWJAR!!!

	/**
	 * Cache version is written to the cache folder inside a version file.
	 * This is read on startup to tell if the cache is out of date or not.
	 */
	public static final int CACHE_VERSION = 661; //just change this to any number up by +1 to make them download new client - 668 is ours
	//public static final String CACHE_LINK = "https://www.dropbox.com/s/60ydf7wqcikgtyj/.zodianXv2.zip?dl=1";  //https://www.dropbox.com/s/qn9uwtdlob710r4/SurgiosV1
	//https://www.dropbox.com/s/6ssmxumf26n9nos2n6au4/.rspsXv2.zip?dl=1


	public static final String CACHE_LINK = "https://www.dropbox.com/scl/fi/rl53qc5n0o9agy32gmixv/.rspsXv2.zip?rlkey=nafrnsg1551chj06oev76dlma&st=n3l8v9ff&dl=1";  //https://www.dropbox.com/s/qn9uwtdlob710r4/SurgiosV1
//https://www.dropbox.com/scl/fi/6ssmxumf26n9nos2n6au4/.rspsXv2.zip?rlkey=7ruteghb4ka5p08ev80hj0v87&dl=0
	/**
	 * The server version. The cache path is append with a _v1/2/3 etc for the version number
	 * to prevent overwriting older version caches.
	 * This should only be changed when a new server is launched, otherwise change {@link Configuration#CLIENT_VERSION}.
	 */

	public static final int SERVER_VERSION = 2;


	public static final String CLIENT_TITLE ="Runescape";//bbrgg.ddns.net
	public static final String WEBSITE = "http://runescape.com/";//147.189.161.23
	public static final String DEDICATED_SERVER_ADDRESS = production ? "71.85.147.55" : "localhost";//dont forgot to change this if u get updates are coming message!
	public static final int PORT = 43594; //
	public static final int TEST_PORT = 43595;

	public static final String MAPFUNCTION_SPRITE_FILE_NAME = "mapfunction_file_sprites";

	public static final String CACHE_NAME = ".rspsXv2";
	public static final String DEV_CACHE_NAME = "local_cache";
	public static final String CACHE_NAME_DEV = CACHE_NAME + "_dev";

	public static final String CUSTOM_ITEM_SPRITES_DIRECTORY = "item_sprites/";
	public static String CUSTOM_MAP_DIRECTORY = "./data/custom_maps/";
	public static String CUSTOM_MODEL_DIRECTORY = "./data/custom_models/";
	public static String CUSTOM_ANIMATION_DIRECTORY = "./data/custom_animations/";
	public static boolean developerMode = false;
	public static boolean packIndexData = false;  // turn to true to pack maps and items etc......
	public static boolean dumpMaps = false;
	public static boolean dumpAnimationData = false;
	public static boolean dumpDataLists = false;
	public static boolean newFonts; // TODO text offsets (i.e. spacing between characters) are incorrect, needs automatic fix from kourend
	public static String cacheName = CACHE_NAME;
	public static String clientTitle = "";

	public static final LocalDateTime LAUNCH_TIME = LocalDateTime.now();
	public static final String ERROR_LOG_DIRECTORY = "error_logs/";
	public static String ERROR_LOG_FILE = ("error_log_"
			+ LAUNCH_TIME.getYear() + "_"
			+ LAUNCH_TIME.getMonth() + "_"
			+ LAUNCH_TIME.getDayOfMonth()
			+ ".txt").toLowerCase();

	/**
	 * Attack option priorities 0 -> Depends on combat level 1 -> Always right-click
	 * 2 -> Left-click where available 3 -> Hidden
	 */
	public static int playerAttackOptionPriority;
	public static int npcAttackOptionPriority = 2;

	public static final boolean DUMP_SPRITES = false;
	public static final boolean PRINT_EMPTY_INTERFACE_SECTIONS = false;

	public static boolean playerNames;

	/**
	 * Seasonal Events
	 */
	public static boolean HALLOWEEN = false;
	public static boolean CHRISTMAS;
	public static boolean CHRISTMAS_EVENT;
	public static boolean EASTER;

	public static boolean osbuddyGameframe = false;

	public static int xpPosition;
	public static boolean escapeCloseInterface;
	public static boolean alwaysLeftClickAttack;
	public static boolean hideCombatOverlay;
	public static boolean particlesEnabled = true;
}