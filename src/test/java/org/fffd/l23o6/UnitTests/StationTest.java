package org.fffd.l23o6.UnitTests;

import org.fffd.l23o6.dao.StationDao;
import org.fffd.l23o6.pojo.entity.StationEntity;
import org.fffd.l23o6.service.StationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class StationTest {
    @Autowired
    private StationService stationService;

    @Autowired
    private StationDao stationDao;

    @Test
    public void test1(){
        String toAdd = "莱希菲尔德";
        stationService.addStation(toAdd);

        StationEntity entity = stationDao.findByName(toAdd);
        assertNotNull(entity);
        assertEquals(stationService.getStation(entity.getId()).getName(), toAdd);

    }


    @Test
    public void test2(){
        String toAdd = "旧奥格斯堡";
        stationService.addStation(toAdd);

        StationEntity oldEntity = stationDao.findByName(toAdd);
        assertNotNull(oldEntity);
        long stationId = oldEntity.getId();
        assertEquals(stationService.getStation(stationId).getName(), toAdd);

        toAdd = "奥格斯堡";
        stationService.editStation(stationId, toAdd);
        assertEquals(stationService.getStation(stationId).getName(), toAdd);
        assertNotNull(stationDao.findByName(toAdd));

    }


}
