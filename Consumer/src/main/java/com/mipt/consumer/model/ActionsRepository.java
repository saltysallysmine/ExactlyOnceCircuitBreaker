package com.mipt.consumer.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionsRepository extends JpaRepository<Action, Long> {
}