package io.github.potcode.jpacustomrepository

import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtPsiFactory

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