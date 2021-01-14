package goblinbob.mobends.core.kumo.state.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import goblinbob.mobends.core.kumo.KumoSerializer;
import goblinbob.mobends.core.kumo.state.template.LayerTemplate;
import java.lang.reflect.Type;

public class LayerTemplateSerializer implements JsonSerializer<LayerTemplate>, JsonDeserializer<LayerTemplate>
{

    @Override
    public JsonElement serialize(LayerTemplate src, Type typeOfSrc, JsonSerializationContext context)
    {
        return (new Gson()).toJsonTree(src, src.getLayerType().getTemplateType());
    }

    @Override
    public LayerTemplate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        Gson gson = new Gson();
        LayerTemplate abstractLayer = gson.fromJson(json, LayerTemplate.class);
        return KumoSerializer.INSTANCE.layerGson.fromJson(json, abstractLayer.getLayerType().getTemplateType());
    }

}
