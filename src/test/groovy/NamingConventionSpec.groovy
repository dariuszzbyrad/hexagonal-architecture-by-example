import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

class NamingConventionSpec extends ArchUnitSpec {

    def "controllers should have Endpoint suffix"() {
        given:
        def rule = classes()
            .that().areAnnotatedWith(RestController.class)
            .should().haveSimpleNameEndingWith("Endpoint")

        expect:
        rule.check(classesToCheck)
    }

    def "configuration classes should have Config suffix"() {
        given:
        def rule = classes()
            .that().areAnnotatedWith(Configuration)
            .should().haveSimpleNameEndingWith("Config")

        expect:
        rule.check(classesToCheck)
    }
}
