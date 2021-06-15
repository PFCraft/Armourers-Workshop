package moe.plushie.armourers_workshop.common.config;

import java.io.File;
import java.util.UUID;
import moe.plushie.armourers_workshop.common.capability.entityskin.EntitySkinCapability;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import moe.plushie.armourers_workshop.utils.ModLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_WARDROBE = "wardrobe";
    public static final String CATEGORY_LIBRARY = "library";
    public static final String CATEGORY_RECIPE = "recipe";
    public static final String CATEGORY_HOLIDAY = "holiday-events";
    public static final String CATEGORY_ENTITY_SKINS = "entity-skins";
    public static final String CATEGORY_CACHE = "cache";
    
    public static Configuration config;
    
    // General
    public static int maxUndos = 100;
    public static int serverSkinSendRate = 4000;
    public static boolean serverCompressesSkins = true;
    
    // Wardrobe
    public static boolean wardrobeAllowOpening = true;
    public static boolean wardrobeTabSkins = true;
    public static boolean wardrobeTabOutfits = true;
    public static boolean wardrobeTabDisplaySettings = true;
    public static boolean wardrobeTabColourSettings = true;
    public static boolean wardrobeTabDyes = true;
    public static int wardrobeStartingSlots = 3;
    public static int wardrobeDropSkinsOnDeath = 0;
    
    // Library
    public static boolean extractOfficialSkins;
    public static boolean libraryShowsModelPreviews = true;
    public static boolean allowDownloadingSkins = false;
    public static boolean allowUploadingSkins = true;
    
    // Recipes
    public static boolean hideDollFromCreativeTabs;
    public static boolean hideGiantFromCreativeTabs;
    public static boolean enableRecoveringSkins;
    
    // Holiday events
    public static boolean disableAllHolidayEvents;
    
    // Entity skins
    public static int enitiySpawnWithSkinsChance = 75;
    public static int entityDropSkinChance = 10;
    public static String enitiySpawnSkinTargetPath = "/";
    
    // Cache
    public static int skinCacheExpireTime;
    public static int skinCacheMaxSize;
    
    // Other
    public static UUID remotePlayerId;
    
    public static void init(File file) {
        if (config == null) {
            config = new Configuration(file, "1");
            loadConfigFile();
        }
    }

    public static void loadConfigFile() {
        loadCategoryGeneral();
        loadCategoryWardrobe();
        loadCategoryLibrary();
        loadCategoryRecipe();
        loadCategoryEntitySkins();
        loadCategoryCache();
        if (config.hasChanged()) {
            config.save();
        }
    }
    
    public static boolean canOpenWardrobe(EntityPlayer player) {
        if (!wardrobeAllowOpening) {
            return false;
        }
        if (player.capabilities.isCreativeMode) {
            return true;
        }
        if (!wardrobeTabSkins & !wardrobeTabOutfits &!wardrobeTabDisplaySettings & !wardrobeTabColourSettings & !wardrobeTabDyes) {
            // No wardrobe tabs are active.
            return false;
        }
        return true;
    }
    
    private static void loadCategoryGeneral() {
        config.setCategoryComment(CATEGORY_GENERAL, "General settings.");
        
        maxUndos = config.getInt("maxUndos", CATEGORY_GENERAL, 100, 0, 1000,
                "Max number of undos a player has for block painting.");
        
        serverSkinSendRate = config.getInt("serverModelSendRate", CATEGORY_GENERAL, 4000, 0, 8000,
                "The maximum number of skins the server is allow to send every minute.\n"
                + "Less that 1 equals unlimited. (not recommended may cause bandwidth and cpu spikes on the server)");
        
        serverCompressesSkins = config.getBoolean("serverCompressesSkins", CATEGORY_GENERAL, true,
                "If enabled the server will compress skins before sending them to clients.\n" +
                "Highly recommended unless the server has a very slow CPU.");
    }
    
    private static void loadCategoryWardrobe() {
        config.setCategoryComment(CATEGORY_WARDROBE, "Setting for the players wardrobe.");
        
        wardrobeAllowOpening = config.getBoolean("wardrobeAllowOpening", CATEGORY_WARDROBE, true,
                "Allow the player to open the wardrobe GUI.");
        
        wardrobeTabSkins = config.getBoolean("wardrobeTabSkins", CATEGORY_WARDROBE, true,
                "Enable the wardrobe skins tab.");
        
        wardrobeTabOutfits = config.getBoolean("wardrobeTabOutfits", CATEGORY_WARDROBE, true,
                "Enable the wardrobe outfits tab.");
        
        wardrobeTabDisplaySettings = config.getBoolean("wardrobeTabDisplaySettings", CATEGORY_WARDROBE, true,
                "Enable the wardrobe display settings tab.");
        
        wardrobeTabColourSettings = config.getBoolean("wardrobeTabColourSettings", CATEGORY_WARDROBE, true,
                "Enable the wardrobe colour settings tab.");
        
        wardrobeTabDyes = config.getBoolean("wardrobeTabDyes", CATEGORY_WARDROBE, true,
                "Enable the wardrobe dyes tab.");
        
        wardrobeStartingSlots = config.getInt("wardrobeStartingSlots", CATEGORY_WARDROBE, 3, 1, EntitySkinCapability.MAX_SLOTS_PER_SKIN_TYPE,
                "Number of slot columns the player starts with for skins.");
        
        wardrobeDropSkinsOnDeath = config.getInt("wardrobeDropSkinsOnDeath", CATEGORY_WARDROBE, 0, 0, 2,
                "Should skins be dropped on player death.\n"
                + "0 = use keep inventory rule\n"
                + "1 = never drop\n"
                + "2 = always drop");
    }
    
    private static void loadCategoryLibrary() {
        config.setCategoryComment(CATEGORY_LIBRARY, "Setting for the library blocks.");
        
        allowDownloadingSkins = config.getBoolean("allowDownloadingSkins", CATEGORY_LIBRARY, false,
                "Allows clients to save skins from a server to their local computer using the library.");
        
        allowUploadingSkins = config.getBoolean("allowUploadingSkins", CATEGORY_LIBRARY, true,
                "Allows clients to load skins from their local computer onto the server using the library.");
        
        extractOfficialSkins = config.getBoolean("extractOfficialSkins", CATEGORY_LIBRARY, true,
                "Allow the mod to extract the official skins that come with the mod into the library folder.");
        
        libraryShowsModelPreviews = config.getBoolean("libraryShowsModelPreviews", CATEGORY_LIBRARY, true,
                        "Shows model previews in the library.\n"
                        + "Causes a lot of extra load on servers.\n"
                        + "Best to turn off on high population servers");
    }
    
    private static void loadCategoryRecipe() {
        config.setCategoryComment(CATEGORY_RECIPE, "Setting for mod recipes.");
        
        hideDollFromCreativeTabs = config.getBoolean("hideDollFromCreativeTabs", CATEGORY_RECIPE, true,
                "Hides the doll block from the creative tab and NEI.");
        
        hideGiantFromCreativeTabs = config.getBoolean("hideGiantFromCreativeTabs", CATEGORY_RECIPE, true,
                "Hides the giant block from the creative tab and NEI.");
        
        enableRecoveringSkins = config.getBoolean("enableRecoveringSkins", CATEGORY_RECIPE, false,
                "Enable copying the skin off an item in the skinning table");
    }
    
    private static void loadCategoryEntitySkins() {
        config.setCategoryComment(CATEGORY_ENTITY_SKINS, "Control how/if entities spawm with skin on them.");
        
        enitiySpawnWithSkinsChance = config.getInt("enitiySpawnWithSkinsChance", CATEGORY_ENTITY_SKINS, 0, 0, 100,
                        "Percentage change that entities will spawn with skins equipped.");
        
        entityDropSkinChance = config.getInt("entityDropSkinChance", CATEGORY_ENTITY_SKINS, 0, 0, 100,
                        "Percentage change that entities will drop equipped skins when killed.");
        
        enitiySpawnSkinTargetPath = "/" + config.getString("enitiySpawnSkinTargetPath", CATEGORY_ENTITY_SKINS, "",
                        "Target library path for skin spawned on entities.\n"
                        + "Examples: 'official/' for only skins in the official folder or 'downloads/' for skins in the downloads folder.\n"
                        + "Leave black for all skins.");
    }
    

    
    private static void loadCategoryCache() {
        config.setCategoryComment(CATEGORY_CACHE, "Change (memory use/IO access) ratio by category setting in this category.");
        
        skinCacheExpireTime = config.getInt("skinCacheExpireTime", CATEGORY_CACHE, 6000, 0, 3600,
                "How long in seconds the server will keep skins in it's cache.\n"
                + "Default 600 seconds is 10 minutes.\n"
                + "Setting to 0 turns off this option.");
        
        skinCacheMaxSize = config.getInt("skinCacheMaxSize", CATEGORY_CACHE, 2000, 0, 10000,
                "Max size the skin cache can reach before skins are removed.\n"
                + "Setting to 0 turns off this option.");
        
    }

    private static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version
        // string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else {
            return Integer.signum(vals1.length - vals2.length);
        }
    }
}
