import org.gradle.api.Action
import org.gradle.api.artifacts.CacheableRule
import org.gradle.api.artifacts.ComponentMetadataContext
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.attributes.Usage
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named
import javax.inject.Inject
import org.gradle.api.artifacts.VariantMetadata

@CacheableRule
abstract class ExposeRuntimeDepsRule @Inject constructor(
    private val objects: ObjectFactory
) : ComponentMetadataRule {

    override fun execute(context: ComponentMetadataContext) {
        val details = context.details

        val javaApiUsage = objects.named<Usage>(Usage.JAVA_API)
        val javaRuntimeUsage = objects.named<Usage>(Usage.JAVA_RUNTIME)

        details.allVariants(object : Action<VariantMetadata> {
            override fun execute(variant: VariantMetadata) {
                println(variant)
            }
        })
    }
}