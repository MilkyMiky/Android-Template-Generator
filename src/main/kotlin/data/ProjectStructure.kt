package data

import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager

class ProjectStructure(private val project: Project) {

    fun getSourceRoots() =
        ProjectRootManager.getInstance(project).contentSourceRoots.map { SourceRoot(project, it) }

    fun getAllModules() = ModuleManager.getInstance(project).modules.map { it.name }

    fun getProjectName() = project.name

    fun getProjectPath() = project.basePath ?: ""
}