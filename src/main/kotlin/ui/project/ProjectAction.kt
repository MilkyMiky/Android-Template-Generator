package ui.project

import com.intellij.openapi.actionSystem.*

class ProjectAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project!= null) ProjectDialog(e.project!!).show()
    }
}