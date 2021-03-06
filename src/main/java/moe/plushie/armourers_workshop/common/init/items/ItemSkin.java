package moe.plushie.armourers_workshop.common.init.items;

import java.util.List;
import moe.plushie.armourers_workshop.api.common.capability.IEntitySkinCapability;
import moe.plushie.armourers_workshop.api.common.capability.IPlayerWardrobeCap;
import moe.plushie.armourers_workshop.api.common.skin.data.ISkinDescriptor;
import moe.plushie.armourers_workshop.api.common.skin.data.ISkinIdentifier;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.client.config.ConfigHandlerClient;
import moe.plushie.armourers_workshop.client.skin.cache.ClientSkinCache;
import moe.plushie.armourers_workshop.common.capability.entityskin.EntitySkinCapability;
import moe.plushie.armourers_workshop.common.capability.wardrobe.player.PlayerWardrobeCap;
import moe.plushie.armourers_workshop.common.lib.LibItemNames;
import moe.plushie.armourers_workshop.common.skin.data.Skin;
import moe.plushie.armourers_workshop.common.skin.data.SkinDescriptor;
import moe.plushie.armourers_workshop.common.skin.data.SkinProperties;
import moe.plushie.armourers_workshop.common.skin.type.SkinTypeRegistry;
import moe.plushie.armourers_workshop.common.world.BlockSkinPlacementHelper;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import moe.plushie.armourers_workshop.utils.SkinUtils;
import moe.plushie.armourers_workshop.utils.TranslateUtils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

public class ItemSkin extends AbstractModItem {

    public ItemSkin() {
        super(LibItemNames.SKIN, false);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, dispenserBehavior);
    }

    public ISkinType getSkinType(ItemStack stack) {
        return SkinNBTHelper.getSkinTypeFromStack(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Skin skin = SkinUtils.getSkinDetectSide(stack, true, false);
        if (skin != null) {
            if (!skin.getCustomName().trim().isEmpty()) {
                return skin.getCustomName();
            }
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        IBlockState state = worldIn.getBlockState(pos);
        ISkinDescriptor descriptor = SkinNBTHelper.getSkinDescriptorFromStack(stack);
        if (descriptor != null && descriptor.getIdentifier().getSkinType() == SkinTypeRegistry.skinBlock) {
            Skin skin = SkinUtils.getSkinDetectSide(descriptor, false, true);
            if (skin != null) {
                IBlockState replaceBlock = worldIn.getBlockState(pos.offset(facing));
                if (replaceBlock.getBlock().isReplaceable(worldIn, pos.offset(facing))) {
                    BlockSkinPlacementHelper.placeSkinAtLocation(worldIn, player, facing, stack, pos.offset(facing), skin, descriptor);
                    return EnumActionResult.SUCCESS;
                }
            }
            return EnumActionResult.FAIL;
        }
        return EnumActionResult.PASS;
    }

    private static final IBehaviorDispenseItem dispenserBehavior = new BehaviorDefaultDispenseItem() {

        @Override
        protected ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack) {
            if (!SkinNBTHelper.stackHasSkinData(itemStack)) {
                return super.dispenseStack(blockSource, itemStack);
            }

            IBlockState state = blockSource.getBlockState();
            EnumFacing facing = state.getValue(BlockDispenser.FACING);
            BlockPos target = blockSource.getBlockPos().offset(facing);
            AxisAlignedBB axisalignedbb = new AxisAlignedBB(target);
            List list = blockSource.getWorld().getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
            for (int i = 0; i < list.size(); i++) {
                EntityLivingBase entitylivingbase = (EntityLivingBase) list.get(i);
                if (entitylivingbase instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entitylivingbase;
                    IEntitySkinCapability skinCap = EntitySkinCapability.get(player);
                    if (skinCap.setStackInNextFreeSlot(itemStack.copy())) {
                        itemStack.shrink(1);
                        skinCap.syncToAllTracking();
                        skinCap.syncToPlayer((EntityPlayerMP) player);
                        return itemStack;
                    }
                }
            }

            return super.dispenseStack(blockSource, itemStack);
        }
    };
}
