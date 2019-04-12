package ui.mvi


import com.intellij.openapi.actionSystem.*

class MVIAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project != null) MVIDialog(e.project!!).show()
    }
}