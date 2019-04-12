package ui.app

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class AppAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project != null) AppDialog(e.project!!).show()
    }
}