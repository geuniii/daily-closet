package com.megait.soir.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetAreaCode {
    // TODO Auto-generated method stub

    private String result;
    private String areaCity;    //지역
    private String areaGu;
//    private String areaDong;
    private String code = "";    //지역 코드
    private String x;
    private String y;


    private URL url;
    private BufferedReader br;
    private URLConnection conn;

    private JSONParser parser;
    private JSONArray jArr;
    private JSONObject jobj;

    //시 검색
    public void searchCity() throws IOException, ParseException {
        url = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt");
        conn = url.openConnection();
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        result = br.readLine().toString();
        br.close();
        //System.out.println(result);

        parser = new JSONParser();
        jArr = (JSONArray) parser.parse(result);
        for (int i = 0; i < jArr.size(); i++) {
            jobj = (JSONObject) jArr.get(i);
            if (jobj.get("value").equals(areaCity)) {
                code = (String) jobj.get("code");
                System.out.println(areaCity + "코드 : " + code);
                break;
            }
        }
    }

//    //구 검색
//    public void searchGU() throws IOException, ParseException {
//        url = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl." + code + ".json.txt");
//        conn = url.openConnection();
//        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        result = br.readLine().toString();
//        br.close();
//        //System.out.println(result);
//
//        parser = new JSONParser();
//        jArr = (JSONArray) parser.parse(result);
//
//        for (int i = 0; i < jArr.size(); i++) {
//            jobj = (JSONObject) jArr.get(i);
//            if (jobj.get("value").equals(areaGu)) {
//                code = (String) jobj.get("code");
//                System.out.println(areaGu + "코드 : " + code);
//                break;
//            }
//        }
//    }
//    //동 검색
//
//    public void searchDong() throws IOException, ParseException{
//        url = new URL("http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf." + code + ".json.txt");
//        conn = url.openConnection();
//        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        result = br.readLine().toString();
//        br.close();
//        //System.out.println(result);
//
//        parser = new JSONParser();
//        jArr = (JSONArray) parser.parse(result);
//
//        if (jobj.equals("종로구")) {
//            for (int i = 0; i < jArr.size(); i++) {
//                jobj = (JSONObject) jArr.get(i);
//
//                String leaf1 = jobj.substring(0, jobj.length() - 3);
//                String leaf2 = jobj.substring(jobj.length() - 3, jobj.length() - 2);
//                String leaf3 = jobj.substring(jobj.length() - 2, jobj.length());
//
//                Pattern pattern = Pattern.compile(leaf1 + "[1-9.]{0,8}" + leaf2 + "[1-9.]{0,8}" + leaf3);
//                Matcher matcher = pattern.matcher((String) jobj.get("value"));
//                if (matcher.find()) {
//                    x = (String) jobj.get("x");
//                    y = (String) jobj.get("y");
//                    System.out.println(areaLeaf + "의 x값 : " + x + ", y값 :" + y);
//                    break;
//                }
//            }
//        } else {
//            for (int i = 0; i < jArr.size(); i++) {
//                jobj = (JSONObject) jArr.get(i);
//                if (jobj.get("value").equals(jobj)) {
//                    x = (String) jobj.get("x");
//                    y = (String) jobj.get("y");
//                    System.out.println(jobj + "의 x값 : " + x + ", y값 :" + y);
//                    break;
//                }
//            }
//        }
//
//
//    }
}

