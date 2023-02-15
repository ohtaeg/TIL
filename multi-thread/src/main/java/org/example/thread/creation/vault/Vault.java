package org.example.thread.creation.vault;

public class Vault {
    private int password;

    public Vault(int password) {
        this.password = password;
    }

    public boolean isCorrectPassword(int guess) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this.password == guess;
    }
}
