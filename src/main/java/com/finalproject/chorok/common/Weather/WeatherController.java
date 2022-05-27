package com.finalproject.chorok.common.Weather;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class WeatherController {

    public static Map<String,Object> jsonToMap(String str){
        Map<String,Object> map = new Gson().fromJson(str,new
                TypeToken<HashMap<String,Object>>() {}.getType());
        return map;
    }

    @GetMapping("/weather/situation")
        public String getWeather(WeatherLocationDto weatherLocationDto) {
        String API_KEY = "4572bc7f6a040b32388ddc0a2675d714";
        String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + weatherLocationDto.getLat() + "&lon=" + weatherLocationDto.getLon() + "&appid=" + API_KEY;
        String apiResult = "";
        log.info("watherLocationDto.getLat ={}",weatherLocationDto.getLat());
        log.info("watherLocationDto.getlon ={}",weatherLocationDto.getLon());
        System.out.println("WeatherController.getWeather");
        System.out.println("weatherLocationDto = " + weatherLocationDto.getLat());
        try {

            StringBuilder result = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            System.out.println(result);

            Map<String, Object> respMap = jsonToMap(result.toString());
            Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString());
            Map<String, Object> windMap = jsonToMap(respMap.get("wind").toString());
            apiResult = result.toString();


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return apiResult;
    }

    }

