package com.optim.bassit;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/*
 * @Author: sarath_sivan
 */

public class MapToJsonConverter {

    /*
     * @Description: Method to convert Map to JSON String
     * @param: map Map<String, String> 
     * @return: json String
     */
    public static String convert(Map<String, String> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }

    /*
     * @Description: Method to convert JSON String to Map
     * @param: json String 
     * @return: map Map<String, String> 
     */
    public static Map<String, String> revert(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> map = gson.fromJson(json, type);
        return map;
    }

    /*
     * @Description: Method to print elements in the Map
     * @param: map Map<String, String> 
     * @return: void 
     */
    public static void printMap(Map<String, String> map) {
        for (String key : map.keySet()) {
            System.out.println("map.get(\"" + key + "\") = " + map.get(key));
        }
    }

    /*
     * @Description: Method to print the JSON String
     * @param: json String 
     * @return: void 
     */
    public static void printJson(String json) {
        System.out.println("json = " + json);
    }

    /*
     * @Description: Main method to test the JSON-MAP convert/revert logic
     */
    public static void main(String[] args) {
        Map<String, String> paymentCards = new HashMap<String, String>();
        paymentCards.put("card_switch", "Master");
        paymentCards.put("issuing_bank", "ICCI");
        paymentCards.put("card_Type", "DebitCard");

        String json = convert(paymentCards); //converting Map to JSON String
        System.out.println("Map to JSON String");
        System.out.println("******************");
        printJson(json); 

        System.out.println();

        paymentCards = revert(json); //converting JSON String to Map
        System.out.println("JSON String to Map");
        System.out.println("******************");
        printMap(paymentCards);
    }

}