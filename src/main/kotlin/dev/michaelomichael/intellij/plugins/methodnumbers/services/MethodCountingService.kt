package dev.michaelomichael.intellij.plugins.methodnumbers.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.childrenOfType

// TODO: This works reasonably well, but the MethodChangeListener doesn't always tell us when something's changed,
//       so we're getting lots of cases where you type a new method and it ends up being told that it doesn't have a
//       method number.
//       The obvious course of action is to refresh the cache when we don't find the method number in there, but it
//       feels like there's a massive risk of us causing an infinite loop.
//       It might be possible for us to just simplify the whole thing by having the Hints Collector just keep track of
//       how many methods it's visited so far, assuming that a single hints collector would be used to display the hints
//       for every single element in the file at once. (I suspect, though, that the hints collector is only used to
//       calculate hints for the _visible_ portion of the screen...
@Service(Service.Level.PROJECT)
class MethodCountingService(val project: Project) {

    private data class FileDetails(
        val fileKey: String,
        val methodsByMethodKey: Map<String,MethodDetails> = emptyMap(),
    )

    private data class MethodDetails(
        val methodKey: String,
        val name: String,
        var numberInFile: Int,
    )

    private val fileDetailsByFileKey = mutableMapOf<String,FileDetails>()

    fun refreshCacheForFile(file: PsiFile) {
        refreshCacheForFileAndGet(file)
    }

    fun getMethodNumber(method: PsiMethod): Int? {
        val fileDetails = fileDetailsByFileKey.getOrPut(method.containingFile.getUniqueFileKey()) { refreshCacheForFileAndGet(method.containingFile) }
        val methodDetails = fileDetails.methodsByMethodKey[method.getUniqueMethodKey()]

        return methodDetails
            ?.numberInFile
    }

    private fun refreshCacheForFileAndGet(file: PsiFile): FileDetails {
        val allMethods = getMethods(file).mapIndexed { idx, psiMethod ->
            MethodDetails(
                psiMethod.getUniqueMethodKey(),
                psiMethod.name,
                idx+1,
            )
        }

        val methodsByMethodKey = allMethods.associateBy { it.methodKey }
        val fileDetails = FileDetails(file.getUniqueFileKey(), methodsByMethodKey)
        fileDetailsByFileKey[file.getUniqueFileKey()] = fileDetails
        return fileDetails
    }

    private fun getMethods(file: PsiFile): List<PsiMethod> {
        val allPsiMethods = mutableListOf<PsiMethod>()

        file.childrenOfType<PsiClass>().forEach {
            allPsiMethods.addAll(getMethods(it))
        }

        return allPsiMethods
    }

    private fun getMethods(clazz: PsiClass) : List<PsiMethod> {
        val allMethodDetails = mutableListOf<PsiMethod>()

        clazz.children.forEach { child ->
            when (child) {
                is PsiClass -> allMethodDetails.addAll(getMethods(child))
                is PsiMethod -> allMethodDetails.add(child)
            }
        }

        return allMethodDetails
    }

    private fun PsiFile.getUniqueFileKey() = virtualFile.path

    // TODO: Replace `parameters` and `type` with something more stable
    private fun PsiMethod.getUniqueMethodKey(): String =
        "${containingClass?.qualifiedName ?: ""}#$name(${parameters.map { it.type }.joinToString(",")})"
}
