package pl.springAndHibernate.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import pl.springAndHibernate.config.PersistenceConfig
import pl.springAndHibernate.config.PersistenceTestConfig
import spock.lang.Specification

@ContextConfiguration(classes = [PersistenceConfig.class, PersistenceTestConfig.class])
@ActiveProfiles("test")
class DaoSpec extends Specification {

    @Autowired
    CatRepository catRepository

    def "save() should persist object in db"() {
        given:
        Cat cat = new Cat()
        cat.setName("Andrzej")

        when:
        catRepository.save(cat)
        def result = catRepository.findById(cat.getId())

        then:
        result.get().name == "Andrzej"
    }

    def cleanup() {
        catRepository.deleteAll()
    }
}
