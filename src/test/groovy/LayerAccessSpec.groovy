import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

class LayerAccessSpec extends ArchUnitSpec {

    def "classes in domain package should not depend on spring classes"() {
        given:
        def rule = noClasses()
            .that().resideInAPackage(DOMAIN_PACKAGE)
            .should().dependOnClassesThat().resideInAPackage(SPRING_PACKAGE)

        expect:
        rule.check(classesToCheck)
    }

    def "classes in domain package should not depend on adapters"() {
        given:
        def rule = noClasses()
            .that().resideInAPackage(DOMAIN_PACKAGE)
            .should().dependOnClassesThat().resideInAPackage(ADAPTERS_PACKAGE)

        expect:
        rule.check(classesToCheck)
    }
}
