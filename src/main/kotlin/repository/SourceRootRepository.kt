package repository

import data.ProjectStructure
import data.SourceRoot

interface SourceRootRepository {

    fun findAppModuleCodeSourceRoot(): SourceRoot
    fun findAppModuleResSourceRoot(): SourceRoot
//    fun findCodeSourceRoots(): List<SourceRoot>
}

class SourceRootRepositoryImpl(private val projectStructure: ProjectStructure) : SourceRootRepository {

    override fun findAppModuleCodeSourceRoot(): SourceRoot {
        var root: SourceRoot? = null
        for (r in findCodeSourceRoots())
            if (r.path.contains("app", false))
                root = r
        return root!!
    }

    override fun findAppModuleResSourceRoot(): SourceRoot {
        var root: SourceRoot? = null
        for (r in findResourcesSourceRoots())
            if (r.path.contains("app", false)) root = r
        return root!!
    }

    private fun findCodeSourceRoots(): List<SourceRoot> {
        val roots = arrayListOf<SourceRoot>()
        for (root in projectStructure.getSourceRoots()) {
            val path = root.path
            if (path.contains("src", true)
                && path.contains("main", true)
                && !path.contains("assets", true)
                && !path.contains("test", false)
                && !path.contains("res", true)
            )
                roots.add(root)
        }
        return roots
    }

    private fun findResourcesSourceRoots(): List<SourceRoot> {
        val roots = arrayListOf<SourceRoot>()
        for (root in projectStructure.getSourceRoots()) {
            val path = root.path
            if ( path.contains("src", true)
                && path.contains("main", true)
                && path.contains("res", true)
            )
                roots.add(root)
        }
        return roots
    }


    private fun String.removeModulePathPrefix(module: String) =
        removePrefix(projectStructure.getProjectPath() + "/" + "com/example/testpluginapp")
}