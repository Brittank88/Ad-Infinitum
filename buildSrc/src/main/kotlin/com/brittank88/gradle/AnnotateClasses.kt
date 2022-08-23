package com.brittank88.gradle

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.*
import java.nio.file.Files

abstract class AnnotateClasses : DefaultTask() {

    abstract val classDirectories    : ConfigurableFileCollection @InputFiles      get
    abstract val destinationDirectory: DirectoryProperty          @OutputDirectory get

    @TaskAction
    fun annotate() {

        // Apply annotation to every class in the directory.
        classDirectories.forEach { classDirectory ->

            val fileTree = project.fileTree(classDirectory)

            // Copy classes, applying annotation.
            fileTree.matching { it.include("**/*.class") }.forEach {

                val reader = it.inputStream().use(::ClassReader)
                val writer = ClassWriter(0)
                reader.accept(AnnotationAdder(writer), 0)

                val resolved = destinationDirectory.asFile.get().resolve(it.relativeTo(classDirectory))
                Files.createDirectories(resolved.parentFile.toPath())
                resolved.writeBytes(writer.toByteArray())
            }
        }
    }

    private class AnnotationAdder(val delegate: ClassVisitor): ClassVisitor(Opcodes.ASM9, delegate) {

        override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
            super.visit(version, access, name, signature, superName, interfaces)
            delegate
                .visitAnnotation("L${Type.getInternalName(Environment::class.java)};", true)
                .visitEnum("value", "L${Type.getInternalName(EnvType::class.java)};", EnvType.CLIENT.name)
        }
    }
}
