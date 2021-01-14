package goblinbob.mobends.standard.main;

import java.util.HashMap;
import java.util.Map;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = LibModInfo.ID)
public class ModConfig
{

    @Config.LangKey(LibModInfo.ID + ".config.show_arrow_trails")
    public static boolean showArrowTrails = true;
    @Config.LangKey(LibModInfo.ID + ".config.show_sword_trails")
    public static boolean showSwordTrail = true;
    @Config.LangKey(LibModInfo.ID + ".config.perform_spin_attack")
    public static boolean performSpinAttack = true;
    @Config.LangKey(LibModInfo.ID + ".config.weapon_items")
    public static String[] weaponItems = new String[] {};
    @Config.LangKey(LibModInfo.ID + ".config.keep_armor_as_vanilla")
    public static String[] keepArmorAsVanilla = new String[] {};

    @Config.Ignore
    private static Map<Item, ItemClassification> itemClassificationCache = new HashMap<>();
    @Config.Ignore
    private static Map<Item, Boolean> keepArmorAsVanillaCache = new HashMap<>();

    @Mod.EventBusSubscriber(modid = LibModInfo.ID)
    private static class EventHandler
    {

        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(LibModInfo.ID))
            {
                ConfigManager.sync(LibModInfo.ID, Config.Type.INSTANCE);
            }
        }

    }

    public enum ItemClassification
    {
        UNKNOWN,
        WEAPON,
    }

    /**
     * Returns true if the check matches the pattern. The pattern can either be:
     * - text - The check has to match this pattern exactly.
     * - *text - The check has to end with the pattern.
     * - text* - The check has to start with the pattern.
     * @param check
     * @param pattern
     * @return
     */
    public static boolean childWildcard(String check, String pattern)
    {
        final boolean startsWithWildcard =  pattern.startsWith("*");
        final boolean endsWithWildcard =  pattern.endsWith("*");

        return pattern.equals("*") ||
                startsWithWildcard && endsWithWildcard && check.contains(pattern.substring(1, pattern.length() - 1)) ||
                startsWithWildcard && check.endsWith(pattern.substring(1)) ||
                endsWithWildcard && check.startsWith(pattern.substring(0, pattern.length() - 1)) ||
                check.equals(pattern);
    }

    private static boolean checkForPatterns(ResourceLocation resourceLocation, String[] patterns)
    {
        final String resourceDomain = resourceLocation.getResourceDomain();
        final String resourcePath = resourceLocation.getResourcePath();

        for (String pattern : patterns)
        {
            final ResourceLocation patternLocation = new ResourceLocation(pattern);

            if (resourceLocation == patternLocation)
                return true;

            if (!childWildcard(resourceDomain, patternLocation.getResourceDomain()))
                continue;

            if (childWildcard(resourcePath, patternLocation.getResourcePath()))
                return true;
        }

        return false;
    }

    private static boolean checkForClassification(Item item, ItemClassification classification, String[] patterns)
    {
        if (checkForPatterns(item.getRegistryName(), patterns))
        {
            itemClassificationCache.put(item, classification);
            return true;
        }

        return false;
    }

    public static ItemClassification getItemClassification(Item item)
    {
        // If cached before, returning the cached classification.
        if (itemClassificationCache.containsKey(item))
            return itemClassificationCache.get(item);

        if (checkForClassification(item,  ItemClassification.WEAPON, weaponItems))
            return ItemClassification.WEAPON;

        // Unclassified
        itemClassificationCache.put(item, ItemClassification.UNKNOWN);
        return ItemClassification.UNKNOWN;
    }

    public static boolean shouldKeepArmorAsVanilla(Item item)
    {
        // If cached before, returning the cached result.
        if (keepArmorAsVanillaCache.containsKey(item))
            return keepArmorAsVanillaCache.get(item);

        if (checkForPatterns(item.getRegistryName(), keepArmorAsVanilla))
        {
            keepArmorAsVanillaCache.put(item, true);
            return true;
        }

        keepArmorAsVanillaCache.put(item, false);
        return false;
    }

}
