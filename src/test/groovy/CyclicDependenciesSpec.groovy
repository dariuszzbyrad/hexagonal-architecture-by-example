import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import spock.lang.Specification

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

class CyclicDependenciesSpec extends Specification {

    static classes = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("tech.allegro.hexagon")

    def "application should be free of cycles"() {
        given:
        def rule = slices()
            .matching("tech.allegro.hexagon.(*)..")
            .should().beFreeOfCycles()

        expect:
        rule.check(classes)
    }
}
