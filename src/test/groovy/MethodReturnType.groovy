import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import spock.lang.Specification

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import static com.tngtech.archunit.library.freeze.FreezingArchRule.freeze

class MethodReturnType extends Specification {

    static classes = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("tech.allegro.hexagon")

    def "endpoint methods should return ResponseEntity"() {
        given:
        def rule = methods()
            .that().areNotPrivate()
            .and().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
            .should().haveRawReturnType(ResponseEntity)

        expect:
        rule.check(classes)
    }
}
