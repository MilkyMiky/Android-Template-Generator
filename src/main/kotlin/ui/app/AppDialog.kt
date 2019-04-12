package ui.app

import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent
import com.intellij.openapi.project.Project
import data.ProjectStructure
import repository.FileCreatorImpl
import repository.SourceRootRepositoryImpl

class AppDialog(project: Project) : DialogWrapper(true), AppView {

    private val presenter : AppPresenter

    private val panel = AppPanel()

    init {
        title = "Architecture template"
        val sourceRootRepository = SourceRootRepositoryImpl(ProjectStructure(project))
        val fileCreator = FileCreatorImpl(project, sourceRootRepository)

        presenter = AppPresenter(this, fileCreator)
        init()
    }

    override fun createCenterPanel(): JComponent? {
        return panel
    }

    override fun close() {
        close(DialogWrapper.OK_EXIT_CODE)
    }

    override fun doOKAction() {
        presenter.onOkClick(panel.packageTextField.text)
    }
}