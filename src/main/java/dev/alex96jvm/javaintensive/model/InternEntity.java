package dev.alex96jvm.javaintensive.model;

import java.util.List;
import java.util.Objects;

public class InternEntity {
    private Long id;
    private String firstName;
    private String lastName;
    private List<MarkEntity> markEntities;

    public InternEntity(Long id, String firstName, String lastName, List<MarkEntity> markEntities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.markEntities = markEntities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<MarkEntity> getMarks() {
        return markEntities;
    }

    public void setMarks(List<MarkEntity> markEntities) {
        this.markEntities = markEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternEntity internEntity = (InternEntity) o;
        return Objects.equals(id, internEntity.id) && Objects.equals(firstName, internEntity.firstName) && Objects.equals(lastName, internEntity.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}


