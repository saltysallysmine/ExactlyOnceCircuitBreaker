package com.mipt.consumer.model;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name="actions")
public class Action {

    @Id
    @NotNull
    private Long id = 0L;

    @Column
    private Long duplicatesCount = 0L;

    Action() {
    }

    Action(@NotNull Long id, Long duplicatesCount) {
        this.id = id;
        this.duplicatesCount = duplicatesCount;
    }

    public @NotNull Long getId() {
        return id;
    }

    public void getId(Long id) {
        this.id = id;
    }

    public Long getDuplicatesCount() {
        return duplicatesCount;
    }

    public void setDuplicatesCount(Long duplicatesCount) {
        this.duplicatesCount = duplicatesCount;
    }

}
