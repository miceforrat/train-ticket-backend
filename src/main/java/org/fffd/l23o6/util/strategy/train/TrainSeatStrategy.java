package org.fffd.l23o6.util.strategy.train;

import jakarta.annotation.Nullable;

import java.util.Map;

public abstract class TrainSeatStrategy {

    public interface SeatType {
        public String getText();
    }

    /**
     * 解除对座位的占用，参数顾名思义
     **/
    public static void deallocSeat(int startStationIdx, int endStationIdx, int seatId, boolean seats[][]){
        for (int i = startStationIdx; i < endStationIdx; i++){
            seats[i][seatId] = false;
        }
    }

    /**
    * 初始化车型的座位信息（包含无座），依赖于具体实例
    * */
    public abstract boolean[][] initSeatMap(int stationCount);

    /**
     * 按照类型分配座位，依赖于具体实例
     * */
    public abstract @Nullable String allocSeat(int startStationIndex, int endStationIndex, String type, boolean[][] seatMap);


    /**
     * 根据座位id分配座位
     * */
    public static void allocSeatById(int startStationIndex, int endStationIndex, int seatId, boolean[][] seatMap){
        for (int i = startStationIndex; i < endStationIndex; i++){
            seatMap[i][seatId] = true;
        }
    }

    /**
     * 统计剩下的座位的信息，startStationIndex和endStationIndex表示开始站和结束站在列表中的序号
     * 返回座位类型和该类型的座位数
     * */
    public abstract Map<SeatType, Integer> getLeftSeatCount(int startStationIndex, int endStationIndex, boolean[][] seatMap);



    /**
     * 判断座位是否为空闲
     * */
    public static boolean judgeIfSeatFree(int startStationIdx, int endStationIdx, int seatId, boolean[][] seatMap){
        for (int i = startStationIdx; i < endStationIdx; i++){
            if (seatMap[i][seatId]){
                return false;
            }
        }
        return true;
    }

    /**
     * 按照座位对应表分配座位，必须给出座位号和对应的String的Map
     * */
    public static @Nullable String allocSeatFreely(int startStationIndex, int endStationIndex, Map<Integer, String> mapHere,
                                            boolean[][]seatMap){
        for (Map.Entry<Integer, String>entry : mapHere.entrySet()){
            int seatNum = entry.getKey();
            if (judgeIfSeatFree(startStationIndex, endStationIndex, seatNum, seatMap)){
                allocSeatById(startStationIndex, endStationIndex, seatNum, seatMap);
                return entry.getValue();
            }
        }
        return null;
    }
}
