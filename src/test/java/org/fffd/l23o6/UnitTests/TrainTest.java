package org.fffd.l23o6.UnitTests;

import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.route.RouteVO;
import org.fffd.l23o6.service.RouteService;
import org.fffd.l23o6.service.TrainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class TrainTest {
    @Autowired
    private TrainService trainService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private TrainDao trainDao;

    /**
     * 这个函数不仅仅是测试用的，在功能完善时，还能向数据库中注入一条火车，其始发站只比当前时刻晚45分钟
     */
    @Test
    public void addCrossingTrain(){
        String name = "T" + new Date().getTime();
        List<RouteVO> routes = routeService.listRoutes();
        int sze = routes.size();
        int idx = (int)(sze * Math.random());
        RouteVO route = routes.get(idx);
        Long routeId = route.getId();
        int stationCnt = route.getStationIds().size();


        List<Date> arrivalDates = new ArrayList<>();
        List<Date> departureDates = new ArrayList<>();

        Date curDate = new Date(new Date().getTime() + 45 * 60 * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateByDay = sdf.format(curDate);

        for (int i = 0; i < stationCnt ; i++){
            arrivalDates.add(curDate);
            if (i != 0 && i != stationCnt - 1){
                curDate = new Date(curDate.getTime() + 15 * 60 * 1000);
            }
            departureDates.add(curDate);
            curDate = new Date(curDate.getTime() + 60 * 60 * 1000);
        }

        TrainType type = ((int)(Math.random() * 2) == 0) ? TrainType.HIGH_SPEED: TrainType.NORMAL_SPEED;
        trainService.addTrain(name, routeId, type, dateByDay,arrivalDates, departureDates);
        System.err.println(name);
    }



}
