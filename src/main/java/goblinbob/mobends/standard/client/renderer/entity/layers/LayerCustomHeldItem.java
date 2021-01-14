package goblinbob.mobends.standard.client.renderer.entity.layers;

import goblinbob.mobends.core.data.EntityData;
import goblinbob.mobends.core.data.EntityDatabase;
import goblinbob.mobends.core.math.SmoothOrientation;
import goblinbob.mobends.core.util.GlHelper;
import goblinbob.mobends.standard.data.BipedEntityData;
import moe.plushie.armourers_workshop.api.common.IExtraColours;
import moe.plushie.armourers_workshop.api.common.capability.IPlayerWardrobeCap;
import moe.plushie.armourers_workshop.api.common.capability.IWardrobeCap;
import moe.plushie.armourers_workshop.api.common.skin.data.ISkinDescriptor;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.client.config.ConfigHandlerClient;
import moe.plushie.armourers_workshop.client.gui.wardrobe.tab.GuiTabWardrobeContributor;
import moe.plushie.armourers_workshop.client.handler.ClientWardrobeHandler;
import moe.plushie.armourers_workshop.client.render.SkinModelRenderHelper;
import moe.plushie.armourers_workshop.client.render.SkinRenderData;
import moe.plushie.armourers_workshop.client.skin.cache.ClientSkinCache;
import moe.plushie.armourers_workshop.common.Contributors;
import moe.plushie.armourers_workshop.common.capability.entityskin.EntitySkinCapability;
import moe.plushie.armourers_workshop.common.capability.wardrobe.ExtraColours;
import moe.plushie.armourers_workshop.common.capability.wardrobe.player.PlayerWardrobeCap;
import moe.plushie.armourers_workshop.common.skin.data.Skin;
import moe.plushie.armourers_workshop.common.skin.data.SkinDye;
import moe.plushie.armourers_workshop.common.skin.data.SkinProperties;
import moe.plushie.armourers_workshop.common.skin.type.SkinTypeRegistry;
import moe.plushie.armourers_workshop.proxies.ClientProxy;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerCustomHeldItem implements LayerRenderer<EntityLivingBase>
{

    protected final RenderLivingBase<?> livingEntityRenderer;

    public LayerCustomHeldItem(RenderLivingBase<?> livingEntityRendererIn)
    {
        this.livingEntityRenderer = livingEntityRendererIn;
    }

    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
        ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
        ItemStack itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();

        if (!itemstack.isEmpty() || !itemstack1.isEmpty())
        {
            GlStateManager.pushMatrix();

            if (this.livingEntityRenderer.getMainModel().isChild)
            {
                GlStateManager.translate(0.0F, 0.75F, 0.0F);
                GlStateManager.scale(0.5F, 0.5F, 0.5F);
            }

            this.renderHeldItem(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
            this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
            GlStateManager.popMatrix();
        }

        if (GuiTabWardrobeContributor.testMode) {
            Contributors.Contributor contributor = Contributors.INSTANCE.getContributor(((EntityPlayer)entitylivingbaseIn).getGameProfile());
            if (contributor != null) {
            }
        }

        double distance = Minecraft.getMinecraft().player.getDistance(entitylivingbaseIn);
        if (distance > ConfigHandlerClient.renderDistanceSkin) {
            return;
        }
        ((ModelBiped)livingEntityRenderer.getMainModel()).bipedLeftArm.isHidden = false;
        ((ModelBiped)livingEntityRenderer.getMainModel()).bipedRightArm.isHidden = false;
        EntitySkinCapability skinCap = (EntitySkinCapability) EntitySkinCapability.get(entitylivingbaseIn);
        if (skinCap == null) {
            return;
        }

        skinCap.hideHead = false;
        skinCap.hideChest = false;
        skinCap.hideArmLeft = false;
        skinCap.hideArmRight = false;
        skinCap.hideLegLeft = false;
        skinCap.hideLegRight = false;

        skinCap.hideHeadOverlay = false;
        skinCap.hideChestOverlay = false;
        skinCap.hideArmLeftOverlay = false;
        skinCap.hideArmRightOverlay = false;
        skinCap.hideLegLeftOverlay = false;
        skinCap.hideLegRightOverlay = false;

        ISkinType[] skinTypes = skinCap.getValidSkinTypes();
        SkinModelRenderHelper modelRenderer = SkinModelRenderHelper.INSTANCE;
        IExtraColours extraColours = ExtraColours.EMPTY_COLOUR;
        IPlayerWardrobeCap wardrobe = PlayerWardrobeCap.get((EntityPlayer)entitylivingbaseIn);
        if (wardrobe != null) {
            extraColours = wardrobe.getExtraColours();
        }

        for (int i = 0; i < skinTypes.length; i++) {
            ISkinType skinType = skinTypes[i];
            ISkinDescriptor skinDescriptorArmour = getSkinDescriptorFromArmourer(entitylivingbaseIn, skinType);
            if (skinDescriptorArmour != null) {
                renderSkin((EntityPlayer) entitylivingbaseIn, skinDescriptorArmour, skinCap, wardrobe, extraColours, distance, entitylivingbaseIn != Minecraft.getMinecraft().player);
            } else {
                if (skinType.getVanillaArmourSlotId() != -1 | skinType == SkinTypeRegistry.skinWings | skinType == SkinTypeRegistry.skinOutfit) {
                    for (int skinIndex = 0; skinIndex < skinCap.getSlotCountForSkinType(skinType); skinIndex++) {
                        ISkinDescriptor skinDescriptor = skinCap.getSkinDescriptor(skinType, skinIndex);
                        if (skinDescriptor != null) {
                            renderSkin((EntityPlayer) entitylivingbaseIn, skinDescriptor, skinCap, wardrobe, extraColours, distance, entitylivingbaseIn != Minecraft.getMinecraft().player);
                        }
                    }
                }
            }
        }
    }

    private void renderHeldItem(EntityLivingBase entity, ItemStack p_188358_2_, ItemCameraTransforms.TransformType p_188358_3_, EnumHandSide handSide)
    {
        if (!p_188358_2_.isEmpty())
        {
            GlStateManager.pushMatrix();

            if (entity.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            // Forge: moved this call down, fixes incorrect offset while sneaking.
            this.translateToHand(handSide, entity);
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            boolean flag = handSide == EnumHandSide.LEFT;
            GlStateManager.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, p_188358_2_, p_188358_3_, flag);
            GlStateManager.popMatrix();
        }
    }

    /**
     * MO BENDS
     * This is the only function that had it's code changed
     */
    protected void translateToHand(EnumHandSide handSide, EntityLivingBase entity)
    {
    	((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, handSide);
    	
    	EntityData<?> entityData = EntityDatabase.instance.get(entity);
    	if (entityData instanceof BipedEntityData)
    	{
    		BipedEntityData<?> bipedData = (BipedEntityData<?>) entityData;
    		SmoothOrientation itemRotation = handSide == EnumHandSide.RIGHT ? bipedData.renderRightItemRotation : bipedData.renderLeftItemRotation;
    		
    		GlStateManager.translate(0, 8F * 0.0625F, 0);
    		GlHelper.rotate(itemRotation.getSmooth());
            GlStateManager.translate(0, -8F * 0.0625F, 0);
    	}
    }

    private void renderSkin(EntityPlayer entityPlayer, ISkinDescriptor skinDescriptor, EntitySkinCapability skinCap, IWardrobeCap wardrobe, IExtraColours extraColours, double distance, boolean doLodLoading) {
        SkinModelRenderHelper modelRenderer = SkinModelRenderHelper.INSTANCE;
        Skin skin = ClientSkinCache.INSTANCE.getSkin(skinDescriptor);

        if (skin != null) {
            if (SkinProperties.PROP_MODEL_OVERRIDE_HEAD.getValue(skin.getProperties())) {
                skinCap.hideHead = true;
            }
            if (SkinProperties.PROP_MODEL_OVERRIDE_CHEST.getValue(skin.getProperties())) {
                skinCap.hideChest = true;
            }
            if (SkinProperties.PROP_MODEL_OVERRIDE_ARM_LEFT.getValue(skin.getProperties())) {
                skinCap.hideArmLeft = true;
            }
            if (SkinProperties.PROP_MODEL_OVERRIDE_ARM_RIGHT.getValue(skin.getProperties())) {
                skinCap.hideArmRight = true;
            }
            if (SkinProperties.PROP_MODEL_OVERRIDE_LEG_LEFT.getValue(skin.getProperties())) {
                skinCap.hideLegLeft = true;
            }
            if (SkinProperties.PROP_MODEL_OVERRIDE_LEG_RIGHT.getValue(skin.getProperties())) {
                skinCap.hideLegRight = true;
            }

            if (SkinProperties.PROP_MODEL_HIDE_OVERLAY_HEAD.getValue(skin.getProperties())) {
                skinCap.hideHeadOverlay = true;
            }
            if (SkinProperties.PROP_MODEL_HIDE_OVERLAY_CHEST.getValue(skin.getProperties())) {
                skinCap.hideChestOverlay = true;
            }
            if (SkinProperties.PROP_MODEL_HIDE_OVERLAY_ARM_LEFT.getValue(skin.getProperties())) {
                skinCap.hideArmLeftOverlay = true;
            }
            if (SkinProperties.PROP_MODEL_HIDE_OVERLAY_ARM_RIGHT.getValue(skin.getProperties())) {
                skinCap.hideArmRightOverlay = true;
            }
            if (SkinProperties.PROP_MODEL_HIDE_OVERLAY_LEG_LEFT.getValue(skin.getProperties())) {
                skinCap.hideLegLeftOverlay = true;
            }
            if (SkinProperties.PROP_MODEL_HIDE_OVERLAY_LEG_RIGHT.getValue(skin.getProperties())) {
                skinCap.hideLegRightOverlay = true;
            }

            if (skin.hasPaintData() & ClientProxy.getTexturePaintType() == ClientProxy.TexturePaintType.MODEL_REPLACE_AW) {
                if (skin.getSkinType() == SkinTypeRegistry.skinHead) {
                    skinCap.hideHead = true;
                }
                if (skin.getSkinType() == SkinTypeRegistry.skinChest) {
                    skinCap.hideChest = true;
                    skinCap.hideArmLeft = true;
                    skinCap.hideArmRight = true;
                }
                if (skin.getSkinType() == SkinTypeRegistry.skinLegs) {
                    skinCap.hideLegLeft = true;
                    skinCap.hideLegRight = true;
                }
                if (skin.getSkinType() == SkinTypeRegistry.skinFeet) {
                    skinCap.hideLegLeft = true;
                    skinCap.hideLegRight = true;
                }
            }

            SkinDye dye = new SkinDye(wardrobe.getDye());
            for (int i = 0; i < 8; i++) {
                if (skinDescriptor.getSkinDye().haveDyeInSlot(i)) {
                    dye.addDye(i, skinDescriptor.getSkinDye().getDyeColour(i));
                }
            }
            GlStateManager.pushMatrix();
            modelRenderer.renderEquipmentPart(skin, new SkinRenderData(0.0625F, dye, extraColours, distance, doLodLoading, false, false, ((AbstractClientPlayer) entityPlayer).getLocationSkin()), entityPlayer, (ModelBiped) livingEntityRenderer.getMainModel());
            // modelRenderer.renderEquipmentPart(entityPlayer, renderPlayer.getMainModel(),
            // skin, dye, extraColours, distance, doLodLoading);
            GlStateManager.popMatrix();
        }
    }

    private ISkinDescriptor getSkinDescriptorFromArmourer(Entity entity, ISkinType skinType) {
        if (skinType.getVanillaArmourSlotId() >= 0 && skinType.getVanillaArmourSlotId() < 4) {
            int slot = 3 - skinType.getVanillaArmourSlotId();
            ItemStack armourStack = ClientWardrobeHandler.getArmourInSlot(slot);
            return SkinNBTHelper.getSkinDescriptorFromStack(armourStack);
        }
        return null;
    }


    public boolean shouldCombineTextures()
    {
        return false;
    }

}