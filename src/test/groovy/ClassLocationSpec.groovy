import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import spock.lang.Specification

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

class ClassLocationSpec extends Specification {

    static classes = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("tech.allegro.hexagon")

    //classes that ${PREDICATE} should ${CONDITION}
    def "controllers should reside in the adapters/api package"() {
        given:
        def rule = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().resideInAPackage("..adapters.api")
            .because("Controllers should reside in the package 'tech.allegro.hexagon.adapters.api'")

        expect:
        rule.check(classes)
    }

    def "configuration classes should reside in config package"() {
        given:
        def rule = classes()
            .that().areAnnotatedWith(Configuration)
            .should().resideInAPackage("..config..")

        expect:
        rule.check(classes)
    }
}
