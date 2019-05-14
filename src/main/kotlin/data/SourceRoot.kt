package data

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class SourceRoot(project: Project, val virtualFile: VirtualFile) {
    val path = virtualFile.path
}
