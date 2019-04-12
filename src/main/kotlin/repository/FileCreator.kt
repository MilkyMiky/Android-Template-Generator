package repository

import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiFileFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.intellij.util.IncorrectOperationException
import data.FileType
import org.jetbrains.kotlin.idea.KotlinLanguage
import com.intellij.psi.PsiDocumentManager


interface FileCreator {

    fun createMVIModule(fileName: String, moduleName: String, packageName: String)

    fun createBuildSrc()

    fun createRepository(packageName: String)

    fun prepareAppModule(packageName: String)

    fun createUtilsModule(packageName: String)
}

class FileCreatorImpl(
    private val project: Project,
    private val sourceRootRepository: SourceRootRepository
) : FileCreator {

    override fun prepareAppModule(packageName: String) {
        ApplicationManager.getApplication().runWriteAction {

            val projectDirectory = project.projectFile!!.parent.parent
            val appVF = projectDirectory.findChild("app")!!

            val appPsiDir = PsiManager.getInstance(project).findDirectory(appVF)!!
            appPsiDir.findFile("build.gradle")!!.delete()
            createBuildGradle(FileType.AppBuildGradle(packageName), appPsiDir)

            var dirVF = sourceRootRepository.findAppModuleCodeSourceRoot().virtualFile
            for (pack in packageName.split(".").toTypedArray()) dirVF = dirVF.findChild(pack)!!

            val applicationPsiDir = createPackageDirectory("application", dirVF)
            val diPsiDir = createPackageDirectory("di", dirVF)
            createKotlinFile(FileType.MainModule(packageName), diPsiDir)
            val uiPsiDir = createPackageDirectory("ui", dirVF)
            val navPsiDirectory = createPackageDirectory("navigation", dirVF)
            createKotlinFile(FileType.Navigator(packageName), navPsiDirectory)
        }
    }

    override fun createUtilsModule(packageName: String) {
        ApplicationManager.getApplication().runWriteAction {

            val projectDirectory = project.projectFile!!.parent.parent

            val psiFile =
                PsiManager.getInstance(project).findDirectory(projectDirectory)!!.findFile("settings.gradle")!!
            val doc = PsiDocumentManager.getInstance(project).getDocument(psiFile)!!

            val newDocumentContent =
                if (doc.text.contains("\n")) doc.text.replace("\n", ", ':utils'\n")
                else doc.text + ", ':utils'\n"

            PsiManager.getInstance(project).findDirectory(projectDirectory)!!.findFile("settings.gradle")!!.delete()
            val file = PsiFileFactory.getInstance(project).createFileFromText("settings.gradle", newDocumentContent)
            PsiManager.getInstance(project).findDirectory(projectDirectory)!!.add(file)

            val utilsDir = createPackageDirectory("utils", projectDirectory)
            val utilsVF = projectDirectory.findChild("utils")!!

            createPackageDirectory("src", utilsVF)
            val srcVF = utilsVF.findChild("src")!!

            val mainDir = createPackageDirectory("main", srcVF)
            val mainVF = srcVF.findChild("main")!!

            createPackageDirectory("java", mainVF)
            val javaVF = mainVF.findChild("java")

            createPackageDirectory("res", mainVF)

            createXMLFile(FileType.Manifest(packageName), mainDir)
            createBuildGradle(FileType.UtilsBuildGradle(), utilsDir)
            createProguard(utilsDir)

            var dirVF = javaVF!!
            for (pack in packageName.split(".").toTypedArray()) {
                createPackageDirectory(pack, dirVF)
                dirVF = dirVF.findChild(pack)!!
            }
            val diPsiDirectory = createPackageDirectory("di", dirVF)
            createKotlinFile(FileType.UtilsModule(packageName), diPsiDirectory)

            val commonPsiDirectory = createPackageDirectory("common", dirVF)
            val commonVF = dirVF.findChild("common")!!


            createKotlinFile(FileType.BaseComponents(packageName), commonPsiDirectory)
            createKotlinFile(FileType.Common(packageName), commonPsiDirectory)
            createKotlinFile(FileType.ImageLoader(packageName), commonPsiDirectory)
            createKotlinFile(FileType.GlideImageLoader(packageName), commonPsiDirectory)
            createKotlinFile(FileType.ImageBindingAdapter(packageName), commonPsiDirectory)

//            createPackageDirectory("anim", sourceRootRepository.findResourcesSourceRoot().virtualFile)
//            createPackageDirectory("drawable", sourceRootRepository.findResourcesSourceRoot().virtualFile)
//            createPackageDirectory("font", sourceRootRepository.findResourcesSourceRoot().virtualFile)
//            createPackageDirectory("menu", sourceRootRepository.findResourcesSourceRoot().virtualFile)
//            createPackageDirectory("values", sourceRootRepository.findResourcesSourceRoot().virtualFile)
        }
    }

    override fun createRepository(packageName: String) {
        ApplicationManager.getApplication().runWriteAction {
            val projectDirectory = project.projectFile!!.parent.parent

            val psiFile =
                PsiManager.getInstance(project).findDirectory(projectDirectory)!!.findFile("settings.gradle")!!
            val doc = PsiDocumentManager.getInstance(project).getDocument(psiFile)!!

            val newDocumentContent =
                if (doc.text.contains("\n")) doc.text.replace("\n", ", ':repository'\n")
                else doc.text + ", ':repository'\n"

            PsiManager.getInstance(project).findDirectory(projectDirectory)!!.findFile("settings.gradle")!!.delete()
            val file = PsiFileFactory.getInstance(project).createFileFromText("settings.gradle", newDocumentContent)
            PsiManager.getInstance(project).findDirectory(projectDirectory)!!.add(file)

            val repositoryDir = createPackageDirectory("repository", projectDirectory)
            val repositoryVF = projectDirectory.findChild("repository")!!

            createPackageDirectory("src", repositoryVF)
            val srcVF = repositoryVF.findChild("src")!!

            val mainDir = createPackageDirectory("main", srcVF)
            val mainVF = srcVF.findChild("main")!!

            createPackageDirectory("java", mainVF)
            val javaVF = mainVF.findChild("java")

            createPackageDirectory("res", mainVF)

            createXMLFile(FileType.Manifest(packageName), mainDir)
            createBuildGradle(FileType.RepoBuildGradle(), repositoryDir)
            createProguard(repositoryDir)

            var dirVF = javaVF!!
            for (pack in packageName.split(".").toTypedArray()) {
                createPackageDirectory(pack, dirVF)
                dirVF = dirVF.findChild(pack)!!
            }

            createPackageDirectory("data", dirVF)
            val dataVF = dirVF.findChild("data")!!
            createPackageDirectory("local", dataVF)
            createPackageDirectory("remote", dataVF)
            val repoImplPsiDirectory = createPackageDirectory("repositoryImpl", dataVF)
            createKotlinFile(FileType.EntitiesRepositoryImpl(packageName), repoImplPsiDirectory)

            createPackageDirectory("serviceImpl", dataVF)

            val localVF = dataVF.findChild("local")!!

            val dataSourcePsiDirectory = createPackageDirectory("datasource", localVF)
            createKotlinFile(FileType.EntityLocalSource(packageName), dataSourcePsiDirectory)

            val dbPsiDirectory = createPackageDirectory("db", localVF)
            createKotlinFile(FileType.DbProvider(project.name, packageName), dbPsiDirectory)
            createKotlinFile(FileType.Database(project.name, packageName), dbPsiDirectory)
            createKotlinFile(FileType.DbTypeConverters(packageName), dbPsiDirectory)
            createKotlinFile(FileType.EntityDao(packageName), dbPsiDirectory)

            val entityLPsiDirectory = createPackageDirectory("entity", localVF)
            createKotlinFile(FileType.EntityL(packageName), entityLPsiDirectory)

            val remoteVF = dataVF.findChild("remote")!!

            val datasourceRPsiDirectory = createPackageDirectory("datasource", remoteVF)
            createKotlinFile(FileType.EntityRemoteSource(project.name, packageName), datasourceRPsiDirectory)

            val apiPsiDirectory = createPackageDirectory("api", remoteVF)
            createKotlinFile(FileType.Api(project.name, packageName), apiPsiDirectory)
            createKotlinFile(FileType.ApiProvider(project.name, packageName), apiPsiDirectory)

            val entityRPsiDirectory = createPackageDirectory("entity", remoteVF)
            createKotlinFile(FileType.EntityR(packageName), entityRPsiDirectory)


            createPackageDirectory("domain", dirVF)
            val domainVF = dirVF.findChild("domain")!!
            val entityPsiDirectory = createPackageDirectory("entity", domainVF)
            createKotlinFile(FileType.Entity(packageName), entityPsiDirectory)

            val dataSourceBasePsiDirectory = createPackageDirectory("datasource", domainVF)
            createKotlinFile(FileType.EntityDataSource(packageName), dataSourceBasePsiDirectory)

            val interactorsPsiDirectory = createPackageDirectory("interactors", domainVF)
            createKotlinFile(FileType.EntityUseCase(packageName), interactorsPsiDirectory)

            val repoPsiDirectory = createPackageDirectory("repository", domainVF)
            createKotlinFile(FileType.EntitiesRepository(packageName), repoPsiDirectory)

            createPackageDirectory("service", domainVF)


            val diPsiDirectory = createPackageDirectory("di", dirVF)
            createKotlinFile(FileType.RepoModule(project.name, packageName), diPsiDirectory)
        }
    }

    override fun createBuildSrc() {
        ApplicationManager.getApplication().runWriteAction {
            val projectDirectory = project.projectFile!!.parent.parent
            val buildSrcDir = createPackageDirectory("buildSrc", projectDirectory)
            createKotlinFile(FileType.BuildSrc(), buildSrcDir)

            val buildSrc = projectDirectory.findChild("buildSrc")
            createPackageDirectory("src", buildSrc!!)

            val src = buildSrc.findChild("src")
            createPackageDirectory("main", src!!)

            val main = src.findChild("main")
            val javaDir = createPackageDirectory("java", main!!)

            createKotlinFile(FileType.Deps(), javaDir)
        }
    }

    override fun createMVIModule(fileName: String, moduleName: String, packageName: String) {
        ApplicationManager.getApplication().runWriteAction {
            val sourceDirVF = sourceRootRepository.findAppModuleCodeSourceRoot().virtualFile
            val resDirVF = sourceRootRepository.findAppModuleResSourceRoot().virtualFile
            val layoutDir = PsiManager.getInstance(project).findDirectory(resDirVF.findChild("layout")!!)!!
            var dirVF = sourceDirVF
            for (pack in packageName.split(".").toTypedArray()) {
                dirVF = dirVF.findChild(pack)!!
            }

            val uiVF = dirVF.findChild("ui")!!
            val modulePsiDirectory = createPackageDirectory(moduleName, uiVF)
            createMVIFiles(fileName, "$packageName.ui.$moduleName", packageName, modulePsiDirectory)
            createXMLFile(FileType.Layout(fileName, "$packageName.ui.$moduleName"), layoutDir)
        }
    }

    private fun findPackageDir(sourceDir: VirtualFile): VirtualFile {
        var packageDir = sourceDir
        while (packageDir.children.isNotEmpty()) {
            if (packageDir.children.first().isDirectory) packageDir = packageDir.children.first()
            else break
        }
        return packageDir
    }

    private fun createPackageDirectory(packageName: String, packageDir: VirtualFile): PsiDirectory =
        PsiManager.getInstance(project).findDirectory(packageDir)!!.createSubdirectory(packageName)

    private fun createMVIFiles(
        fileName: String,
        createdPackagePath: String,
        userPackagePath: String,
        directory: PsiDirectory
    ) {
        createKotlinFile(FileType.ViewModel(fileName, createdPackagePath, userPackagePath), directory)
        createKotlinFile(FileType.State(fileName, createdPackagePath), directory)
        createKotlinFile(FileType.Fragment(fileName, createdPackagePath, userPackagePath), directory)
    }

    private fun createKotlinFile(fileType: FileType, directory: PsiDirectory) {
        try {
            val psiFile = PsiFileFactory.getInstance(project)
                .createFileFromText(fileType.fileName, KotlinLanguage.INSTANCE, fileType.content)
            directory.add(psiFile)
        } catch (e: IncorrectOperationException) {
        }
    }

    private fun createProguard(directory: PsiDirectory) {
        try {
            val proguard = FileType.Proguard()
            val psiFile = PsiFileFactory.getInstance(project).createFileFromText(proguard.fileName, proguard.content)
            directory.add(psiFile)
        } catch (e: IncorrectOperationException) {
        }
    }

    private fun createBuildGradle(fileType: FileType, directory: PsiDirectory) {
        try {
            val psiFile = PsiFileFactory.getInstance(project).createFileFromText(fileType.fileName, fileType.content)
            directory.add(psiFile)
        } catch (e: IncorrectOperationException) {
            e.localizedMessage
            e.printStackTrace()
        }
    }

    private fun createXMLFile(fileType: FileType, directory: PsiDirectory) {
        try {
            val psiFile = PsiFileFactory.getInstance(project)
                .createFileFromText(fileType.fileName, XMLLanguage.INSTANCE, fileType.content)
            directory.add(psiFile)
        } catch (e: IncorrectOperationException) {
        }
    }
}