package ui.mvi

import repository.FileCreator

class MVIPresenter(
        private val view: MVIView,
        private val fileCreator: FileCreator) {

    fun onOkClick(fileName: String, filePackage: String) {
        fileCreator.createModule(fileName, filePackage)
        view.close()
    }

}