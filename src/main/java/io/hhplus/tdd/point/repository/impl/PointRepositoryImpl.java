package io.hhplus.tdd.point.repository.impl;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    //유저 포인트 조회
    @Override
    public UserPoint getUserPoint(long id) {
        return userPointTable.selectById(id);
    }

    //유저 포인트 충전/사용
    @Override
    public UserPoint insertUserPoint(long id, long amount) {
        return userPointTable.insertOrUpdate(id, amount);
    }

    // 포인트 히스토리 조회
    @Override
    public List<PointHistory> getPointHistory(long id){
        return pointHistoryTable.selectAllByUserId(id);
    }

    //포인트 히스토리 추가
    @Override
    public PointHistory insertPointHistory(long id, long amount, TransactionType type, long updateMillis) {
        return pointHistoryTable.insert(id, amount, type, updateMillis);
    }

}
