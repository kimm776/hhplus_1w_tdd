# TDD 로 개발하기

## 📝목차

1. [요구사항](#1.-요구사항)
2. [동시성 제어 방식에 대한 분석 및 보고서](#2.-동시성-제어-방식에-대한-분석-및-보고서)

<br />

## 1. 요구사항
#### ❓ `point` 패키지의 TODO 와 테스트코드를 작성해주세요.

* PATCH  `/point/{id}/charge` : 포인트를 충전한다.
* PATCH `/point/{id}/use` : 포인트를 사용한다.
* GET `/point/{id}` : 포인트를 조회한다.
* GET `/point/{id}/histories` : 포인트 내역을 조회한다.
* 잔고가 부족할 경우, 포인트 사용은 실패하여야 합니다.
* 동시에 여러 건의 포인트 충전, 사용 요청이 들어올 경우 순차적으로 처리되어야 합니다.

<br />

## 2. 동시성 제어 방식에 대한 분석 및 보고서

- 동시성 제어 목표
  - 동시에 여러 건의 충전/사용 요청이 들어오더라도 한번에 하나의 요청씩만 제어될 수 있도록 해야함.
  

- `ReentrantLock`을 통한 동시성 제어
  - 포인트 충전/사용에는 공정성을 보장할 수 있는 `ReentrantLock`이 적합하다고 판단하여 선택했습니다.
  - `ReentrantLock` 을 사용하여 여러 스레드가 동시에 포인트 충전/사용하는 문제를 방지했습니다.
  - `finally` 블록을 사용하여 락의 해제를 보장함으로써, 데드락의 위험 없이 안전하게 처리하도록 구현하였습니다.
  - ```java
    @Override
    public UserPoint chargePoint(long id, long amount){
        lock.lock(); // 락 획득
        try{
            ...
        }finally {
            lock.unlock(); // 락 해제
        }
    }
    ```

- `CompletableFuture`를 통한 동시성 테스트
  - `CompletableFuture`로 포인트 충전/사용 요청의 비동기적 상황을 테스트하였고, 정상 작동과 예외 처리를 검증했습니다.
  - ```java
    @Test
    void useChargeConcurrencyTest() {
        ...
    
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
    
        ...
    }
    ```
