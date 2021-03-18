import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

class ClassLocationSpec extends ArchUnitSpec {

    def "controllers should reside in the adapters/api package"() {
        given:
        def rule = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().resideInAPackage(ADAPTERS_PACKAGE)
            .because("Controllers should reside in the package '{$ADAPTERS_PACKAGE}'")

        expect:
        rule.check(classesToCheck)
    }

    def "configuration classes should reside in config package"() {
        given:
        def rule = classes()
            .that().areAnnotatedWith(Configuration)
            .should().resideInAPackage(CONFIG_PACKAGE)

        expect:
        rule.check(classesToCheck)
    }
}
