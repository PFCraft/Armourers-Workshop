package moe.plushie.armourers_workshop.common.init.blocks;

import java.util.ArrayList;

import moe.plushie.armourers_workshop.common.lib.LibBlockNames;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import moe.plushie.armourers_workshop.common.tileentities.TileEntitySkinLibrary;
import moe.plushie.armourers_workshop.common.tileentities.TileEntitySkinnable;
import moe.plushie.armourers_workshop.common.tileentities.TileEntitySkinnableChild;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static final ArrayList<Block> BLOCK_LIST = new ArrayList<Block>();

    public static final Block SKIN_LIBRARY = new BlockSkinLibrary();
    public static final Block SKINNABLE = new BlockSkinnable();
    public static final Block SKINNABLE_GLOWING = new BlockSkinnableGlowing();
    public static final Block SKINNABLE_CHILD = new BlockSkinnableChild();
    public static final Block SKINNABLE_CHILD_GLOWING = new BlockSkinnableChildGlowing();

    public ModBlocks() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> reg = event.getRegistry();
        for (int i = 0; i < BLOCK_LIST.size(); i++) {
            reg.register(BLOCK_LIST.get(i));
        }
    }

    @SubscribeEvent
    public void registerItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (int i = 0; i < BLOCK_LIST.size(); i++) {
            Block block = BLOCK_LIST.get(i);
            if (block instanceof ICustomItemBlock) {
                ((ICustomItemBlock) block).registerItemBlock(registry);
            }
        }
    }

    public void registerTileEntities() {
        registerTileEntity(TileEntitySkinLibrary.class, LibBlockNames.SKIN_LIBRARY);
        registerTileEntity(TileEntitySkinnable.class, LibBlockNames.SKINNABLE);
        registerTileEntity(TileEntitySkinnableChild.class, LibBlockNames.SKINNABLE_CHILD);
    }

    private void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id) {
        GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(LibModInfo.ID, "tileentity." + id));
    }
}
