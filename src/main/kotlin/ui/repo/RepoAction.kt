package ui.repo

import com.intellij.openapi.actionSystem.*

class RepoAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project!= null) RepoDialog(e.project!!).show()
    }
}