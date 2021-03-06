package moe.plushie.armourers_workshop.proxies;

import com.mojang.authlib.GameProfile;
import goblinbob.mobends.core.Core;
import goblinbob.mobends.standard.client.event.RenderingEventHandler;
import goblinbob.mobends.standard.client.renderer.entity.RenderBendsSpectralArrow;
import goblinbob.mobends.standard.client.renderer.entity.RenderBendsTippedArrow;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import moe.plushie.armourers_workshop.ArmourersWorkshop;
import moe.plushie.armourers_workshop.api.ArmourersWorkshopClientApi;
import moe.plushie.armourers_workshop.api.common.painting.IPantable;
import moe.plushie.armourers_workshop.client.config.ConfigHandlerClient;
import moe.plushie.armourers_workshop.client.gui.style.GuiResourceManager;
import moe.plushie.armourers_workshop.client.handler.BlockHighlightRenderHandler;
import moe.plushie.armourers_workshop.client.handler.ClientWardrobeHandler;
import moe.plushie.armourers_workshop.client.handler.ModClientFMLEventHandler;
import moe.plushie.armourers_workshop.client.handler.PlayerTextureHandler;
import moe.plushie.armourers_workshop.client.handler.SkinPreviewHandler;
import moe.plushie.armourers_workshop.client.handler.SkinRenderHandlerApi;
import moe.plushie.armourers_workshop.client.library.ClientLibraryManager;
import moe.plushie.armourers_workshop.client.model.ICustomModel;
import moe.plushie.armourers_workshop.client.model.bake.ModelBakery;
import moe.plushie.armourers_workshop.client.model.bake.ModelBakery.BakedSkin;
import moe.plushie.armourers_workshop.client.model.bake.ModelBakery.IBakedSkinReceiver;
import moe.plushie.armourers_workshop.client.palette.PaletteManager;
import moe.plushie.armourers_workshop.client.render.RenderBridge;
import moe.plushie.armourers_workshop.client.render.SkinModelRenderHelper;
import moe.plushie.armourers_workshop.client.render.entity.EntitySkinRenderHandler;
import moe.plushie.armourers_workshop.client.render.entity.RenderSpectralArrowSkinned;
import moe.plushie.armourers_workshop.client.render.entity.RenderTippedArrowSkinned;
import moe.plushie.armourers_workshop.client.render.item.RenderItemEquipmentSkin;
import moe.plushie.armourers_workshop.client.render.tileentities.RenderBlockColourable;
import moe.plushie.armourers_workshop.client.render.tileentities.RenderBlockSkinnable;
import moe.plushie.armourers_workshop.client.skin.cache.ClientSkinCache;
import moe.plushie.armourers_workshop.client.skin.cache.ClientSkinPaintCache;
import moe.plushie.armourers_workshop.client.skin.cache.FastCache;
import moe.plushie.armourers_workshop.common.addons.ModAddonManager;
import moe.plushie.armourers_workshop.common.init.blocks.ModBlocks;
import moe.plushie.armourers_workshop.common.init.items.ModItems;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import moe.plushie.armourers_workshop.common.library.LibraryFile;
import moe.plushie.armourers_workshop.common.library.LibraryFileType;
import moe.plushie.armourers_workshop.common.network.messages.server.MessageServerClientCommand.CommandType;
import moe.plushie.armourers_workshop.common.network.messages.server.MessageServerLibrarySendSkin.SendType;
import moe.plushie.armourers_workshop.common.painting.PaintingHelper;
import moe.plushie.armourers_workshop.common.skin.SkinExtractor;
import moe.plushie.armourers_workshop.common.skin.cache.CommonSkinCache;
import moe.plushie.armourers_workshop.common.skin.data.Skin;
import moe.plushie.armourers_workshop.common.skin.data.SkinIdentifier;
import moe.plushie.armourers_workshop.common.tileentities.TileEntityColourable;
import moe.plushie.armourers_workshop.common.tileentities.TileEntitySkinnable;
import moe.plushie.armourers_workshop.utils.ModLogger;
import moe.plushie.armourers_workshop.utils.SkinIOUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ICrashCallable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.lwjgl.Sys;

@Mod.EventBusSubscriber(modid = LibModInfo.ID, value = { Side.CLIENT })
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IBakedSkinReceiver {

    public static ClientWardrobeHandler wardrobeHandler;
    public static PlayerTextureHandler playerTextureHandler;
    private static PaletteManager paletteManager;

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        for (int i = 0; i < ModBlocks.BLOCK_LIST.size(); i++) {
            Block block = ModBlocks.BLOCK_LIST.get(i);
            if (block instanceof ICustomModel) {
                ((ICustomModel) block).registerModels();
            }
        }
        for (int i = 0; i < ModItems.ITEM_LIST.size(); i++) {
            Item item = ModItems.ITEM_LIST.get(i);
            if (item instanceof ICustomModel) {
                ((ICustomModel) item).registerModels();
            }
        }
        ModItems.SKIN.setTileEntityItemStackRenderer(new RenderItemEquipmentSkin());
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ConfigHandlerClient.init(new File(getModConfigDirectory(), "client.cfg"));
        new GuiResourceManager();

        ReflectionHelper.setPrivateValue(ArmourersWorkshopClientApi.class, null, SkinRenderHandlerApi.INSTANCE, "skinRenderHandler");

        RenderingRegistry.registerEntityRenderingHandler(EntityTippedArrow.class, new IRenderFactory() {

            @Override
            public Render createRenderFor(RenderManager manager) {
                return new RenderTippedArrowSkinned(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntitySpectralArrow.class, new IRenderFactory() {

            @Override
            public Render createRenderFor(RenderManager manager) {
                return new RenderSpectralArrowSkinned(manager);
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EntitySpectralArrow.class, RenderBendsSpectralArrow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTippedArrow.class, RenderBendsTippedArrow::new);
    }

    @Override
    public void initLibraryManager() {
        libraryManager = new ClientLibraryManager();
    }

    @Override
    public void initRenderers() {
        SkinModelRenderHelper.init();
        EntitySkinRenderHandler.init();
        new BlockHighlightRenderHandler();
        new SkinPreviewHandler();
        RenderBridge.init();
        /*
         * Render arrowRender = new RenderSkinnedArrow();
         * arrowRender.setRenderManager(RenderManager.instance);
         * RenderManager.instance.entityRenderMap.put(EntityArrow.class, arrowRender);
         */
        // Register tile entity renderers.
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySkinnable.class, new RenderBlockSkinnable());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityColourable.class, new RenderBlockColourable());

    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        wardrobeHandler = new ClientWardrobeHandler();
        playerTextureHandler = new PlayerTextureHandler();
        ClientSkinCache.init();
        FMLCommonHandler.instance().bus().register(new ModClientFMLEventHandler());
        FastCache.INSTANCE.loadCacheData();
        paletteManager = new PaletteManager(getModDirectory());
        MinecraftForge.EVENT_BUS.register(new RenderingEventHandler());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        ModAddonManager.initRenderers();
        EntitySkinRenderHandler.INSTANCE.initRenderer();

        loadErrorSkin();
        FMLCommonHandler.instance().registerCrashCallable(new ICrashCallable() {
            @Override
            public String call() throws Exception {
                int bakeQueue = ModelBakery.INSTANCE.getBakingQueueSize();
                String error = "\n";
                error += "\t\tBaking Queue: " + bakeQueue + "\n";
                error += "\t\tRequest Queue: " + (ClientSkinCache.INSTANCE.getRequestQueueSize() - bakeQueue) + "\n";
                error += "\t\tTexture Paint Type: " + getTexturePaintType().toString() + "\n";
                error += "\t\tMultipass Skin Rendering: " + useMultipassSkinRendering() + "\n";
                error += "\tRender Layers:";
                for (RenderPlayer playerRender : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
                    error += "\n\t\t Render Class: " + playerRender.getClass().getName();
                    Object object = ReflectionHelper.getPrivateValue(RenderLivingBase.class, playerRender, new String[] {"field_177097_h", "layerRenderers"});
                    if (object != null) {
                        List<LayerRenderer<?>> layerRenderers = (List<LayerRenderer<?>>) object;
                        for (LayerRenderer<?> layerRenderer : layerRenderers) {
                            error += "\n\t\t\t" + layerRenderer.getClass().getName();
                        }
                    }
                }
                return error;
            }

            @Override
            public String getLabel() {
                return "Armourer's Workshop";
            }
        });
    }

    public static TexturePaintType getTexturePaintType() {
        return TexturePaintType.TEXTURE_REPLACE;
    }

    public static boolean useMultipassSkinRendering() {
        return ConfigHandlerClient.multipassSkinRendering;
    }

    public static int getNumberOfRenderLayers() {
        if (useMultipassSkinRendering()) {
            return 4;
        } else {
            return 2;
        }
    }

    @Override
    public void receivedCommandFromSever(CommandType command) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        switch (command) {
        case CLEAR_MODEL_CACHE:
            ClientSkinCache.INSTANCE.clearCache();
            CommonSkinCache.INSTANCE.clearAll();
            ClientSkinPaintCache.INSTANCE.clear();
            break;
        case OPEN_MOD_FOLDER:
            openFolder(getModDirectory());
            break;
        }
    }

    private void openFolder(File folder) {
        String packPath = folder.getAbsolutePath();

        if (Util.getOSType() == Util.EnumOS.OSX) {
            try {
                Runtime.getRuntime().exec(new String[] { "/usr/bin/open", packPath });
                return;
            } catch (IOException ioexception1) {
                ArmourersWorkshop.getLogger().error("Couldn\'t open file: " + ioexception1);
            }
        } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
            String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[] { packPath });
            try {
                Runtime.getRuntime().exec(s1);
                return;
            } catch (IOException ioexception) {
                ArmourersWorkshop.getLogger().error("Couldn\'t open file: " + ioexception);
            }
        }

        boolean openedFailed = false;

        try {
            Class oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
            oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { folder.toURI() });
        } catch (Throwable throwable) {
            ArmourersWorkshop.getLogger().error("Couldn\'t open link: " + throwable);
            openedFailed = true;
        }

        if (openedFailed) {
            ArmourersWorkshop.getLogger().error("Opening via system class!");
            Sys.openURL("file://" + packPath);
        }
    }

    @Override
    public void receivedSkinFromLibrary(String fileName, String filePath, Skin skin, SendType sendType) {
        switch (sendType) {
        case LIBRARY_SAVE:
            SkinIOUtils.saveSkinFromFileName(filePath, fileName + SkinIOUtils.SKIN_FILE_EXTENSION, skin);
            ArmourersWorkshop.getProxy().libraryManager.addFileToListType(new LibraryFile(fileName, filePath, skin.getSkinType()), LibraryFileType.LOCAL, null);
            break;
        case GLOBAL_UPLOAD:
            break;
        }
    }

    @Override
    public boolean isLocalPlayer(String username) {
        GameProfile gameProfile = getLocalGameProfile();
        if (gameProfile != null && !StringUtils.isNullOrEmpty(gameProfile.getName())) {
            if (username.equals(gameProfile.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean haveFullLocalProfile() {
        GameProfile gameProfile = getLocalGameProfile();
        if (gameProfile.isComplete()) {
            if (gameProfile.getProperties().containsKey("textures")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public GameProfile getLocalGameProfile() {
        return Minecraft.getMinecraft().player.getGameProfile();
    }

    private void loadErrorSkin() {
        ModLogger.log("Loading error model.");
        InputStream inputStream = null;
        try {
            inputStream = SkinExtractor.class.getClassLoader().getResourceAsStream("assets/armourers_workshop/skins/error.armour");
            Skin skin = SkinIOUtils.loadSkinFromStream(inputStream);
            SkinIdentifier identifier = new SkinIdentifier(skin);
            ModelBakery.INSTANCE.receivedUnbakedModel(skin, identifier, identifier, this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public void onBakedSkin(BakedSkin bakedSkin) {
        ClientSkinCache.errorSkin = bakedSkin.getSkin();
        ModLogger.log("Error skin loaded.");
    }

    public static enum TexturePaintType {
        DISABLED, TEXTURE_REPLACE, MODEL_REPLACE_MC, MODEL_REPLACE_AW
    }

    private static class BlockColour implements IBlockColor {

        @Override
        public int colorMultiplier(IBlockState state, IBlockAccess worldIn, BlockPos pos, int tintIndex) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity != null && tileEntity instanceof IPantable) {
                return ((IPantable) tileEntity).getColour(tintIndex);
            }
            return 0xFFFFFFFF;
        }
    }

    private static class ItemColour implements IItemColor {

        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            if (tintIndex == 1) {
                return PaintingHelper.getToolDisplayColourRGB(stack);
            }
            return 0xFFFFFFFF;
        }
    }

    @Override
    public MinecraftServer getServer() {
        return Minecraft.getMinecraft().getIntegratedServer();
    }

    public static PaletteManager getPaletteManager() {
        return paletteManager;
    }

    @Override
    public void createCore()
    {
        Core.createAsClient();
    }
}
