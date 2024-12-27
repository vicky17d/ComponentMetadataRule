plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    components {
        all<ExposeRuntimeDepsRule>()
    }
    implementation("org.hibernate:hibernate-core:6.4.4.Final")
}

tasks.register("compareClasspaths") {
    doLast {
        val compileFiles = configurations["compileClasspath"].files.map { it.nameWithoutExtension }.toSet()
        val runtimeFiles = configurations["runtimeClasspath"].files.map { it.nameWithoutExtension }.toSet()

        println("=== FILES ONLY IN RUNTIME CLASSPATH ===")
        (runtimeFiles - compileFiles).sorted().forEach { println(it) }

        println("\n=== FILES IN BOTH CLASSPATHS ===")
        (compileFiles intersect runtimeFiles).sorted().forEach { println(it) }
    }
}