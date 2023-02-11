package app.company.reader.model;

import java.util.Objects;

public class Group {
    private int number;

    public Group(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return number == group.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
