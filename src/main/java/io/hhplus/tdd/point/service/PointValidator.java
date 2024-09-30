package io.hhplus.tdd.point.service;

import org.springframework.stereotype.Component;

@Component
public class PointValidator {

    /**
     * 존재하는 사용자인지 검증
     * @param id
     */
    public void checkId(long id) {
        if (id < 0) throw new IllegalArgumentException("사용자 ID는 0보다 커야 합니다.");
    }

    /**
     * 포인트 금액이 양수인지 검증
     * @param amount
     */
    public void checkAmount(long amount) {
        if (amount < 0) throw new IllegalArgumentException("0원 이상의 포인트를 입력해주세요.");
    }

    /**
     * 기존 포인트가 사용하려는 포인트보다 많은지 검증
     * @param amount 사용하려는 포인트
     * @param defaultPoint 기존 보유 포인트
     */
    public void notEnoughPoint(long amount, long defaultPoint) {
        if(amount > defaultPoint) throw new IllegalArgumentException("포인트 잔액이 부족합니다.");
    }

}
