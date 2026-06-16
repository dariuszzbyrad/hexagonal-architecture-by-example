import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.core.importer.Location
import tech.allegro.hexagon.articles.domain.ports.ArticleRepository

class AdditionalSpec extends ArchUnitSpec {

    static classes = new ClassFileImporter()
        .withImportOption(new ImportOption() {
            @Override
            boolean includes(Location location) {
                return !location.contains("/test/")
            }
        })
        .importPackages(APP_PACKAGE)

    def "ArticleRepository class should have correct MD5 sum"() {
        given:
        def md5sum = classes.get(ArticleRepository.class).getSource().get().getMd5sum()

        expect:
        md5sum.getAt('text') == 'cd6caefc0a96db29c73cd28561559341'
    }
}
