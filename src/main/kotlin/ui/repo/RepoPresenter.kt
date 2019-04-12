package ui.repo

import repository.FileCreator

class RepoPresenter(private val view: RepoView, private val fileCreator: FileCreator) {
    fun onOkClick(packageName : String) {
        fileCreator.createRepository("$packageName.repository")
        view.close()
    }

}