package com.avito;

import org.json.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestCaseStructure {

    public static void main(String[] args) {
        String testCaseStructureJsonPath = "/Users/nikita/TestcaseStructure.json";
        String valuesPath = "/Users/nikita/Values.json";

        try {
            String testCaseStructureJson = new String(Files.readAllBytes(Paths.get(testCaseStructureJsonPath)));
            String valuesJson = new String(Files.readAllBytes(Paths.get(valuesPath)));

            JSONObject testCaseStructure = new JSONObject(testCaseStructureJson);
            JSONArray values = new JSONArray(valuesJson);

            JSONObject structureWithValues = new JSONObject();

            JSONArray parameters = testCaseStructure.getJSONArray("params");

            for (int i = 0; i < parameters.length(); i++) {
                JSONObject parameter = parameters.getJSONObject(i);
                int parameterId = parameter.getInt("id");

                if (parameter.has("values")) {
                    JSONArray parameterValues = parameter.getJSONArray("values");
                    JSONObject matchedValue = findMatchedValue(parameterValues, values, parameterId);
                    if (matchedValue != null) {
                        parameter.put("params", matchedValue.getString("title"));
                        if (matchedValue.has("params")) {
                            parameter.put("params", matchedValue.getJSONArray("params"));
                        }
                    }
                } else {
                    JSONObject matchedValue = findMatchedValue(values, parameterId, null);
                    if (matchedValue != null) {
                        parameter.put("value", matchedValue.optString("value"));
                    }
                }
                structureWithValues.put("parameter" + (i + 1), parameter);
            }

            FileWriter fileWriter = new FileWriter("StructureWithValues.json");
            fileWriter.write(structureWithValues.toString(4));
            fileWriter.close();

            System.out.println("Structure values file has been created successfully");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            createErrorFile("Invalid input files");
        }
    }

    private static JSONObject findMatchedValue(JSONArray parameterValues, JSONArray values, int parameterId) {
        for (int i = 0; i < parameterValues.length(); i++) {
            int valueId = parameterValues.optInt(i);
            JSONObject matchedValue = findMatchedValue(values, parameterId, valueId);
            if (matchedValue != null) {
                return matchedValue;
            }
        }
        return null;
    }

    private static JSONObject findMatchedValue(JSONArray values, int parameterId, Integer valueId) {
        for (int i = 0; i < values.length(); i++) {
            JSONObject value = values.getJSONObject(i);
            int valueParameterId = value.optInt("id");
            if (valueParameterId == parameterId && (valueId == null || value.optInt("value") == valueId)) {
                return value;
            }
        }
        return null;
    }

    private static void createErrorFile(String errorMessage) {
        try {
            JSONObject errorObject = new JSONObject();
            errorObject.put("error", errorMessage);

            FileWriter fileWriter = new FileWriter("error.json");
            fileWriter.write(errorObject.toString(4));
            fileWriter.close();

            System.out.println("Error file has been created successfully");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
