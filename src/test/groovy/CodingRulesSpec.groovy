import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.library.GeneralCodingRules
import spock.lang.Specification

import java.util.logging.Logger

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields

class CodingRulesSpec extends Specification {

    static classes = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("tech.allegro.hexagon")

    def "logger should be private static final"() {
        given:
        def rule = fields().that().haveRawType(Logger.class)
            .should().bePrivate()
            .andShould().beStatic()
            .andShould().beFinal()

        expect:
        rule.check(classes)
    }

    def "general coding rules"() {
        expect:
        GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.because("You should use a logger").check(classes)
        GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.because("You should name your exceptions").check(classes)
        GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME.because("You should not use the jodatime")
        GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION.because("You should use constructor injection").check(classes)
    }
}
