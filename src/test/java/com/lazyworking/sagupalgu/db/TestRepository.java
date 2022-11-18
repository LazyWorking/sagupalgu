package com.lazyworking.sagupalgu.db;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
}
