package io.github.potcode.jpacustomrepository

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.FileContentUtil
import com.intellij.util.IncorrectOperationException
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory

class GenerateBlazeViewCustomRepository : GenerateCustomRepositoryBase() {
    override fun getText(): String {
        return "Extends blaze EntityView repository"
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        val interfaceClass = PsiTreeUtil.getParentOfType(element, KtClass::class.java) ?: return
        val interfaceName: String = interfaceClass.name ?: return

        val currentFile = interfaceClass.containingFile as KtFile
        val currentDir: PsiDirectory = currentFile.containingDirectory ?: return
        val currentPackage = currentFile.packageFqName.asString()
        val psiFactory = KtPsiFactory(project)

        val customRepoName = "${interfaceName}Custom"
        val customRepoFilename = "$customRepoName.kt"
        if (currentDir.findFile(customRepoFilename) == null) {
            val customRepoFile = psiFactory.createFile(
                customRepoFilename, """
            package $currentPackage
            
            interface $customRepoName {
                // TODO
            }
        """.trimIndent()
            )
            currentDir.add(customRepoFile)
        }

        val customRepoImplName = "${customRepoName}Impl"
        val customRepoImplFilename = "$customRepoImplName.kt"
        if (currentDir.findFile(customRepoImplFilename) == null) {
            val customRepoImplFile = psiFactory.createFile(
                "$customRepoImplName.kt", """
            package $currentPackage
            
            import com.blazebit.persistence.CriteriaBuilderFactory
            import com.blazebit.persistence.view.EntityViewManager
            import jakarta.persistence.EntityManager

            class $customRepoImplName(
                private val em: EntityManager,
                private val evm: EntityViewManager,
                private val cbf: CriteriaBuilderFactory,
            ) : $customRepoName {
            }
        """.trimIndent()
            )
            currentDir.add(customRepoImplFile)
        }

        if (!existsSuperTypeByPattern(interfaceClass, customRepoName)) {
            appendSuperInterface(psiFactory, interfaceClass, customRepoName)
        }

        val styleManager = CodeStyleManager.getInstance(project)
        styleManager.reformat(currentFile)
        FileContentUtil.reparseFiles(project, listOf(currentFile.virtualFile), false)
    }
}