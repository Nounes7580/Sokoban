package sokoban.model;

import java.util.Stack;

public class CommandManager {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

/** Exécute une commande fournie et la place sur la pile d'annulation (undoStack).
 La pile de réexécution (redoStack) est vidée chaque fois qu'une nouvelle commande est exécutée, car toute action nouvelle invalide le chemin de réexécution précédent.
 Cette méthode permet d'assurer que toutes les commandes peuvent être annulées et que les commandes annulées ne sont pas mélangées avec de nouvelles actions. **/
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }
/** Annule la dernière commande exécutée si la pile d'annulation n'est pas vide.
 La commande annulée est retirée de la pile d'annulation et poussée sur la pile de réexécution (redoStack).
 Cela permet de rétablir les actions dans leur état précédent en inversant l'effet de la dernière commande exécutée. **/
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }
/** Réexécute la dernière commande annulée si la pile de réexécution n'est pas vide.
 La commande est retirée de la pile de réexécution et repoussée sur la pile d'annulation.
 Cette méthode permet de réappliquer une action qui a été annulée précédemment, restauration ainsi les changements précédemment inversés par l'annulation. **/
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.redo();
            undoStack.push(command);
        }
    }
}