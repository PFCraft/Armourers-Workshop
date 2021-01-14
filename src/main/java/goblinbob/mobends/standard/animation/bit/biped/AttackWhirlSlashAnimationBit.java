package goblinbob.mobends.standard.animation.bit.biped;

import goblinbob.mobends.core.animation.bit.AnimationBit;
import goblinbob.mobends.core.client.model.IModelPart;
import goblinbob.mobends.core.math.SmoothOrientation;
import goblinbob.mobends.core.util.GUtil;
import goblinbob.mobends.standard.data.BipedEntityData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.vector.Vector3f;

public class AttackWhirlSlashAnimationBit extends AnimationBit<BipedEntityData<?>>
{
	private static final String[] ACTIONS = new String[] { "attack", "attack_2" };
	
	@Override
	public String[] getActions(BipedEntityData<?> entityData)
	{
		return ACTIONS;
	}

	@Override
	public void perform(BipedEntityData<?> data)
	{
		data.localOffset.slideToZero(0.3F);

		final EntityLivingBase living = data.getEntity();
		final EnumHandSide primaryHand = living.getPrimaryHand();

		boolean mainHandSwitch = primaryHand == EnumHandSide.RIGHT;
		// Main Hand Direction Multiplier - it helps switch animation sides depending on
		// what is your main hand.
		float handDirMtp = mainHandSwitch ? 1 : -1;
		IModelPart mainArm = mainHandSwitch ? data.rightArm : data.leftArm;
		IModelPart offArm = mainHandSwitch ? data.leftArm : data.rightArm;
		IModelPart mainForeArm = mainHandSwitch ? data.rightForeArm : data.leftForeArm;
		IModelPart offForeArm = mainHandSwitch ? data.leftForeArm : data.rightForeArm;
		SmoothOrientation mainItemRotation = mainHandSwitch ? data.renderRightItemRotation : data.renderLeftItemRotation;
		
		if (data.getTicksAfterAttack() < 0.5f)
		{
			data.swordTrail.reset();
		}

		if (living.getHeldItem(EnumHand.MAIN_HAND) != null)
		{
			if (living.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword)
			{
				data.swordTrail.add(data);
			}
		}

		float attackState = data.getTicksAfterAttack() / 10.0f;
		float armSwing = attackState * 2.0f;
		armSwing = Math.min(armSwing, 1F);

		float var5 = GUtil.clamp(attackState * 1.6F, 0F, 1F);

		Vector3f bodyRot = new Vector3f(0, 0, 0);
		bodyRot.x = 20F - attackState * 20F;
		bodyRot.y = 20F * attackState * handDirMtp;

		data.body.rotation.setSmoothness(.9F).orientX(bodyRot.x)
				.orientY(bodyRot.y);
		data.head.rotation.orientX(MathHelper.wrapDegrees(data.headPitch.get()) - bodyRot.x)
						  .rotateY(MathHelper.wrapDegrees(data.headYaw.get()) - bodyRot.y - 30);
		
		offArm.getRotation().setSmoothness(.3F).orientZ(20F * handDirMtp);
		offArm.getRotation().setSmoothness(.3F).orientZ(-80F * handDirMtp);
		
		mainArm.getRotation().setSmoothness(.3F).orientZ(-(-10.0f - var5 * 120) * handDirMtp)
				.rotateInstantY(-20 + armSwing * 70);

		mainForeArm.getRotation().setSmoothness(.3F).orientX(-20);
		offForeArm.getRotation().setSmoothness(.3F).orientX(-60);

		if (data.isStillHorizontally())
		{
			data.rightLeg.rotation.setSmoothness(.3F).orientX(-30F)
					.rotateZ(10)
					.rotateY(25);
			data.leftLeg.rotation.setSmoothness(.3F).orientX(-30F)
					.rotateZ(-10)
					.rotateY(-25);
			
			data.rightForeLeg.rotation.setSmoothness(.3F).orientX(30F);
			data.leftForeLeg.rotation.setSmoothness(.3F).orientX(30F);
		}
		
		data.globalOffset.slideY(-2F);
		mainItemRotation.setSmoothness(.9F).orientX(90 * attackState);
		float renderRotationY = 30 + 360 * var5;
		data.renderRotation.orientInstantY(MathHelper.wrapDegrees(-renderRotationY * handDirMtp));
	}
}
