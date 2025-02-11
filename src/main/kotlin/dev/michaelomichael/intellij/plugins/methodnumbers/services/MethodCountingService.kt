package dev.michaelomichael.intellij.plugins.methodnumbers.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import dev.michaelomichael.intellij.plugins.methodnumbers.services.introspector.IntrospectorFactory.methodFinderFor
import dev.michaelomichael.intellij.plugins.methodnumbers.services.introspector.MethodRef

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
        val methodsByMethodRef: Map<MethodRef,MethodRefWithIndex> = emptyMap(),
    )

    private data class MethodRefWithIndex(
        val methodRef: MethodRef,
        var numberInFile: Int,
    )

    private val fileDetailsByFileKey = mutableMapOf<String,FileDetails>()

    fun refreshCacheForFile(file: PsiFile) {
        refreshCacheForFileAndGet(file)
    }

    fun getMethodNumber(method: PsiElement): Int? =
        fileDetailsByFileKey
            .getOrPut(method.containingFile.getUniqueFileKey()) { refreshCacheForFileAndGet(method.containingFile) }
            .methodsByMethodRef[method.getMethodRef()]
            ?.numberInFile

    private fun refreshCacheForFileAndGet(file: PsiFile): FileDetails =
        methodFinderFor(file)
            .findAllMethods(file)
            .mapIndexed { idx, methodRef -> MethodRefWithIndex(methodRef, idx + 1) }
            .associateBy { it.methodRef }
            .let { FileDetails(file.getUniqueFileKey(), it) }
            .also {
                fileDetailsByFileKey[it.fileKey] = it
            }

    private fun PsiFile.getUniqueFileKey() = virtualFile.path

    private fun PsiElement.getMethodRef(): MethodRef =
        methodFinderFor(containingFile).toMethodRef(this)
}
