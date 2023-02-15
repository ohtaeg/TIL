package org.example.thread.creation;

import org.example.thread.creation.vault.AscendingHacker;
import org.example.thread.creation.vault.DescendingHacker;
import org.example.thread.creation.vault.PoliceThread;
import org.example.thread.creation.vault.Vault;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 돈을 보관할 금고를 설계한다.
 * 금고를 열려는 해커 쓰레드들을 만든다.
 * 경찰 쓰레드를 추가한다.
 * 경찰 쓰레드는 10초안에 금고에 도착한다.
 * 그 동안 해커들이 금고를 못열었다면 경찰이 해커를 체포할 수 있다.
 * 경찰 스레드는 도착 상황을 카운트한다.
 */
public class CaseStudy {

    public static final int MAX_PASSWORD = 9999;

    public static void main(String[] args) {
        Random random = new Random();

        Vault vault = new Vault(random.nextInt(MAX_PASSWORD));

        List<Thread> threads = List.of(new AscendingHacker(vault), new DescendingHacker(vault), new PoliceThread());

        threads.forEach(Thread::start);
    }
}
