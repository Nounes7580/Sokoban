package sokoban.model;

public class Move {
    int[] playerStartPosition;
    int[] playerEndPosition;
    Integer[] boxStartPosition; // Utilisez Integer pour permettre null si aucun box n'est déplacé
    Integer[] boxEndPosition;

    public Move(int[] playerStart, int[] playerEnd, Integer[] boxStart, Integer[] boxEnd) {
        this.playerStartPosition = playerStart;
        this.playerEndPosition = playerEnd;
        this.boxStartPosition = boxStart;
        this.boxEndPosition = boxEnd;
    }
}
