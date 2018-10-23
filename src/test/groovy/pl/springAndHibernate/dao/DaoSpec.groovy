package pl.springAndHibernate.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import pl.springAndHibernate.config.PersistenceConfig

import spock.lang.Specification

@ContextConfiguration(classes = [PersistenceConfig.class])
@ActiveProfiles("test")
class DaoSpec extends Specification {

    @Autowired
    private Environment environment
    @Autowired
    CatRepository catRepository

    def "active profile should be test"(){
        expect:
        environment.activeProfiles.length == 1
        environment.getActiveProfiles()[0] == "test"
    }

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
