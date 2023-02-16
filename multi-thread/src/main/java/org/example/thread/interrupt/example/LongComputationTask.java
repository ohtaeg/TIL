package org.example.thread.interrupt.example;

import java.math.BigInteger;

/**
 * 숫자를 거듭제곱으로 계산하는 테스크
 * 아주 큰 숫자를 넣고 프로그램일 실행하면 계산이 오래걸린다.
 *
 */
public class LongComputationTask implements Runnable{

    private BigInteger base;
    private BigInteger power;

    public LongComputationTask(BigInteger base, BigInteger power) {
        this.base = base;
        this.power = power;
    }

    @Override
    public void run() {
        System.out.println(base + "^" + power + " = " + pow(base, power));
    }

    private BigInteger pow(BigInteger base, BigInteger power) {
        BigInteger result = BigInteger.ONE;
        for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i.add(BigInteger.ONE)) {
//            if (Thread.currentThread().isInterrupted()) {
//                System.out.println("receive interrupt, stop");
//                return result;
//            }
            result = result.multiply(base);
        }
        return result;
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new LongComputationTask(new BigInteger("200000"), new BigInteger("1000000000")));

        /**
         * 이런 경우 인터럽트 신호를 보내도 프로그램이 끝나질 않는다.
         * 이유는 인터럽트는 정상적으로 받아도 이를 처리할 메서드나 로직이 별도로 존재하지 않기 때문
         *
         * 이 문제를 해결할 수 있는 방법은 오래 걸리는 지점을 찾아 이 스레드가 외부에서 인터럽트 당했는지 확인하는 메서드 isInterrupted()를 추가하여 핸들링할 수 있다.
         *
         * 혹은 스레드 인터럽트를 통해 정상적으로 처리할 필요가 없다면 데몬 쓰레드로 실행하는 방법도 있다.
         * 데몬 쓰레드로 설정하여 쓰레드가 앱의 종료를 방해하지 않게 된다.
         */
        thread.setDaemon(true);
        thread.start();
        thread.interrupt();
    }
}
