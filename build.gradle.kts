@file:Suppress("UnstableApiUsage")

import com.brittank88.gradle.AnnotateClasses
import org.gradle.configurationcache.extensions.capitalized
import javax.xml.transform.Source

// TODO: KDoc task

plugins {
    id("fabric-loom"  ).version("0.12-SNAPSHOT")
    id("maven-publish")
    java
    kotlin("jvm").version("latest.release")
}

val modVersion : String by project
val mavenGroup : String by project

val minecraftVersion    : String by project
val fabricLoaderVersion : String by project
val fabricAPIVersion    : String by project

version = modVersion
group   = mavenGroup

loom.splitEnvironmentSourceSets()

val client: SourceSet by sourceSets.getting

val datagenDir = layout.projectDirectory.dir("src").dir(client.name).dir("generated")

val util: SourceSet by sourceSets.creating { depends(sourceSets.main.get()) }
val utilClient: SourceSet by sourceSets.creating {
    extend(util)
    depends(client)
}
val api: SourceSet by sourceSets.creating {
    extend(util)
    depends(sourceSets.main.get())
}
val apiClient: SourceSet by sourceSets.creating {
    extend(util, utilClient, api)
    depends(client)
}
val datagen: SourceSet by sourceSets.creating {
    extend(client, api, apiClient, util, utilClient)
    depends(client)
}
sourceSets.main { extend(api, util) }
sourceSets.named(client.name) {
    extend(api, apiClient, util, utilClient)

    // Add the datagen-generated files into the jar.
    resources.srcDir(datagenDir)
}

loom {
    accessWidenerPath.set(file("src/main/resources/ad-infinitum.accesswidener"))
    mixin.add(client)

    runs {
        // This adds a new gradle task that runs the datagen API: "gradlew runDatagenClient"
        create("datagenClient") {
            inherit(runConfigs[client.name])
            name("Data Generation")
            vmArgs(
                "-Dfabric-api.datagen",
                "-Dfabric-api.datagen.output-dir=${datagenDir}",
                "-Dfabric-api.datagen.strict-validation"
            )
            ideConfigGenerated(true)
            runDir("build/datagen")
        }
    }
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.

    maven("https://maven.wispforest.io/")
    maven("https://storage.googleapis.com/devan-maven/")
    maven("https://maven.terraformersmc.com/")
    maven("https://repo.essential.gg/repository/maven-public")
}

dependencies {

    val yarnMappingsVersion : String by project
    val owoVersion          : String by project
    val modMenuVersion      : String by project
    val vigilanceMCVersion  : String by project
    val vigilanceVersion    : String by project
    val colorThiefVersion   : String by project

    // To change the versions see the gradle.properties file.
    minecraft        (group = "com.mojang"  , name = "minecraft"    , version = minecraftVersion   )
    mappings         (group = "net.fabricmc", name = "yarn"         , version = yarnMappingsVersion)
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)

    // FAPI (https://github.com/FabricMC/fabric)
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricAPIVersion)

    // oωo-lib (https://github.com/wisp-forest/owo-lib)
    modImplementation(group = "io.wispforest", name = "owo-lib"     , version = owoVersion) // Versions tagged with +1.18 onwards.
    include          (group = "io.wispforest", name = "owo-sentinel", version = owoVersion) // Sentinel will warn users without oωo-lib and give the option to download it automatically.

    // ModMenu (https://github.com/TerraformersMC/ModMenu)
    modCompileOnly(group = "com.terraformersmc", name = "modmenu", version = modMenuVersion)
    modRuntimeOnly(group = "com.terraformersmc", name = "modmenu", version = modMenuVersion)

    // Vigilance (https://github.com/EssentialGG/Vigilance)
    modImplementation(include(group = "gg.essential", name = "vigilance-$vigilanceMCVersion-fabric", version = vigilanceVersion)) { isChanging = true }

    // Smile-Kotlin (https://mvnrepository.com/artifact/com.github.haifengl/smile-kotlin)
    implementation(include(group = "com.github.haifengl", name = "smile-kotlin", version = "latest.release")) {
        exclude(group = "org.slf4j", module = "slf4j-api")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
    withJavadocJar()

    // API feature.
    registerFeature(api.name) {
        usingSourceSet(api)

        withSourcesJar()
        withJavadocJar()
    }
}

// Skip unnecessary api variants.
components.getByName("java") {
    this as AdhocComponentWithVariants
    withVariantsFromConfiguration(configurations[api.apiElementsConfigurationName    ]) { skip() }
    withVariantsFromConfiguration(configurations[api.runtimeElementsConfigurationName]) { skip() }
}

// Clear unnecessary api artifacts and only keep api jar as an artifact.
configurations.apiElements { artifacts.clear() }
artifacts.apiElements(tasks.named(api.jarTaskName))

tasks {

    processResources {
        inputs.property("version", modVersion)
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(mapOf(
                "id" to project.name,
                "name" to project.name.split('-').joinToString(" ", transform = String::capitalized),
                "version" to modVersion,
                "fabricLoaderVersion" to ">=$fabricLoaderVersion",
                "fabricVersion" to ">=$fabricAPIVersion",
                "minecraftVersion" to minecraftVersion
            ))
        }
    }

    withType<JavaCompile> {
        // Ensure that the encoding is set to UTF-8, no matter what the system default is.
        // This fixes some edge cases with special characters not displaying correctly.
        // See: http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
        // If Javadoc is generated, this must be specified in that task too.
        options.encoding = "UTF-8"
    }

    val annotateClientClasses     by registerAnnotateClassTaskForSourceSet(client    )
    val annotateApiClientClasses  by registerAnnotateClassTaskForSourceSet(apiClient )
    val annotateUtilClientClasses by registerAnnotateClassTaskForSourceSet(utilClient)

    jar {
        from("LICENSE") {
            rename { "${it}_${project.name}" }
        }

        exclude { fileTreeElement -> fileTreeElement.file in client.output.classesDirs.asFileTree }
        from(annotateClientClasses.flatMap(AnnotateClasses::destinationDirectory))

        from(api .output)
        from(util.output)
        from(annotateApiClientClasses .flatMap(AnnotateClasses::destinationDirectory))
        from(annotateUtilClientClasses.flatMap(AnnotateClasses::destinationDirectory))
    }

    // API jar.
    named<Jar>(api.jarTaskName) {
        from(util.output)
        from(annotateApiClientClasses .flatMap(AnnotateClasses::destinationDirectory))
        from(annotateUtilClientClasses.flatMap(AnnotateClasses::destinationDirectory))
    }

    assemble {
        dependsOn(api.jarTaskName)
    }
}

fun registerAnnotateClassTaskForSourceSet(sourceSet: SourceSet) = project.tasks.registering(AnnotateClasses::class) {
    classDirectories.from(sourceSet.output)
    destinationDirectory.set(layout.buildDirectory.dir("annotatedClasses").map { it.dir(sourceSet.name) })
}

fun SourceSet.extend(vararg sourceSets: SourceSet) {
    sourceSets.forEach {
        configurations[apiConfigurationName           ].extendsFrom(configurations[it.apiConfigurationName           ])
        configurations[implementationConfigurationName].extendsFrom(configurations[it.implementationConfigurationName])
        configurations[runtimeOnlyConfigurationName   ].extendsFrom(configurations[it.runtimeOnlyConfigurationName   ])
        configurations[compileOnlyConfigurationName   ].extendsFrom(configurations[it.compileOnlyConfigurationName   ])

        runtimeClasspath += it.output
        compileClasspath += it.output
    }
}

fun SourceSet.depends(sourceSet: SourceSet) {
    runtimeClasspath += sourceSet.runtimeClasspath
    compileClasspath += sourceSet.compileClasspath
}
