package io.hhplus.tdd.point.service;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointRepository;
import io.hhplus.tdd.point.service.impl.PointServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    PointServiceImpl pointService;

    @Mock
    PointValidator pointValidator;

    @Mock
    PointRepository pointRepository;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("유저 포인트 조회 정상 작동 테스트")
    @Test
    void getUserPoint() {
        //given
        UserPoint user = new UserPoint(1L, 1000L, 0L);
        given(pointRepository.getUserPoint(anyLong())).willReturn(user);

        //when
        UserPoint result = pointService.getUserPoint(1L);

        //then
        assertThat(result.id()).isEqualTo(user.id());

    }

    @DisplayName("유저 포인트 내역 조회 정상 작동 테스트")
    @Test
    void getPointHistory(){
        //given
        PointHistory history1 = new PointHistory(1L, 1L, 1000L, TransactionType.CHARGE, System.currentTimeMillis());
        PointHistory history2 = new PointHistory(2L, 1L, 500L, TransactionType.USE, System.currentTimeMillis());
        List<PointHistory> histories = List.of(history1, history2);
        given(pointRepository.getPointHistory(anyLong())).willReturn(histories);

        //when
        List<PointHistory> result = pointService.getPointHistory(1L);

        //then
        assertThat(result.get(0)).isEqualTo(history1);
        assertThat(result.get(1)).isEqualTo(history2);

    }

    @DisplayName("포인트 충전 정상 작동 테스트")
    @Test
    void chargePoint() {

        //given
        long userId = 1L;
        long existPoint = 500L;
        long chargePoint = 1000L;

        UserPoint user = new UserPoint(userId, existPoint, 0);
        given(pointRepository.getUserPoint(anyLong())).willReturn(user);
        given(pointRepository.insertUserPoint(anyLong(), anyLong())).willReturn(new UserPoint(userId, user.point() + chargePoint, 0));

        //when
        UserPoint resultUser = pointService.chargePoint(userId, chargePoint);

        //then
        Assertions.assertNotNull(resultUser);
        Assertions.assertEquals(1500L, resultUser.point());

        System.out.println("기존포인트 = " + user);
        System.out.println("충전후포인트 = " + resultUser);

    }

    @DisplayName("포인트 사용 정상 작동 테스트")
    @Test
    void usePoint(){

        //given
        long userId = 1L;
        long existPoint = 1000L;
        long usePoint = 200L;

        UserPoint user = new UserPoint(userId, existPoint, 0);
        given(pointRepository.getUserPoint(anyLong())).willReturn(user);
        given(pointRepository.insertUserPoint(anyLong(), anyLong())).willReturn(new UserPoint(userId, user.point() - usePoint, 0));

        //when
        UserPoint resultUser = pointService.chargePoint(userId, usePoint);

        //then
        Assertions.assertNotNull(resultUser);
        Assertions.assertEquals(800L, resultUser.point());

        System.out.println("기존포인트 = " + user);
        System.out.println("사용후포인트 = " + resultUser);
    }

}