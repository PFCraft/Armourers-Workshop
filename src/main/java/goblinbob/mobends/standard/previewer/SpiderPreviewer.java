package goblinbob.mobends.standard.previewer;

import goblinbob.mobends.core.bender.BoneMetadata;
import goblinbob.mobends.core.bender.IPreviewer;
import goblinbob.mobends.core.client.event.DataUpdateHandler;
import goblinbob.mobends.standard.data.SpiderData;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;

public class SpiderPreviewer implements IPreviewer<SpiderData>
{

	/**
	 * The Entity is generated specifically just for preview, so
	 * it can be manipulated in any way.
	 */
	@Override
	public void prePreview(SpiderData data, String animationToPreview)
	{
		data.limbSwingAmount.override(0F);
		
		switch (animationToPreview)
		{
			case "jump":
				{
					final float ticks = DataUpdateHandler.getTicks();
					
					final float JUMP_DURATION = 10;
					final float WAIT_DURATION = 10;
					final float TOTAL_DURATION = JUMP_DURATION + WAIT_DURATION;
					float t = ticks % TOTAL_DURATION;
					
					if (t <= JUMP_DURATION)
					{
						data.overrideOnGroundState(false);
						
						double yOffset = Math.sin(t/JUMP_DURATION * Math.PI) * 1.5;
						GlStateManager.translate(0, yOffset, 0);
					} else {
						data.overrideOnGroundState(true);
					}
					
					data.limbSwingAmount.override(0F);
					data.overrideStillness(true);
				}
				break;
			case "move":
				final float ticks = DataUpdateHandler.getTicks();
				
				data.getEntity().posZ += DataUpdateHandler.ticksPerFrame * 0.1F;
				data.getEntity().prevPosZ = data.getEntity().posZ;
				data.getEntity().noClip = true;
				//System.out.println(data.getEntity().posZ);
				data.limbSwing.override(ticks * 0.6F);
				data.overrideOnGroundState(true);
				data.limbSwingAmount.override(1F);
				data.overrideStillness(false);
				break;
			default:
				data.overrideOnGroundState(true);
				data.overrideStillness(true);
		}
	}

	@Override
	public void postPreview(SpiderData data, String animationToPreview)
	{
		// No behaviour
	}

	@Override
	public Map<String, BoneMetadata> getBoneMetadata()
	{
		return null;
	}
	
}
