import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import spock.lang.Specification

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

class NamingConventionSpec extends Specification {

    static classes = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("tech.allegro.hexagon")

    def "controllers should have Endpoint suffix"() {
        given:
        def rule = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().haveSimpleNameEndingWith("Endpoint")

        expect:
        rule.check(classes)
    }

    def "configuration classes should have Config suffix"() {
        given:
        def rule = classes()
            .that().areAnnotatedWith(Configuration)
            .should().haveSimpleNameEndingWith("Config")

        expect:
        rule.check(classes)
    }
}
