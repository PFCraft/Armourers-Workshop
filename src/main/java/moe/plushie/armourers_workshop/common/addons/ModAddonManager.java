package moe.plushie.armourers_workshop.common.addons;

import java.util.ArrayList;
import java.util.HashSet;
import moe.plushie.armourers_workshop.common.config.ConfigHandlerOverrides;
import moe.plushie.armourers_workshop.utils.ModLogger;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModAddonManager {
    
    private static final ArrayList<ModAddon> LOADED_ADDONS = new ArrayList<ModAddon>(); 
    
    private static final HashSet<String> ITEM_OVERRIDES = new HashSet<String>();

    public static ModAddon addonColoredLights;
    public static AddonCustomEntities addonCustomEntities;
    public static AddonCustomNPCS addonCustomNPCS;
    public static AddonHauntedAstolfoBeanPlushie addonHauntedAstolfoBeanPlushie;
    public static AddonJBRAClient addonJBRAClient;
    public static AddonJEI addonJEI;
    public static AddonMinecraft addonMinecraft;
    public static ModAddon addonMobends;
    public static ModAddon addonMorePlayerModels;
    public static AddonNEI addonNEI;
    public static AddonOverlord addonOverlord;
    public static AddonRealFirstPerson addonRealFirstPerson;
    public static AddonRealFirstPerson2 addonRealFirstPerson2;
    public static AddonShaders addonShaders;
    public static ModAddon addonSmartMoving;
    
    private ModAddonManager() {
    }
    
    public static void preInit() {
        loadAddons();
        for (ModAddon modAddon : LOADED_ADDONS) {
            modAddon.preInit();
        }
    }
    
    private static void loadAddons() {
        ModLogger.log("Loading addons");
        addonColoredLights = new ModAddon("easycoloredlights", "Colored Lights");
        addonCustomEntities = new  AddonCustomEntities();
        addonCustomNPCS = new AddonCustomNPCS();
        addonHauntedAstolfoBeanPlushie = new AddonHauntedAstolfoBeanPlushie();
        addonJBRAClient = new AddonJBRAClient();
        addonJEI = new AddonJEI();
        addonMinecraft = new AddonMinecraft();
        addonMobends = new ModAddon("mobends", "Mo' Bends");
        addonMorePlayerModels = new ModAddon("moreplayermodels", "More Player Models");
        addonNEI = new AddonNEI();
        addonOverlord = new AddonOverlord();
        addonRealFirstPerson = new AddonRealFirstPerson();
        addonRealFirstPerson2 = new AddonRealFirstPerson2();
        addonShaders = new AddonShaders();
        addonSmartMoving = new ModAddon("SmartMoving", "Smart Moving");
    }
    
    public static void init() {
        for (ModAddon modAddon : LOADED_ADDONS) {
            modAddon.init();
        }
    }
    
    public static void postInit() {
        for (ModAddon modAddon : LOADED_ADDONS) {
            modAddon.postInit();
        }
        buildOverridesList();
    }
    
    @SideOnly(Side.CLIENT)
    public static void initRenderers() {
        for (ModAddon modAddon : LOADED_ADDONS) {
            modAddon.initRenderers();
        }
    }
    
    public static void buildOverridesList() {
        ITEM_OVERRIDES.clear();
        for (ModAddon modAddon : LOADED_ADDONS) {
            if (modAddon.isItemSkinningSupport() & modAddon.isModLoaded()) {
                ITEM_OVERRIDES.addAll(modAddon.getItemOverrides());
            }
        }
        ITEM_OVERRIDES.addAll(ConfigHandlerOverrides.getOverrides());
    }
    
    public static HashSet<String> getItemOverrides() {
        return ITEM_OVERRIDES;
    }
    
    public static ArrayList<ModAddon> getLoadedAddons() {
        return LOADED_ADDONS;
    }
    
    public static void setOverridesFromServer(String[] itemOverrides) {
        ITEM_OVERRIDES.clear();
        for (int i = 0; i < itemOverrides.length; i++) {
            ITEM_OVERRIDES.add(itemOverrides[i]);
        }
    }
    
    public static boolean isOverrideItem(ItemOverrideType type, Item item) {
        String key = type.toString().toLowerCase() + ":" + item.getRegistryName().toString();
        return ITEM_OVERRIDES.contains(key);
    }
    
    public static enum ItemOverrideType {
        SWORD,
        SHIELD,
        BOW,
        
        PICKAXE,
        AXE,
        SHOVEL,
        HOE,
        
        ITEM;
    }
}
