package com.megait.soir.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class WeatherService {

        private String currentDate;
        private String currentTime;

    public void ApiExplore()throws IOException{
        //?serviceKey=aNwjr86g5tqCgWiI6HlwBUj4%2B37Oox6xiH6LCMBmEw7BzxxOB12tDV4akwo6VvBhllO3HY4FubXfN0VVZof4Ig%3D%3D&pageNo=1&numOfRows=10&dataType=JSON&regId=11B00000&tmFc=202103220600
        //ex)tmfc = 202103220600
        String serviceKey = "aNwjr86g5tqCgWiI6HlwBUj4%2B37Oox6xiH6LCMBmEw7BzxxOB12tDV4akwo6VvBhllO3HY4FubXfN0VVZof4Ig%3D%3D";
        LocalDate localDate = LocalDate.now(); // ex) result: 2021-03-23
        LocalTime localTime = LocalTime.now(); // ex) result: 12:34:56
        currentDate = localDate.toString().replaceAll("-", ""); // -를 제거
        currentTime = localTime.toString().replaceAll(":","");

        StringBuilder urlBuilder = new StringBuilder(
                "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst" +
                        serviceKey +
                "&pageNo=1&numOfRows=10&dataType=JSON&regId=11B00000&tmFc=" +
                        currentTime + "0600"
            ); /*URL*/
//            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+serviceKey); /*Service Key*/
//            urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("-", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
//            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
//            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
//            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
//            urlBuilder.append("&" + URLEncoder.encode("stnId","UTF-8") + "=" + URLEncoder.encode("108", "UTF-8")); /*하단 참고자료 참조*/
//            urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode("201310170600", "UTF-8")); /*-일 2회(06:00,18:00)회 생성 되며 발표시각을 입력-최근 24시간 자료만 제공*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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
            System.out.println(sb.toString());
    }
}
