package goblinbob.mobends.standard.mutators;

import goblinbob.mobends.core.client.model.IModelPart;
import goblinbob.mobends.core.client.model.ModelPart;
import goblinbob.mobends.core.data.IEntityDataFactory;
import goblinbob.mobends.core.mutators.Mutator;
import goblinbob.mobends.standard.data.SquidData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.passive.EntitySquid;

public class SquidMutator extends Mutator<SquidData, EntitySquid, ModelSquid>
{
	
	public ModelPart squidBody;
	public ModelPart[][] squidTentacles = new ModelPart[8][SquidData.TENTACLE_SECTIONS];

	public SquidMutator(IEntityDataFactory<EntitySquid> dataFactory)
	{
		super(dataFactory);
	}
	
	@Override
	public void storeVanillaModel(ModelSquid model)
	{
		this.vanillaModel = new ModelSquid();
		this.vanillaModel.squidBody = model.squidBody;
		this.vanillaModel.squidTentacles = model.squidTentacles;
	}

	@Override
	public void applyVanillaModel(ModelSquid model)
	{
		model.squidBody = this.vanillaModel.squidBody;
		model.squidTentacles = this.vanillaModel.squidTentacles;
	}

	@Override
	public void swapLayer(RenderLivingBase<? extends EntitySquid> renderer, int index, boolean isModelVanilla)
	{
		// No behaviour
	}

	@Override
	public void deswapLayer(RenderLivingBase<? extends EntitySquid> renderer, int index)
	{
		// No behaviour
	}

	@Override
	public boolean createParts(ModelSquid original, float scaleFactor)
	{
		float legLength = 12F;
		float foreLegLength = 15F;

		original.squidBody = this.squidBody = new ModelPart(original, 0, 0);
		this.squidBody.setPosition(0.0F, 8.0F, 0.0F);
		this.squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);

		original.squidTentacles = new ModelRenderer[8];
		for (int i = 0; i < this.squidTentacles.length; ++i)
		{
			original.squidTentacles[i] = this.squidTentacles[i][0] = new ModelPart(original, 48, 0);
			double d0 = (double) i * Math.PI * 2.0D / (double) this.squidTentacles.length;
			float f = (float) Math.cos(d0) * 4.0F;
			float f1 = (float) Math.sin(d0) * 4.0F;
			d0 = (double) i * -360.0D / (double) this.squidTentacles.length + 90.0D;
			this.squidTentacles[i][0].setPosition(f, 16.0F, f1);
			this.squidTentacles[i][0].addBox(-1.0F, 0.0F, 0.0F, 2, SquidData.SECTION_HEIGHT, 2);
			this.squidTentacles[i][0].rotation.rotateY((float) d0);

			for (int j = 1; j < SquidData.TENTACLE_SECTIONS; ++j)
			{
				this.squidTentacles[i][j] = new ModelPart(original, 48, 0);
				this.squidTentacles[i][j].setPosition(0, SquidData.SECTION_HEIGHT, 0);
				this.squidTentacles[i][j].addBox(-1.0F, 0.0F, -2.0F, 2, SquidData.SECTION_HEIGHT, 2);
				this.squidTentacles[i][j - 1].addChild(this.squidTentacles[i][j]);
			}
		}

		return true;
	}
	
	@Override
	public void syncUpWithData(SquidData data)
	{
		this.squidBody.syncUp(data.squidBody);
		for (int i = 0; i < this.squidTentacles.length; ++i)
		{
			for (int j = 0; j < SquidData.TENTACLE_SECTIONS; ++j)
			{
				this.squidTentacles[i][j].syncUp(data.squidTentacles[i][j]);
			}
		}
	}

	@Override
	public boolean isModelVanilla(ModelSquid model)
	{
		return !(model.squidBody instanceof IModelPart);
	}

	@Override
	public boolean shouldModelBeSkipped(ModelBase model)
	{
		return !(model instanceof ModelSquid);
	}

}
