package ui.utils

import com.intellij.openapi.actionSystem.*

class UtilsAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project!= null) UtilsDialog(e.project!!).show()
    }
}