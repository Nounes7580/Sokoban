package sokoban.model;

import sokoban.commands.Command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class CommandManager {
    private static final Deque<Command> undoStack = new ArrayDeque<>();
    private static final Deque<Command> redoStack = new ArrayDeque<>();
    public static void executeCommand(Command command) {
        if (command.execute()) {
            undoStack.push(command);
        }
    }

    public static void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        } else {
            System.out.println("Undo stack is empty");
        }
    }

    public static void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        } else {
            System.out.println("Redo stack is empty");
        }
    }
    public static Stack<Command> getUndoStack() {
        return (Stack<Command>) undoStack;
    }

    public static Stack<Command> getRedoStack() {
        return (Stack<Command>) redoStack;
    }
}