package com.dabsquared.gitlabjenkins;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class GitLabRequest {
    private static final String[] DATE_FORMATS = new String[]{
            "yyyy-MM-dd HH:mm:ss Z", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"};

    ;

    protected enum Builder {
        INSTANCE;
        private final Gson gson;

        Builder() {
            gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(Date.class, new GitLabRequest.DateSerializer())
                    .create();
        }

        public Gson get() {
            return gson;
        }
    }

    private static class DateSerializer implements JsonDeserializer<Date> {
        public Date deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
            for (String format : DATE_FORMATS) {
                try {
                    return new SimpleDateFormat(format, Locale.US)
                            .parse(jsonElement.getAsString());
                } catch (ParseException e) {
                }
            }
            throw new JsonParseException("Unparseable date: \""
                    + jsonElement.getAsString() + "\". Supported formats: "
                    + Arrays.toString(DATE_FORMATS));
        }
    }

}
