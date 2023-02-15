package org.example.thread.creation.vault;

import static org.example.thread.creation.CaseStudy.MAX_PASSWORD;

public class DescendingHacker extends HackerThread {

    public DescendingHacker(Vault vault) {
        super(vault);
    }

    @Override
    public void run() {
        for (int guess = MAX_PASSWORD; guess >= 0; guess--) {
            if (vault.isCorrectPassword(guess)) {
                System.out.println(this.getName() + " is guessed, password is " + guess);
                System.exit(0);
            }
        }
    }
}
