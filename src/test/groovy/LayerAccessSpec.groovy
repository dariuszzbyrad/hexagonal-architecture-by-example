import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import spock.lang.Specification

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

class LayerAccessSpec extends Specification {

    static classes = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("tech.allegro.hexagon")

    def "classes in domain package should not depend on spring classes"() {
        given:
        def rule = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..")

        expect:
        rule.check(classes)
    }

    def "classes in domain package should not depend on adapters"() {
        given:
        def rule = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..adapters..")

        expect:
        rule.check(classes)
    }
}
