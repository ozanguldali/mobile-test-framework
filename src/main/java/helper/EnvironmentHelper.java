package helper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class EnvironmentHelper {

    protected static JsonElement getJsonElement(JsonObject jsonObject, String key) {

        try {

            return jsonObject.get( key );

        } catch (Exception e) {

            e.printStackTrace();

        }

        return new JsonElement() {

            @Override
            public JsonElement deepCopy() {

                return null;

            }

        };

    }

}
