package dev.alex96jvm.javaintensive.dto;

import java.util.List;
import java.util.Objects;

public class InternDto {
    private Long id;
    private String firstName;
    private String lastName;
    private List<MarkDto> marks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<MarkDto> getMarks() {
        return marks;
    }

    public void setMarks(List<MarkDto> marks) {
        this.marks = marks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternDto internDto = (InternDto) o;
        return Objects.equals(id, internDto.id) && Objects.equals(firstName, internDto.firstName) && Objects.equals(lastName, internDto.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}


