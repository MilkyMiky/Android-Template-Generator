package ui.utils

import com.intellij.openapi.ui.DialogWrapper
import repository.FileCreatorImpl
import javax.swing.JComponent
import com.intellij.openapi.project.Project
import data.ProjectStructure
import repository.SourceRootRepositoryImpl

class UtilsDialog(project: Project) : DialogWrapper(true), UtilsView {

    private val presenter : UtilsPresenter

    private val panel = UtilsPanel()

    init {
        title = "Architecture template"
        val sourceRootRepository = SourceRootRepositoryImpl(ProjectStructure(project))
        val fileCreator = FileCreatorImpl(project, sourceRootRepository)

        presenter = UtilsPresenter(this, fileCreator)
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