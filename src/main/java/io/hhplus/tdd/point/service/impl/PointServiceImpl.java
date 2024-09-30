package io.hhplus.tdd.point.service.impl;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointRepository;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.point.service.PointValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointValidator pointValidator;
    private final PointRepository pointRepository;
    private final Lock lock = new ReentrantLock();

    //특정 유저의 포인트를 조회
    @Override
    public UserPoint getUserPoint(long id) {

        pointValidator.checkId(id); //존재하는 사용자인지 검증
        UserPoint userPoint = pointRepository.getUserPoint(id);

        return userPoint;

    }

    //특정 유저의 포인트 충전/이용 내역을 조회
    @Override
    public List<PointHistory> getPointHistory(long id){

        pointValidator.checkId(id); //존재하는 사용자인지 검증
        List<PointHistory> historyList = pointRepository.getPointHistory(id);

        return historyList;

    }

    //특정 유저의 포인트를 충전하는 기능
    @Override
    public UserPoint chargePoint(long id, long amount){
        lock.lock();
        try{

            pointValidator.checkAmount(amount); //입력 포인트 금액이 양수인지 검증
            UserPoint userPoint = pointRepository.getUserPoint(id); //기존 유저 포인트 조회
            PointHistory pointHistory = pointRepository.insertPointHistory(id, amount, TransactionType.CHARGE, System.currentTimeMillis()); //충전하는 히스토리 추가

            return  pointRepository.insertUserPoint(userPoint.id(), userPoint.point() + amount); //기존유저 포인트 + 충전하는 포인트

        }finally {
            lock.unlock();
        }
    }

    //특정 유저의 포인트를 사용하는 기능
    @Override
    public UserPoint usePoint(long id, long amount){
        lock.lock();
        try{

            pointValidator.checkAmount(amount); //입력 포인트 금액이 양수인지 검증
            UserPoint userPoint = pointRepository.getUserPoint(id); //기존 유저 포인트 조회
            pointValidator.notEnoughPoint(amount, userPoint.point()); //기존 포인트가 사용하려는 포인트보다 많은지 검증
            PointHistory pointHistory = pointRepository.insertPointHistory(id, amount, TransactionType.USE, System.currentTimeMillis()); //사용하는 히스토리 추가

            return  pointRepository.insertUserPoint(userPoint.id(), userPoint.point() - amount); //기존유저 포인트 - 사용하는 포인트

        }finally {
            lock.unlock();
        }
    }

}
