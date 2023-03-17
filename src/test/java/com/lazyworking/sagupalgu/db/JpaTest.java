package com.lazyworking.sagupalgu.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class JpaTest {
    @Autowired
    private TestService testService;

    @PersistenceContext
    private EntityManager em;


    @BeforeEach
    void insertData(){
        TestEntity testEntity1 = new TestEntity();
        testEntity1.setId(1L);
        testEntity1.setName("test1");
        testEntity1.setAge(12);
        testService.join(testEntity1);

        TestEntity testEntity2 = new TestEntity();
        testEntity2.setId(2L);
        testEntity2.setName("test2");
        testEntity2.setAge(15);
        testService.join(testEntity2);

        TestEntity testEntity3 = new TestEntity();
        testEntity3.setId(3L);
        testEntity3.setName("test3");
        testEntity3.setAge(23);
        testService.join(testEntity3);
    }

    @Test
    void searchEntity(){
        //given
        TestEntity entity = new TestEntity();
        entity.setId(4L);
        entity.setName("test4");
        entity.setAge(25);

        //when
        testService.join(entity);
        TestEntity searchedEntity = testService.findEntityById(4L);

        //then
        assertThat(entity.equals(searchedEntity));
    }

    @Test
    void searchAllEntities(){
        List<TestEntity> entities = testService.findAllEntities();
        for (TestEntity entity : entities) {
            System.out.println("entity = " + entity);
        }
    }

    @TestConfiguration
    @EnableJpaRepositories(basePackages = "com.lazyworking.sagupalgu.db")
    static class TestConfig{
        @Bean
        public TestService testService(TestRepository testRepository){
            return new TestService(testRepository);
        }
    }
}
