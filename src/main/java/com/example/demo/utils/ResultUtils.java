package com.example.demo.utils;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

/**
 * @author ZhaiZhch
 */
public class ResultUtils {
    public static JSONObject getResult(int status, String msg) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("msg", msg);
        return result;
    }

    public static <T> JSONObject getResult(int status, String msg, T res) throws JSONException {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("msg", msg);
        result.put("res", res);
        return result;
    }
}
