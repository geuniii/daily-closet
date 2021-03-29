package com.megait.soir.repository;

import com.megait.soir.domain.Item;
import com.megait.soir.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    @Query("SELECT w FROM Weather w ORDER BY w.id ASC")
    List <Weather> findAllDesc();

    //원하는 지역만 찾을때 사용
    @Query(value = "select * from  WEATHER e where e.city like %:city% ", nativeQuery = true)
    List <Weather> findByCurrentLocalWeather(@Param("city") String city);


}
