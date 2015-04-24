package riskyken.armourersWorkshop.common.skin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayerMP;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;

import riskyken.armourersWorkshop.common.exception.InvalidCubeTypeException;
import riskyken.armourersWorkshop.common.exception.NewerFileVersionException;
import riskyken.armourersWorkshop.common.network.PacketHandler;
import riskyken.armourersWorkshop.common.network.messages.MessageServerSendEquipmentData;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.utils.ModLogger;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;

/**
 * Holds a cache of equipment data on the server that will be sent to clients if
 * they request it.
 * 
 * @author RiskyKen
 *
 */
public final class SkinDataCache {
    
    public static SkinDataCache INSTANCE = null;
    
    private HashMap<Integer, Skin> equipmentDataCache = new HashMap<Integer, Skin>();
    private ArrayList<QueueMessage> messageQueue = new ArrayList<QueueMessage>();
    private long lastTick;
    
    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = new SkinDataCache();
        }
    }
    
    public SkinDataCache() {
        FMLCommonHandler.instance().bus().register(this);
    }
    
    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent event) {
        if (event.side == Side.SERVER && event.type == Type.SERVER && event.phase == Phase.END) {
            processMessageQueue();
        }
    }
    
    public void processMessageQueue() {
        long curTick = System.currentTimeMillis();
        if (curTick >= lastTick + 80L) {
            lastTick = curTick;
            if (messageQueue.size() > 0) {
                processMessage(messageQueue.get(0));
                messageQueue.remove(0);
            }
        }
    }
    
    public void processMessage(QueueMessage queueMessage) {
        
        if (!equipmentDataCache.containsKey(queueMessage.equipmentId)) {
            if (haveEquipmentOnDisk(queueMessage.equipmentId)) {
                Skin equipmentData;
                equipmentData = loadEquipmentFromDisk(queueMessage.equipmentId);
                addEquipmentDataToCache(equipmentData, queueMessage.equipmentId);
            }
        }
        
        if (equipmentDataCache.containsKey(queueMessage.equipmentId)) {
            Skin equpmentData = equipmentDataCache.get(queueMessage.equipmentId);
            PacketHandler.networkWrapper.sendTo(new MessageServerSendEquipmentData(equpmentData), queueMessage.player);
        } else {
            ModLogger.log(Level.ERROR, "Equipment id:" + queueMessage.equipmentId +" was requested by "
        + queueMessage.player.getCommandSenderName() + " but was not found.");
        }
    }
    
    public void addEquipmentDataToCache(Skin equipmentData) {
        addEquipmentDataToCache(equipmentData, equipmentData.hashCode());
    }
    
    public void addEquipmentDataToCache(Skin equipmentData, int equipmentId) {
        if (equipmentData == null) {
            return;
        }
        if (!equipmentDataCache.containsKey(equipmentId)) {
            equipmentDataCache.put(equipmentId, equipmentData);
            if (!haveEquipmentOnDisk(equipmentId)) {
                saveEquipmentToDisk(equipmentData);
            }
        }
    }
    
    public Skin getEquipmentData(int equipmentId) {
        if (!equipmentDataCache.containsKey(equipmentId)) {
            if (haveEquipmentOnDisk(equipmentId)) {
                Skin equipmentData;
                equipmentData = loadEquipmentFromDisk(equipmentId);
                addEquipmentDataToCache(equipmentData, equipmentId);
            }
        }
        if (equipmentDataCache.containsKey(equipmentId)) {
            return equipmentDataCache.get(equipmentId);
        }
        return null;
    }
    
    public void clientRequestEquipmentData(int equipmentId, EntityPlayerMP player) {
        QueueMessage queueMessage = new QueueMessage(equipmentId, player);
        messageQueue.add(queueMessage);
    }
    
    private boolean haveEquipmentOnDisk(int equipmentId) {
        createEquipmentDirectory();
        File equipmentDir = new File(System.getProperty("user.dir"));
        equipmentDir = new File(equipmentDir, "equipment-database");
        
        File targetFile = new File(equipmentDir, File.separatorChar + String.valueOf(equipmentId));
        return targetFile.exists();
    }
    
    private void saveEquipmentToDisk(Skin equipmentData) {
        createEquipmentDirectory();
        File equipmentDir = new File(System.getProperty("user.dir"));
        equipmentDir = new File(equipmentDir, "equipment-database");
        
        File targetFile = new File(equipmentDir, File.separatorChar + String.valueOf(equipmentData.hashCode()));
        
        DataOutputStream stream = null;
        try {
            stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(targetFile)));
            equipmentData.writeToStream(stream);
            stream.flush();
        } catch (FileNotFoundException e) {
            ModLogger.log(Level.WARN, "Armour file not found.");
            e.printStackTrace();
            return;
        } catch (IOException e) {
            ModLogger.log(Level.ERROR, "Armour file save failed.");
            e.printStackTrace();
            return;
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
    
    private Skin loadEquipmentFromDisk(int equipmentId) {
        createEquipmentDirectory();
        File equipmentDir = new File(System.getProperty("user.dir"));
        equipmentDir = new File(equipmentDir, "equipment-database");
        
        File targetFile = new File(equipmentDir, File.separatorChar + String.valueOf(equipmentId));
        if (!targetFile.exists()) {
            return null;
        }
        
        Skin equipmentData = null;
        DataInputStream stream = null;
        
        try {
            stream = new DataInputStream(new BufferedInputStream(new FileInputStream(targetFile)));
            equipmentData = new Skin(stream);
        } catch (FileNotFoundException e) {
            ModLogger.log(Level.WARN, "Armour file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            ModLogger.log(Level.ERROR, "Armour file load failed.");
            e.printStackTrace();
        } catch (NewerFileVersionException e) {
            ModLogger.log(Level.ERROR, "Can not load custom armour, was saved in newer version.");
            e.printStackTrace();
        } catch (InvalidCubeTypeException e) {
            ModLogger.log(Level.ERROR, "Unable to load skin. Unknown cube types found.");
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(stream);
        }
        
        return equipmentData;
    }
    
    private boolean createEquipmentDirectory() {
        File equipmentDir = new File(System.getProperty("user.dir"));
        equipmentDir = new File(equipmentDir, "equipment-database");
        
        if (!equipmentDir.exists()) {
            try {
                equipmentDir.mkdir();
            } catch (Exception e) {
                ModLogger.log(Level.WARN, "Unable to create equipment directory.");
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    public class QueueMessage {
        
        public final int equipmentId;
        public final EntityPlayerMP player;
        
        public QueueMessage(int equipmentId, EntityPlayerMP player) {
            this.equipmentId = equipmentId;
            this.player = player;
        }
    }
}