package com.lazyworking.sagupalgu.db;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional
public class TestService {
    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public void join(TestEntity entity) {
        testRepository.save(entity);
    }

    public TestEntity findEntityById(Long id) {
        return testRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<TestEntity> findAllEntities() {
        return testRepository.findAll();
    }
}
