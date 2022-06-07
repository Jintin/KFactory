package com.jintin.kfactory.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.validate
import com.jintin.kfactory.AutoElement
import com.jintin.kfactory.AutoFactory
import com.squareup.kotlinpoet.*

class BuilderProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val factories = getFactories(resolver)
        val data = getElements(resolver, factories)
        data.forEach {
            writeFile(codeGenerator, genFile(it.key, it.value))
        }
        return emptyList()
    }

    private fun getFactories(resolver: Resolver): Set<ClassName> {
        return resolver.getSymbolsWithAnnotation(AutoFactory::class.qualifiedName.orEmpty())
            .filterIsInstance<KSClassDeclaration>()
            .filter(KSNode::validate)
            .map { it.toClassName() }
            .toSet()
    }

    private fun genFile(key: ClassName, list: List<ElementIfo>): FileSpec {
        val packageName = key.packageName
        val fileName = key.simpleName + "Factory"
        return FileSpec.builder(packageName, fileName)
            .addType(
                TypeSpec.objectBuilder(fileName)
                    .addFunction(
                        FunSpec.builder("create")
                            .addParameter("key", String::class)
                            .returns(key)
                            .beginControlFlow("return when (key)")
                            .also { builder ->
                                list.forEach {
                                    builder.addStatement(""""${it.key}" -> %T()""", it.className)
                                }
                            }
                            .addStatement("""else -> throw RuntimeException("Not support type")""")
                            .endControlFlow()
                            .build()
                    ).build()
            ).build()
    }

    private fun writeFile(codeGenerator: CodeGenerator, fileSpec: FileSpec) {
        codeGenerator.createNewFile(
            dependencies = Dependencies(true),
            packageName = fileSpec.packageName,
            fileName = fileSpec.name
        ).use {
            it.writer().use(fileSpec::writeTo)
        }
    }

    private fun getElements(
        resolver: Resolver,
        factories: Set<ClassName>
    ): Map<ClassName, List<ElementIfo>> {
        val result = mutableMapOf<ClassName, MutableList<ElementIfo>>()
        factories.forEach { result[it] = mutableListOf() }
        resolver.getSymbolsWithAnnotation(AutoElement::class.qualifiedName.orEmpty())
            .filterIsInstance<KSClassDeclaration>()
            .filter(KSNode::validate)
            .forEach { d ->
                val element = ElementIfo(getLabel(d), d.toClassName())
                d.superTypes
                    .map { it.resolve().declaration.toClassName() }
                    .filter { result.containsKey(it) }
                    .forEach { name ->
                        result[name]?.add(element)
                    }
            }
        return result
    }

    private fun getLabel(declaration: KSClassDeclaration): String {
        return declaration.annotations.filter {
            it.shortName.asString() == AutoElement::class.simpleName.orEmpty()
        }.map { annotation ->
            annotation.arguments.filter {
                it.name?.getShortName() == "label"
            }.map {
                it.value as? String
            }.firstOrNull()
        }.firstOrNull().orEmpty()
    }
}