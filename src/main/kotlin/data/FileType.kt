package data

sealed class FileType(val fileName: String, val content: String) {


    class App(projectName: String, packageName: String)  :FileType(
        "${projectName}App.kt",
        "package $packageName.application\n" +
                "\n" +
                "import android.app.Application\n" +
                "import androidx.databinding.DataBindingUtil\n" +
                "import $packageName.di.BindingComponent\n" +
                "import $packageName.di.mainModule\n" +
                "import $packageName.repository.di.repoModule\n" +
                "import org.koin.android.ext.android.startKoin\n" +
                "\n" +
                "" +
                "class ${projectName}App : Application() {\n" +
                "\n" +
                "  override fun onCreate() {\n" +
                "    super.onCreate()\n" +
                "    startKoin(this, listOf(mainModule, repoModule))\n" +
                "    DataBindingUtil.setDefaultComponent(BindingComponent())\n" +
                "  }\n" +
                "}"
    )

    class NavigationExtensions(packageName: String) : FileType(
        "NavigationExtensions.kt",
        "package $packageName.common\n" +
                "\n" +
                "import android.content.Intent\n" +
                "import android.util.SparseArray\n" +
                "import androidx.core.util.forEach\n" +
                "import androidx.core.util.set\n" +
                "import androidx.fragment.app.FragmentManager\n" +
                "import androidx.lifecycle.LiveData\n" +
                "import androidx.lifecycle.MutableLiveData\n" +
                "import androidx.navigation.NavController\n" +
                "import androidx.navigation.fragment.NavHostFragment\n" +
                "import com.google.android.material.bottomnavigation.BottomNavigationView\n" +
                "import $packageName.R\n" +
                "\n" +
                "\n" +
                "fun BottomNavigationView.setupWithNavController(\n" +
                "    navGraphIds: List<Int>,\n" +
                "    fragmentManager: FragmentManager,\n" +
                "    containerId: Int,\n" +
                "    intent: Intent\n" +
                "): LiveData<NavController> {\n" +
                "\n" +
                "    // Map of tags\n" +
                "    val graphIdToTagMap = SparseArray<String>()\n" +
                "    // Result. Mutable live data with the selected controlled\n" +
                "    val selectedNavController = MutableLiveData<NavController>()\n" +
                "\n" +
                "    var firstFragmentGraphId = 0\n" +
                "\n" +
                "    // First create a NavHostFragment for each NavGraph ID\n" +
                "    navGraphIds.forEachIndexed { index, navGraphId ->\n" +
                "        val fragmentTag = getFragmentTag(index)\n" +
                "\n" +
                "        // Find or create the Navigation host fragment\n" +
                "        val navHostFragment = obtainNavHostFragment(\n" +
                "            fragmentManager,\n" +
                "            fragmentTag,\n" +
                "            navGraphId,\n" +
                "            containerId\n" +
                "        )\n" +
                "\n" +
                "        // Obtain its id\n" +
                "        val graphId = navHostFragment.navController.graph.id\n" +
                "\n" +
                "        if (index == 0) {\n" +
                "            firstFragmentGraphId = graphId\n" +
                "        }\n" +
                "\n" +
                "        // Save to the map\n" +
                "        graphIdToTagMap[graphId] = fragmentTag\n" +
                "\n" +
                "        // Attach or detach nav host fragment depending on whether it's the selected item.\n" +
                "        if (this.selectedItemId == graphId) {\n" +
                "            // Update livedata with the selected graph\n" +
                "            selectedNavController.value = navHostFragment.navController\n" +
                "            attachNavHostFragment(fragmentManager, navHostFragment, index == 0)\n" +
                "        } else {\n" +
                "            detachNavHostFragment(fragmentManager, navHostFragment)\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    // Now connect selecting an item with swapping Fragments\n" +
                "    var selectedItemTag = graphIdToTagMap[this.selectedItemId]\n" +
                "    val firstFragmentTag = graphIdToTagMap[firstFragmentGraphId]\n" +
                "    var isOnFirstFragment = selectedItemTag == firstFragmentTag\n" +
                "\n" +
                "    // When a navigation item is selected\n" +
                "    setOnNavigationItemSelectedListener { item ->\n" +
                "        // Don't do anything if the state is state has already been saved.\n" +
                "        if (fragmentManager.isStateSaved) {\n" +
                "            false\n" +
                "        } else {\n" +
                "            val newlySelectedItemTag = graphIdToTagMap[item.itemId]\n" +
                "            if (selectedItemTag != newlySelectedItemTag) {\n" +
                "                // Pop everything above the first fragment (the \"fixed start destination\")\n" +
                "                fragmentManager.popBackStack(\n" +
                "                    firstFragmentTag,\n" +
                "                    FragmentManager.POP_BACK_STACK_INCLUSIVE\n" +
                "                )\n" +
                "                val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)\n" +
                "                        as NavHostFragment\n" +
                "\n" +
                "                // Exclude the first fragment tag because it's always in the back stack.\n" +
                "                if (firstFragmentTag != newlySelectedItemTag) {\n" +
                "                    // Commit a transaction that cleans the back stack and adds the first fragment\n" +
                "                    // to it, creating the fixed started destination.\n" +
                "                    fragmentManager.beginTransaction()\n" +
                "                        .show(selectedFragment)\n" +
                "                        .setPrimaryNavigationFragment(selectedFragment)\n" +
                "                        .apply {\n" +
                "                            // Detach all other Fragments\n" +
                "                            graphIdToTagMap.forEach { _, fragmentTagIter ->\n" +
                "                                if (fragmentTagIter != newlySelectedItemTag) {\n" +
                "                                    hide(fragmentManager.findFragmentByTag(firstFragmentTag)!!)\n" +
                "                                }\n" +
                "                            }\n" +
                "                        }\n" +
                "                        .addToBackStack(firstFragmentTag)\n" +
                "                        .setCustomAnimations(\n" +
                "                            R.anim.nav_default_enter_anim,\n" +
                "                            R.anim.nav_default_exit_anim,\n" +
                "                            R.anim.nav_default_pop_enter_anim,\n" +
                "                            R.anim.nav_default_pop_exit_anim\n" +
                "                        )\n" +
                "                        .setReorderingAllowed(true)\n" +
                "                        .commit()\n" +
                "                }\n" +
                "                selectedItemTag = newlySelectedItemTag\n" +
                "                isOnFirstFragment = selectedItemTag == firstFragmentTag\n" +
                "                selectedNavController.value = selectedFragment.navController\n" +
                "                true\n" +
                "            } else {\n" +
                "                false\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    // Optional: on item reselected, pop back stack to the destination of the graph\n" +
                " //   setupItemReselected(graphIdToTagMap, fragmentManager)\n" +
                "\n" +
                "    // Handle deep link\n" +
                " //   setupDeepLinks(navGraphIds, fragmentManager, containerId, intent)\n" +
                "\n" +
                "    // Finally, ensure that we update our BottomNavigationView when the back stack changes\n" +
                "    fragmentManager.addOnBackStackChangedListener {\n" +
                "        if (!isOnFirstFragment && !fragmentManager.isOnBackStack(firstFragmentTag)) {\n" +
                "            this.selectedItemId = firstFragmentGraphId\n" +
                "        }\n" +
                "\n" +
                "        // Reset the graph if the currentDestination is not valid (happens when the back\n" +
                "        // stack is popped after using the back button).\n" +
                "        selectedNavController.value?.let { controller ->\n" +
                "            if (controller.currentDestination == null) {\n" +
                "                controller.navigate(controller.graph.id)\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    return selectedNavController\n" +
                "}\n" +
                "\n" +
                "private fun BottomNavigationView.setupDeepLinks(\n" +
                "    navGraphIds: List<Int>,\n" +
                "    fragmentManager: FragmentManager,\n" +
                "    containerId: Int,\n" +
                "    intent: Intent\n" +
                ") {\n" +
                "    navGraphIds.forEachIndexed { index, navGraphId ->\n" +
                "        val fragmentTag = getFragmentTag(index)\n" +
                "\n" +
                "        // Find or create the Navigation host fragment\n" +
                "        val navHostFragment = obtainNavHostFragment(\n" +
                "            fragmentManager,\n" +
                "            fragmentTag,\n" +
                "            navGraphId,\n" +
                "            containerId\n" +
                "        )\n" +
                "        // Handle Intent\n" +
                "        if (navHostFragment.navController.handleDeepLink(intent)) {\n" +
                "            this.selectedItemId = navHostFragment.navController.graph.id\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "private fun BottomNavigationView.setupItemReselected(\n" +
                "    graphIdToTagMap: SparseArray<String>,\n" +
                "    fragmentManager: FragmentManager\n" +
                ") {\n" +
                "    setOnNavigationItemReselectedListener { item ->\n" +
                "        val newlySelectedItemTag = graphIdToTagMap[item.itemId]\n" +
                "        val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag)\n" +
                "                as NavHostFragment\n" +
                "        val navController = selectedFragment.navController\n" +
                "        // Pop the back stack to the start destination of the current navController graph\n" +
                "        navController.popBackStack(\n" +
                "            navController.graph.startDestination, false\n" +
                "        )\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "private fun detachNavHostFragment(\n" +
                "    fragmentManager: FragmentManager,\n" +
                "    navHostFragment: NavHostFragment\n" +
                ") {\n" +
                "    fragmentManager.beginTransaction()\n" +
                "        .hide(navHostFragment)\n" +
                "        .commitNow()\n" +
                "}\n" +
                "\n" +
                "private fun attachNavHostFragment(\n" +
                "    fragmentManager: FragmentManager,\n" +
                "    navHostFragment: NavHostFragment,\n" +
                "    isPrimaryNavFragment: Boolean\n" +
                ") {\n" +
                "    fragmentManager.beginTransaction()\n" +
                "        .show(navHostFragment)\n" +
                "        .apply {\n" +
                "            if (isPrimaryNavFragment) {\n" +
                "                setPrimaryNavigationFragment(navHostFragment)\n" +
                "            }\n" +
                "        }\n" +
                "        .commitNow()\n" +
                "\n" +
                "}\n" +
                "\n" +
                "private fun obtainNavHostFragment(\n" +
                "    fragmentManager: FragmentManager,\n" +
                "    fragmentTag: String,\n" +
                "    navGraphId: Int,\n" +
                "    containerId: Int\n" +
                "): NavHostFragment {\n" +
                "    // If the Nav Host fragment exists, return it\n" +
                "    val existingFragment = fragmentManager.findFragmentByTag(fragmentTag) as NavHostFragment?\n" +
                "    existingFragment?.let { return it }\n" +
                "\n" +
                "    // Otherwise, create it and return it.\n" +
                "    val navHostFragment = NavHostFragment.create(navGraphId)\n" +
                "    fragmentManager.beginTransaction()\n" +
                "        .add(containerId, navHostFragment, fragmentTag)\n" +
                "        .commitNow()\n" +
                "    return navHostFragment\n" +
                "}\n" +
                "\n" +
                "private fun FragmentManager.isOnBackStack(backStackName: String): Boolean {\n" +
                "    val backStackCount = backStackEntryCount\n" +
                "    for (index in 0 until backStackCount) {\n" +
                "        if (getBackStackEntryAt(index).name == backStackName) {\n" +
                "            return true\n" +
                "        }\n" +
                "    }\n" +
                "    return false\n" +
                "}\n" +
                "\n" +
                "private fun getFragmentTag(index: Int) = \"bottomNavigation#\$index\""
    )

    class MainModule(packageName: String) : FileType(
        "MainModule.kt",
        "package $packageName.di\n" +
                "\n" +
                "import $packageName.common.ImageBindingAdapter\n" +
                "import org.koin.dsl.module.module\n" +
                "import org.koin.standalone.KoinComponent\n" +
                "import org.koin.standalone.inject\n" +
                "\n" +
                "val mainModule = module {\n" +
                "\n" +
                "  single { ImageBindingAdapter(get()) }\n" +
                "}\n" +
                "\n" +
                "class BindingComponent : androidx.databinding.DataBindingComponent, KoinComponent {\n" +
                "\n" +
                "  private val imgLoader: ImageBindingAdapter by inject()\n" +
                "\n" +
                "  override fun getImageBindingAdapter(): ImageBindingAdapter = imgLoader\n" +
                "}"
    )

    class Fragment(fileName: String, packageName: String, userPackage: String) : FileType(
        "${fileName}Fragment.kt",
        "package $packageName\n" +
                "\n" +
                "import android.os.Bundle\n" +
                "import android.view.LayoutInflater\n" +
                "import android.view.View\n" +
                "import android.view.ViewGroup\n" +
                "import androidx.lifecycle.Observer\n" +
                "import $userPackage.common.BaseFragment\n" +
                "import $userPackage.common.BaseView\n" +
                "import $userPackage.R\n" +
                "import $userPackage.databinding.Fragment${fileName}Binding\n" +
                "import $packageName.${fileName}Intent.GetSampleData\n" +

                "import io.reactivex.Observable\n" +
                "import org.koin.androidx.viewmodel.ext.android.viewModel\n" +
                "\n" +
                "class ${fileName}Fragment : BaseFragment<Fragment${fileName}Binding>(), BaseView<${fileName}State> {\n" +
                "\n" +
                "  private val vm${fileName}Screen: ${fileName}ViewModel by viewModel()\n" +
                "\n" +
                "   override fun resLayoutId(): Int = R.layout.fragment_${fileName.replace(
                    Regex("([^_A-Z])([A-Z])"),
                    "$1_$2"
                ).toLowerCase()}\n" +
                "\n" +
                "  override fun onCreate(savedInstanceState: Bundle?) {\n" +
                "    super.onCreate(savedInstanceState)\n" +
                "    handleStates()\n" +
                "  }\n" +
                "\n" +
                " override fun onCreateView(\n" +
                "    inflater: LayoutInflater,\n" +
                "    container: ViewGroup?,\n" +
                "    savedInstanceState: Bundle?\n" +
                "  ): View? = super.onCreateView(inflater, container, savedInstanceState)\n" +
                "    .also {\n" +
                "      initIntents()\n" +
                "    }\n" +
                "\n" +
                "  override fun initIntents() {\n" +
                "    viewSubscriptions = Observable.merge(\n" +
                "      listOf(\n" +
                "        Observable.just(GetSampleData)\n" +
                "      )\n" +
                "    ).subscribe(vm${fileName}Screen.viewIntentsConsumer())\n" +
                "  }\n" +
                "\n" +
                "  override fun handleStates() {\n" +
                "    vm${fileName}Screen.stateReceived().observe(this, Observer { state -> render(state) })\n" +
                "  }\n" +
                "\n" +
                "  override fun render(state: ${fileName}State) {\n" +
                "    viewBinding!!.viewState = state\n" +
                "  }\n" +
                "}"
    )

    class ViewModel(fileName: String, packageName: String, userPackage: String) : FileType(
        "${fileName}ViewModel.kt",
        "package $packageName\n" +
                "\n" +
                "import $userPackage.common.BaseViewModel\n" +
                "import $userPackage.common.startWithAndErrHandleWithIO\n" +
                "import $packageName.${fileName}StateChange.*\n" +
                "import $packageName.${fileName}Intent.*\n" +
                "import io.reactivex.Observable\n" +
                "\n" +
                "class ${fileName}ViewModel() : BaseViewModel<${fileName}State>() {\n" +
                "\n" +
                "  override fun initState(): ${fileName}State = ${fileName}State()\n" +
                "\n" +
                "  override fun viewIntents(intentStream: Observable<*>): Observable<Any> = \n" +
                "    Observable.merge(\n" +
                "      listOf(\n" +
                "           intentStream.ofType(GetSampleData::class.java)\n" +
                "                    .map { Success }\n" +
                "                    .startWithAndErrHandleWithIO(Loading) { Observable.just(Error(it), HideError) }\n" +
                "       )\n" +
                "    )\n" +
                "\n" +
                "  override fun reduceState(previousState: ${fileName}State, stateChange: Any): ${fileName}State = \n" +
                "  when (stateChange) {\n" +
                "            is Loading -> previousState.copy(\n" +
                "                loading = true,\n" +
                "                success = false,\n" +
                "                error = null\n" +
                "            )\n" +
                "\n" +
                "            is Success -> previousState.copy(\n" +
                "                loading = false,\n" +
                "                success = true,\n" +
                "                error = null\n" +
                "            )\n" +
                "\n" +
                "            is Error -> previousState.copy(\n" +
                "                loading = false,\n" +
                "                success = false,\n" +
                "                error = stateChange.error\n" +
                "            )\n" +
                "\n" +
                "            is HideError -> previousState.copy(error = null)\n" +
                "\n" +
                "            else -> previousState\n" +
                "        }\n" +
                "}"
    )

    class State(fileName: String, packageName: String) : FileType(
        "${fileName}State.kt",
        "package $packageName\n" +
                "\n" +
                "data class ${fileName}State(\n" +
                "  val success: Boolean = false," +
                "  val loading: Boolean = false,\n" +
                "  val error: Throwable? = null\n" +
                ")\n" +
                "\n" +
                "sealed class ${fileName}Intent {\n" +
                "  object GetSampleData : ${fileName}Intent()\n" +
                "}\n" +
                "\n" +
                "sealed class ${fileName}StateChange {\n" +
                "  class Error(val error: Throwable) : ${fileName}StateChange()\n" +
                "  object HideError : ${fileName}StateChange()\n" +
                "  object Loading : ${fileName}StateChange()\n" +
                "  object Success : ${fileName}StateChange()\n" +
                "}"
    )

    class Entity(packageName: String) : FileType(
        "Entity.kt",
        "package $packageName.domain.entity\n" +
                "\n" +
                "data class Entity(\n" +
                "  val id: String = \"\",\n" +
                "  val data: String = \"\"\n" +
                ")"
    )

    class EntityR(packageName: String) : FileType(
        "EntityR.kt",
        "package $packageName.data.remote.entity\n" +
                "\n" +
                "import com.bluelinelabs.logansquare.annotation.JsonField\n" +
                "import com.bluelinelabs.logansquare.annotation.JsonObject\n" +
                "\n" +
                "@JsonObject\n" +
                "data class EntityR(\n" +
                "  @JsonField(name = [\"id\"]) var id: String? = \"\",\n" +
                "  @JsonField(name = [\"data\"]) var data: String? = \"\"\n" +
                ")"
    )

    class EntityL(packageName: String) : FileType(
        "EntityL.kt",
        "package $packageName.data.local.entity\n" +
                "\n" +
                "import androidx.room.Entity\n" +
                "import androidx.room.PrimaryKey\n" +
                "\n" +
                "@Entity\n" +
                "data class EntityL(\n" +
                "  var id: String = \"\",\n" +
                "  var data: String = \"\"\n" +
                "){\n" +
                "\n" +
                "  @PrimaryKey(autoGenerate = true)\n" +
                "  var dbId: Int = 0\n" +
                "}"
    )

    class EntityDao(packageName: String) : FileType(
        "EntityDao.kt",
        "package $packageName.data.local.db\n" +
                "\n" +
                "import androidx.room.Dao\n" +
                "import androidx.room.Insert\n" +
                "import androidx.room.OnConflictStrategy\n" +
                "import androidx.room.Query\n" +
                "import io.reactivex.Flowable\n" +
                "import $packageName.data.local.entity.EntityL\n" +
                "\n" +
                "@Dao\n" +
                "interface EntityDao {\n" +
                "\n" +
                "  @Query(\"SELECT * FROM EntityL ORDER BY dbId ASC\")\n" +
                "  fun getAll(): Flowable<List<EntityL>>\n" +
                "\n" +
                "  @Insert(onConflict = OnConflictStrategy.REPLACE)\n" +
                "  fun insert(entity: EntityL): Long\n" +
                "\n" +
                "  @Insert(onConflict = OnConflictStrategy.REPLACE)\n" +
                "  fun insertAll(entityList: List<EntityL>): List<Long>\n" +
                "\n" +
                "  @Query(\"DELETE FROM EntityL\")\n" +
                "  fun deleteAll(): Int\n" +
                "\n" +
                "  @Query(\"SELECT * FROM EntityL WHERE id = :id\")\n" +
                "  fun find(id: Int): Flowable<List<EntityL>>\n" +
                "\n" +
                "  @Query(\"SELECT * FROM EntityL WHERE id LIKE '%' || :id || '%'\")\n" +
                "  fun findById(id: String): Flowable<List<EntityL>>\n" +
                "}"
    )

    class EntitiesRepositoryImpl(packageName: String) : FileType(
        "EntitiesRepositoryImpl.kt",
        "package $packageName.data.repositoryImpl\n" +
                "\n" +
                "import $packageName.domain.datasource.EntityDataSource\n" +
                "import $packageName.domain.entity.Entity\n" +
                "import $packageName.domain.repository.EntitiesRepository\n" +
                "import io.reactivex.Observable\n" +
                "\n" +
                "class EntitiesRepositoryImpl(\n" +
                "  private val localSource: EntityDataSource,\n" +
                "  private val remoteSource: EntityDataSource\n" +
                ") : EntitiesRepository {\n" +
                "\n" +
                "  private val entitiesObservable: Observable<List<Entity>> = localSource.observeEntities().share()\n" +
                "\n" +
                "    override fun observeEntities(): Observable<List<Entity>> = entitiesObservable\n" +
                "\n" +
                "}"
    )

    class EntitiesRepository(packageName: String) : FileType(
        "EntitiesRepository.kt",
        "package $packageName.domain.repository\n" +
                "\n" +
                "import $packageName.domain.entity.Entity\n" +
                "import io.reactivex.Observable\n" +
                "\n" +
                "interface EntitiesRepository {\n" +
                "\n" +
                "  fun observeEntities(): Observable<List<Entity>>\n" +
                "\n" +
                "}"
    )

    class EntityUseCase(packageName: String) : FileType(
        "EntityUseCases.kt",
        "package $packageName.domain.interactors\n" +
                "\n" +
                "import $packageName.domain.entity.Entity\n" +
                "import $packageName.domain.repository.EntitiesRepository\n" +
                "import io.reactivex.Observable\n" +
                "\n" +
                "class ObserveEntitiesUseCase(private val repository: EntitiesRepository) {\n" +
                "\n" +
                "  fun execute(): Observable<List<Entity>> = repository.observeEntities()\n" +
                "}\n"
    )

    class EntityDataSource(packageName: String) : FileType(
        "EntityDataSource.kt",
        "package $packageName.domain.datasource\n" +
                "\n" +
                "import $packageName.domain.entity.Entity\n" +
                "import io.reactivex.Observable\n" +
                "\n" +
                "interface EntityDataSource {\n" +
                "  fun observeEntities(): Observable<List<Entity>>\n" +
                "}\n"
    )

    class EntityRemoteSource(projectName: String, packageName: String) : FileType(
        "EntityRemoteSource.kt",

        "package $packageName.data.remote.datasource\n" +
                "\n" +
                "import $packageName.data.remote.api.${projectName}Api\n" +
                "import $packageName.domain.datasource.EntityDataSource\n" +
                "import $packageName.domain.entity.Entity\n" +
                "import io.reactivex.Observable\n" +
                "\n" +
                "class EntityRemoteSource(private val api: ${projectName}Api) : EntityDataSource {\n" +
                "\n" +
                "  override fun observeEntities(): Observable<List<Entity>> {\n" +
                "    TODO(\"not implemented\") //To change body of created functions use File | Settings | File Templates.\n" +
                "  }\n" +
                "}"
    )

    class EntityLocalSource(packageName: String) : FileType(
        "EntityLocalSource.kt",
        "package $packageName.data.local.datasource\n" +
                "\n" +
                "import $packageName.data.local.db.EntityDao\n" +
                "import $packageName.data.local.entity.EntityL\n" +
                "import $packageName.domain.datasource.EntityDataSource\n" +
                "import $packageName.domain.entity.Entity\n" +
                "import io.reactivex.Observable\n" +
                "\n" +
                "class EntityLocalSource(private val entityDao: EntityDao) : EntityDataSource {\n" +
                "\n" +
                "  override fun observeEntities(): Observable<List<Entity>> =\n" +
                "    entityDao.getAll()\n" +
                "      .map { if (it.isEmpty()) emptyList() else it.map { item -> item.toDomain() } }\n" +
                "      .toObservable()\n" +
                "\n" +
                "  private fun EntityL.toDomain() =\n" +
                "    Entity(\n" +
                "      id = id,\n" +
                "      data = data\n" +
                "    )\n" +
                "\n" +
                "  private fun Entity.toLocal() =\n" +
                "    EntityL(\n" +
                "      id = id,\n" +
                "      data = data\n" +
                "    )\n" +
                "}"
    )

    class ApiProvider(projectName: String, packageName: String) : FileType(
        projectName + "ApiProvider.kt",
        "package $packageName.data.remote.api\n" +
                "\n" +
                "import $packageName.BuildConfig\n" +
                "import com.github.aurae.retrofit2.LoganSquareConverterFactory\n" +
                "import okhttp3.Interceptor\n" +
                "import okhttp3.OkHttpClient\n" +
                "import okhttp3.logging.HttpLoggingInterceptor\n" +
                "import okhttp3.logging.HttpLoggingInterceptor.Level.BODY\n" +
                "import okhttp3.logging.HttpLoggingInterceptor.Level.NONE\n" +
                "import retrofit2.Retrofit\n" +
                "import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory\n" +
                "\n" +
                "class ${projectName}ApiProvider {\n" +
                "\n" +
                "  companion object {\n" +
                "\n" +
                "    fun createApi(): ${projectName}Api {\n" +
                "      val client = OkHttpClient.Builder()\n" +
                "        .addInterceptor((Interceptor { chain ->\n" +
                "          val original = chain.request()\n" +
                "\n" +
                "          val interceptedRequest = original.newBuilder()\n" +
                "            .header(\"Content-Type\", \"application/json\")\n" +
                "            .header(\"x-api-key\", BuildConfig.API_KEY)\n" +
                "            .method(original.method(), original.body())\n" +
                "            .build()\n" +
                "\n" +
                "          return@Interceptor chain.proceed(interceptedRequest)\n" +
                "        }))\n" +
                "        .addInterceptor(HttpLoggingInterceptor().apply {\n" +
                "          level = if (BuildConfig.DEBUG) BODY else NONE\n" +
                "        })\n" +
                "        .build()\n" +
                "\n" +
                "      return Retrofit.Builder()\n" +
                "        .baseUrl(BuildConfig.BASE_URL)\n" +
                "        .client(client)\n" +
                "        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())\n" +
                "        .addConverterFactory(LoganSquareConverterFactory.create())\n" +
                "        .build()\n" +
                "        .create(${projectName}Api::class.java)\n" +
                "    }\n" +
                "  }\n" +
                "}"
    )

    class RepoModule(projectName: String, packageName: String) : FileType(
        "RepoModule.kt",
        "package $packageName.di\n" +
                "\n" +
                "import android.content.Context.MODE_PRIVATE\n" +
                "import $packageName.data.local.datasource.EntityLocalSource\n" +
                "import $packageName.data.local.db.${projectName}DbProvider\n" +
                "import $packageName.data.remote.api.${projectName}ApiProvider\n" +
                "import $packageName.data.remote.datasource.EntityRemoteSource\n" +
                "import $packageName.data.repositoryImpl.EntitiesRepositoryImpl\n" +
                "import $packageName.data.serviceImpl.GlideImageLoader\n" +
                "import $packageName.domain.datasource.EntityDataSource\n" +
                "import $packageName.domain.interactors.*\n" +
                "import $packageName.domain.repository.EntitiesRepository\n" +
                "import $packageName.domain.service.ImageLoader\n" +
                "import org.koin.android.ext.koin.androidContext\n" +
                "import org.koin.dsl.module.module\n" +
                "\n" +
                "val repoModule = module {\n" +
                "\n" +
                "  single { ${projectName}DbProvider.getInstance(androidContext()).entityDao() }\n" +
                "\n" +
                "  single { ${projectName}ApiProvider.createApi() }\n" +
                "\n" +
                "  single { androidContext().getSharedPreferences(\"sharedPrefs\", MODE_PRIVATE) }\n" +
                "\n" +
                "  single<ImageLoader> { GlideImageLoader }\n" +
                "  single<EntityDataSource>(\"local\") { EntityLocalSource(get()) }\n" +
                "  single<EntityDataSource>(\"remote\") { EntityRemoteSource(get()) }\n" +
                "\n" +
                "  single<EntitiesRepository> { EntitiesRepositoryImpl(get(\"local\"), get(\"remote\")) }\n" +
                "\n" +
                "  factory { ObserveEntitiesUseCase(get()) }\n" +
                "\n" +
                "}"
    )

    class Api(projectName: String, packageName: String) : FileType(
        "${projectName}Api.kt",
        "package $packageName.data.remote.api\n" +
                "\n" +
                "import $packageName.data.remote.entity.EntityR\n" +
                "import io.reactivex.Observable\n" +
                "import retrofit2.http.GET\n" +
                "\n" +
                "interface ${projectName}Api {\n" +
                "\n" +
                "  @GET(\"path\")\n" +
                "  fun getEntities(): Observable<List<EntityR>>\n" +
                "}"
    )


    class DbTypeConverters(packageName: String) : FileType(
        "DbTypeConverters.kt",
        "package $packageName.data.local.db\n" +
                "\n" +
                "import androidx.room.TypeConverter\n" +
                "import com.bluelinelabs.logansquare.LoganSquare\n" +
                "import java.text.SimpleDateFormat\n" +
                "import java.util.*\n" +
                "\n" +
                "class DbTypeConverters {\n" +
                "\n" +
                "  @TypeConverter\n" +
                "  fun fromStringToList(value: String): List<String> =\n" +
                "    LoganSquare.parseList(value, String::class.java)\n" +
                "\n" +
                "  @TypeConverter\n" +
                "  fun fromListToString(list: List<String>): String = LoganSquare.serialize(list)\n" +
                "\n" +
                "  @TypeConverter\n" +
                "  fun fromStringToDate(dateString: String): Date {\n" +
                "    val dateFormat = SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss\", Locale.getDefault())\n" +
                "    return dateFormat.parse(dateString)\n" +
                "  }\n" +
                "\n" +
                "  @TypeConverter\n" +
                "  fun fromDateToString(date: Date): String {\n" +
                "    val dateFormat = SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss\", Locale.getDefault())\n" +
                "    return dateFormat.format(date)\n" +
                "  }\n" +
                "}"
    )

    class DbProvider(projectName: String, packageName: String) : FileType(
        "${projectName}DbProvider.kt",
        "package $packageName.data.local.db\n" +
                "\n" +
                "import android.content.Context\n" +
                "import androidx.room.Room\n" +
                "\n" +
                "class ${projectName}DbProvider {\n" +
                "  companion object {\n" +
                "\n" +
                "    @Volatile private var INSTANCE: ${projectName}Db? = null\n" +
                "\n" +
                "    fun getInstance(context: Context): ${projectName}Db =\n" +
                "      INSTANCE ?: synchronized(this) {\n" +
                "        INSTANCE ?: buildDatabase(context).also { INSTANCE = it }\n" +
                "      }\n" +
                "\n" +
                "    private fun buildDatabase(context: Context) =\n" +
                "      Room.databaseBuilder(context, ${projectName}Db::class.java, \"${projectName}Database\")\n" +
                "        .build()\n" +
                "  }" +
                "}"
    )

    class Database(projectName: String, packageName: String) : FileType(
        "${projectName}Db.kt",
        "package $packageName.data.local.db\n" +
                "\n" +
                "import androidx.room.Database\n" +
                "import androidx.room.RoomDatabase\n" +
                "import androidx.room.TypeConverters\n" +
                "import $packageName.data.local.entity.EntityL\n" +
                "\n" +
                "@Database(entities = [EntityL::class], version = 1, exportSchema = false)\n" +
                "@TypeConverters(DbTypeConverters::class)\n" +
                "abstract class ${projectName}Db : RoomDatabase() {\n" +
                "\n" +
                "  abstract fun entityDao(): EntityDao\n" +
                "\n" +
                "}"
    )

    class Layout(fileName: String, packageName: String) : FileType(
        "fragment_${fileName.replace(Regex("([^_A-Z])([A-Z])"), "$1_$2").toLowerCase()}.xml",
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<layout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" +
                "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                "    >\n" +
                " <data>\n" +
                "    <import type=\"android.view.View\"/>\n" +
                "\n" +
                "    <variable\n" +
                "        name=\"viewState\"\n" +
                "        type=\"$packageName.${fileName}State\"/>\n" +
                "  </data>" +
                "\n" +
                "<androidx.constraintlayout.widget.ConstraintLayout\n" +
                "        android:layout_width=\"match_parent\"\n" +
                "        android:layout_height=\"match_parent\"\n" +
                "        >\n" +
                "\n" +
                "    <TextView\n" +
                "            android:layout_width=\"wrap_content\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:text=\"Hello World!\"\n" +
                "            app:layout_constraintBottom_toBottomOf=\"parent\"\n" +
                "            app:layout_constraintLeft_toLeftOf=\"parent\"\n" +
                "            app:layout_constraintRight_toRightOf=\"parent\"\n" +
                "            app:layout_constraintTop_toTopOf=\"parent\"/>\n" +
                "\n" +
                " </androidx.constraintlayout.widget.ConstraintLayout>\n" +
                "</layout>"
    )

    class ImageBindingAdapter(packageName: String) : FileType(
        "ImageBindingAdapter.kt",
        "package $packageName.common\n" +
                "\n" +
                "import android.widget.ImageView\n" +
                "import androidx.databinding.BindingAdapter\n" +
                "import $packageName.repository.domain.service.ImageLoader\n" +
                "\n" +
                "class ImageBindingAdapter constructor(private val imageLoader: ImageLoader) {\n" +
                "\n" +
                "  lateinit var packageName: String\n" +
                "\n" +
                "  @BindingAdapter(\n" +
                "    value = [\n" +
                "      \"imgUrl\",\n" +
                "      \"imgResId\",\n" +
                "      \"placeholderResId\",\n" +
                "      \"transformCenterCrop\",\n" +
                "      \"transformCircle\",\n" +
                "      \"transformRoundedCorners\",\n" +
                "      \"fade\"],\n" +
                "    requireAll = false\n" +
                "  )\n" +
                "  fun loadImage(\n" +
                "    imageView: ImageView,\n" +
                "    imgUrl: String? = null,\n" +
                "    imgResId: Int = 0,\n" +
                "    placeholderResId: Int = 0,\n" +
                "    transformCenterCrop: Boolean = false,\n" +
                "    transformCircle: Boolean = false,\n" +
                "    transformRoundedCorners: Int = 0,\n" +
                "    fade: Boolean = false\n" +
                "  ) {\n" +
                "    if (!::packageName.isInitialized)\n" +
                "      packageName = imageView.context.packageName\n" +
                "\n" +
                "    val imageUrl = if (imgResId == 0) imgUrl\n" +
                "    else \"android.resource://\$packageName/\$imgResId\"\n" +
                "\n" +
                "    imageLoader.loadImg(\n" +
                "      imageView,\n" +
                "      imageUrl,\n" +
                "      ImageLoader.Args(\n" +
                "        placeholderResId,\n" +
                "        transformCenterCrop,\n" +
                "        transformCircle,\n" +
                "        transformRoundedCorners\n" +
                "      )\n" +
                "    )\n" +
                "  }\n" +
                "}"
    )

    class GlideImageLoader(packageName: String) : FileType(
        "GlideImageLoader.kt",
        "package $packageName.data.serviceImpl\n" +
                "\n" +
                "import android.content.res.Resources\n" +
                "import android.graphics.Bitmap\n" +
                "import android.widget.ImageView\n" +
                "import com.bumptech.glide.Glide\n" +
                "import com.bumptech.glide.load.MultiTransformation\n" +
                "import com.bumptech.glide.load.Transformation\n" +
                "import com.bumptech.glide.load.resource.bitmap.CenterCrop\n" +
                "import com.bumptech.glide.load.resource.bitmap.CircleCrop\n" +
                "import com.bumptech.glide.load.resource.bitmap.RoundedCorners\n" +
                "import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions\n" +
                "import com.bumptech.glide.request.RequestOptions\n" +
                "import $packageName.domain.service.ImageLoader\n" +
                "import java.util.ArrayList\n" +
                "\n" +
                "val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()\n" +
                "\n" +
                "object GlideImageLoader : ImageLoader {\n" +
                "\n" +
                "  override fun loadImg(\n" +
                "    iv: ImageView,\n" +
                "    url: String?,\n" +
                "    args: ImageLoader.Args\n" +
                "  ) {\n" +
                "    val (placeholderResId, transformCenterCrop, transformCircle, roundedCornersRadiusDp) = args\n" +
                "\n" +
                "    require(!transformCircle || roundedCornersRadiusDp == 0) {\n" +
                "      \"Cannot apply transformCircle and roundedCornersRadiusDp attrs at the same time\"\n" +
                "    }\n" +
                "\n" +
                "    val transformations = ArrayList<Transformation<Bitmap>>().apply {\n" +
                "      if (transformCenterCrop) add(CenterCrop())\n" +
                "      if (transformCircle) add(CircleCrop())\n" +
                "      if (roundedCornersRadiusDp > 0) add(RoundedCorners(roundedCornersRadiusDp.px))\n" +
                "    }\n" +
                "    // is transform requested?\n" +
                "    val requestTransformOptions = if (!transformations.isEmpty()) {\n" +
                "      RequestOptions.bitmapTransform(MultiTransformation(transformations))\n" +
                "    } else RequestOptions()\n" +
                "\n" +
                "    if (placeholderResId != 0)\n" +
                "      requestTransformOptions.placeholder(placeholderResId)\n" +
                "\n" +
                "    Glide.with(iv)\n" +
                "      .load(url)\n" +
                "      .apply(requestTransformOptions)\n" +
                "      .transition(DrawableTransitionOptions.withCrossFade())\n" +
                "      .into(iv)\n" +
                "  }\n" +
                "}"
    )

    class ImageLoader(packageName: String) : FileType(
        "ImageLoader.kt",
        "package $packageName.domain.service\n" +
                "\n" +
                "import android.widget.ImageView\n" +
                "\n" +
                "interface ImageLoader {\n" +
                "  fun loadImg(\n" +
                "    iv: ImageView,\n" +
                "    url: String?,\n" +
                "    args: Args\n" +
                "  )\n" +
                "\n" +
                "  data class Args(\n" +
                "    val placeholderResId: Int = 0,\n" +
                "    val transformCenterCrop: Boolean = false,\n" +
                "    val transformCircle: Boolean = false,\n" +
                "    val roundedCornersRadiusDp: Int = 0\n" +
                "  )\n" +
                "}"
    )

    class Common(packageName: String) : FileType(
        "Common.kt",
        "package $packageName.common\n" +
                "\n" +
                "import android.content.res.Resources\n" +

                "import android.view.animation.OvershootInterpolator\n" +
                "import android.widget.ImageView\n" +
                "import android.widget.TextView\n" +
                "import androidx.annotation.DrawableRes\n" +
                "import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent\n" +
                "import com.jakewharton.rxbinding2.widget.afterTextChangeEvents\n" +
                "import io.reactivex.Observable\n" +
                "import io.reactivex.ObservableTransformer\n" +
                "import io.reactivex.android.schedulers.AndroidSchedulers\n" +
                "import io.reactivex.schedulers.Schedulers\n" +
                "import java.util.concurrent.TimeUnit.MILLISECONDS\n" +
                "import kotlin.LazyThreadSafetyMode.NONE" +
                "\n" +
                "val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()\n" +
                "val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()\n" +
                "\n" +
                "fun changeViewImageResource(imageView: ImageView, @DrawableRes resId: Int) {\n" +
                "  imageView.rotation = 0f\n" +
                "  imageView.animate()\n" +
                "    .rotationBy(360f)\n" +
                "    .setDuration(400)\n" +
                "    .setInterpolator(OvershootInterpolator())\n" +
                "    .start()\n" +
                "\n" +
                "  imageView.postDelayed({ imageView.setImageResource(resId) }, 120)\n" +
                "}\n" +
                "\n" +
                "inline fun <T, reified R> Observable<T>.startWithAndErrHandleWithIO(\n" +
                "  startWith: R,\n" +
                "  noinline errorHandler: (Throwable) -> Observable<R>\n" +
                "): Observable<Any> =\n" +
                "  this.cast(Any::class.java)\n" +
                "    .startWith(startWith)\n" +
                "    .onErrorResumeNext(errorHandler)\n" +
                "    .subscribeOn(Schedulers.io())\n" +
                "    .observeOn(AndroidSchedulers.mainThread())\n" +
                "\n" +
                "inline fun <T, reified R> Observable<T>.errHandleWithIO(\n" +
                "  noinline errorHandler: (Throwable) -> Observable<R>\n" +
                "): Observable<Any> =\n" +
                "  this.cast(Any::class.java)\n" +
                "    .onErrorResumeNext(errorHandler)\n" +
                "    .subscribeOn(Schedulers.io())\n" +
                "    .observeOn(AndroidSchedulers.mainThread())\n" +
                "\n" +
                "val editTextAfterTextChangeTransformer by lazy(NONE) {\n" +
                "  ObservableTransformer<TextViewAfterTextChangeEvent, String> {\n" +
                "    it.skip(1)\n" +
                "      .map { it.editable().toString() }\n" +
                "      .distinctUntilChanged()\n" +
                "      .debounce(350, MILLISECONDS)\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "inline fun <T> TextView.debouncedAfterTextChanges(noinline mapper: (String) -> T): Observable<T> =\n" +
                "  afterTextChangeEvents().compose(editTextAfterTextChangeTransformer).map(mapper)"
    )

    class BaseComponents(packageName: String) : FileType(
        "BaseComponents.kt",
        "package $packageName.common\n" +
                "\n" +
                "import android.os.Bundle\n" +
                "import android.view.LayoutInflater\n" +
                "import android.view.View\n" +
                "import android.view.ViewGroup\n" +
                "import androidx.databinding.DataBindingUtil\n" +
                "import androidx.databinding.ViewDataBinding\n" +
                "import androidx.fragment.app.Fragment\n" +
                "import androidx.lifecycle.LiveData\n" +
                "import androidx.lifecycle.MutableLiveData\n" +
                "import androidx.lifecycle.ViewModel\n" +
                "import com.jakewharton.rxrelay2.PublishRelay\n" +
                "import io.reactivex.Observable\n" +
                "import io.reactivex.android.schedulers.AndroidSchedulers\n" +
                "import io.reactivex.disposables.CompositeDisposable\n" +
                "import io.reactivex.disposables.Disposable\n" +
                "\n" +
                "abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {\n" +
                "\n" +
                "    protected var viewSubscriptions: Disposable? = null\n" +
                "    protected var compositeDisposable: CompositeDisposable? = null\n" +
                "\n" +
                "    protected var viewBinding: DB? = null\n" +
                "        private set\n" +
                "\n" +
                "    abstract fun resLayoutId(): Int\n" +
                "\n" +
                "    override fun onCreateView(\n" +
                "        inflater: LayoutInflater,\n" +
                "        container: ViewGroup?,\n" +
                "        savedInstanceState: Bundle?\n" +
                "    ): View? {\n" +
                "        compositeDisposable = CompositeDisposable()\n" +
                "        viewBinding = DataBindingUtil.inflate(\n" +
                "            LayoutInflater.from(context),\n" +
                "            resLayoutId(),\n" +
                "            container,\n" +
                "            false\n" +
                "        )\n" +
                "        return viewBinding!!.root\n" +
                "    }\n" +
                "\n" +
                "    override fun onDestroyView() {\n" +
                "        super.onDestroyView()\n" +
                "        compositeDisposable?.dispose()\n" +
                "        viewSubscriptions?.dispose()\n" +
                "        viewBinding = null\n" +
                "    }\n" +
                "\n" +
                "}\n" +
                "\n" +
                "interface BaseView<State> {\n" +
                "\n" +
                "    fun initIntents()\n" +
                "\n" +
                "    fun handleStates()\n" +
                "\n" +
                "    fun render(state: State)\n" +
                "}\n" +
                "\n" +
                "\n" +
                "abstract class BaseViewModel<State> : ViewModel() {\n" +
                "\n" +
                "    private val states = MutableLiveData<State>()\n" +
                "    private val viewIntentsConsumer: PublishRelay<Any> = PublishRelay.create()\n" +
                "    private var intentsDisposable: Disposable? = null\n" +
                "\n" +
                "    protected abstract fun initState(): State\n" +
                "\n" +
                "    private fun handleIntents() {\n" +
                "        intentsDisposable = Observable.merge(vmIntents(), viewIntents(viewIntentsConsumer))\n" +
                "            .scan(initState()) { previousState, stateChanges ->\n" +
                "                reduceState(previousState, stateChanges)\n" +
                "            }\n" +
                "            .observeOn(AndroidSchedulers.mainThread())\n" +
                "            .subscribe { state -> states.value = state }\n" +
                "    }\n" +
                "\n" +
                "    protected open fun vmIntents(): Observable<Any> = Observable.never()\n" +
                "\n" +
                "    protected abstract fun viewIntents(intentStream: Observable<*>): Observable<Any>\n" +
                "\n" +
                "    protected abstract fun reduceState(previousState: State, stateChange: Any): State\n" +
                "\n" +
                "    fun viewIntentsConsumer() = viewIntentsConsumer.also {\n" +
                "        if (intentsDisposable == null)\n" +
                "            handleIntents()\n" +
                "    }\n" +
                "\n" +
                "    fun stateReceived(): LiveData<State> = states\n" +
                "\n" +
                "    override fun onCleared() {\n" +
                "        intentsDisposable?.dispose()\n" +
                "    }\n" +
                "}"
    )

    class Manifest(packageName: String) : FileType(
        "AndroidManifest.xml",
        "<manifest package=\"$packageName\"/>"
    )

    class Proguard : FileType(
        "proguard-rules.pro",
        "# Add project specific ProGuard rules here.\n" +
                "# You can control the set of applied configuration files using the\n" +
                "# proguardFiles setting in build.gradle.\n" +
                "#\n" +
                "# For more details, see\n" +
                "#   http://developer.android.com/guide/developing/tools/proguard.html\n" +
                "\n" +
                "# If your project uses WebView with JS, uncomment the following\n" +
                "# and specify the fully qualified class name to the JavaScript interface\n" +
                "# class:\n" +
                "#-keepclassmembers class fqcn.of.javascript.interface.for.webview {\n" +
                "#   public *;\n" +
                "#}\n" +
                "\n" +
                "# Uncomment this to preserve the line number information for\n" +
                "# debugging stack traces.\n" +
                "#-keepattributes SourceFile,LineNumberTable\n" +
                "\n" +
                "# If you keep the line number information, uncomment this to\n" +
                "# hide the original source file name.\n" +
                "#-renamesourcefileattribute SourceFile"
    )

    class DepsBuildGradle() : FileType(
        "dependencies.gradle",
    "ext {\n" +
            "    sourceCompatibility = JavaVersion.VERSION_1_8\n" +
            "    targetCompatibility = JavaVersion.VERSION_1_8\n" +
            "\n" +
            "   versionMajor = 0\n" +
            "   versionMinor = 0\n" +
            "   versionPatch = 1\n" +
            "   versionClassifier = \"\"\n" +

            "    // project\n" +
            "    minSdk = 22\n" +
            "    targetSdk = 28\n" +
            "    buildToolsVersion = \"28.0.2\"\n" +
            "    compileSdk = 28\n" +
            "    androidGradle = \"3.4.0-beta03\"\n" +
            "\n" +
            "    // android\n" +
            "    androidXV = \"1.0.0\"\n" +
            "    androidKtxV = \"1.0.0-rc02\"   //https://github.com/android/android-ktx\n" +
            "    androidLifecycleV = \"2.0.0-alpha1\"\n" +
            "    multidexV = \"2.0.0\"\n" +
            "    constraintLayoutV = \"2.0.0-alpha2\"\n" +
            "    workersV = \"2.0.1\"\n" +
            "\n" +
            "    // kotlin\n" +
            "    kotlinV = \"1.3.10\" // https://kotlinlang.org/\n" +
            "    coroutinesAndroidV = \"1.2.0-alpha-2\" // https://kotlinlang.org/\n" +
            "    coroutinesCoreV = \"1.2.0-alpha-2\" // https://kotlinlang.org/\n" +
            "\n" +
            "    //rxExtensions\n" +
            "    rxJavaV = \"2.2.0\"            //https://github.com/ReactiveX/RxJava\n" +
            "    rxRelayV = \"2.0.0\"           //https://github.com/JakeWharton/RxRelay\n" +
            "    rxKotlinV = \"2.2.0\"          //https://github.com/ReactiveX/RxKotlin\n" +
            "    rxAndroidV = \"2.1.0\"         //https://github.com/ReactiveX/RxAndroid\n" +
            "    rxBindingV = \"2.1.1\"         //https://github.com/JakeWharton/RxBinding\n" +
            "    rxBinding3V = \"3.0.0-alpha1\"         //https://github.com/JakeWharton/RxBinding\n" +
            "    rxNetworkV = \"2.1.0\"         //https://github.com/pwittchen/ReactiveNetwork\n" +
            "    rxPermissionsV = \"0.10.2\"    //https://github.com/tbruyelle/RxPermissions\n" +
            "\n" +
            "    // dependency injection\n" +
            "    daggerV = \"2.17\" //https://github.com/google/dagger\n" +
            "    koinV = \"1.0.1\"  //https://github.com/InsertKoinIO/koin\n" +
            "\n" +
            "    // general\n" +
            "    mosbyV = \"3.1.0\" //https://github.com/sockeqwe/mosby\n" +
            "\n" +
            "    //pdf\n" +
            "    pdfViewV = \"3.1.0-beta.1\"\n" +
            "\n" +
            "    // network\n" +
            "    okHttpV = \"3.11.0\" //https://github.com/square/okhttp\n" +
            "\n" +
            "    // serialization\n" +
            "    gsonV = \"2.8.5\"        //https://github.com/google/gson\n" +
            "    loganSquareV = \"1.3.7\" //https://github.com/bluelinelabs/LoganSquare\n" +
            "    jacksonCoreV = \"2.9.7\" //https://github.com/bluelinelabs/LoganSquare\n" +
            "\n" +
            "    // retrofit\n" +
            "    retrofitV = \"2.4.0\"                     //https://github.com/square/retrofit\n" +
            "    retrofitConverterGsonV =\n" +
            "            \"2.4.0\"        //https://github.com/square/retrofit/tree/master/retrofit-converters/gson\n" +
            "    retrofitConverterLoganSquareV = \"1.4.1\" //https://github.com/mannodermaus/retrofit-logansquare\n" +
            "\n" +
            "    // apollo\n" +
            "    apolloV = \"1.0.0-alpha\"      // https://github.com/apollographql/apollo-android\n" +
            "\n" +
            "    //persistence\n" +
            "    roomV = \"2.1.0-alpha06\" //https://developer.android.com/topic/libraries/architecture/room\n" +
            "\n" +
            "    //lifecycle\n" +
            "    lifecycleV = \"2.0.0-rc01\" //https://developer.android.com/topic/libraries/architecture/lifecycle\n" +
            "    viewmodelKTXV = \"2.1.0-alpha04\" //https://developer.android.com/topic/libraries/architecture/lifecycle\n" +
            "    lifecycleReactiveStreamsV = \"2.0.0\" //https://developer.android.com/topic/libraries/architecture/lifecycle\n" +
            "\n" +
            "    //navigation\n" +
            "    navigationV = \"2.1.0-alpha01\" //https://developer.android.com/topic/libraries/architecture/navigation\n" +
            "\n" +
            "    //imageLoading\n" +
            "    glideV = \"4.8.0\"  //https://github.comalpha07/bumptech/glide\n" +
            "    picassoV = \"2.71828\"       //https://github.com/square/picasso\n" +
            "\n" +
            "    //profiling\n" +
            "    debugDbV = \"1.0.4\"    //https://github.com/amitshekhariitbhu/Android-Debug-Database\n" +
            "    leakCanaryV = \"1.6.2\" //https://github.com/square/leakcanary\n" +
            "\n" +
            "    //social auth\n" +
            "    facebookAnalyticsV = \"[4,5)\"\n" +
            "    crashlyticsV = \"2.9.4@aar\"\n" +
            "    fabricPluginV = \"1.+\"\n" +
            "    simpleAuthV = \"2.1.3\"  //https://github.com/jaychang0917/SimpleAuth\n" +
            "\n" +
            "    // firebase\n" +
            "    firebaseCoreV = \"16.0.1\"\n" +
            "    firebaseMessagingV = \"17.1.0\"\n" +
            "\n" +
            "    //Google\n" +
            "    googleServicesV = \"4.0.1\"\n" +
            "    googleAuthV = \"16.0.0\"\n" +
            "    googleMapsV = \"16.0.0\"\n" +
            "    googleLocationV = \"16.0.0\"\n" +
            "\n" +
            "    // tests\n" +
            "    jUnitV = \"4.12\"\n" +
            "    androidTestRunnerV = \"1.0.0-rc01\"\n" +
            "    espressoV = \"3.1.0-alpha4\"\n" +
            "    mockitoV = \"2.22.0\"\n" +
            "    mockitoAndroidV = \"2.22.0\"\n" +
            "    mockitoKotlinV = \"2.0.0-RC1\"\n" +
            "\n" +
            "    //ImageProcessing\n" +
            "    gpuImageV = \"2.0.3\"\n" +
            "    exifinterfaceV = \"1.0.0\"\n" +
            "    compressorV = \"2.1.0\"\n" +
            "\n" +
            "    //Camera\n" +
            "    cameraViewV = \"2.0.0-beta03\"\n" +
            "    //Billing\n" +
            "    billingV = \"1.2.1\"\n" +
            "    skeletonV = \"1.1.2\"\n" +
            "    shimmerlayoutV = \"2.1.0\"\n" +
            "\n" +
            "    exoplayerCoreV = \"2.9.6\"\n" +
            "    exoplayerDashV = \"2.9.6\"\n" +
            "    exoplayerUiV = \"2.9.6\"\n" +
            "\n" +
            "    dashedCircularProgressV = \"1.4\"\n" +
            "\n" +
            "    customTabsV = \"23.3.0\"\n" +
            "\n" +
            "    // Android\n" +
            "    androidCore = \"androidx.core:core:\${androidXV}\"\n" +
            "    androidAnnotation = \"androidx.annotation:annotation:\${androidXV}\"\n" +
            "    androidLifecycle = \"androidx.lifecycle:lifecycle-runtime:\${androidLifecycleV}\"\n" +
            "    appCompat = \"androidx.appcompat:appcompat:\${androidXV}\"\n" +
            "    material = \"com.google.android.material:material:\${androidXV}\"\n" +
            "    palette = \"androidx.palette:palette:\${androidXV}\"\n" +
            "    constraintLayout = \"androidx.constraintlayout:constraintlayout:\${constraintLayoutV}\"\n" +
            "    cardview = \"androidx.cardview:cardview:\${androidXV}\"\n" +
            "    multidex = \"androidx.multidex:multidex:\${multidexV}\"\n" +
            "    animatedVector = \"androidx.vectordrawable:vectordrawable-animated:\${androidXV}\"\n" +
            "    workersKtx = \"androidx.work:work-runtime-ktx:\${workersV}\"\n" +
            "\n" +
            "    androidKtx = \"androidx.core:core-ktx:\${androidKtxV}\"\n" +
            "    paletteKtx = \"androidx.palette:palette-ktx:\${androidKtxV}\"\n" +
            "    fragmentKtx = \"androidx.fragment:fragment-ktx:\${androidKtxV}\"\n" +
            "    collectionKtx = \"androidx.collection:collection-ktx:\${androidKtxV}\"\n" +
            "\n" +
            "    // Kotlin\n" +
            "    kotlinStdlib = \"org.jetbrains.kotlin:kotlin-stdlib-jdk7:\${kotlinV}\"\n" +
            "\n" +
            "    // Coroutines\n" +
            "    coroutinesCore = \"org.jetbrains.kotlinx:kotlinx-coroutines-core:\${coroutinesCoreV}\"\n" +
            "    coroutinesAndroid = \"org.jetbrains.kotlinx:kotlinx-coroutines-core:\${coroutinesAndroidV}\"\n" +
            "\n" +
            "    // RxExtension\n" +
            "    rxJava = \"io.reactivex.rxjava2:rxjava:\${rxJavaV}\"\n" +
            "    rxRelay = \"com.jakewharton.rxrelay2:rxrelay:\${rxRelayV}\"\n" +
            "    rxKotlin = \"io.reactivex.rxjava2:rxkotlin:\${rxKotlinV}\"\n" +
            "    rxAndroid = \"io.reactivex.rxjava2:rxandroid:\${rxAndroidV}\"\n" +
            "\n" +
            "    rxBinding = \"com.jakewharton.rxbinding3:rxbinding:\${rxBinding3V}\"\n" +
            "    rxBindingCore = \"com.jakewharton.rxbinding3:rxbinding-core:\${rxBinding3V}\"\n" +
            "    rxBindingAppCompat = \"com.jakewharton.rxbinding3:rxbinding-appcompat:\${rxBinding3V}\"\n" +
            "    rxBindingDrawer = \"com.jakewharton.rxbinding3:rxbinding-drawerlayout:\${rxBinding3V}\"\n" +
            "    rxBindingLeanBack = \"com.jakewharton.rxbinding3:rxbinding-leanback:\${rxBinding3V}\"\n" +
            "    rxBindingRecyclerView = \"com.jakewharton.rxbinding3:rxbinding-recyclerview:\${rxBinding3V}\"\n" +
            "    rxBindingSlidingPanel = \"com.jakewharton.rxbinding3:rxbinding-slidingpanelayout:\${rxBinding3V}\"\n" +
            "    rxBindingSwipeRefresh = \"com.jakewharton.rxbinding3:rxbinding-swiperefreshlayout:\${rxBinding3V}\"\n" +
            "    rxBindingViewPager = \"com.jakewharton.rxbinding3:rxbinding-viewpager:\${rxBinding3V}\"\n" +
            "    rxBindingDesign = \"com.jakewharton.rxbinding3:rxbinding-material:\${rxBinding3V}\"\n" +
            "\n" +
            "    rxNetwork = \"com.github.pwittchen:reactivenetwork-rx2:\${rxNetworkV}\"\n" +
            "    rxPermissions = \"com.github.tbruyelle:rxpermissions:\${rxPermissionsV}\"\n" +
            "    rxBindingKotlin = \"com.jakewharton.rxbinding2:rxbinding-kotlin:\${rxBindingV}\"\n" +
            "    rxBindingDesignKotlin = \"com.jakewharton.rxbinding2:rxbinding-design-kotlin:\${rxBindingV}\"\n" +
            "    rxBindingAppCompatV4Kotlin = \"com.jakewharton.rxbinding2:rxbinding-support-v4-kotlin:\${rxBindingV}\"\n" +
            "    rxBindingAppCompatV7Kotlin = \"com.jakewharton.rxbinding2:rxbinding-appcompat-v7:\${rxBindingV}\"\n" +
            "    rxBindingRecyclerViewV7Kotlin =\n" +
            "            \"com.jakewharton.rxbinding2:rxbinding-recyclerview-v7-kotlin:\${rxBindingV}\"\n" +
            "\n" +
            "    // Dependency Injection\n" +
            "    inject = \"javax.inject:javax.inject:1\"\n" +
            "    dagger = \"com.google.dagger:dagger:\${daggerV}\"\n" +
            "    daggerCompiler = \"com.google.dagger:dagger-compiler:\${daggerV}\"\n" +
            "    koinCore = \"org.koin:koin-core:\${koinV}\"\n" +
            "    koinAndroid = \"org.koin:koin-android:\${koinV}\"\n" +
            "    koinCoreScope = \"org.koin:koin-androidx-scope:\${koinV}\"\n" +
            "    koinAndroidViewModel = \"org.koin:koin-androidx-viewmodel:\${koinV}\"\n" +
            "    koinTests = \"org.koin:koin-test:\${koinV}\"\n" +
            "\n" +
            "    // General\n" +
            "    mosby = \"com.hannesdorfmann.mosby3:mvi:\${mosbyV}\"\n" +
            "\n" +
            "    //PDF\n" +
            "    pdfView = \"com.github.barteksc:android-pdf-viewer:\${pdfViewV}\"\n" +
            "\n" +
            "    // Network\n" +
            "    okHttp = \"com.squareup.okhttp3:okhttp:\${okHttpV}\"\n" +
            "    okHttpInteceptor = \"com.squareup.okhttp3:logging-interceptor:\${okHttpV}\"\n" +
            "\n" +
            "    // Retrofit\n" +
            "    retrofit = \"com.squareup.retrofit2:retrofit:\${retrofitV}\"\n" +
            "    retrofitRxJavaAdapter = \"com.squareup.retrofit2:adapter-rxjava2:\${retrofitV}\"\n" +
            "    retrofitConverterGson = \"com.squareup.retrofit2:converter-gson:\${retrofitConverterGsonV}\"\n" +
            "    retrofitConverterLoganSquare =\n" +
            "            \"com.github.aurae.retrofit2:converter-logansquare:\${retrofitConverterLoganSquareV}\"\n" +
            "\n" +
            "    // Apollo\n" +
            "    apollo = \"com.apollographql.apollo:apollo-runtime:\${apolloV}\"\n" +
            "    apolloAndroidSupport = \"com.apollographql.apollo:apollo-android-support:\${apolloV}\"\n" +
            "    apolloRxJavaSupport = \"com.apollographql.apollo:apollo-rx2-support:\${apolloV}\"\n" +
            "\n" +
            "    // Serialization\n" +
            "    gson = \"com.google.code.gson:gson:\${gsonV}\"\n" +
            "    loganSquare = \"com.bluelinelabs:logansquare:\${loganSquareV}\"\n" +
            "    loganSquareCompiler = \"com.bluelinelabs:logansquare-compiler:\${loganSquareV}\"\n" +
            "    jacksonCore = \"com.fasterxml.jackson.core:jackson-core:\${loganSquareV}\"\n" +
            "\n" +
            "    // Persistence\n" +
            "    room = \"androidx.room:room-runtime:\${roomV}\"\n" +
            "    roomCompiler = \"androidx.room:room-compiler:\${roomV}\"\n" +
            "    rxRoom = \"androidx.room:room-rxjava2:\${roomV}\"\n" +
            "\n" +
            "    // Lifecycle\n" +
            "    lifecycle = \"androidx.lifecycle:lifecycle-extensions:\${lifecycleV}\"\n" +
            "    viewmodelKTX = \"androidx.lifecycle:lifecycle-viewmodel-ktx:\${viewmodelKTXV}\"\n" +
            "    lifecycleReactiveStreams = \"androidx.lifecycle:lifecycle-reactivestreams-ktx:\${lifecycleReactiveStreamsV}\"\n" +
            "\n" +
            "    // Navigation\n" +
            "\n" +
            "    navigationRuntime = \"androidx.navigation:navigation-runtime:\${navigationV}\"\n" +
            "    navigationRuntime_KTX = \"androidx.navigation:navigation-runtime-ktx:\${navigationV}\"\n" +
            "    navigationFragment = \"androidx.navigation:navigation-fragment:\${navigationV}\"\n" +
            "    navigationFragment_KTX = \"androidx.navigation:navigation-fragment-ktx:\${navigationV}\"\n" +
            "    navigationUI = \"androidx.navigation:navigation-ui:\${navigationV}\"\n" +
            "    navigationUI_KTX = \"androidx.navigation:navigation-ui-ktx:\${navigationV}\"\n" +
            "    navigationSafeArgsPlugin = \"androidx.navigation:navigation-safe-args-gradle-plugin:\${navigationV}\"\n" +
            "\n" +
            "    // ImageLoading\n" +
            "    glide = \"com.github.bumptech.glide:glide:\${glideV}\"\n" +
            "    glideCompiler = \"com.github.bumptech.glide:compiler:\${glideV}\"\n" +
            "    glideOkHttp3 = \"com.github.bumptech.glide:okhttp3-integration:\${glideV}\"\n" +
            "    picasso = \"com.squareup.picasso:picasso:\${picassoV}\"\n" +
            "\n" +
            "    // Profiling\n" +
            "    debugDb = \"com.amitshekhar.android:debug-db:\${debugDbV}\"\n" +
            "    leakCanaryDebug = \"com.squareup.leakcanary:leakcanary-android:\${leakCanaryV}\"\n" +
            "    leakCanaryRelease = \"com.squareup.leakcanary:leakcanary-android-no-op:\${leakCanaryV}\"\n" +
            "    leakCanaryFragment = \"com.squareup.leakcanary:leakcanary-support-fragment:\${leakCanaryV}\"\n" +
            "\n" +
            "    // Social auth\n" +
            "    googleAuth = \"com.google.android.gms:play-services-auth:\${googleAuthV}\"\n" +
            "    socialAuth = \"com.jaychang:simpleauth:\${simpleAuthV}\"\n" +
            "    socialAuthFacebook = \"com.jaychang:simpleauth-facebook:\${simpleAuthV}\"\n" +
            "    socialAuthGoogle = \"com.jaychang:simpleauth-google:\${simpleAuthV}\"\n" +
            "    socialAuthInstagram = \"com.jaychang:simpleauth-instagram:\${simpleAuthV}\"\n" +
            "    socialAuthTwitter = \"com.jaychang:simpleauth-twitter:\${simpleAuthV}\"\n" +
            "\n" +
            "    // Facebook\n" +
            "    facebookAnalytics = \"com.facebook.android:facebook-android-sdk:\${facebookAnalyticsV}\"\n" +
            "\n" +
            "    // Fabric\n" +
            "    fabricPlugin = \"io.fabric.tools:gradle:\${fabricPluginV}\"\n" +
            "    crashlytics = \"com.crashlytics.sdk.android:crashlytics:\${crashlyticsV}\"\n" +
            "\n" +
            "    // Firebase\n" +
            "    firebaseCore = \"com.google.firebase:firebase-core:\${firebaseCoreV}\"\n" +
            "    firebaseMessaging = \"com.google.firebase:firebase-messaging:\${firebaseMessagingV}\"\n" +
            "\n" +
            "    //Maps\n" +
            "    googleMaps = \"com.google.android.gms:play-services-maps:\${googleMapsV}\"\n" +
            "    googleLocation = \"com.google.android.gms:play-services-location:\${googleLocationV}\"\n" +
            "\n" +
            "    // Tests\n" +
            "    jUnit = \"junit:junit:\${jUnitV}\"\n" +
            "    mockito = \"org.mockito:mockito-core:\${mockitoV}\"\n" +
            "    mockitoKotlin = \"com.nhaarman.mockitokotlin2:mockito-kotlin:\${mockitoKotlinV}\"\n" +
            "    mockitoAndroid = \"org.mockito:mockito-android:\${mockitoAndroidV}\"\n" +
            "    mockitoInline = \"org.mockito:mockito-inline:+\"\n" +
            "    androidTestRunner = \"androidx.test:runner:\${androidTestRunnerV}\"\n" +
            "    espresso = \"androidx.test.espresso:espresso-core:\${espressoV}\"\n" +
            "\n" +
            "    //ImageProcessing\n" +
            "    gpuImage = \"jp.co.cyberagent.android:gpuimage:\${gpuImageV}\"\n" +
            "    exifinterface = \"androidx.exifinterface:exifinterface:\${exifinterfaceV}\"\n" +
            "    compressor = \"id.zelory:compressor:\${compressorV}\"\n" +
            "\n" +
            "    //Camera\n" +
            "    cameraView = \"com.otaliastudios:cameraview:\${cameraViewV}\"\n" +
            "\n" +
            "    //Billings\n" +
            "    billing = \"com.android.billingclient:billing:\${billingV}\"\n" +
            "\n" +
            "    skeleton = \"com.ethanhua:skeleton:\${skeletonV}\"\n" +
            "    shimmerlayout = \"io.supercharge:shimmerlayout:\${shimmerlayoutV}\"\n" +
            "\n" +
            "    exoplayerCore = \"com.google.android.exoplayer:exoplayer-core:\${exoplayerCoreV}\"\n" +
            "    exoplayerDash = \"com.google.android.exoplayer:exoplayer-dash:\${exoplayerDashV}\"\n" +
            "    exoplayerUi = \"com.google.android.exoplayer:exoplayer-ui:\${exoplayerUiV}\"\n" +
            "\n" +
            "    dashedCircularProgress = \"com.github.jakob-grabner:Circle-Progress-View:\${dashedCircularProgressV}\"\n" +
            "\n" +
            "    customTabs = \"com.android.support:customtabs:\${customTabsV}\"\n" +
            "\n" +
            "}"
    )

    class AppBuildGradle(appName:String, packageName: String) : FileType(
        "build.gradle",
        "apply plugin: 'com.android.application'\n" +
                "apply plugin: 'kotlin-android'\n" +
                "apply plugin: 'kotlin-android-extensions'\n" +
                "apply plugin: 'kotlin-kapt'\n" +
                "apply from: '../dependencies.gradle'\n" +
                "\n" +
                "android {\n" +
                "    compileSdkVersion compileSdk\n" +
                "    defaultConfig {\n" +
                "        applicationId \"$packageName\"\n" +
                "        minSdkVersion minSdk\n" +
                "        targetSdkVersion targetSdk\n" +
                "        versionCode generateVersionCode()\n" +
                "        versionName generateVersionName()\n" +
                "        testInstrumentationRunner \"androidx.test.runner.AndroidJUnitRunner\"\n" +
                "    }\n" +
                "\n" +
                "   applicationVariants.all { variant ->\n" +
                "        variant.outputs.all { output ->\n" +
                "            def date = new Date().format(\"dd-MM-yy\", TimeZone.getTimeZone(\"UTC\"))\n" +
                "            outputFileName = \"$appName\" +\n" +
                "                    \"_v\${defaultConfig.versionName}\" +\n" +
                "                    \"_\${date}\" +\n" +
                "                    \".apk\"\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "  flavorDimensions \"default\"\n" +
                "\n" +
                "    productFlavors {\n" +
                "        stage {\n" +
                "            dimension \"default\"\n" +
                "            buildConfigField \"String\", \"BASE_URL\", '\"url\"'\n" +
                "        }\n" +
                "\n" +
                "        prod {\n" +
                "            dimension \"default\"\n" +
                "            buildConfigField \"String\", \"BASE_URL\", '\"url\"'\n" +
                "        }\n" +
                "    }" +
                "\n" +
                "    buildTypes {\n" +
                "        debug {\n" +
                "            minifyEnabled false\n" +
                "        }\n" +
                "\n" +
                "        release {\n" +
                "            minifyEnabled true\n" +
                "            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'),\n" +
                "                    'proguard-rules.pro'\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    dataBinding {\n" +
                "        enabled = true\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "private Integer generateVersionCode() {\n" +
                "    return minSdk * 10000000 +\n" +
                "            versionMajor * 10000 +\n" +
                "            versionMinor * 100 +\n" +
                "            versionPatch\n" +
                "}\n" +
                "\n" +
                "private String generateVersionName() {\n" +
                "    String versionName = \"\${versionMajor}.\${versionMinor}.\${versionPatch}\"\n" +
                "    if (versionClassifier != null && !versionClassifier.isEmpty()) versionName += \"-\" + ext.versionClassifier\n" +
                "    return versionName\n" +
                "}\n" +
                "\n" +
               "dependencies {\n" +
                "\n" +
                "    implementation fileTree(dir: 'libs', include: ['*.jar'])\n" +
                "\n" +
                "    implementation project(':repository')\n" +
                "\n" +
                "    implementation \"org.jetbrains.kotlin:kotlin-stdlib-jdk7:\${kotlinV}\"\n" +
                "    implementation \"androidx.core:core-ktx:\${androidKtxV}\"\n" +
                "\n" +
                "    implementation \"androidx.constraintlayout:constraintlayout:\${constraintLayoutV}\"\n" +
                "    implementation \"androidx.appcompat:appcompat:\${androidXV}\"\n" +
                "    implementation \"com.google.android.material:material:\${androidXV}\"\n" +
                "\n" +
                "    implementation \"io.reactivex.rxjava2:rxjava:\${rxJavaV}\"\n" +
                "    implementation \"io.reactivex.rxjava2:rxkotlin:\${rxKotlinV}\"\n" +
                "    implementation \"io.reactivex.rxjava2:rxandroid:\${rxAndroidV}\"\n" +
                "    implementation \"com.jakewharton.rxrelay2:rxrelay:\${rxRelayV}\"\n" +
                "    implementation \"com.jakewharton.rxbinding2:rxbinding-kotlin:\${rxBindingV}\"\n" +
                "    implementation \"com.jakewharton.rxbinding2:rxbinding-design-kotlin:\${rxBindingV}\"\n" +
                "    implementation \"com.jakewharton.rxbinding2:rxbinding-appcompat-v7:\${rxBindingV}\"\n" +
                "\n" +
                "    implementation \"androidx.navigation:navigation-fragment-ktx:\${navigationV}\"\n" +
                "    implementation \"androidx.navigation:navigation-ui-ktx:\${navigationV}\"\n" +
                "    implementation \"androidx.lifecycle:lifecycle-viewmodel-ktx:\${viewmodelKTXV}\"\n" +
                "    implementation \"androidx.lifecycle:lifecycle-reactivestreams-ktx:\${lifecycleReactiveStreamsV}\"\n" +
                "    implementation \"androidx.lifecycle:lifecycle-extensions:\${lifecycleV}\"\n" +
                "\n" +
                "    implementation \"org.koin:koin-core:\${koinV}\"\n" +
                "    implementation \"org.koin:koin-android:\${koinV}\"\n" +
                "    implementation \"org.koin:koin-androidx-scope:\${koinV}\"\n" +
                "    implementation \"org.koin:koin-androidx-viewmodel:\${koinV}\"\n" +
                "\n" +
                "    testImplementation \"junit:junit:\${jUnitV}\"\n" +
                "    androidTestImplementation \"androidx.test:runner:\${androidTestRunnerV}\"\n" +
                "    androidTestImplementation \"androidx.test.espresso:espresso-core:\${espressoV}\"\n" +
                "}"
    )

    class RepoBuildGradle : FileType(
        "build.gradle",
        "apply plugin: 'com.android.library'\n" +
                "apply plugin: 'kotlin-android-extensions'\n" +
                "apply plugin: 'kotlin-android'\n" +
                "apply plugin: \"kotlin-kapt\"\n" +
                "apply from: '../dependencies.gradle'\n" +
                "\n" +
                "repositories {\n" +
                "    mavenCentral()\n" +
                "}\n" +
                "\n" +
                "android {\n" +
                "    compileSdkVersion compileSdk\n" +
                "    defaultConfig {\n" +
                "        minSdkVersion minSdk\n" +
                "        targetSdkVersion targetSdk\n" +
                "        versionCode 1\n" +
                "        versionName \"1.0\"\n" +
                "\n" +
                "        testInstrumentationRunner \"android.support.test.runner.AndroidJUnitRunner\"\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    buildTypes {\n" +
                "        debug {\n" +
                "            minifyEnabled false\n" +
                "        }\n" +
                "\n" +
                "        release {\n" +
                "            minifyEnabled true\n" +
                "            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "flavorDimensions \"default\"\n" +
                "\n" +
                "    productFlavors {\n" +
                "        stage {\n" +
                "            dimension \"default\"\n" +
                "            buildConfigField \"String\", \"BASE_URL\", '\"HERE_MUST_BE_YOUR_URL\"'\n" +
                "            buildConfigField \"String\", \"API_KEY\", '\"HERE_MUST_BE_YOUR_API_KEY\"'\n" +
                "        }\n" +
                "\n" +
                "        prod {\n" +
                "            dimension \"default\"\n" +
                "            buildConfigField \"String\", \"BASE_URL\", '\"HERE_MUST_BE_YOUR_URL\"'\n" +
                "            buildConfigField \"String\", \"API_KEY\", '\"HERE_MUST_BE_YOUR_API_KEY\"'\n" +
                "        }\n" +
                "    }" +
                "}\n" +
                "\n" +
                "dependencies {\n" +
                "    implementation fileTree(dir: 'libs', include: ['*.jar'])\n" +
                "\n" +
                "    implementation \"androidx.appcompat:appcompat:\${androidXV}\"\n" +
                "    implementation \"org.jetbrains.kotlin:kotlin-stdlib-jdk7:\${kotlinV}\"\n" +
                "\n" +
                "    implementation \"androidx.lifecycle:lifecycle-extensions:\${lifecycleV}\"\n" +
                "\n" +
                "    implementation \"io.reactivex.rxjava2:rxjava:\${rxJavaV}\"\n" +
                "    implementation \"com.jakewharton.rxrelay2:rxrelay:\${rxRelayV}\"\n" +
                "    implementation \"io.reactivex.rxjava2:rxandroid:\${rxAndroidV}\"\n" +
                "\n" +
                "    implementation \"com.squareup.okhttp3:okhttp:\${okHttpV}\"\n" +
                "    implementation \"com.squareup.okhttp3:logging-interceptor:\${okHttpV}\"\n" +
                "\n" +
                "    implementation \"com.squareup.retrofit2:retrofit:\${retrofitV}\"\n" +
                "    implementation \"com.squareup.retrofit2:adapter-rxjava2:\${retrofitV}\"\n" +
                "    implementation  \"com.github.aurae.retrofit2:converter-logansquare:\${retrofitConverterLoganSquareV}\"\n" +
                "\n" +
                "    implementation \"com.bluelinelabs:logansquare:\${loganSquareV}\"\n" +
                "    kapt \"com.bluelinelabs:logansquare-compiler:\${loganSquareV}\"\n" +
                "    implementation \"com.fasterxml.jackson.core:jackson-core:\${loganSquareV}\"\n" +
                "\n" +
                "    implementation \"androidx.room:room-runtime:\${roomV}\"\n" +
                "    implementation \"androidx.room:room-rxjava2:\${roomV}\"\n" +
                "    kapt \"androidx.room:room-compiler:\${roomV}\"\n" +
                "\n" +
                "    debugImplementation \"com.amitshekhar.android:debug-db:\${debugDbV}\"\n" +
                "\n" +
                "    implementation \"org.koin:koin-core:\${koinV}\"\n" +
                "    implementation \"org.koin:koin-android:\${koinV}\"\n" +
                "    implementation \"org.koin:koin-androidx-scope:\${koinV}\"\n" +
                "    implementation \"org.koin:koin-androidx-viewmodel:\${koinV}\"\n" +
                "\n" +
                "    implementation \"com.github.bumptech.glide:glide:\${glideV}\"\n" +
                "    kapt \"com.github.bumptech.glide:compiler:\${glideV}\"\n" +
                "\n" +
                "    testImplementation \"junit:junit:\${jUnitV}\"\n" +
                "    androidTestImplementation \"androidx.test:runner:\${androidTestRunnerV}\"\n" +
                "    androidTestImplementation \"androidx.test.espresso:espresso-core:\${espressoV}\"\n" +
                "}"

    )
}