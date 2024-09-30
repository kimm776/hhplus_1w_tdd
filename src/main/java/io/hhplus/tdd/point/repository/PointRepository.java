package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;

import java.util.List;

public interface PointRepository {

    //유저 포인트 조회
    UserPoint getUserPoint(long id);

    //유저 포인트 충전/사용
    UserPoint insertUserPoint(long id, long amount);

    //포인트 히스토리 조회
    List<PointHistory> getPointHistory(long id);

    //포인트 히스토리 추가
    PointHistory insertPointHistory(long id, long amount, TransactionType type, long updateMillis);

}
