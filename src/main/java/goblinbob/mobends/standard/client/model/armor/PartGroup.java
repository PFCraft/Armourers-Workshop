package goblinbob.mobends.standard.client.model.armor;

import goblinbob.mobends.core.client.model.ModelPartContainer;
import goblinbob.mobends.core.client.model.ModelPartTransform;
import goblinbob.mobends.core.data.EntityData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.model.ModelRenderer;

public class PartGroup<T extends EntityData> implements Iterable<ModelPartContainer>
{

    private List<ModelPartContainer> parts;
    private DataPartSelector<T> dataPartSelector;
    private ModelPartSelector modelPartSelector;

    public PartGroup(DataPartSelector<T> dataPartSelector, ModelPartSelector modelPartSelector)
    {
        this.parts = new ArrayList<>();
        this.dataPartSelector = dataPartSelector;
        this.modelPartSelector = modelPartSelector;
    }

    public void syncUp(T data)
    {
        parts.forEach(part -> part.syncUp(dataPartSelector.selectPart(data)));
    }

    public void updateVisibility(MutatedArmorModel model)
    {
        parts.forEach(part -> part.setVisible(modelPartSelector.selectPart(model).showModel));
    }

    public void add(ModelPartContainer part)
    {
        parts.add(part);
    }

    public void clear()
    {
        parts.clear();
    }

    @Override
    public Iterator<ModelPartContainer> iterator()
    {
        return parts.iterator();
    }

    public List<ModelPartContainer> getParts()
    {
        return parts;
    }

    @FunctionalInterface
    public interface DataPartSelector<T extends EntityData>
    {

        ModelPartTransform selectPart(T data);

    }

    @FunctionalInterface
    public interface ModelPartSelector<T extends EntityData>
    {

        ModelRenderer selectPart(MutatedArmorModel model);

    }

}
