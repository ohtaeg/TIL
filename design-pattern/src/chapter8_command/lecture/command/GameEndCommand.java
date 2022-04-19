package chapter8_command.lecture.command;

import chapter8_command.lecture.Game;

public class GameEndCommand implements Command {

    private Game game;

    public GameEndCommand(final Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.end();
    }

    @Override
    public void undo() {
        new GameStartCommand(this.game).execute();
    }
}
