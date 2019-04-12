package repository

import data.ProjectStructure
import data.SourceRoot

interface SourceRootRepository {

    fun findCodeSourceRoot(): SourceRoot
    fun findResourcesSourceRoot(): SourceRoot
}

class SourceRootRepositoryImpl(private val projectStructure: ProjectStructure) : SourceRootRepository {

    override fun findCodeSourceRoot() =
        projectStructure.getSourceRoots().first {
             val path = it.path
            //TODO(Remove user package)
            path.contains("src", true)
                    && path.contains("main", true)
                    && !path.contains("assets", true)
                    && !path.contains("test", false)
                    && !path.contains("res", true)
    }

    override fun findResourcesSourceRoot() =
            projectStructure.getSourceRoots().first {
                //TODO(Remove user package)
                val path = it.path
                path.contains("src", true)
                        && path.contains("main", true)
                        && path.contains("res", true)
            }

    private fun String.removeModulePathPrefix(module: String) =
            removePrefix(projectStructure.getProjectPath() + "/" + "com/example/testpluginapp")
}