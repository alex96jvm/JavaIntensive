package dev.alex96jvm.javaintensive.model;

import java.util.Objects;

public class MarkEntity {
    private Long id;
    private String subject;
    private Integer mark;
    private Long internId;

    public MarkEntity(Long id, String subject, Integer mark, Long internId) {
        this.id = id;
        this.subject = subject;
        this.mark = mark;
        this.internId = internId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public Integer getMark() {
        return mark;
    }

    public Long getInternId() {
        return internId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkEntity markEntity1 = (MarkEntity) o;
        return Objects.equals(id, markEntity1.id) && Objects.equals(subject, markEntity1.subject) && Objects.equals(mark, markEntity1.mark) && Objects.equals(internId, markEntity1.internId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, mark, internId);
    }
}
