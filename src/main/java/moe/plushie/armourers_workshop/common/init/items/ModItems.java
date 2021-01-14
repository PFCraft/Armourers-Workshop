package moe.plushie.armourers_workshop.common.init.items;

import java.util.ArrayList;
import moe.plushie.armourers_workshop.common.lib.LibItemNames;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static final ArrayList<Item> ITEM_LIST = new ArrayList<Item>();

    public static final Item SKIN = new ItemSkin();

    public ModItems() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();
        for (int i = 0; i < ITEM_LIST.size(); i++) {
            reg.register(ITEM_LIST.get(i));
        }
    }
}
