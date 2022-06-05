package com.jintin.kactory.processor

import com.google.devtools.ksp.symbol.KSDeclaration
import com.google.devtools.ksp.symbol.KSTypeParameter
import com.squareup.kotlinpoet.ClassName

fun KSDeclaration.toClassName(): ClassName {
    return ClassName(packageName.asString(), asNameList())
}

fun KSDeclaration.asNameList(): List<String> {
    val list = mutableListOf<String>()
    var definition: KSDeclaration? = this
    while (definition != null) {
        list.add(0, definition.simpleName.asString())
        if (definition is KSTypeParameter) {
            break
        }
        definition = definition.parentDeclaration
    }
    return list
}