import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaPackage
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.library.metrics.*

class MetricsSpec extends ArchUnitSpec {

    def "Cumulative Dependency Metrics by John Lakos"() {
        given:
        MetricsComponents<JavaClass> components = MetricsComponents.fromClasses(classesToCheck)

        LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(components)

        def CCD = metrics.getCumulativeComponentDependency()
        def ACD = metrics.getAverageComponentDependency()
        def RACD = metrics.getRelativeAverageComponentDependency()
        def NCCD = metrics.getNormalizedCumulativeComponentDependency()

        expect:
        CCD < 500 // The sum of all DependsOn values of all components
        ACD < 10 // The CCD divided by the number of all components
        RACD < 1 // The ACD divided by the number of all components
        NCCD < 3 // The CCD of the system divided by the CCD of a balanced binary tree with the same number of components
    }

    def "Component Dependency Metrics by Robert C. Martin"() {
        given:
        Set<JavaPackage> packages = new ClassFileImporter().importPackages(APP_PACKAGE).getPackage(APP_PACKAGE).getSubpackages()
        MetricsComponents<JavaClass> components = MetricsComponents.fromPackages(packages)
        ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(components)

        def Ce = metrics.getEfferentCoupling(DOMAIN_ROOT_PACKAGE)
        def Ca = metrics.getAfferentCoupling(DOMAIN_ROOT_PACKAGE)
        def I = metrics.getInstability(DOMAIN_ROOT_PACKAGE)
        def A = metrics.getAbstractness(DOMAIN_ROOT_PACKAGE)
        def D = metrics.getNormalizedDistanceFromMainSequence(DOMAIN_ROOT_PACKAGE)

        expect:
        Ce == 0 // Efferent Coupling (Ce): The number of outgoing dependencies to any other component
        Ca < 5 // Afferent Coupling (Ca): The number of incoming dependencies from any other component
        I == 0 // Instability (I): Ce / (Ca + Ce), i.e. the relationship of outgoing dependencies to all dependencies
        A < 0.4 // Abstractness (A): num(abstract_classes) / num(all_classes) in the component
        D < 0.7 // Distance from Main Sequence (D): | A + I - 1 |, i.e. the normalized distance from the ideal line between (A=1, I=0) and (A=0, I=1)
    }

    def "Visibility Metrics by Herbert Dowalil"() {
        given:
        Set<JavaPackage> packages = classesToCheck.getPackage(APP_PACKAGE).getSubpackages()
        MetricsComponents<JavaClass> components = MetricsComponents.fromPackages(packages)
        VisibilityMetrics metrics = ArchitectureMetrics.visibilityMetrics(components)

        def RV = metrics.getRelativeVisibility(DOMAIN_ROOT_PACKAGE)
        def ARV = metrics.getAverageRelativeVisibility()
        def GRV = metrics.getGlobalRelativeVisibility()

        expect:
        RV == 1 // Relative Visibility (RV): num(visible_elements) / num(all_elements) for each component
        ARV < 1 // Average Relative Visibility (ARV): The average of all RV values
        GRV < 0.5 // Global Relative Visibility (GRV): num(visible_elements) / num(all_elements) over all components
    }
}
