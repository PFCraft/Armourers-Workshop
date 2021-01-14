package goblinbob.mobends.standard.animation.bit.pigzombie;

import goblinbob.mobends.standard.data.PigZombieData;
import net.minecraft.util.math.MathHelper;

public class WalkAnimationBit extends goblinbob.mobends.standard.animation.bit.biped.WalkAnimationBit<PigZombieData>
{
	@Override
	public void perform(PigZombieData data)
	{
		super.perform(data);
		
		data.globalOffset.slideY(-3F);
		
		data.body.rotation.localRotateX(20F)
						  .rotateZ(-10F);
		data.head.rotation.rotateX(-20F);
		
		data.rightArm.rotation.rotateX(-20F)
						      .rotateZ(10F);
		data.leftArm.rotation.rotateX(-20F)
	    					 .rotateZ(10F);
		
		data.rightLeg.rotation.rotateZ(10F);
		data.leftLeg.rotation.rotateZ(-10F);
		
		data.rightLeg.rotation.rotateX(-30F);
		data.leftLeg.rotation.rotateX(-10F)
							 .rotateY(-10F);
		
		data.rightForeLeg.rotation.rotateX(25);
		data.leftForeLeg.rotation.rotateX(25);
		
		float limbSwing = data.limbSwing.get() * 0.6662F;
		data.globalOffset.slideY(Math.abs(MathHelper.sin(limbSwing)) * -1.4F - 3F);
	}
}
