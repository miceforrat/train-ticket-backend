package org.fffd.l23o6.service.impl;

import java.awt.dnd.DropTarget;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.fffd.l23o6.dao.RouteDao;
import org.fffd.l23o6.dao.TrainDao;
import org.fffd.l23o6.mapper.TrainMapper;
import org.fffd.l23o6.pojo.entity.RouteEntity;
import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.fffd.l23o6.pojo.enum_.TrainType;
import org.fffd.l23o6.pojo.vo.train.AdminTrainVO;
import org.fffd.l23o6.pojo.vo.train.TrainVO;
import org.fffd.l23o6.pojo.vo.train.TicketInfo;
import org.fffd.l23o6.pojo.vo.train.TrainDetailVO;
import org.fffd.l23o6.service.TrainService;
import org.fffd.l23o6.util.strategy.train.GSeriesSeatStrategy;
import org.fffd.l23o6.util.strategy.train.KSeriesSeatStrategy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.lyc8503.spring.starter.incantation.exception.BizException;
import io.github.lyc8503.spring.starter.incantation.exception.CommonErrorType;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {
    private final TrainDao trainDao;
    private final RouteDao routeDao;

    @Override
    public TrainDetailVO getTrain(Long trainId) {
        TrainEntity train = trainDao.findById(trainId).get();
        RouteEntity route = routeDao.findById(train.getRouteId()).get();
        return TrainDetailVO.builder().id(trainId).date(train.getDate()).name(train.getName())
                .stationIds(route.getStationIds()).arrivalTimes(train.getArrivalTimes())
                .departureTimes(train.getDepartureTimes()).extraInfos(train.getExtraInfos()).build();
    }

    @Override
    public List<TrainVO> listTrains(Long startStationId, Long endStationId, String date) {
        // TODO
        // First, get all routes contains [startCity, endCity]
        // Then, Get all trains on that day with the wanted routes


        List<TrainEntity> byDate = trainDao.findAll();
        date += " 00:00:00";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dayStart = df.parse(date);
            Calendar tmpC = Calendar.getInstance();
            tmpC.setTime(dayStart);
            tmpC.add(Calendar.DATE, 1);
            Date dayEnd = tmpC.getTime();

            List<TrainVO> toRet = new ArrayList<>();

            int startTimestamp = -1,endTimestamp = -1;
            for (TrainEntity trainHere: byDate){
                long curRouteId = trainHere.getRouteId();
                List<Long> stations = routeDao.findById(curRouteId).get().getStationIds();
                List<Date> departureTimes = trainHere.getDepartureTimes();
                List<Date> arrivalTimes = trainHere.getArrivalTimes();

                Date departureTime = new Date();
                Date arrivalTime = new Date();
                for (int i = 0; i < stations.size(); i++){
                    long stationIdHere = stations.get(i);
                    if (stationIdHere == startStationId){
                        startTimestamp = i ;
                        departureTime = departureTimes.get(i);
                        if (departureTime.before(dayStart) || departureTime.after(dayEnd)){
                            break;
                        }
                    }
                    if (stationIdHere == endStationId){
                        endTimestamp = i ;
                        arrivalTime = arrivalTimes.get(i);
                    }
                }
                if(startTimestamp != -1 && endTimestamp != -1 && startTimestamp < endTimestamp){
                    TrainVO toAdd = TrainVO.builder().name(trainHere.getName()).id(trainHere.getId()).ticketInfo(new ArrayList<>()).build();
                    toAdd.setArrivalTime(arrivalTime);
                    toAdd.setDepartureTime(departureTime);
                    toAdd.setEndStationId(endStationId);
                    toAdd.setStartStationId(startStationId);
                    toRet.add(toAdd);
                }
            }
            return toRet;
        } catch (ParseException e){
            e.printStackTrace();
        }
        return new ArrayList<>();


    }

    @Override
    public List<AdminTrainVO> listTrainsAdmin() {
        return trainDao.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(TrainMapper.INSTANCE::toAdminTrainVO).collect(Collectors.toList());
    }

    @Override
    public void addTrain(String name, Long routeId, TrainType type, String date, List<Date> arrivalTimes,
            List<Date> departureTimes) {
        TrainEntity entity = TrainEntity.builder().name(name).routeId(routeId).trainType(type)
                .date(date).arrivalTimes(arrivalTimes).departureTimes(departureTimes).build();
        RouteEntity route = routeDao.findById(routeId).get();
        if (route.getStationIds().size() != entity.getArrivalTimes().size()
                || route.getStationIds().size() != entity.getDepartureTimes().size()) {
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "列表长度错误");
        }
        if (loopCheck(route.getStationIds())){
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "路线存在回路");
        }
        entity.setExtraInfos(new ArrayList<String>(Collections.nCopies(route.getStationIds().size(), "预计正点")));
        switch (entity.getTrainType()) {
            case HIGH_SPEED:
                entity.setSeats(GSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
            case NORMAL_SPEED:
                entity.setSeats(KSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
        }
        trainDao.save(entity);
    }

    @Override
    public void changeTrain(Long id, String name, Long routeId, TrainType type, String date, List<Date> arrivalTimes,
                            List<Date> departureTimes) {
        // TODO: edit train info, please refer to `addTrain` above
        RouteEntity route = routeDao.findById(routeId).get();
        if (route.getStationIds().size() != arrivalTimes.size()
                || route.getStationIds().size() != departureTimes.size()) {
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "列表长度错误");
        }
        if (loopCheck(route.getStationIds())){
            throw new BizException(CommonErrorType.ILLEGAL_ARGUMENTS, "路线存在回路");
        }

        TrainEntity toEdit = trainDao.findById(id).get();
        toEdit.setRouteId(routeId).setName(name).setTrainType(type).setDate(date).setArrivalTimes(arrivalTimes)
                .setDepartureTimes(departureTimes);

        toEdit.setExtraInfos(new ArrayList<>(Collections.nCopies(route.getStationIds().size(), "预计正点")));

        switch (toEdit.getTrainType()) {
            case HIGH_SPEED:
                toEdit.setSeats(GSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
            case NORMAL_SPEED:
                toEdit.setSeats(KSeriesSeatStrategy.INSTANCE.initSeatMap(route.getStationIds().size()));
                break;
        }
        trainDao.save(toEdit);

    }

    @Override
    public void deleteTrain(Long id) {
        trainDao.deleteById(id);
    }


    private boolean loopCheck(List<Long> stationgIds){
        Set<Long> appearSet = new HashSet<>();
        for (Long stationId: stationgIds){
            if (appearSet.contains(stationId)){
                return true;
            }
            appearSet.add(stationId);
        }
        return false;
    }
}
