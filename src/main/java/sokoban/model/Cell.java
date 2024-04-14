package sokoban.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import sokoban.model.element.*;

public abstract class Cell {
    private int row;
    private int column;
    /** Le premier constructeur initialise une cellule avec des coordonnées spécifiées (row et column).
     Le second constructeur est un constructeur par défaut sans paramètres. **/
    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }
    /** Définit la valeur de la cellule en effaçant toutes les valeurs précédentes avant d'ajouter le nouvel élément. Cette méthode est utile pour remplacer complètement le contenu de la cellule. **/
    public void setValue(Element element) {
        clearValues();
        addValue(element);
    }
    protected final ListProperty<Element> toolObject = new SimpleListProperty<>(FXCollections.observableArrayList());
    /** Retourne une propriété de liste en lecture seule (ReadOnlyListProperty) qui contient tous les éléments (comme des boîtes, des joueurs, des murs) présents dans la cellule.**/
    public ReadOnlyListProperty<Element> getValue() {
        return toolObject;
    }

    public Cell(){}
    /** Ajoute un élément à la cellule selon des règles spécifiques basées sur le type de l'élément (CellValue):
     Si l'élément est de type GROUND ou WALL, la liste est d'abord vidée avant d'ajouter l'élément.
     Si l'élément est de type BOX ou PLAYER, et que la cellule contient déjà un objectif (Goal), l'objectif est maintenu et l'élément ajouté.
     Si l'élément est de type GOAL, il est ajouté à la cellule, sauf si elle contient déjà d'autres éléments spécifiques.**/
    public void addValue(Element value){
        if (value.getType() == CellValue.GROUND || value.getType() == CellValue.WALL) {
            toolObject.clear();
            toolObject.add(value);
        } else if (value.getType() == CellValue.BOX || value.getType() == CellValue.PLAYER) {
            if (toolObject.contains(new Goal())) {
                toolObject.clear();
                toolObject.add(value);
                toolObject.add(new Goal());
            } else {
                toolObject.clear();
                toolObject.add(value);
            }
        } else if (value.getType() == CellValue.GOAL) {
            if (toolObject.contains(new Goal()) || toolObject.contains(new Player()) ||  toolObject.isEmpty()) {
                toolObject.add(value);
            } else {
                toolObject.clear();
                toolObject.add(value);
            }
        }
    }
    /**Retire un élément spécifique de la liste des éléments dans la cellule.**/
    public void removeValue(Element value){
        toolObject.remove(value);
    }
    /** Efface tous les éléments de la cellule, réinitialisant son contenu. **/
    public void clearValues() {
        this.toolObject.clear();
    }

/** Vérifie si la cellule contient un élément d'un type spécifique, en utilisant la réflexion pour comparer les types d'éléments. **/
    public boolean hasElementOfType(Class<? extends Element> type) {
        for (Element e : toolObject) {
            if (type.isInstance(e)) {
                return true;
            }
        }
        return false;
    }
    /** Retourne le premier élément du type spécifié présent dans la cellule, ou null s'il n'y en a aucun. **/
    public Element getElementOfType(Class<? extends Element> type) {
        for (Element e : toolObject) {
            if (type.isInstance(e)) {
                return e;
            }
        }
        return null;
    }
/** Gère l'ajout ou la suppression d'un élément dans la cellule en fonction de la présence de l'élément dans la cellule.
  Si l'élément est déjà présent, il est retiré, sinon il est ajouté.
 Gère également les cas spéciaux liés à l'élément Goal quand un Box est retiré.**/
    public void play(Element value) {
        if (toolObject.contains(value)) {
            boolean isGoal = hasElementOfType(Goal.class);
            removeValue(value);
            if (isGoal && value.getType() == CellValue.BOX) {
                addValue(new Goal());
            }
        } else {
            addValue(value);
        }
    }
/** Détermine si la cellule est vide. Une cellule est considérée comme vide si elle ne contient aucun élément ou seulement un élément de type Ground. **/
    public boolean isEmpty() {
        return toolObject.isEmpty() || (toolObject.contains(new Ground()) && toolObject.size() == 1);
    }



}
