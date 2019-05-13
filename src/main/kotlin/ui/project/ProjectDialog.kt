package ui.project

import com.intellij.openapi.ui.DialogWrapper
import repository.FileCreatorImpl
import javax.swing.JComponent
import com.intellij.openapi.project.Project
import data.ProjectStructure
import repository.SourceRootRepositoryImpl

class ProjectDialog(project: Project) : DialogWrapper(true), ProjectView {

    private val presenter : ProjectPresenter

    private val panel = ProjectPanel()

    init {
        title = "Architecture template"
        val sourceRootRepository = SourceRootRepositoryImpl(ProjectStructure(project))
        val fileCreator = FileCreatorImpl(project, sourceRootRepository)

        presenter = ProjectPresenter(this, fileCreator)
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