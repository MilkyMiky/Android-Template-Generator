package ui.project

import repository.FileCreator

class ProjectPresenter(private val view: ProjectView, private val fileCreator: FileCreator) {
    fun onOkClick(packageName: String) {
//        fileCreator.createBuildSrc()
        fileCreator.createRepository("$packageName.repository")
//        fileCreator.createUtilsModule("$packageName.utils")
        fileCreator.prepareAppModule(packageName)
        view.close()
    }

}