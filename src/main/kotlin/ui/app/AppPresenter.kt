package ui.app

import repository.FileCreator

class AppPresenter(private val view: AppView, private val fileCreator: FileCreator) {
    fun onOkClick(packageName : String) {
        fileCreator.prepareAppModule(packageName)
        view.close()
    }

}