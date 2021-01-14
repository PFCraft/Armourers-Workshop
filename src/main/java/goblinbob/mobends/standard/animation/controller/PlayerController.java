package goblinbob.mobends.standard.animation.controller;

import goblinbob.mobends.core.animation.bit.AnimationBit;
import goblinbob.mobends.core.animation.controller.IAnimationController;
import goblinbob.mobends.core.animation.keyframe.ArmatureMask;
import goblinbob.mobends.core.animation.layer.HardAnimationLayer;
import goblinbob.mobends.standard.animation.bit.biped.BowAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.EatingAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.FallingAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.HarvestAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.JumpAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.LadderClimbAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.RidingAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.ShieldAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.SittingAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.SneakAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.StandAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.SwimmingAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.TorchHoldingAnimationBit;
import goblinbob.mobends.standard.animation.bit.player.AttackAnimationBit;
import goblinbob.mobends.standard.animation.bit.player.CapeAnimationBit;
import goblinbob.mobends.standard.animation.bit.player.ElytraAnimationBit;
import goblinbob.mobends.standard.animation.bit.player.FlyingAnimationBit;
import goblinbob.mobends.standard.animation.bit.player.SleepingAnimationBit;
import goblinbob.mobends.standard.animation.bit.player.SprintAnimationBit;
import goblinbob.mobends.standard.animation.bit.player.SprintJumpAnimationBit;
import goblinbob.mobends.standard.animation.bit.player.WalkAnimationBit;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.data.PlayerData;
import goblinbob.mobends.standard.main.ModConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;

/**
 * This is an animation controller for a player instance. It's a part of the EntityData structure.
 *
 * @author Iwo Plaza
 */
public class PlayerController implements IAnimationController<PlayerData>
{

    protected HardAnimationLayer<BipedEntityData<?>> layerBase = new HardAnimationLayer<>();
    protected HardAnimationLayer<BipedEntityData<?>> layerTorch = new HardAnimationLayer<>();
    protected HardAnimationLayer<BipedEntityData<?>> layerSneak = new HardAnimationLayer<>();
    protected HardAnimationLayer<BipedEntityData<?>> layerAction = new HardAnimationLayer<>();
    protected HardAnimationLayer<BipedEntityData<?>> layerCape = new HardAnimationLayer<>();

    protected AnimationBit<BipedEntityData<?>> bitStand = new StandAnimationBit<>();
    protected AnimationBit<BipedEntityData<?>> bitJump = new JumpAnimationBit<>();
    protected AnimationBit<BipedEntityData<?>> bitSneak = new SneakAnimationBit();
    protected AnimationBit<BipedEntityData<?>> bitLadderClimb = new LadderClimbAnimationBit();
    protected AnimationBit<BipedEntityData<?>> bitSwimming = new SwimmingAnimationBit();
    protected AnimationBit<BipedEntityData<?>> bitRiding = new RidingAnimationBit();
    protected AnimationBit<BipedEntityData<?>> bitSitting = new SittingAnimationBit();
    protected AnimationBit<BipedEntityData<?>> bitFalling = new FallingAnimationBit();
    protected AnimationBit<PlayerData> bitWalk = new WalkAnimationBit();
    protected AnimationBit<PlayerData> bitSprint = new SprintAnimationBit();
    protected AnimationBit<PlayerData> bitSprintJump = new SprintJumpAnimationBit();
    protected AnimationBit<BipedEntityData<?>> bitTorchHolding = new TorchHoldingAnimationBit();
    protected AnimationBit<PlayerData> bitAttack = new AttackAnimationBit();
    protected FlyingAnimationBit bitFlying = new FlyingAnimationBit();
    protected ElytraAnimationBit bitElytra = new ElytraAnimationBit();
    protected BowAnimationBit bitBow = new BowAnimationBit();
    protected EatingAnimationBit bitEating = new EatingAnimationBit();
    protected HarvestAnimationBit bitHarvest = new HarvestAnimationBit();
    protected ShieldAnimationBit bitShield = new ShieldAnimationBit();
    protected CapeAnimationBit bitCape = new CapeAnimationBit();
    protected SleepingAnimationBit bitSleeping = new SleepingAnimationBit();

    protected ArmatureMask upperBodyOnlyMask;

    public PlayerController()
    {
        this.upperBodyOnlyMask = new ArmatureMask(ArmatureMask.Mode.EXCLUDE_ONLY);
        this.upperBodyOnlyMask.exclude("root");
        this.upperBodyOnlyMask.exclude("head");
        this.upperBodyOnlyMask.exclude("leftLeg");
        this.upperBodyOnlyMask.exclude("leftForeLeg");
        this.upperBodyOnlyMask.exclude("rightLeg");
        this.upperBodyOnlyMask.exclude("rightForeLeg");
    }

    public static boolean isHoldingFood(Item activeItem)
    {
        return activeItem instanceof ItemFood;
    }

    public static boolean isHoldingBow(ModelBiped.ArmPose mainArmPose, ModelBiped.ArmPose offArmPose)
    {
        return mainArmPose == ArmPose.BOW_AND_ARROW || offArmPose == ArmPose.BOW_AND_ARROW;
    }

    public static boolean isShielding(ModelBiped.ArmPose mainArmPose, ModelBiped.ArmPose offArmPose)
    {
        return mainArmPose == ArmPose.BLOCK || offArmPose == ArmPose.BLOCK;
    }

    public static boolean isHoldingWeapon(Item heldItemMainhand)
    {
        return heldItemMainhand instanceof ItemSword || ModConfig.getItemClassification(heldItemMainhand) == ModConfig.ItemClassification.WEAPON;
    }

    public void performActionAnimations(PlayerData data, AbstractClientPlayer player)
    {
        if (player.isEntityAlive() && player.isPlayerSleeping())
        {
            layerAction.clearAnimation();
            return;
        }

        final EnumHandSide primaryHand = player.getPrimaryHand();
        final EnumHandSide offHand = primaryHand == EnumHandSide.RIGHT ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
        final ItemStack heldItemMainhand = player.getHeldItemMainhand();
        final ItemStack heldItemOffhand = player.getHeldItemOffhand();
        final Item activeItem = player.getActiveItemStack().getItem();
        final ModelBiped.ArmPose armPoseMain = getAction(player, heldItemMainhand);
        final ModelBiped.ArmPose armPoseOff = getAction(player, heldItemOffhand);
        final EnumHandSide activeHandSide = player.getActiveHand() == EnumHand.MAIN_HAND ? primaryHand : offHand;

        if (isShielding(armPoseMain, armPoseOff))
        {
            bitShield.setActionHand(armPoseMain == ArmPose.BLOCK ? primaryHand : offHand);
            layerAction.playOrContinueBit(bitShield, data);
        }
        else if (isHoldingFood(activeItem))
        {
            bitEating.setActionHand(activeHandSide);
            layerAction.playOrContinueBit(bitEating, data);
        }
        else if (isHoldingBow(armPoseMain, armPoseOff))
        {
            bitBow.setActionHand(armPoseMain == ArmPose.BOW_AND_ARROW ? primaryHand : offHand);
            layerAction.playOrContinueBit(bitBow, data);
        }
        else if (isHoldingWeapon(heldItemMainhand.getItem()) || heldItemMainhand.isEmpty())
        {
            layerAction.playOrContinueBit(bitAttack, data);
        }
        else
        {
            bitHarvest.setActionHand(primaryHand);
            if (player.isSwingInProgress)
                layerAction.playOrContinueBit(bitHarvest, data);
            else
                layerAction.clearAnimation();
        }
    }

    @Override
    public Collection<String> perform(PlayerData data)
    {
        final AbstractClientPlayer player = data.getEntity();

        layerCape.playOrContinueBit(bitCape, data);

        if (player.isEntityAlive() && player.isPlayerSleeping())
        {
            layerBase.playOrContinueBit(bitSleeping, data);
            layerSneak.clearAnimation();
        }
        else if (player.isRiding())
        {
            if (player.getRidingEntity() instanceof EntityLivingBase)
            {
                layerBase.playOrContinueBit(bitRiding, data);
            }
            else
            {
                layerBase.playOrContinueBit(bitSitting, data);
            }
            layerSneak.clearAnimation();
        }
        else
        {
            if (player.getTicksElytraFlying() > 4)
            {
                layerBase.playOrContinueBit(bitElytra, data);
                layerSneak.clearAnimation();
                layerTorch.clearAnimation();
            }
            else if (data.isClimbing())
            {
                layerBase.playOrContinueBit(bitLadderClimb, data);
                layerSneak.clearAnimation();
                layerTorch.clearAnimation();
            }
            else if (player.isInWater())
            {
                layerBase.playOrContinueBit(bitSwimming, data);
                layerSneak.clearAnimation();
                layerTorch.clearAnimation();
            }
            else if (!data.isOnGround() || data.getTicksAfterTouchdown() < 1)
            {
                // Airborne
                if (data.isFlying())
                {
                    // Flying
                    layerBase.playOrContinueBit(bitFlying, data);
                }
                else
                {
                    if (data.getTicksFalling() > FallingAnimationBit.TICKS_BEFORE_FALLING)
                    {
                        layerBase.playOrContinueBit(bitFalling, data);
                    }
                    else
                    {
                        if (player.isSprinting())
                            layerBase.playOrContinueBit(bitSprintJump, data);
                        else
                            layerBase.playOrContinueBit(bitJump, data);
                    }
                }

                layerSneak.clearAnimation();
                layerTorch.clearAnimation();
            }
            else
            {
                if (data.isStillHorizontally())
                {
                    layerBase.playOrContinueBit(bitStand, data);
                    layerTorch.playOrContinueBit(bitTorchHolding, data);
                }
                else
                {
                    if (player.isSprinting())
                    {
                        layerBase.playOrContinueBit(bitSprint, data);
                        layerTorch.clearAnimation();
                    }
                    else
                    {
                        layerBase.playOrContinueBit(bitWalk, data);
                        layerTorch.playOrContinueBit(bitTorchHolding, data);
                    }
                }

                if (player.isSneaking())
                    layerSneak.playOrContinueBit(bitSneak, data);
                else
                    layerSneak.clearAnimation();
            }
        }

        this.performActionAnimations(data, player);

        final List<String> actions = new ArrayList<>();
        layerBase.perform(data, actions);
        layerSneak.perform(data, actions);
        layerTorch.perform(data, actions);
        layerAction.perform(data, actions);
        layerCape.perform(data, actions);
        return actions;
    }

    private ArmPose getAction(AbstractClientPlayer player, ItemStack heldItem)
    {
        if (!heldItem.isEmpty())
        {
            if (player.getItemInUseCount() > 0)
            {
                EnumAction enumaction = heldItem.getItemUseAction();

                if (enumaction == EnumAction.BLOCK)
                    return ArmPose.BLOCK;
                else if (enumaction == EnumAction.BOW)
                    return ArmPose.BOW_AND_ARROW;
            }

            return ArmPose.ITEM;
        }

        return ArmPose.EMPTY;
    }

}
