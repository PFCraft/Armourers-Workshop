package goblinbob.mobends.core;

import goblinbob.mobends.core.bender.EntityBenderRegistry;
import goblinbob.mobends.core.client.event.DataUpdateHandler;
import goblinbob.mobends.core.client.event.EntityRenderHandler;
import goblinbob.mobends.core.client.event.FluxHandler;
import goblinbob.mobends.core.client.event.KeyboardHandler;
import goblinbob.mobends.core.client.event.WorldJoinHandler;
import goblinbob.mobends.core.configuration.CoreClientConfig;
import goblinbob.mobends.core.pack.PackManager;
import javax.annotation.Nullable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CoreClient extends Core<CoreClientConfig>
{

    private static CoreClient INSTANCE;

    private CoreClientConfig configuration;

    CoreClient()
    {
        INSTANCE = this;
    }

    @Override
    public CoreClientConfig getConfiguration()
    {
        return configuration;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);

        configuration = new CoreClientConfig(event.getSuggestedConfigurationFile());
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);

        PackManager.INSTANCE.initialize(configuration);
        KeyboardHandler.initKeyBindings();

        MinecraftForge.EVENT_BUS.register(new EntityRenderHandler());
        MinecraftForge.EVENT_BUS.register(new DataUpdateHandler());
        MinecraftForge.EVENT_BUS.register(new KeyboardHandler());
        MinecraftForge.EVENT_BUS.register(new FluxHandler());
        MinecraftForge.EVENT_BUS.register(new WorldJoinHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);

        EntityBenderRegistry.instance.applyConfiguration(configuration);
    }

    @Nullable
    public static CoreClient getInstance()
    {
        return INSTANCE;
    }

}
