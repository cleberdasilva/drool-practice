package com.drool.practice.repository;

import com.drool.practice.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroolRulesRepo extends JpaRepository<Rule, Integer> {
}

