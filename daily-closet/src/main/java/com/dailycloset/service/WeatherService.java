package com.dailycloset.service;

import com.dailycloset.repository.RegionRepository;
import com.dailycloset.repository.WeatherRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.dailycloset.domain.Region;
import com.dailycloset.domain.Weather;
import com.dailycloset.domain.WeatherListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {
    final int API_TYPE_WEATHER = 1;
    final int API_TYPE_TEMPERATURE = 2;

    private final RegionRepository regionRepository;
    private final WeatherRepository weatherRepository;

    /**
     * 날씨 데이터 저장
     */
    @PostConstruct
    @Transactional
    public void Init() {
        insertRegion();

        List<Region> regions = regionRepository.findAll();

        for (Region region : regions) {
            try {
                JsonParser jp = new JsonParser();

                // 1. get weather
                String weather_result = callWeatherApi(API_TYPE_WEATHER, region.getRegionWeatherId());
                JsonObject weather_json = jp.parse(weather_result)
                        .getAsJsonObject()
                        .getAsJsonObject("response")
                        .getAsJsonObject("body")
                        .getAsJsonObject("items")
                        .getAsJsonArray("item")
                        .get(0)
                        .getAsJsonObject();

                // 2. get temperature
                String temperature_result = callWeatherApi(API_TYPE_TEMPERATURE, region.getRegionTemperatureId());
                JsonObject temperature_json = jp.parse(temperature_result)
                        .getAsJsonObject()
                        .getAsJsonObject("response")
                        .getAsJsonObject("body")
                        .getAsJsonObject("items")
                        .getAsJsonArray("item")
                        .get(0)
                        .getAsJsonObject();

                // 3. save temp data(1,2,3)
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 2; j++) {
                        String wt = "";
                        String temp = "";
                        if (i == 0) {
                            wt = "맑음";
                            temp = "15";
                        } else if (i == 1) {
                            wt = "흐림";
                            temp = "12";
                        } else if (i == 2) {
                            wt = "비";
                            temp = "13";
                        }

                        String meridiem = "";
                        if (j == 0) {
                            meridiem = "AM";
                        } else {
                            meridiem = "PM";
                        }
                        Weather weather = Weather.builder()
                                .baseDate(getAddDate(i))
                                .city(region.getCity())
                                .meridiem(meridiem)
                                .localWeather(wt)
                                .temperature(Long.parseLong(temp))
                                .build();
                        weatherRepository.save(weather);
                    }
                }

                // 4. save real data(4~10)
                for (int i = 3; i < 11; i++) {
                    for (int j = 0; j < 2; j++) {
                        // set weather key
                        String meridiem = "";
                        String weather_key = "wf" + i;
                        if (j == 0) {
                            meridiem = "Am";
                            if (i < 8) {
                                weather_key += meridiem;
                            }
                        } else {
                            meridiem = "Pm";
                            if (i < 8) {
                                weather_key += meridiem;
                            }
                        }
                        meridiem = meridiem.toUpperCase();


                        String temperature_key = "taMax" + i;

                        Weather weather = Weather.builder()
                                .baseDate(getAddDate(i))
                                .city(region.getCity())
                                .meridiem(meridiem)
                                .localWeather(weather_json.get(weather_key).getAsString())
                                .temperature(temperature_json.get(temperature_key).getAsLong())
                                .build();
                        weatherRepository.save(weather);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 날짜 추가
     * @param day : 추가할 날짜
     * @return : 추가된 날짜
     */
    public String getAddDate(int day) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        date = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     * 현재 날짜 조회
     * @return : 현재 날짜
     */
    public String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd:HHmm");
        Date date = new Date();
        return format.format(date);
    }

    /**
     * 오전 / 오후 조회
     * @param currentDate : 현재 시간
     * @return : 오전 / 오후
     */
    public String getMeridiem(String currentDate) {
        if (Integer.parseInt(currentDate.split(":")[1]) >= 0000
                && Integer.parseInt(currentDate.split(":")[1]) < 1200) {
            return "am";
        } else {
            return "pm";
        }
    }

    /**
     * 중기 날짜 조회
     * @param currentDate : 현재 날짜
     * @return : 중기 날짜
     */
    public String getBatchTime(String currentDate) {
        if (Integer.parseInt(currentDate.split(":")[1]) >= 0600
                && Integer.parseInt(currentDate.split(":")[1]) < 1800) {
            return currentDate.split(":")[0] + "0600";
        } else if (Integer.parseInt(currentDate.split(":")[1]) >= 1800
                && Integer.parseInt(currentDate.split(":")[1]) < 2400) {
            return currentDate.split(":")[0] + "1800";
        } else {
            return getAddDate(-1) + "1800";
        }
    }

    /**
     * 지역 추가
     */
    private void insertRegion() {
        regionRepository.saveAll(List.of(
                Region.builder().city("서울_인천_경기도").regionTemperatureId("11B10101").regionWeatherId("11B00000").build(),
                Region.builder().city("부산_울산_경상남도").regionTemperatureId("11H20201").regionWeatherId("11D10000").build(),
                Region.builder().city("대구_경상북도").regionTemperatureId("11H10701").regionWeatherId("11D20000").build(),
                Region.builder().city("광주_전라남도").regionTemperatureId("11F20501").regionWeatherId("11C20000").build(),
                Region.builder().city("전라북도").regionTemperatureId("11F10201").regionWeatherId("11F20000").build(),
                Region.builder().city("대전_세종_충청남도").regionTemperatureId("11C20401").regionWeatherId("11F10000").build(),
                Region.builder().city("충청북도").regionTemperatureId("11C10301").regionWeatherId("11H10000").build(),
                Region.builder().city("강원도").regionTemperatureId("11D10301").regionWeatherId("11H20000").build(),
                Region.builder().city("제주도").regionTemperatureId("11G00201").regionWeatherId("11G00000").build())
        );
    }

    /**
     * 날씨 조회 API 요청
     * @param apiType : API 타입
     * @param regionId : 지역 아이디
     * @return : 날씨 데이터
     * @throws Exception
     */
    private String callWeatherApi(int apiType, String regionId) throws Exception {
        String api_url = "";
        if (apiType == API_TYPE_WEATHER) {
            // 날씨
            api_url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";
        } else {
            // 온도
            api_url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa";
        }

        StringBuilder urlBuilder = new StringBuilder(api_url); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + URLEncoder.encode("/tpu/YUap+AKJVZ8wBRmoDcQjhE1QN01m5tAt0uNquuFM7au5V4OsdaTslntZyw1hpcWY2kCq3G973tnedGbuw==", "UTF-8")); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("regId", "UTF-8") + "=" + URLEncoder.encode(regionId, "UTF-8")); /*하단 참고자료 참조*/
        urlBuilder.append("&" + URLEncoder.encode("tmFc", "UTF-8") + "=" + URLEncoder.encode(getBatchTime(getCurrentDate()), "UTF-8")); /*-일 2회(06:00,18:00)회 생성 되며 발표시각을 입력-최근 24시간 자료만 제공*/

        System.out.println(urlBuilder.toString());
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }

    /**
     * 모든 날씨 조회
     * @return : 모든 날씨
     */
    @Transactional
    public List<WeatherListResponse> findAllDesc() {
        return weatherRepository.findAllDesc().stream()
                .map(WeatherListResponse::new)
                .collect(Collectors.toList());

    }

    /**
     * 지역별 날씨 조회
     * @param weatherCity : 조회할 지역
     * @return : 해당 지역 날씨
     */
    @Transactional
    public List<WeatherListResponse> findByWeatherCity(String weatherCity) {
        return weatherRepository.findByWeatherCityAndBaseDateAndMeridiemOrderByBaseDateDesc(
                weatherCity, getMeridiem(getCurrentDate()).toUpperCase(),
                getCurrentDate().split(":")[0], getAddDate(10)
        )
                .stream()
                .map(WeatherListResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 지역 날씨 조회
     * @param city : 현재 지역
     * @return: 해당 지역 기온
     */
    public List<Weather> findCurrentLocalWeather(String city) {
        return weatherRepository.findByCurrentLocalWeather(city);
    }

    /**
     * 현재 지역 기온 조회
     * @param currentdate : 현재 시간
     * @param city : 현재 지역
     * @param meridien : 오전 / 오후
     * @return : 현재 기온
     */
    public List<Weather> findCurrentDateTemperature(String currentdate, String city, String meridien) {
        return weatherRepository.findCurrentDateTemperature(currentdate, city, meridien);
    }

    /**
     * 오전 기온 조회
     * @return : 오전 기온
     */
    public Long am_temperature() {
        Weather weather = weatherRepository.getOne(Long.valueOf("76")); // 4일째 오전 온도, id = 76
        return weather.getTemperature();

    }

    /**
     * 오후 기온 조회
     * @return : 오후 기온
     */
    public Long pm_temperature() {
        Weather weather = weatherRepository.getOne(Long.valueOf("77")); // 4일째 오후 온도,  id = 77
        return weather.getTemperature();
    }

}