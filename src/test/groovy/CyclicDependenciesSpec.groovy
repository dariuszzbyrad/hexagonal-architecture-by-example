import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

class CyclicDependenciesSpec extends ArchUnitSpec {

    static classes = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("tech.allegro.hexagon")

    def "application should be free of cycles"() {
        given:
        def rule = slices()
            .matching(APP_PACKAGE + '.(*)..')
            .should().beFreeOfCycles()

        expect:
        rule.check(classes)
    }
}
