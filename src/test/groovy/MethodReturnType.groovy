import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods

class MethodReturnType extends ArchUnitSpec {

    def "endpoint methods should return ResponseEntity"() {
        given:
        def rule = methods()
            .that().areNotPrivate()
            .and().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
            .should().haveRawReturnType(ResponseEntity)

        expect:
        rule.check(classesToCheck)
    }
}
