import org.gradle.api.Action
import org.gradle.api.artifacts.CacheableRule
import org.gradle.api.artifacts.ComponentMetadataContext
import org.gradle.api.artifacts.DirectDependenciesMetadata
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.artifacts.ComponentMetadataRule
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.Attribute
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named
import javax.inject.Inject
import org.gradle.api.artifacts.VariantMetadata

//@CacheableRule // comment or else rule does not print everytime
abstract class ExposeRuntimeDepsRule @Inject constructor(
    private val objects: ObjectFactory
) : ComponentMetadataRule {

    override fun execute(context: ComponentMetadataContext) {
        val details = context.details

        val javaApiUsage = objects.named<Usage>(Usage.JAVA_API)
        val javaRuntimeUsage = objects.named<Usage>(Usage.JAVA_RUNTIME)

        details.allVariants(object : Action<VariantMetadata> {
            override fun execute(variant: VariantMetadata) {
                variant.attributes(object : Action<AttributeContainer> {
                    override fun execute(attributes: AttributeContainer) {
                        val usageAttribute = Attribute.of("org.gradle.usage", String::class.java)
                        println("Variant: ${attributes.getAttribute(usageAttribute)}")
                    }
                })
                println("  - Dependencies: ")
                variant.withDependencies(object : Action<DirectDependenciesMetadata> {
                    override fun execute(dependencies: DirectDependenciesMetadata) {
                        dependencies.forEach { dep ->
                            println("    - ${dep.group}:${dep.name}")
                        }
                    }
                })
            }
        })
    }
}