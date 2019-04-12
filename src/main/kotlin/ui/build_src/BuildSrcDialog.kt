package ui.build_src

import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent
import com.intellij.openapi.project.Project
import data.ProjectStructure
import repository.FileCreatorImpl
import repository.SourceRootRepositoryImpl

class BuildSrcDialog(project: Project) : DialogWrapper(true), BuildSrcView {

    private val presenter : BuildSrcPresenter

    private val panel = BuildSrcPanel()

    init {
        title = "Architecture template"
        val sourceRootRepository = SourceRootRepositoryImpl(ProjectStructure(project))
        val fileCreator = FileCreatorImpl(project, sourceRootRepository)

        presenter = BuildSrcPresenter(this, fileCreator)
        init()
    }

    override fun createCenterPanel(): JComponent? {
        return panel
    }

    override fun close() {
        close(DialogWrapper.OK_EXIT_CODE)
    }

    override fun doOKAction() {
        presenter.onOkClick()
    }
}