package com.avito;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonReaderFile {
    public JSONObject readJsonFile(String filePath) throws IOException, JSONException {
        String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONObject(fileContent);
    }

    public JSONArray readJsonArrayFile(String filePath) throws IOException, JSONException {
        String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
        return new JSONArray(fileContent);
    }

}
