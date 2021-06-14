package moe.plushie.armourers_workshop.utils;

import moe.plushie.armourers_workshop.ArmourersWorkshop;
import org.apache.logging.log4j.Level;

public class ModLogger {
	
    public static void log(Object object) {
    	ArmourersWorkshop.getLogger().log(Level.INFO, String.valueOf(object));
    }

    public static void log(Level logLevel, Object object) {
    	ArmourersWorkshop.getLogger().log(logLevel, String.valueOf(object));
    }
}
