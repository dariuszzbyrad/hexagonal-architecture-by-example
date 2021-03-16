import com.tngtech.archunit.library.GeneralCodingRules

import java.util.logging.Logger

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields

class CodingRulesSpec extends ArchUnitSpec {

    def "logger should be private static final"() {
        given:
        def rule = fields().that().haveRawType(Logger.class)
            .should().bePrivate()
            .andShould().beStatic()
            .andShould().beFinal()

        expect:
        rule.check(classesToCheck)
    }

    def "general coding rules"() {
        expect:
        GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS.because("You should use a logger").check(classesToCheck)
        GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS.because("You should name your exceptions").check(classesToCheck)
        GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME.because("You should not use the jodatime").check(classesToCheck)
        GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION.because("You should use constructor injection").check(classesToCheck)
    }
}
