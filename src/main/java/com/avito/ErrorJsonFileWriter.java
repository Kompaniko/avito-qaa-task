package com.avito;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class ErrorJsonFileWriter {

    public void writeErrorToJsonFile(String errorMessage, String filePath) {
        try {
            JSONObject errorObject = new JSONObject();
            errorObject.put("error", errorMessage);

            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(errorObject.toString(4));
            fileWriter.close();

            System.out.println("Error file has been created successfully");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}

