package com.megait.soir.repository;

import com.megait.soir.domain.Item;
import com.megait.soir.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    public Region findByRegionTemperatureIdLike(String regionTemperatureId);
    public Region findByRegionWeatherIdLike(String regionWeatherId);

}
