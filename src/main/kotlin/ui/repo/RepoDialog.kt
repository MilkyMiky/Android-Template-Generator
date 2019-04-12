package ui.repo

import com.intellij.openapi.ui.DialogWrapper
import repository.FileCreatorImpl
import javax.swing.JComponent
import com.intellij.openapi.project.Project
import data.ProjectStructure
import repository.SourceRootRepositoryImpl

class RepoDialog(project: Project) : DialogWrapper(true), RepoView {

    private val presenter : RepoPresenter

    private val panel = RepoPanel()

    init {
        title = "Architecture template"
        val sourceRootRepository = SourceRootRepositoryImpl(ProjectStructure(project))
        val fileCreator = FileCreatorImpl(project, sourceRootRepository)

        presenter = RepoPresenter(this, fileCreator)
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