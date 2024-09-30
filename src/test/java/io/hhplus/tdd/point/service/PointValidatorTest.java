package io.hhplus.tdd.point.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointValidatorTest {

    private final PointValidator pointValidator = new PointValidator();

    @DisplayName("존재하지 않는 유저를 조회하면 예외 발생 테스트")
    @Test
    void userNotFound() {
        // given
        long id = -1L;

        // then
        IllegalArgumentException resultException = assertThrows(IllegalArgumentException.class, () -> {
            pointValidator.checkId(id);
        });

        assertThat(resultException.getMessage()).isEqualTo("사용자 ID는 0보다 커야 합니다.");
    }

    @DisplayName("음수 포인트 입력시 예외 발생 테스트")
    @Test
    void checkAmount_ThrowsException() {
        // given
        long amount = -1000L; // 음수 포인트

        // then
        IllegalArgumentException resultException = assertThrows(IllegalArgumentException.class, () -> {
            pointValidator.checkAmount(amount);
        });

        assertThat(resultException.getMessage()).isEqualTo("0원 이상의 포인트를 입력해주세요.");
    }

    @DisplayName("포인트 잔액이 부족한 경우 예외 발생 테스트")
    @Test
    void notEnoughPoint_ThrowsException() {
        // given
        long amount = 5000L; // 사용하려는 포인트
        long defaultPoint = 1000L; // 현재 포인트 잔액

        // then
        IllegalArgumentException resultException = assertThrows(IllegalArgumentException.class, () -> {
            pointValidator.notEnoughPoint(amount, defaultPoint);
        });

        assertThat(resultException.getMessage()).isEqualTo("포인트 잔액이 부족합니다.");
    }

}
