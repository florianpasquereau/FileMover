package org.fpasquer.file_transfer.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.fpasquer.file_transfer.data.FileTransferData;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class FileTransferDataAdapter extends TypeAdapter<FileTransferData> {

    @Override
    public void write(JsonWriter jsonWriter, FileTransferData fileTransferData) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("sha256")
                .value(fileTransferData.getSha256())
                .name("nameFormatted")
                .value(fileTransferData.getNameFormatted())
                .name("action")
                .value(fileTransferData.getAction());
        jsonWriter.endObject();
    }

    @Override
    public FileTransferData read(JsonReader jsonReader) throws IOException {
        FileTransferData data = new FileTransferData();
        jsonReader.beginObject();
        String fieldName = null;
        Map<String, Method> maps;
        try {
            maps = Map.of("sha256", FileTransferData.class.getMethod("setSha256", String.class),
                    "nameFormatted", FileTransferData.class.getMethod("setNameFormatted", String.class),
                    "action", FileTransferData.class.getMethod("setAction", String.class)
            );
        } catch (NoSuchMethodException e) {
            throw new IOException(e);
        }

        while (jsonReader.hasNext()) {
            JsonToken token = jsonReader.peek();
            if (token.equals(JsonToken.NAME)) {
                fieldName = jsonReader.nextName();
            }
            if (maps.containsKey(fieldName)) {
                token = jsonReader.peek();
                try {
                    maps.get(fieldName).invoke(data, jsonReader.nextString());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IOException(e);
                }
            } else {
                throw new IOException("The key : '" + fieldName + "' is not valid");
            }
        }
        jsonReader.endObject();
        return data;
    }
}
