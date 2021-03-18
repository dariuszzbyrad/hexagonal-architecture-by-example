import com.tngtech.archunit.core.domain.JavaField
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import org.springframework.data.mongodb.core.index.Indexed

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields

class MongoRulesSpec extends ArchUnitSpec {

    //classes/methods/fields that ${PREDICATE} should ${CONDITION}
    def "mongo documents should have configured background indexes"() {
        expect:
        fields().that()
            .areAnnotatedWith(Indexed)
            .should(indexBuildInBackground)
            .check(classesToCheck)
    }

    ArchCondition<JavaField> indexBuildInBackground = new ArchCondition<JavaField>("Index should be build in background") {
        @Override
        void check(JavaField item, ConditionEvents events) {
            for (Indexed indexedAnnotation : item.getAnnotationOfType(Indexed)) {
                if (!indexedAnnotation.background()) {
                    String message = "{$item.owner} annotated by @Indexed does not have setup background value to TRUE"
                    events.add(SimpleConditionEvent.violated(indexedAnnotation, message))
                }
            }
        }
    }
}
