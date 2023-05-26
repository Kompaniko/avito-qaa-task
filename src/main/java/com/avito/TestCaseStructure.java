package com.avito;

import org.json.*;
import com.avito.ErrorJsonFileWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestCaseStructure {

    public static void main(String[] args) {
        String testCaseStructureJsonPath = "/Users/nikita/TestcaseStructure.json";
        String valuesPath = "/Users/nikita/Values.json";

        try {
            JsonReaderFile jsonFileReader = new JsonReaderFile();
            JSONObject testCaseStructure = jsonFileReader.readJsonFile(testCaseStructureJsonPath);
            JSONArray values = jsonFileReader.readJsonArrayFile(valuesPath);

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

            JsonFileWriter jsonFileWriter = new JsonFileWriter();
            jsonFileWriter.writeJsonToFile(structureWithValues, "StructureWithValues.json");

            System.out.println("Файл значений структуры успешно создан");
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
            ErrorJsonFileWriter errorJsonFileWriter = new ErrorJsonFileWriter();
            errorJsonFileWriter.writeErrorToJsonFile(errorMessage, "error.json");

            System.out.println("Файл ошибки успешно создан");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
