package ui.build_src

import repository.FileCreator

class BuildSrcPresenter(private val view: BuildSrcView, private val fileCreator: FileCreator) {
    fun onOkClick() {
        fileCreator.createBuildSrc()
        view.close()
    }

}