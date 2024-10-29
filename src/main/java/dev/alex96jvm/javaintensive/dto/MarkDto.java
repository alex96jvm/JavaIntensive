package dev.alex96jvm.javaintensive.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class MarkDto {
    private String subject;
    private Integer mark;
    private Long internId;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    @JsonProperty
    public void setInternId(Long internId) {
        this.internId = internId;
    }

    @JsonIgnore
    public Long getInternId() {
        return internId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkDto markDto = (MarkDto) o;
        return Objects.equals(subject, markDto.subject) && Objects.equals(mark, markDto.mark) && Objects.equals(internId, markDto.internId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, mark, internId);
    }
}
