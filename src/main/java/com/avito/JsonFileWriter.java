package com.avito;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonFileWriter {
    public void writeJsonToFile(JSONObject jsonObject, String filePath) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(jsonObject.toString(4));
        fileWriter.close();
    }

}
