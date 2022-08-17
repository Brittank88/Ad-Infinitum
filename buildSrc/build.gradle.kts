import org.codehaus.groovy.runtime.DefaultGroovyMethods.use
import org.jetbrains.kotlin.konan.file.use
import java.util.Properties

plugins {
    kotlin("jvm").version("1.7.+")
}

repositories {
    maven("https://maven.fabricmc.net")
    mavenCentral()
}

dependencies {

    val properties = file("../gradle.properties").inputStream().use {
        Properties().apply { load(it) }
    }

    implementation(group = "org.ow2.asm" , name = "asm"           , version = "latest.release"                             )
    implementation(group = "net.fabricmc", name = "fabric-loader" , version = properties.getProperty("fabricLoaderVersion"))
}
