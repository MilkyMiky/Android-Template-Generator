package ui.build_src

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class BuildSrcAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project != null) BuildSrcDialog(e.project!!).show()
    }
}