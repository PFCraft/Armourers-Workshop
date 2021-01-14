package goblinbob.mobends.core.configuration;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public abstract class CoreConfig
{

    protected Configuration configuration;

    CoreConfig(File file)
    {
        configuration = new Configuration(file);
    }

    public abstract void save();

}
