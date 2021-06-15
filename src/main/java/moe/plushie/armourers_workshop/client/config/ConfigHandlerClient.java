package moe.plushie.armourers_workshop.client.config;

import java.io.File;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ConfigHandlerClient {

    public static final String CATEGORY_MISC = "misc";
    public static final String CATEGORY_PERFORMANCE = "performance";
    public static final String CATEGORY_CACHE = "cache";
    public static final String CATEGORY_SKIN_PREVIEW = "skin-preview";
    public static final String CATEGORY_TOOLTIP = "tooltip";
    public static final String CATEGORY_DEBUG = "debug";

    // Performance
    public static int renderDistanceSkin;
    public static int renderDistanceBlockSkin;
    public static int modelBakingThreadCount;
    public static double lodDistance = 32F;
    public static boolean multipassSkinRendering = true;
    public static int maxLodLevels = 4;
    public static boolean useClassicBlockModels;

    // Cache
    public static int skinCacheExpireTime;
    public static int skinCacheMaxSize;
    public static int modelPartCacheExpireTime;
    public static int modelPartCacheMaxSize;
    public static int textureCacheExpireTime;
    public static int textureCacheMaxSize;
    public static int maxSkinRequests;
    public static int fastCacheSize;

    // Skin preview
    public static boolean skinPreEnabled = false;
    public static boolean skinPreDrawBackground = true;
    public static float skinPreSize = 96F;
    public static float skinPreLocHorizontal = 1F;
    public static float skinPreLocVertical = 0.5F;
    public static boolean skinPreLocFollowMouse = false;
    // Debug tool
    public static boolean showArmourerDebugRender;
    public static boolean wireframeRender;
    public static boolean showLodLevels;
    public static boolean showSkinBlockBounds;
    public static boolean showSkinRenderBounds;
    public static boolean showSortOrderToolTip;

    public static Configuration config;

    public static void init(File file) {
        if (config == null) {
            config = new Configuration(file, "1");
            loadConfigFile();
        }
    }

    public static void loadConfigFile() {
        loadCategoryPerformance();
        loadCategoryCache();
        //皮肤预览
        //loadCategorySkinPreview();
        if (config.hasChanged()) {
            config.save();
        }
    }

    private static void loadCategoryPerformance() {
        config.setCategoryComment(CATEGORY_PERFORMANCE, "Change (visual quality/performance) ratio by changing setting in this category.");

        renderDistanceSkin = config.getInt("renderDistanceSkin", CATEGORY_PERFORMANCE, 128, 16, 512,
                "The max distance in blocks that skins will render.");

        renderDistanceBlockSkin = config.getInt("renderDistanceBlockSkin", CATEGORY_PERFORMANCE, 128, 16, 512,
                "The max distance in blocks that block skins will be rendered.");
        renderDistanceBlockSkin = renderDistanceBlockSkin * renderDistanceBlockSkin;


        int cores = Runtime.getRuntime().availableProcessors();
        int bakingCores = MathHelper.ceil(cores / 2F);
        bakingCores = MathHelper.clamp(bakingCores, 1, 16);
        modelBakingThreadCount = config.getInt("modelBakingThreadCount", CATEGORY_PERFORMANCE, bakingCores, 1, 16, "");
        config.getCategory(CATEGORY_PERFORMANCE).get("modelBakingThreadCount").setComment(
                "The maximum number of threads that will be used to bake models. [range: " + 1 + " ~ " + 16 + ", default: " + "core count / 2" + "]");
        config.getCategory(CATEGORY_PERFORMANCE).get("modelBakingThreadCount").setRequiresMcRestart(true);

        multipassSkinRendering = config.getBoolean("multipassSkinRendering", CATEGORY_PERFORMANCE, true,
                "When enabled skin will render in multiple passes to reduce visual artifacts.\n"
                        + "Disabling this will improve skin rendering performance at the cost of visual quality.");

        lodDistance = config.getFloat("lodDistance", CATEGORY_PERFORMANCE, 32F, 8F, 128F,
                "Distance away that skins will have lod applied to them.");

        maxLodLevels = config.getInt("maxLodLevels", CATEGORY_PERFORMANCE, 4, 0, 4,
                "Number of LOD models to create. Higher number should give a boost to framerate at a small cost to VRAM.");

        useClassicBlockModels = config.getBoolean("useClassicBlockModels", CATEGORY_PERFORMANCE, false,
                "Use classic block models instead of the 3D model versions.");
    }

    private static void loadCategoryCache() {
        config.setCategoryComment(CATEGORY_CACHE, "Change (memory use/IO access) ratio by changing setting in this category.");

        // Skin cache
        skinCacheExpireTime = config.getInt("skinCacheExpireTime", CATEGORY_CACHE, 600, 0, 3600,
                "How long in seconds the client will keep skins in it's cache.\n"
                        + "Default 600 seconds is 10 minutes.\n"
                        + "Setting to 0 turns off this option.");
        config.getCategory(CATEGORY_CACHE).get("skinCacheExpireTime").setRequiresMcRestart(true);

        skinCacheMaxSize = config.getInt("skinCacheMaxSize", CATEGORY_CACHE, 2000, 0, 10000,
                "Max size the skin cache can reach before skins are removed.\n"
                        + "Setting to 0 turns off this option.");
        config.getCategory(CATEGORY_CACHE).get("skinCacheMaxSize").setRequiresMcRestart(true);

        // Model cache
        modelPartCacheExpireTime = config.getInt("modelPartCacheExpireTime", CATEGORY_CACHE, 600, 0, 3600,
                "How long in seconds the client will keep model parts in it's cache.\n"
                        + "Default 600 seconds is 10 minutes.\n"
                        + "Setting to 0 turns off this option.");
        config.getCategory(CATEGORY_CACHE).get("modelPartCacheExpireTime").setRequiresMcRestart(true);

        modelPartCacheMaxSize = config.getInt("modelPartCacheMaxSize", CATEGORY_CACHE, 2000, 0, 10000,
                "Max size the cache can reach before model parts are removed.\n"
                        + "Setting to 0 turns off this option.");
        config.getCategory(CATEGORY_CACHE).get("modelPartCacheMaxSize").setRequiresMcRestart(true);

        // Texture cache
        textureCacheExpireTime = config.getInt("textureCacheExpireTime", CATEGORY_CACHE, 600, 0, 3600,
                "How long in seconds the client will keep textures in it's cache.\n" +
                        "Default 600 seconds is 10 minutes.\n"
                        + "Setting to 0 turns off this option.");
        config.getCategory(CATEGORY_CACHE).get("textureCacheExpireTime").setRequiresMcRestart(true);

        textureCacheMaxSize = config.getInt("textureCacheMaxSize", CATEGORY_CACHE, 1000, 0, 5000,
                "Max size the texture cache can reach before textures are removed.\n"
                        + "Setting to 0 turns off this option.");
        config.getCategory(CATEGORY_CACHE).get("textureCacheMaxSize").setRequiresMcRestart(true);

        maxSkinRequests = config.getInt("maxSkinRequests", CATEGORY_CACHE, 10, 1, 50,
                "Maximum number of skin the client can request at one time.");

        fastCacheSize = config.getInt("fastCacheSize", CATEGORY_CACHE, 5000, 0, Integer.MAX_VALUE,
                "Size of client size cache.\n"
                        + "Setting to 0 turns off this option.");
    }

    private static void loadCategorySkinPreview() {
        config.setCategoryComment(CATEGORY_SKIN_PREVIEW, "Setting to configure the skin preview box.");

        skinPreEnabled = config.getBoolean("skinPreEnabled", CATEGORY_SKIN_PREVIEW, true,
                "Enables a larger skin preview box when hovering the mouse over a skin.");

        skinPreDrawBackground = config.getBoolean("skinPreDrawBackground", CATEGORY_SKIN_PREVIEW, true,
                "Draw a background box for the skin preview.");

        skinPreSize = config.getFloat("skinPreSize", CATEGORY_SKIN_PREVIEW, 96F, 16F, 256F,
                "Size of the skin preview.");

        skinPreLocHorizontal = config.getFloat("skinPreLocHorizontal", CATEGORY_SKIN_PREVIEW, 0F, 0F, 1F,
                "Horizontal location of the skin preview: 0 = left, 1 = right.");

        skinPreLocVertical = config.getFloat("skinPreLocVertical", CATEGORY_SKIN_PREVIEW, 0.5F, 0F, 1F,
                "Vertical location of the skin preview: 0 = top, 1 = bottom.");

        skinPreLocFollowMouse = config.getBoolean("skinPreLocFollowMouse", CATEGORY_SKIN_PREVIEW, true,
                "Skin preview will be rendered next to the mouse.");
    }
}
