package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.UserPoint;

import java.util.List;

public interface PointService {

    //특정 유저의 포인트를 조회
    UserPoint getUserPoint(long id);

    //특정 유저의 포인트 충전/이용 내역을 조회
    List<PointHistory> getPointHistory(long id);

    //특정 유저의 포인트를 충전하는 기능
    UserPoint chargePoint(long id, long amount);

    //특정 유저의 포인트를 사용하는 기능
    UserPoint usePoint(long id, long amount);

}
