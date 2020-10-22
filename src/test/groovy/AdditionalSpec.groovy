import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.Location
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import org.springframework.web.bind.annotation.RestController
import spock.lang.Specification
import tech.allegro.hexagon.articles.domain.ports.ArticleRepository

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

class AdditionalSpec extends Specification {

    static classes = new ClassFileImporter()
        .withImportOption(new ImportOption() {
            @Override
            boolean includes(Location location) {
                return !location.contains("/test/")
            }
        })
        .importPackages("tech.allegro.hexagon")

    def "ArticleRepository class should have correct MD5 sum"() {
        given:
        def md5sum = classes.get(ArticleRepository.class).getSource().get().getMd5sum()

        expect:
        md5sum.getAt('text') == 'fab1f61fe0148c13bb17d670bad6415a'
    }

    def "custom rule"() {
        given:
        DescribedPredicate<JavaClass> isAnnotatedWithRestController =
            new DescribedPredicate<JavaClass>("is annotated with the RestController") {
                @Override
                boolean apply(JavaClass input) {
                    return input.isAnnotatedWith(RestController.class)
                }
            }

        ArchCondition<JavaClass> shouldResideInAPIPackage =
            new ArchCondition<JavaClass>("should reside in api package") {
                @Override
                void check(JavaClass item, ConditionEvents events) {
                    if (!item.getPackage().getName().contains('.adapters.api')) {
                        String message = String.format(
                            "class %s reside in incorrect package %s", item.getName(), item.getPackageName())
                        events.add(SimpleConditionEvent.violated(item, message))
                    }
                }
            }

        def rule = classes()
            .that(isAnnotatedWithRestController)
            .should(shouldResideInAPIPackage)

        expect:
        rule.check(classes)
    }
}
