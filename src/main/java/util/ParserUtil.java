package util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ParserUtil {

    public static JsonObject jsonFileParsing(String jsonFile) {

        JsonParser jsonParser = new JsonParser();
        Object object = null;

        try {

            FileReader fileReader = new FileReader( EnvironmentUtil.PROJECT_DIR
                                                    + EnvironmentUtil.SLASH + "src" + EnvironmentUtil.SLASH + "test" + EnvironmentUtil.SLASH + "resources" + EnvironmentUtil.SLASH + "config"
                                                    + EnvironmentUtil.SLASH + jsonFile + ".json" );
            object = jsonParser.parse( fileReader );

        } catch ( FileNotFoundException fne) {

            fne.printStackTrace();

        }

        JsonObject jsonObject = (JsonObject) object;

        assert jsonObject != null;
        return jsonObject;

    }

}
