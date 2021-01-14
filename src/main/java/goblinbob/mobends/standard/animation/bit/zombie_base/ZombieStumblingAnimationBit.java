package goblinbob.mobends.standard.animation.bit.zombie_base;

import goblinbob.mobends.core.animation.bit.AnimationBit;
import goblinbob.mobends.standard.data.ZombieDataBase;
import net.minecraft.util.math.MathHelper;

public class ZombieStumblingAnimationBit extends AnimationBit<ZombieDataBase<?>>
{
	@Override
	public String[] getActions(ZombieDataBase<?> data)
	{
		return new String[] { "stumbling" };
	}

	@Override
	public void perform(ZombieDataBase<?> data)
	{
		
		final float PI = (float) Math.PI;
		float limbSwing = data.limbSwing.get() * 0.6662F;
		limbSwing += Math.cos(limbSwing * 2.0F) * 0.3F;
		float swingAmount = 45F * data.limbSwingAmount.get();

		data.rightLeg.rotation.setSmoothness(1F).orientX((MathHelper.cos(limbSwing) * swingAmount));
		data.leftLeg.rotation.setSmoothness(1F).orientX((MathHelper.cos(limbSwing + PI) * swingAmount));
		data.rightArm.rotation.setSmoothness(1F).orientX((MathHelper.cos(limbSwing + PI) * swingAmount));
		data.leftArm.rotation.setSmoothness(1F).orientX((MathHelper.cos(limbSwing) * swingAmount));
		data.body.rotation.setSmoothness(.5F).orientY((MathHelper.cos(limbSwing + PI) * swingAmount));
		
		float heavyStompValue = Math.min((limbSwing % PI) / PI, 1F);
		float heavyStompValueInv = 1F - Math.min((limbSwing % PI) / PI, 1F);
		data.body.rotation.rotateX(heavyStompValueInv * 40F);
		data.body.rotation.rotateZ(MathHelper.cos(limbSwing) * 10F);
		
		data.head.rotation.rotateX(-heavyStompValueInv * 40F);
		// Head tilt
		data.head.rotation.rotateZ(-40F + heavyStompValue * 20.0F);
	}
}
