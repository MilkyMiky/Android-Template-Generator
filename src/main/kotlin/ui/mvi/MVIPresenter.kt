package ui.mvi

import repository.FileCreator

class MVIPresenter(
    private val view: MVIView,
    private val fileCreator: FileCreator
) {

    fun onOkClick(fileName: String, moduleName: String, packageName: String) {
        fileCreator.createMVIModule(fileName, moduleName, packageName)
        view.close()
    }

}