package goblinbob.mobends.standard.animation.bit.player;

import goblinbob.mobends.core.animation.bit.AnimationBit;
import goblinbob.mobends.standard.data.PlayerData;

public class PunchAnimationBit extends AnimationBit<PlayerData>
{

	@Override
	public String[] getActions(PlayerData entityData)
	{
		return new String[] { "attack", "punch" };
	}

	@Override
	public void perform(PlayerData data)
	{
		data.rightArm.rotation.setSmoothness(.3F).orientX(-90).rotateZ(20);
		data.leftArm.rotation.setSmoothness(.3F).orientZ(-20).rotateX(-90);

		data.rightForeArm.rotation.setSmoothness(.3F).orientX(-80);
		data.leftForeArm.rotation.setSmoothness(.3F).orientX(-80);

		float renderRotationY = 0F;

		if (data.isStillHorizontally())
		{
			renderRotationY = -20F;
			data.globalOffset.slideY(-2.0f);

			data.rightLeg.rotation.setSmoothness(.3F).orientX(-30F).rotateZ(10);
			data.leftLeg.rotation.setSmoothness(.3F).orientX(-30F).rotateY(-25F).rotateZ(-10);
			data.rightForeLeg.rotation.setSmoothness(.3F).orientX(30);
			data.leftForeLeg.rotation.setSmoothness(.3F).orientX(30);
		}

		if (data.getFistPunchArm())
		{
			data.rightArm.rotation.setSmoothness(.9F).orientY(-90).rotateX(-90.0f + data.headPitch.get()).rotateY(10);
			data.rightForeArm.rotation.setSmoothness(.9F).orientX(0);

			data.body.rotation.setSmoothness(.6F).orientY(-20.0f + renderRotationY);
			data.head.rotation.rotateY(20.0F);
		}
		else
		{
			data.leftArm.rotation.setSmoothness(.9F).orientY(100).rotateX(-90F + data.headPitch.get()).rotateY(-16.0F);

			data.leftForeArm.rotation.setSmoothness(.9F).orientX(0);
			data.body.rotation.setSmoothness(.6F).orientY(20.0f + renderRotationY);
			data.head.rotation.rotateY(-20.0F);
		}

		data.renderRotation.orientY(renderRotationY);
	}

}
