package org.fffd.l23o6.UnitTests;

import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.vo.route.RouteVO;
import org.fffd.l23o6.pojo.vo.station.StationVO;
import org.fffd.l23o6.service.RouteService;
import org.fffd.l23o6.service.StationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class RouteStationTest {

    @Autowired
    private RouteService routeService;

    @Autowired
    private StationService stationService;

    @Autowired
    private RouteDao routeDao;

    @Test
    public void addRouteRandomly(){
        int length = (int) (Math.random() * 5) + 4;
        List<Long> toAddstationIds = new ArrayList<>();
        List<StationVO> realStations = stationService.listStations();
        int sze = realStations.size();
        for (int i = 0; i < length; i++){
            while (true) {
                int idxNow = (int) (Math.random() * sze);
                long toAdd = realStations.get(idxNow).getId();
                if (toAddstationIds.contains(toAdd)){
                    continue;
                }
                toAddstationIds.add(toAdd);
                break;
            }
        }

        String name = "test" + new Date().getTime();
        routeService.addRoute(name , toAddstationIds);

        RouteEntity routeEntity = routeDao.findRouteEntityByName(name);
        Assertions.assertNotNull(routeEntity);

        List<Long> getStations = routeEntity.getStationIds();
        Assertions.assertArrayEquals(toAddstationIds.toArray(), getStations.toArray());
    }


    @Test
    public void editTest(){
        String name = "test1688708769928";
        RouteEntity routeEntity = routeDao.findRouteEntityByName(name);
        Assertions.assertNotNull(routeEntity);

        long routeId = routeEntity.getId();

        int length = (int) (Math.random() * 5) + 4;
        List<Long> toAddstationIds = new ArrayList<>();
        List<StationVO> realStations = stationService.listStations();
        int sze = realStations.size();
        for (int i = 0; i < length; i++){
            while (true) {
                int idxNow = (int) (Math.random() * sze);
                long toAdd = realStations.get(idxNow).getId();
                if (toAddstationIds.contains(toAdd)){
                    continue;
                }
                toAddstationIds.add(toAdd);
                break;
            }
        }

        routeService.editRoute(routeId, name, toAddstationIds);

        RouteVO newRouteVO = routeService.getRoute(routeId);
        Assertions.assertNotNull(newRouteVO);

        RouteEntity newRouteEntity = routeDao.findById(routeId).get();
        Assertions.assertNotNull(newRouteEntity);
        Assertions.assertArrayEquals(toAddstationIds.toArray(), newRouteEntity.getStationIds().toArray());
    }


}
