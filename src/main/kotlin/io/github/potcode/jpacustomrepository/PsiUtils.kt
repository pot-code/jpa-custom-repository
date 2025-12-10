package io.github.potcode.jpacustomrepository

import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtPsiFactory

/**
 * 为目标类追加接口继承
 *
 * @param psiFactory KtPsiFactory
 * @param targetClass 目标类
 * @param superInterface 接口名称
 */
fun appendSuperInterface(psiFactory: KtPsiFactory, targetClass: KtClass, superInterface: String) {
    val superTypeEntryList = targetClass.getSuperTypeList()
    if (superTypeEntryList == null) {
        val newSuperList = psiFactory.createSuperTypeEntry(": $superInterface")
        val anchor = targetClass.nameIdentifier
        if (anchor != null) {
            targetClass.addAfter(anchor, newSuperList)
        }
    } else {
        superTypeEntryList.add(psiFactory.createComma())
        superTypeEntryList.add(psiFactory.createType(superInterface))
    }
}