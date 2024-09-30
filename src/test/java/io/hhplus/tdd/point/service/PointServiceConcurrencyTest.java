package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.UserPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class PointServiceConcurrencyTest {

    @Autowired
    PointService pointService;

    @DisplayName("충전, 사용 동시성 테스트")
    @Test
    void useChargeConcurrencyTest() {
        //given
        long id = 1L;
        UserPoint userPoint = pointService.getUserPoint(id);
        pointService.chargePoint(userPoint.id(), 5000L);

        //when
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    pointService.usePoint(userPoint.id(), 3000L);
                }),
                CompletableFuture.runAsync(() -> {
                    pointService.chargePoint(userPoint.id(), 2000L);
                }),
                CompletableFuture.runAsync(() -> {
                    pointService.usePoint(userPoint.id(), 1000L);
                })
        ).join();

        UserPoint resultUserPoint = pointService.getUserPoint(id);

        //then
        assertThat(resultUserPoint.point()).isEqualTo(5000L - 3000L + 2000L - 1000L);
    }

    @DisplayName("동시성 테스트에서 포인트 잔액 부족 예외 발생 테스트")
    @Test
    void useChargeConcurrencyTest_ThrowsException() {
        // given
        long id = 1L;
        UserPoint userPoint = pointService.getUserPoint(id);
        pointService.chargePoint(userPoint.id(), 5000L);

        // when
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                pointService.usePoint(userPoint.id(), 6000L);
            } catch (IllegalArgumentException e) {
                System.out.println("6000포인트 사용 실패: " + e.getMessage());
            }
        });

        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            try {
                pointService.usePoint(userPoint.id(), 2000L);
            } catch (IllegalArgumentException e) {
                System.out.println("2000포인트 사용 실패: " + e.getMessage());
            }
        });

        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {
            try {
                pointService.usePoint(userPoint.id(), 1000L);
            } catch (IllegalArgumentException e) {
                System.out.println("1000포인트 사용 실패: " + e.getMessage());
            }
        });
        CompletableFuture.allOf(future1, future2, future3).join();
        UserPoint resultUserPoint = pointService.getUserPoint(id);

        // then
        assertThat(resultUserPoint.point()).isEqualTo(5000L - 2000L - 1000L);
    }

}
