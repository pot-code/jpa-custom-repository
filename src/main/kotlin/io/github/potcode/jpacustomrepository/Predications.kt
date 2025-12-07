package io.github.potcode.jpacustomrepository

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtClass

fun isKotlinFile(element: PsiElement): Boolean {
    return element.language == KotlinLanguage.INSTANCE
}

/**
 * 检查当前 PsiElement 是否是一个 Kotlin 接口的标识符
 *
 * @param element 被检查的 PsiElement
 */
fun isKotlinInterfaceIdentifier(element: PsiElement): Boolean {
    if (element.elementType != KtTokens.IDENTIFIER) return false
    val containingPsiClass = PsiTreeUtil.getParentOfType(element, KtClass::class.java) ?: return false
    if (!containingPsiClass.isInterface()) return false
    return containingPsiClass.nameIdentifier == element
}

/**
 * 检查 KtClass 继承的父类的类名是否包含特定字符串
 *
 * @param cls 被检查的 KtClass
 * @param pattern 指定包名
 */
fun existsSuperTypeByPattern(cls: KtClass, pattern: String): Boolean {
    return cls.superTypeListEntries.any { superTypeEntry ->
        val typeReference = superTypeEntry.typeReference
        typeReference?.text?.contains(pattern) ?: false
    }
}
