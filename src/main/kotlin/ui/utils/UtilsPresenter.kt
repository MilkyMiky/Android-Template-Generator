package ui.utils

import repository.FileCreator

class UtilsPresenter(private val view: UtilsView, private val fileCreator: FileCreator) {
    fun onOkClick(packageName: String) {
        fileCreator.createUtilsModule("$packageName.utils")
        view.close()
    }

}