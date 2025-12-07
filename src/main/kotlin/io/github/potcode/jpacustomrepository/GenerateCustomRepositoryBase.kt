package io.github.potcode.jpacustomrepository

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.annotations.Nls
import org.jetbrains.kotlin.psi.KtClass

abstract class GenerateCustomRepositoryBase : PsiElementBaseIntentionAction() {
    @Nls
    override fun getFamilyName(): String {
        return "JPA Custom Repository"
    }

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        if (!isKotlinFile(element)) return false
        if (!isKotlinInterfaceIdentifier(element)) return false

        val interfaceClass = PsiTreeUtil.getParentOfType(element, KtClass::class.java) ?: return false
        if (!existsSuperTypeByPattern(interfaceClass, "Repository")) return false

        val customRepoName = "${interfaceClass.name}Custom"
        return !existsSuperTypeByPattern(interfaceClass, customRepoName)
    }
}