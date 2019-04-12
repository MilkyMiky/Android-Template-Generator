package ui.mvi

import com.intellij.openapi.ui.DialogWrapper
import repository.FileCreatorImpl
import javax.swing.JComponent
import com.intellij.openapi.project.Project
import data.ProjectStructure
import repository.SourceRootRepositoryImpl

class MVIDialog(project: Project) : DialogWrapper(true), MVIView {

    private val presenter : MVIPresenter

    private val panel = MVIPanel()

    init {
        title = "Architecture template"
        val sourceRootRepository = SourceRootRepositoryImpl(ProjectStructure(project))
        val fileCreator = FileCreatorImpl(project, sourceRootRepository)

        presenter = MVIPresenter(this, fileCreator)
        init()
    }

    override fun createCenterPanel(): JComponent? {
        return panel
    }

    override fun close() {
        close(DialogWrapper.OK_EXIT_CODE)
    }

    override fun doOKAction() {
        presenter.onOkClick(
                panel.nameTextField.text,
                panel.packageTextField.text
        )
    }
}