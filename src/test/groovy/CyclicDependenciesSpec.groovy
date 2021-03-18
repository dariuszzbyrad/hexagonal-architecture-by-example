import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices

class CyclicDependenciesSpec extends ArchUnitSpec {

    def "application should be free of cycles"() {
        given:
        def rule = slices()
            .matching(APP_PACKAGE + '.(*)..')
            .should().beFreeOfCycles()

        expect:
        rule.check(classesToCheck)
    }
}
