package goblinbob.mobends.core;

import goblinbob.mobends.core.configuration.CoreServerConfig;
import javax.annotation.Nullable;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CoreServer extends Core<CoreServerConfig>
{

	private static CoreServer INSTANCE;

	private CoreServerConfig configuration;

	CoreServer()
	{
		INSTANCE = this;
	}

	@Override
	public CoreServerConfig getConfiguration()
	{
		return configuration;
	}

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);

		configuration = new CoreServerConfig(event.getSuggestedConfigurationFile());
	}

	@Nullable
	public static CoreServer getInstance()
	{
		return INSTANCE;
	}
	
}
