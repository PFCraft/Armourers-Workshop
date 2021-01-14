package goblinbob.mobends.standard.animation.bit.player;

import goblinbob.mobends.core.animation.bit.AnimationBit;
import goblinbob.mobends.core.animation.layer.HardAnimationLayer;
import goblinbob.mobends.standard.animation.bit.biped.AttackSlashDownAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.AttackSlashInwardAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.AttackSlashOutwardAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.AttackSlashUpAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.AttackStanceSprintAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.AttackWhirlSlashAnimationBit;
import goblinbob.mobends.standard.animation.bit.biped.FistGuardAnimationBit;
import goblinbob.mobends.standard.data.BipedEntityData;
import goblinbob.mobends.standard.data.PlayerData;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class AttackAnimationBit extends AnimationBit<PlayerData>
{

	protected HardAnimationLayer<BipedEntityData<?>> layerBase;
	protected AttackStanceAnimationBit bitAttackStance;
	protected AttackStanceSprintAnimationBit bitAttackStanceSprint;
	protected AttackSlashUpAnimationBit bitAttackSlashUp;
	protected AttackSlashDownAnimationBit bitAttackSlashDown;
	protected AttackSlashInwardAnimationBit bitAttackSlashInward;
	protected AttackSlashOutwardAnimationBit bitAttackSlashOutward;
	protected AttackWhirlSlashAnimationBit bitAttackWhirlSlash;
	protected FistGuardAnimationBit bitFistGuard;
	protected PunchAnimationBit bitPunch;

	public AttackAnimationBit()
	{
		this.layerBase = new HardAnimationLayer<>();
		this.bitAttackStance = new AttackStanceAnimationBit();
		this.bitAttackStanceSprint = new AttackStanceSprintAnimationBit();
		this.bitAttackSlashUp = new AttackSlashUpAnimationBit();
		this.bitAttackSlashDown = new AttackSlashDownAnimationBit();
		this.bitAttackSlashInward = new AttackSlashInwardAnimationBit();
		this.bitAttackSlashOutward = new AttackSlashOutwardAnimationBit();
		this.bitAttackWhirlSlash = new AttackWhirlSlashAnimationBit();
		this.bitFistGuard = new FistGuardAnimationBit();
		this.bitPunch = new PunchAnimationBit();
	}

	@Override
	public String[] getActions(PlayerData entityData)
	{
		if (this.layerBase.isPlaying())
		{
			return this.layerBase.getPerformedBit().getActions(entityData);
		}

		return null;
	}

	public boolean shouldPerformAttack(AbstractClientPlayer player)
	{
		final ItemStack heldItemStack = player.getHeldItem(EnumHand.MAIN_HAND);
		return heldItemStack.getItem() != Items.AIR;
	}

	@Override
	public void perform(PlayerData playerData)
	{
		final AbstractClientPlayer player = playerData.getEntity();
		
		if (this.shouldPerformAttack(player))
		{
			if (playerData.getTicksAfterAttack() < 10)
			{
				if (playerData.getCurrentAttack() == 1)
				{
					this.layerBase.playOrContinueBit(this.bitAttackSlashUp, playerData);
				}
				else if (playerData.getCurrentAttack() == 2)
				{
					this.layerBase.playOrContinueBit(this.bitAttackSlashDown, playerData);
				}
				else if (playerData.getCurrentAttack() == 3)
				{
					this.layerBase.playOrContinueBit(this.bitAttackSlashInward, playerData);
				}
				else if (playerData.getCurrentAttack() == 4)
				{
					this.layerBase.playOrContinueBit(this.bitAttackSlashOutward, playerData);
				}
				else if (playerData.getCurrentAttack() == 5)
				{
					this.layerBase.playOrContinueBit(this.bitAttackWhirlSlash, playerData);
				}
				else
				{
					this.layerBase.clearAnimation();
				}
			}
			else if (playerData.getTicksAfterAttack() < 60 && playerData.isOnGround())
			{
				if (player.isSprinting())
				{
					this.layerBase.playOrContinueBit(this.bitAttackStanceSprint, playerData);
				}
				else if (playerData.isStillHorizontally())
				{
					this.layerBase.playOrContinueBit(this.bitAttackStance, playerData);
				}
				else
				{
					this.layerBase.clearAnimation();
				}
			}
			else
			{
				this.layerBase.clearAnimation();
			}
		}
		else
		{
			if (playerData.getTicksAfterAttack() < 10)
			{
				this.layerBase.playOrContinueBit(this.bitPunch, playerData);
			}
			else if (playerData.getTicksAfterAttack() < 60 && playerData.isStillHorizontally())
			{
				this.layerBase.playOrContinueBit(this.bitFistGuard, playerData);
			}
			else
			{
				this.layerBase.clearAnimation();
			}
		}

		this.layerBase.perform(playerData);
	}

}
