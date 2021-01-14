package goblinbob.mobends.core.kumo.state.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import goblinbob.mobends.core.kumo.KumoSerializer;
import goblinbob.mobends.core.kumo.state.condition.TriggerConditionRegistry;
import goblinbob.mobends.core.kumo.state.template.TriggerConditionTemplate;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TriggerConditionTemplateSerializer implements JsonSerializer<TriggerConditionTemplate>, JsonDeserializer<TriggerConditionTemplate>
{

    private final Map<JsonElement, Type> toDeserialize = new HashMap<>();

    @Override
    public JsonElement serialize(TriggerConditionTemplate src, Type typeOfSrc, JsonSerializationContext context)
    {
        return KumoSerializer.INSTANCE.gson.toJsonTree(src);
    }

    @Override
    public TriggerConditionTemplate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        final Gson gson = new Gson();

        TriggerConditionTemplate abstractTriggerCondition = gson.fromJson(json, TriggerConditionTemplate.class);
        String typeName = abstractTriggerCondition.getType();

        if (typeName == null)
            return null;

        Type templateType = TriggerConditionRegistry.instance.getTemplateClass(typeName);

        if (templateType == null)
            return null;

        if (templateType.equals(typeOfT))
        {
            // If the template type of this condition is the base class.
            return abstractTriggerCondition;
        }

        return KumoSerializer.INSTANCE.gson.fromJson(json, templateType);
    }

}
