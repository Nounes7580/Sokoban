package sokoban.model.element;

import sokoban.model.CellValue;

import java.util.Objects;

public abstract class Element {



    private CellValue type;


    public Element(CellValue type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return type == element.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    public CellValue getType() {
        return type;
    }


}
