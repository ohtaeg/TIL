package chapter8_command.lecture.command;

import chapter8_command.lecture.Game;

public class GameStartCommand implements Command {

    private Game game;

    public GameStartCommand(final Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.start();
    }

    @Override
    public void undo() {
        new GameEndCommand(this.game).execute();
    }
}
