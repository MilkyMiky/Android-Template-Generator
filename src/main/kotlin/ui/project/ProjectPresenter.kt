package ui.project

import repository.FileCreator

class ProjectPresenter(private val view: ProjectView, private val fileCreator: FileCreator) {
    fun onOkClick(projectName: String, packageName: String) {
        fileCreator.createRepository(projectName, "$packageName.repository")
        fileCreator.prepareAppModule(projectName, packageName)
        view.close()
    }
}