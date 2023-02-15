package org.example.thread.creation.vault;

import static org.example.thread.creation.CaseStudy.MAX_PASSWORD;

/**
 * 비밀번호를 오름차순으로 추측하는 해커
 */
public class AscendingHacker extends HackerThread {

    public AscendingHacker(Vault vault) {
        super(vault);
    }

    @Override
    public void run() {
        for (int guess = 0; guess <= MAX_PASSWORD; guess++) {
            if (vault.isCorrectPassword(guess)) {
                System.out.println(this.getName() + " is guessed, password is " + guess);
                System.exit(0);
            }
        }
    }
}
