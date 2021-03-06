package moe.plushie.armourers_workshop;

import goblinbob.mobends.core.Core;
import goblinbob.mobends.core.addon.AddonHelper;
import goblinbob.mobends.standard.DefaultAddon;
import moe.plushie.armourers_workshop.common.command.CommandArmourers;
import moe.plushie.armourers_workshop.common.creativetab.CreativeTabMain;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import moe.plushie.armourers_workshop.common.skin.cache.CommonSkinCache;
import moe.plushie.armourers_workshop.proxies.CommonProxy;
import moe.plushie.armourers_workshop.utils.ModLogger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = LibModInfo.ID, name = LibModInfo.NAME, version = "0.51.0", guiFactory = LibModInfo.GUI_FACTORY_CLASS, dependencies = LibModInfo.DEPENDENCIES, acceptedMinecraftVersions = LibModInfo.MC_VERSION)
public class ArmourersWorkshop {

    /*
     * Hello and welcome to the Armourer's Workshop source code.
     * 
     * Note: Any time the texture that is used on the player model is referred to,
     * (normal called the players skin) it will be called the player texture or
     * entity texture to prevent confusion with AW skins.
     */

    @Instance(LibModInfo.ID)
    private static ArmourersWorkshop instance;

    private static Logger logger;

    @SidedProxy(clientSide = LibModInfo.PROXY_CLIENT_CLASS, serverSide = LibModInfo.PROXY_COMMNON_CLASS)
    private static CommonProxy proxy;

    public static final CreativeTabMain TAB_MAIN = new CreativeTabMain();

    @EventHandler
    public void perInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
        proxy.initLibraryManager();
        proxy.createCore();
        Core.getInstance().preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        proxy.registerKeyBindings();
        proxy.initRenderers();
        Core.getInstance().init(event);
        // Registering the standard set of animations.
        AddonHelper.registerAddon(LibModInfo.ID, new DefaultAddon());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
        Core.getInstance().postInit(event);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandArmourers());
        CommonSkinCache.INSTANCE.serverStarted();
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        CommonSkinCache.INSTANCE.serverStopped();
    }

    public static boolean isDedicated() {
        return proxy.getClass() == CommonProxy.class;
    }

    public static CommonProxy getProxy() {
        return proxy;
    }

    public static ArmourersWorkshop getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return logger;
    }
}
