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

    private fun genFile(key: ClassName, list: List<ClassName>): FileSpec {
        val packageName = key.packageName
        val funcName = key.simpleName + "Factory"
        val enumName = key.simpleName + "Type"

        return FileSpec.builder(packageName, funcName)
            .addType(TypeSpec.enumBuilder(enumName)
                .apply {
                    list.forEach {
                        addEnumConstant(it.simpleName.uppercase())
                    }
                }
                .build())
            .addFunction(FunSpec.builder(funcName)
                .addParameter("key", ClassName(packageName, enumName))
                .returns(key)
                .beginControlFlow("return when (key)")
                .apply {
                    list.forEach {
                        addStatement("${enumName}.${it.simpleName.uppercase()} -> %T()", it)
                    }
                }
                .endControlFlow()
                .build())
            .build()
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
    ): Map<ClassName, List<ClassName>> {
        val result = mutableMapOf<ClassName, MutableList<ClassName>>()
        factories.forEach { result[it] = mutableListOf() }
        resolver.getSymbolsWithAnnotation(AutoElement::class.qualifiedName.orEmpty())
            .filterIsInstance<KSClassDeclaration>()
            .filter(KSNode::validate)
            .forEach { d ->
                d.superTypes
                    .map { it.resolve().declaration.toClassName() }
                    .filter { result.containsKey(it) }
                    .forEach { name ->
                        result[name]?.add(d.toClassName())
                    }
            }
        return result
    }
}