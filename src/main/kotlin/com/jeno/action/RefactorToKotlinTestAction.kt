package com.jeno.action

import com.intellij.codeInsight.navigation.NavigationUtil
import com.intellij.execution.junit.JUnitUtil
import com.intellij.lang.ContextAwareActionHandler
import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtil
import com.intellij.refactoring.RefactoringActionHandler
import com.intellij.refactoring.actions.BasePlatformRefactoringAction
import org.jetbrains.kotlin.idea.actions.JavaToKotlinAction
import java.util.Arrays

class ToKotlinTestAction: BasePlatformRefactoringAction() {

	override fun isAvailableInEditorOnly(): Boolean {
		return false
	}

	override fun getRefactoringHandler(provider: RefactoringSupportProvider): RefactoringActionHandler? {
		return ToKotlinTestHandler()
	}

}

class ToKotlinTestHandler: RefactoringActionHandler, ContextAwareActionHandler {

	val title = "Convert Java to Kotlin"

	override fun isAvailableForQuickList(editor: Editor, file: PsiFile, dataContext: DataContext): Boolean {
		return !PsiUtil.isModuleFile(file) && Arrays.stream((file as PsiJavaFile).classes).anyMatch(JUnitUtil::isTestClass)
	}

	override fun invoke(project: Project, editor: Editor?, file: PsiFile?, dataContext: DataContext?) {
		val javaFile = file as PsiJavaFile
		val error = PsiTreeUtil.findChildOfType(javaFile, PsiErrorElement::class.java)
		if (error != null) {
			val okText = "Investigate Errors"
			val cancelText = "Proceed with Conversion"
			if (Messages.showOkCancelDialog(
							project,
							"The file contains syntax errors, the conversion result may be incorrect",
							title,
							okText,
							cancelText,
							Messages.getWarningIcon()
					) == Messages.OK) {
				NavigationUtil.activateFileWithPsiElement(error.navigationElement)
				return
			}
		}
		JavaToKotlinAction.convertFiles(listOf(javaFile), project)
	}

	override fun invoke(project: Project, elements: Array<out PsiElement>, dataContext: DataContext?) {
	}

}