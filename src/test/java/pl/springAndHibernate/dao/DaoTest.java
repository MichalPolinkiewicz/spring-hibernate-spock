package pl.springAndHibernate.dao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.springAndHibernate.config.PersistenceConfig;
import pl.springAndHibernate.config.PersistenceTestConfig;

import javax.transaction.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { PersistenceConfig.class, PersistenceTestConfig.class}
)
@ActiveProfiles("test")
@Transactional
public class DaoTest {

    @Autowired
    private CatRepository catRepository;

    @Test
    public void testSave(){
        Cat cat = new Cat();
        cat.setName("Mruczek");

        catRepository.save(cat);
        String name = catRepository.findAll().get(catRepository.findAll().size()-1).getName();
        int size = catRepository.findAll().size();

        Assert.assertEquals(name, "Mruczek");
        Assert.assertEquals(size, 1);
    }

    @After
    public void cleanup(){
        catRepository.deleteAll();
    }
}
