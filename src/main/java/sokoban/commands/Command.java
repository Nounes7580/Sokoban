package sokoban.commands;

public interface Command {
    boolean execute();
    void undo();
    void redo();
}