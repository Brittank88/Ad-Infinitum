import com.brittank88.gradle.AnnotateClasses
import org.apache.commons.text.WordUtils

plugins {
    id("fabric-loom"  ).version("0.12-SNAPSHOT")
    id("maven-publish")
    java
}

buildscript {
    dependencies {
        val fabricLoaderVersion     : String by project

        classpath(group = "org.apache.commons", name = "commons-text"  , version = "latest.release")
        classpath(group = "org.ow2.asm"       , name = "asm-tree"      , version = "latest.release")
        classpath(group = "net.fabricmc"      , name = "fabric-loader" , version = fabricLoaderVersion)
    }
}

val modVersion: String by project
val mavenGroup: String by project

val minecraftVersion        : String by project
val fabricLoaderVersion     : String by project
val fabricAPIVersion        : String by project

version = modVersion
group   = mavenGroup

var datagenDir = layout.projectDirectory.dir("src").dir("client").dir("generated")

@Suppress("UnstableApiUsage")
loom {
    splitEnvironmentSourceSets()

    accessWidenerPath.set(file("src/main/resources/ad-infinitum.accesswidener"))
    mixin.add(sourceSets["client"])

    runs {
        // This adds a new gradle task that runs the datagen API: "gradlew runDatagenClient"
        create("datagenClient") {
            inherit(runConfigs["client"])
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

val client: SourceSet by sourceSets.getting

val util: SourceSet by sourceSets.creating {
    compileClasspath += configurations.compileClasspath.get()
    runtimeClasspath += configurations.runtimeClasspath.get()
}
val utilClient: SourceSet by sourceSets.creating {
    compileClasspath += client.compileClasspath + util.output
    runtimeClasspath += client.runtimeClasspath + util.output
}
val api: SourceSet by sourceSets.creating {
    compileClasspath += configurations.compileClasspath.get() + util.output
    runtimeClasspath += configurations.runtimeClasspath.get() + util.output
}
val apiClient: SourceSet by sourceSets.creating {
    val apiClientSourceSets = util.output + utilClient.output + api.output
    compileClasspath += client.compileClasspath + apiClientSourceSets
    runtimeClasspath += client.runtimeClasspath + apiClientSourceSets
}
val datagen: SourceSet by sourceSets.creating {
    val datagenSourceSets = client.output + api.output + apiClient.output + util.output + utilClient.output
    compileClasspath += client.compileClasspath + datagenSourceSets
    runtimeClasspath += client.runtimeClasspath + datagenSourceSets
}
sourceSets.main {
    val mainSourceSets = api.output + util.output
    compileClasspath += mainSourceSets
    runtimeClasspath += mainSourceSets
}
sourceSets.named(client.name) {
    val clientSourceSets = api.output + apiClient.output + util.output + utilClient.output
    client.compileClasspath += clientSourceSets
    client.runtimeClasspath += clientSourceSets

    // Add the datagen-generated files into the jar.
    resources.srcDir(datagenDir)
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.

    maven("https://maven.wispforest.io")
    maven("https://storage.googleapis.com/devan-maven/")
}

dependencies {

    val yarnMappingsVersion     : String by project
    val owoVersion              : String by project
    val arrpVersion             : String by project
    val apacheCommonsTextVersion: String by project
    val colorThiefVersion       : String by project

    // To change the versions see the gradle.properties file.
    minecraft        (group = "com.mojang"  , name = "minecraft"    , version = minecraftVersion   )
    mappings         (group = "net.fabricmc", name = "yarn"         , version = yarnMappingsVersion)
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = fabricLoaderVersion)

    // FAPI.
    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = fabricAPIVersion)

    // oωo-lib
    modImplementation(group = "io.wispforest", name = "owo-lib"     , version = owoVersion) // Versions tagged with +1.18 onwards.
    include          (group = "io.wispforest", name = "owo-sentinel", version = owoVersion) // Sentinel will warn users without oωo-lib and give the option to download it automatically.

    // ARRP
    modImplementation(group = "net.devtech", name = "arrp", version = arrpVersion)

    // Apache Commons Text
    include(implementation(group = "org.apache.commons", name = "commons-text", version = apacheCommonsTextVersion))  // TODO: Stop using this.

    // https://mvnrepository.com/artifact/de.androidpit/color-thief
    include(utilClient.implementationConfigurationName(group = "de.androidpit", name = "color-thief", version = colorThiefVersion))
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

tasks {

    processResources {
        inputs.property("version", modVersion)
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(mapOf(
                "id" to project.name,
                "name" to WordUtils.capitalizeFully(project.name.replace('-', ' ')),
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
