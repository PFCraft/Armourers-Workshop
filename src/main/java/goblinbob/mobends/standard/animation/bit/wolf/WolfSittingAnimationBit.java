package goblinbob.mobends.standard.animation.bit.wolf;

import goblinbob.mobends.core.animation.bit.KeyframeAnimationBit;
import goblinbob.mobends.standard.data.WolfData;
import moe.plushie.armourers_workshop.common.lib.LibModInfo;
import net.minecraft.util.ResourceLocation;

public class WolfSittingAnimationBit extends KeyframeAnimationBit<WolfData>
{

    private static final String[] ACTIONS = new String[] { "sitting" };
    private static final ResourceLocation SITTING_ANIMATION = new ResourceLocation(LibModInfo.ID, "animations/wolf_sitting_down.json");

    public WolfSittingAnimationBit(float animationSpeed)
    {
        super(SITTING_ANIMATION, animationSpeed);
    }

    @Override
    public String[] getActions(WolfData entityData)
    {
        return ACTIONS;
    }

    @Override
    public void perform(WolfData data)
    {
        super.perform(data);
    }

}
