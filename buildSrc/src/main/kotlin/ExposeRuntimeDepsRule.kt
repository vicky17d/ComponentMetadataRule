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
        val processedModules = mutableSetOf<String>()

        details.allVariants(object : Action<VariantMetadata> {
            override fun execute(variant: VariantMetadata) {
                // Create a unique identifier for this module
                val moduleId = "${context.details.id}"

                // Only process if we haven't seen this module before
                if (!processedModules.contains(moduleId)) {
                    variant.attributes(object : Action<AttributeContainer> {
                        override fun execute(attributes: AttributeContainer) {
                            val usageAttribute = Attribute.of("org.gradle.usage", String::class.java)
                            val categoryAttribute = Attribute.of("org.gradle.category", String::class.java)

                            if (attributes.getAttribute(categoryAttribute) == "library") {
                                variant.withDependencies(object : Action<DirectDependenciesMetadata> {
                                    override fun execute(deps: DirectDependenciesMetadata) {
                                        if (deps.isNotEmpty()) {
                                            println("\n=== Processing $moduleId ===")
                                            println("${attributes.getAttribute(usageAttribute)} dependencies:")
                                            deps.forEach { dep ->
                                                println("  - ${dep.group}:${dep.name}")
                                            }
                                        }
                                    }
                                })
                            }
                        }
                    })
                    processedModules.add(moduleId)
                }
            }
        })
    }
}