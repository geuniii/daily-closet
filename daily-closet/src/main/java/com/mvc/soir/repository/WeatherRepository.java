package com.megait.soir.repository;

import com.megait.soir.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

    /**
     * 모든 날씨 데이터 조회
     *
     * @return : 날씨 데이터 리스트
     */
    @Query("SELECT w FROM Weather w ORDER BY w.id ASC")
    List<Weather> findAllDesc();

    /**
     * 지역별 중기 날씨 조회
     *
     * @param weatherCity : 조회 지역
     * @param meridiem    : 중기 날짜
     * @param startDate   : 시작 날짜
     * @param endDate     : 끝 날짜
     * @return : 날씨 데이터 리스트
     */
    @Query(value = "select * from  WEATHER w where w.meridiem = :meridiem and w.city like %:weatherCity% and w.BASE_DATE BETWEEN :startDate and :endDate", nativeQuery = true)
    List<Weather> findByWeatherCityAndBaseDateAndMeridiemOrderByBaseDateDesc(
            @Param("weatherCity") String weatherCity,
            @Param("meridiem") String meridiem,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    /**
     * 원하는 지역의 날씨 조회
     *
     * @param city : 조회할 지역
     * @return : 해당 지역 날씨
     */
    @Query(value = "select * from  WEATHER e where e.city like %:city% ", nativeQuery = true)
    List<Weather> findByCurrentLocalWeather(@Param("city") String city);

    // 날짜, 지역, 오전/오후인지와 일치하는 값 뽑기

    /**
     * 현재 시각 기준 중기 날씨
     *
     * @param currentDate : 현재 시각
     * @param city        : 현재 지역
     * @param meridiem    : 중기 날짜
     * @return : 현재 시각을 기준으로 조회한 중기 날씨
     */
    @Query(value = "select * from  WEATHER e where e.base_date like %:base_date%  and e.city like %:city% and e.meridiem like %:meridiem%", nativeQuery = true)
    List<Weather> findCurrentDateTemperature(@Param("base_date") String currentDate,
                                             @Param("city") String city,
                                             @Param("meridiem") String meridiem
    );

}
