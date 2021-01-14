package goblinbob.mobends.core;

import goblinbob.mobends.core.configuration.CoreConfig;
import goblinbob.mobends.core.network.msg.MessageConfigRequest;
import goblinbob.mobends.core.network.msg.MessageConfigResponse;
import java.util.logging.Logger;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public abstract class Core<T extends CoreConfig>
{

    private static Core INSTANCE;
    public static final Logger LOG = Logger.getLogger("mobends-core");

    private SimpleNetworkWrapper networkWrapper;
    private static final int MESSAGE_CONFIG_REQUEST = 0;
    private static final int MESSAGE_CONFIG_RESPONSE = 1;

    public abstract T getConfiguration();

    public void preInit(FMLPreInitializationEvent event)
    {
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(LibModInfo.ID);
        networkWrapper.registerMessage(MessageConfigRequest.Handler.class, MessageConfigRequest.class, MESSAGE_CONFIG_REQUEST, Side.SERVER);
        networkWrapper.registerMessage(MessageConfigResponse.Handler.class, MessageConfigResponse.class, MESSAGE_CONFIG_RESPONSE, Side.CLIENT);
    }

    public void init(FMLInitializationEvent event)
    {

    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }

    // Static methods

    public static Core getInstance()
    {
        return INSTANCE;
    }

    public static void createAsClient()
    {
        if (INSTANCE == null)
            INSTANCE = new CoreClient();
    }

    public static void createAsServer()
    {
        if (INSTANCE == null)
            INSTANCE = new CoreServer();
    }

    public static SimpleNetworkWrapper getNetworkWrapper()
    {
        return INSTANCE.networkWrapper;
    }

    public static void saveConfiguration()
    {
        INSTANCE.getConfiguration().save();
    }

}
