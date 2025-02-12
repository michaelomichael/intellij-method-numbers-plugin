package dev.michaelomichael.intellij.plugins.methodnumbers.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import dev.michaelomichael.intellij.plugins.methodnumbers.introspector.IntrospectorFactory.introspectorFor
import dev.michaelomichael.intellij.plugins.methodnumbers.introspector.MethodKey

@Service(Service.Level.PROJECT)
class MethodCountingService(val project: Project) {
    
    data class MethodIndexDetails(val methodIndexInFile: Int, val numMethodsInFile: Int)
    
    private data class FileDetails(
        val fileKey: String,
        val methodsByMethodKey: Map<MethodKey,MethodDetails> = emptyMap(),
    )

    private data class MethodDetails(
        val methodKey: MethodKey,
        var indexDetails: MethodIndexDetails,
    )

    private val fileDetailsByFileKey = mutableMapOf<String,FileDetails>()

    fun getMethodNumber(method: PsiElement): MethodIndexDetails? {
        val fileKey = method.containingFile.fileKey
        val methodKey = method.methodKey
        
        // If not we don't find the exact method in-cache then we'll reload the cache
        // and try again.
        val methodDetails = fileDetailsByFileKey[fileKey]?.methodsByMethodKey?.get(methodKey)
            ?: refreshCacheForFile(method.containingFile)
                    .methodsByMethodKey[methodKey]
        
        return methodDetails?.indexDetails
    }

    private fun refreshCacheForFile(file: PsiFile): FileDetails =
        introspectorFor(file)
            .findAllMethods(file)
            .withTotalCount { methods, count ->
                methods.mapIndexed { idx, methodRef -> MethodDetails(methodRef, MethodIndexDetails(idx, count)) }
            }
            .associateBy { it.methodKey }
            .let { FileDetails(file.fileKey, it) }
            .also {
                fileDetailsByFileKey[it.fileKey] = it
            }

    private val PsiFile.fileKey: String
        get() = virtualFile.path

    private val PsiElement.methodKey: MethodKey 
        get() = introspectorFor(containingFile).toMethodKey(this)
    
    private fun <E,F> List<E>.withTotalCount(body: (List<E>, Int) -> F): F =
        body.invoke(this, this.size)
}

