package data

sealed class FileType(val fileName: String, val content: String) {


    class Navigator(packageName: String) : FileType(
        "KeepStateNavigator.kt",
        "package $packageName.navigation\n" +
                "\n" +
                "import android.content.Context\n" +
                "import android.os.Bundle\n" +
                "import androidx.fragment.app.FragmentManager\n" +
                "import androidx.navigation.NavDestination\n" +
                "import androidx.navigation.NavOptions\n" +
                "import androidx.navigation.Navigator\n" +
                "import androidx.navigation.fragment.FragmentNavigator\n" +
                "\n" +
                "@Navigator.Name(\"keep_state_fragment\") // `keep_state_fragment` is used in navigation xml\n" +
                "class KeepStateNavigator(\n" +
                "  private val context: Context,\n" +
                "  private val manager: FragmentManager, // Should pass childFragmentManager.\n" +
                "  private val containerId: Int\n" +
                ") : FragmentNavigator(context, manager, containerId) {\n" +
                "\n" +
                "  override fun navigate(\n" +
                "    destination: Destination,\n" +
                "    args: Bundle?,\n" +
                "    navOptions: NavOptions?,\n" +
                "    navigatorExtras: Navigator.Extras?\n" +
                "  ): NavDestination? {\n" +
                "    val tag = destination.id.toString()\n" +
                "    val transaction = manager.beginTransaction()\n" +
                "\n" +
                "    val currentFragment = manager.primaryNavigationFragment\n" +
                "    var initialNavigate = false\n" +
                "    if (currentFragment != null) {\n" +
                "      transaction.detach(currentFragment)\n" +
                "    } else {\n" +
                "      initialNavigate = true\n" +
                "    }\n" +
                "\n" +
                "    var fragment = manager.findFragmentByTag(tag)\n" +
                "    if (fragment == null) {\n" +
                "      val className = destination.className\n" +
                "      fragment = instantiateFragment(context, manager, className, args)\n" +
                "      transaction.add(containerId, fragment, tag)\n" +
                "    } else {\n" +
                "      transaction.attach(fragment)\n" +
                "    }\n" +
                "\n" +
                "    transaction.setPrimaryNavigationFragment(fragment)\n" +
                "    transaction.setReorderingAllowed(true)\n" +
                "    transaction.commit()\n" +
                "\n" +
                "    return if (initialNavigate) {\n" +
                "      // If always return null, selected BottomNavigation item is not same as app:startDestination in first time.\n" +
                "      destination\n" +
                "    } else {\n" +
                "      null\n" +
                "    }\n" +
                "  }\n" +
                "}"
    )

    class MainModule(packageName: String) : FileType(
        "MainModule.kt",
        "package $packageName.di\n" +
                "\n" +
                "import $packageName.utils.common.ImageBindingAdapter\n" +
                "import org.koin.dsl.module.module\n" +
                "import org.koin.standalone.KoinComponent\n" +
                "import org.koin.standalone.inject\n" +
                "\n" +
                "val mainModule = module {\n" +
                "\n" +
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
                "import androidx.databinding.DataBindingUtil\n" +
                "import androidx.fragment.app.Fragment\n" +
                "\n" +
                "import androidx.lifecycle.Observer\n" +
                "import $userPackage.R\n" +
                "import $userPackage.databinding.Fragment${fileName}Binding\n" +
                "import $packageName.${fileName}StateIntent.GetSampleData\n" +
                "import $packageName.${fileName}ViewModel\n"+
                "import $userPackage.utils.common.BaseView\n" +
                "import io.reactivex.Observable\n" +
                "import io.reactivex.disposables.CompositeDisposable\n" +
                "import io.reactivex.disposables.Disposable\n" +
                "import org.koin.androidx.viewmodel.ext.android.viewModel\n" +
                "\n" +
                "class ${fileName}Fragment : Fragment(), BaseView<${fileName}State> {\n" +
                "\n" +
                "  private var viewSubscriptions: Disposable? = null\n" +
                "  private var compositeDisposable: CompositeDisposable? = null\n" +
                "  private var binding: Fragment${fileName}Binding? = null\n" +
                "  private val vm${fileName}Screen: ${fileName}ViewModel by viewModel()\n" +
                "\n" +
                "  override fun onCreate(savedInstanceState: Bundle?) {\n" +
                "    super.onCreate(savedInstanceState)\n" +
                "    handleStates()\n" +
                "  }\n" +
                "\n" +
                "  override fun onCreateView(\n" +
                "    inflater: LayoutInflater,\n" +
                "    container: ViewGroup?,\n" +
                "    savedInstanceState: Bundle?\n" +
                "  ): View? {\n" +
                "\n" +
                "    initBinding(inflater, container)\n" +
                "    initIntents()\n" +
                "    return binding!!.root\n" +
                "  }\n" +
                "\n" +
                "  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {\n" +
                "    super.onViewCreated(view, savedInstanceState)\n" +
                "  }\n" +
                "\n" +
                "  override fun onDestroyView() {\n" +
                "    super.onDestroyView()\n" +
                "    compositeDisposable?.dispose()\n" +
                "    viewSubscriptions?.dispose()\n" +
                "    binding = null\n" +
                "  }\n" +
                "\n" +
                "  private fun initBinding(\n" +
                "    inflater: LayoutInflater,\n" +
                "    container: ViewGroup?\n" +
                "  ) {\n" +
                "    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_${fileName.toLowerCase()}, container, false)\n" +
                "  }\n" +
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
                "    binding!!.viewState = state\n" +
                "  }\n" +
                "}"
    )

    class ViewModel(fileName: String, packageName: String, userPackage: String) : FileType(
        "${fileName}ViewModel.kt",
        "package $packageName\n" +
                "\n" +
                "import $userPackage.utils.common.BaseViewModel\n" +
                "import io.reactivex.Observable\n" +
                "\n" +
                "class ${fileName}ViewModel() : BaseViewModel<${fileName}State>() {\n" +
                "\n" +
                "  override fun initState(): ${fileName}State = ${fileName}State()\n" +
                "\n" +
                "  override fun viewIntents(intentStream: Observable<*>): Observable<Any> {\n" +
                "    TODO(\"not implemented\") //To change body of created functions use File | Settings | File Templates.\n" +
                "  }\n" +
                "\n" +
                "  override fun reduceState(previousState: ${fileName}State, stateChange: Any): ${fileName}State {\n" +
                "    TODO(\"not implemented\") //To change body of created functions use File | Settings | File Templates.\n" +
                "  }\n" +
                "}"
    )

    class State(fileName: String, packageName: String) : FileType(
        "${fileName}State.kt",
        "package $packageName\n" +
                "\n" +
                "class ${fileName}State(\n" +
                "  val loading: Boolean = false,\n" +
                "  val error: Throwable? = null\n" +
                ")\n" +
                "\n" +
                "sealed class ${fileName}StateIntent {\n" +
                "  object GetSampleData : ${fileName}StateIntent()\n" +
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
                "  fun insert(cat: EntityL): Long\n" +
                "\n" +
                "  @Insert(onConflict = OnConflictStrategy.REPLACE)\n" +
                "  fun insertAll(catList: List<EntityL>): List<Long>\n" +
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
                "import io.reactivex.Completable\n" +
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
//                    "import com.gis.repoimpl.data.remote.entity.CatR\n" +
                "import $packageName.domain.datasource.EntityDataSource\n" +
                "import $packageName.domain.entity.Entity\n" +
                "import io.reactivex.Completable\n" +
                "import io.reactivex.Observable\n" +
                "\n" +
                "class EntityRemoteSource(private val api: ${projectName}Api) : EntityDataSource {\n" +
                "\n" +
                "  override fun observeEntities(): Observable<List<Entity>> {\n" +
                "    TODO(\"not implemented\") //To change body of created functions use File | Settings | File Templates.\n" +
                "  }\n" +

//                    "  override fun getNextCatsPage(page: Int, limit: Int): Observable<List<Cat>> =\n" +
//                    "    api.getCats(page, limit)\n" +
//                    "      .map { list -> if (list.isNotEmpty()) list.map { it.toDomain() } else emptyList() }\n" +
//                    "\n" +
//                    "  override fun findByName(name: String): Observable<List<Cat>> {\n" +
//                    "    TODO(\"not implemented\") //To change body of created functions use File | Settings | File Templates.\n" +
//                    "  }\n" +
//                    "\n" +
//                    "  override fun insertCats(cats: List<Cat>): Completable =\n" +
//                    "    Completable.fromAction { val i = 0 }\n" +
//                    "\n" +
//                    "  private fun CatR.toDomain() =\n" +
//                    "    Cat(\n" +
//                    "      id = id ?: \"\",\n" +
//                    "      url = url ?: \"\"\n" +
//                    "    )\n" +
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
                "import io.reactivex.Completable\n" +
                "import io.reactivex.Observable\n" +
                "\n" +
                "class EntityLocalSource(private val entityDao: EntityDao) : EntityDataSource {\n" +
                "\n" +
//                    "  override fun observeEntities(): Observable<List<Entity>> {\n" +
//                    "    TODO(\"not implemented\") //To change body of created functions use File | Settings | File Templates.\n" +
//                    "  }\n" +
//                    "class CatsLocalSource(private val catsDao: CatsDao) :\n" +
//                    "  CatsDataSource {\n" +
//                    "\n" +
                "  override fun observeEntities(): Observable<List<Entity>> =\n" +
                "    entityDao.getAll()\n" +
                "      .map { if (it.isEmpty()) emptyList() else it.map { item -> item.toDomain() } }\n" +
                "      .toObservable()\n" +
//                    "\n" +
//                    "  override fun getNextCatsPage(page: Int, limit: Int): Observable<List<Cat>> =\n" +
//                    "    throw UnsupportedOperationException(\"Local data source doesn't support getNextCatsPage()\")\n" +
//                    "\n" +
//                    "  override fun findByName(name: String): Observable<List<Cat>> =\n" +
//                    "    catsDao.findById(name)\n" +
//                    "      .map { if (it.isEmpty()) emptyList() else it.map { item -> item.toDomain() } }\n" +
//                    "      .toObservable()\n" +
//                    "\n" +
//                    "  override fun insertCats(cats: List<Cat>): Completable = Completable.fromAction {\n" +
//                    "    catsDao.insertAll(cats.map { it.toLocal() })\n" +
//                    "  }\n" +
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
                "import $packageName.domain.datasource.EntityDataSource\n" +
                "import $packageName.domain.interactors.*\n" +
                "import $packageName.domain.repository.EntitiesRepository\n" +
                "import org.koin.android.ext.koin.androidContext\n" +
                "import org.koin.dsl.module.module\n" +
                "\n" +
                "val repoModule = module {\n" +
                "\n" +
                "  single { ${projectName}DbProvider.createDb(androidContext()).entityDao() }\n" +
                "\n" +
                "  single { ${projectName}ApiProvider.createApi() }\n" +
                "\n" +
                "  single { androidContext().getSharedPreferences(\"sharedPrefs\", MODE_PRIVATE) }" +
                "\n" +
                "  single<EntityDataSource>(\"local\") { EntityLocalSource(get()) }\n" +
                "\n" +
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
                "\n" +
                "  companion object {\n" +
                "    fun createDb(context: Context): ${projectName}Db {\n" +
                "      return Room.databaseBuilder(context, ${projectName}Db::class.java, \"${projectName}Database\")\n" +
                "        .build()\n" +
                "    }\n" +
                "  }\n" +
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

    class Layout(fileName: String) : FileType(
        "fragment_${fileName.toLowerCase()}.xml",
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<layout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    xmlns:app=\"http://schemas.android.com/apk/res-auto\"\n" +
                "    xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                "    >\n" +
                "\n" +
                "<androidx.constraintlayout.widget.ConstraintLayout\n" +
                "        xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "        xmlns:tools=\"http://schemas.android.com/tools\"\n" +
                "        xmlns:utils=\"http://schemas.android.com/apk/res-auto\"\n" +
                "        android:layout_width=\"match_parent\"\n" +
                "        android:layout_height=\"match_parent\"\n" +
                "        >\n" +
                "\n" +
                "    <TextView\n" +
                "            android:layout_width=\"wrap_content\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:text=\"Hello World!\"\n" +
                "            utils:layout_constraintBottom_toBottomOf=\"parent\"\n" +
                "            utils:layout_constraintLeft_toLeftOf=\"parent\"\n" +
                "            utils:layout_constraintRight_toRightOf=\"parent\"\n" +
                "            utils:layout_constraintTop_toTopOf=\"parent\"/>\n" +
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
                "import $packageName.common.ImageLoader\n" +
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
        "package $packageName.common\n" +
                "\n" +
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
                "import $packageName.common.px\n" +
                "import $packageName.common.ImageLoader\n" +
                "import java.util.ArrayList\n" +
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
        "package $packageName.common\n" +
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
                "import android.animation.Animator\n" +
                "import android.animation.AnimatorListenerAdapter\n" +
                "import android.content.res.Resources\n" +
                "import android.view.State\n" +
                "import android.view.animation.LinearInterpolator\n" +
                "import android.view.animation.OvershootInterpolator\n" +
                "import android.widget.ImageView\n" +
                "import androidx.annotation.DrawableRes\n" +
                "import io.reactivex.Observable\n" +
                "import io.reactivex.android.schedulers.AndroidSchedulers\n" +
                "import io.reactivex.schedulers.Schedulers\n" +
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
                "fun State.touchDownAnimation() {\n" +
                "  this.animate()\n" +
                "    .scaleX(0.88f)\n" +
                "    .scaleY(0.88f)\n" +
                "    .setDuration(300)\n" +
                "    .setInterpolator(OvershootInterpolator())\n" +
                "    .start()\n" +
                "}\n" +
                "\n" +
                "fun State.expandPresetAnimationOut() {\n" +
                "  this.animate()\n" +
                "    .scaleX(0f)\n" +
                "    .alpha(0f)\n" +
                "    .setDuration(200)\n" +
                "    .setListener(object : AnimatorListenerAdapter() {\n" +
                "      override fun onAnimationEnd(animation: Animator?) {\n" +
                "        super.onAnimationEnd(animation)\n" +
                "        this@expandPresetAnimationOut.visibility = State.GONE\n" +
                "      }\n" +
                "    })\n" +
                "    .setInterpolator(LinearInterpolator())\n" +
                "    .start()\n" +
                "}\n" +
                "\n" +
                "fun State.expandPresetsAnimationIn(xPoint: Float, yPoint: Float) {\n" +
                "  with(this) {\n" +
                "    pivotX = xPoint\n" +
                "    pivotY = yPoint\n" +
                "    visibility = State.VISIBLE\n" +
                "    animate()\n" +
                "      .scaleX(1f)\n" +
                "      .alpha(1f)\n" +
                "      .setListener(object : AnimatorListenerAdapter() {\n" +
                "        override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {\n" +
                "          super.onAnimationEnd(animation, isReverse)\n" +
                "          animate().setListener(null)\n" +
                "        }\n" +
                "      })\n" +
                "      .setDuration(200)\n" +
                "      .setInterpolator(LinearInterpolator())\n" +
                "      .start()\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "fun State.touchUpAnimation() {\n" +
                "  this.animate()\n" +
                "    .scaleX(1f)\n" +
                "    .scaleY(1f)\n" +
                "    .setDuration(200)\n" +
                "    .setInterpolator(OvershootInterpolator())\n" +
                "    .start()\n" +
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
                "    .observeOn(AndroidSchedulers.mainThread())"
    )

    class UtilsModule(packageName: String) : FileType(
        "UtilsModule.kt",
        "package $packageName.di\n" +
                "\n" +
                "import $packageName.common.GlideImageLoader\n" +
                "import $packageName.common.ImageLoader\n" +
                "import $packageName.common.ImageBindingAdapter\n" +
                "import org.koin.dsl.module.module\n" +
                " */\n" +
                "val utilsModule = module {\n" +
                "\n" +
                "  single<ImageLoader> { GlideImageLoader }\n" +
                "  single { ImageBindingAdapter(get()) }\n" +
                "}"
    )

    class BaseComponents(packageName: String) : FileType(
        "BaseComponents.kt",
        "package $packageName.common\n" +
                "\n" +
                "import androidx.lifecycle.LiveData\n" +
                "import androidx.lifecycle.MutableLiveData\n" +
                "import androidx.lifecycle.ViewModel\n" +
                "import com.jakewharton.rxrelay2.PublishRelay\n" +
                "import io.reactivex.Observable\n" +
                "import io.reactivex.android.schedulers.AndroidSchedulers\n" +
                "import io.reactivex.disposables.Disposable\n" +
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

    class BuildSrc : FileType(
        "build.gradle.kts",
        "import org.jetbrains.kotlin.gradle.tasks.KotlinCompile\n" +
                "\n" +
                "plugins {\n" +
                "    `kotlin-dsl`\n" +
                "}\n" +
                "dependencies {\n" +
                "    compile(kotlin(\"stdlib-jdk8\"))\n" +
                "}\n" +
                "repositories {\n" +
                "    mavenCentral()\n" +
                "}\n" +
                "val compileKotlin: KotlinCompile by tasks\n" +
                "compileKotlin.kotlinOptions {\n" +
                "    jvmTarget = \"1.8\"\n" +
                "}\n" +
                "val compileTestKotlin: KotlinCompile by tasks\n" +
                "compileTestKotlin.kotlinOptions {\n" +
                "    jvmTarget = \"1.8\"\n" +
                "}"
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


    class UtilsBuildGradle : FileType(
        "build.gradle",
        "apply plugin: 'com.android.library'\n" +
                "apply plugin: 'kotlin-android-extensions'\n" +
                "apply plugin: 'kotlin-android'\n" +
                "apply plugin: \"kotlin-kapt\"\n" +
                "\n" +
                "android {\n" +
                "    compileSdkVersion 28\n" +
                "\n" +
                "\n" +
                "    defaultConfig {\n" +
                "        minSdkVersion 21\n" +
                "        targetSdkVersion 28\n" +
                "        versionCode 1\n" +
                "        versionName \"1.0\"\n" +
                "\n" +
                "        testInstrumentationRunner \"android.support.test.runner.AndroidJUnitRunner\"\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "    buildTypes {\n" +
                "        release {\n" +
                "            minifyEnabled false\n" +
                "            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    dataBinding {\n" +
                "        enabled = true\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "dependencies {\n" +
                "    implementation fileTree(dir: 'libs', include: ['*.jar'])\n" +
                "\n" +
                "    implementation Deps.kotlinStdlib\n" +
                "    implementation Deps.appCompat\n" +
                "\n" +
                "    implementation Deps.rxJava\n" +
                "    implementation Deps.rxRelay\n" +
                "    implementation Deps.rxKotlin\n" +
                "    implementation Deps.rxAndroid\n" +
                "\n" +
                "    implementation Deps.glide\n" +
                "    kapt Deps.glideCompiler\n" +
                "\n" +
                "    implementation Deps.koinCore\n" +
                "    implementation Deps.koinAndroid\n" +
                "    implementation Deps.koinCoreScope\n" +
                "\n" +
                "    testImplementation Deps.jUnit\n" +
                "    androidTestImplementation Deps.androidTestRunner\n" +
                "    androidTestImplementation Deps.espresso\n" +
                "}\n" +
                "repositories {\n" +
                "    mavenCentral()\n" +
                "}\n"
    )

    class AppBuildGradle(packageName: String) : FileType(
        "build.gradle",
        "apply plugin: 'com.android.application'\n" +
                "apply plugin: 'kotlin-android'\n" +
                "apply plugin: 'kotlin-android-extensions'\n" +
                "apply plugin: 'kotlin-kapt'\n" +
                "\n" +
                "android {\n" +
                "    compileSdkVersion 28\n" +
                "    defaultConfig {\n" +
                "        applicationId \"$packageName\"\n" +
                "        minSdkVersion 22\n" +
                "        targetSdkVersion 28\n" +
                "        versionCode 1\n" +
                "        versionName \"0.0.1\"\n" +
                "        testInstrumentationRunner \"androidx.test.runner.AndroidJUnitRunner\"\n" +
                "    }\n" +
                "\n" +
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
                "dependencies {\n" +
                "\n" +
                "    implementation fileTree(dir: 'libs', include: ['*.jar'])\n" +
                "\n" +
                "    implementation project(':repository')\n" +
                "    implementation project(':utils')\n" +
                "\n" +
                "    implementation Deps.kotlinStdlib\n" +
                "    implementation Deps.androidKtx\n" +
                "\n" +
                "    implementation Deps.constraintLayout\n" +
                "    implementation Deps.appCompat\n" +
                "\n" +
                "    implementation Deps.material\n" +
                "\n" +
                "    implementation Deps.rxJava\n" +
                "    implementation Deps.rxKotlin\n" +
                "    implementation Deps.rxAndroid\n" +
                "    implementation Deps.rxRelay\n" +
                "    implementation Deps.rxBindingKotlin\n" +
                "    implementation Deps.rxBindingDesignKotlin\n" +
                "    implementation Deps.rxBindingAppCompatV7Kotlin\n" +
                "\n" +
                "    implementation Deps.navigationFragment\n" +
                "    implementation Deps.navigationUI\n" +
                "\n" +
                "    implementation Deps.lifecycle\n" +
                "\n" +
                "    implementation Deps.koinCore\n" +
                "    implementation Deps.koinAndroid\n" +
                "    implementation Deps.koinCoreScope\n" +
                "    implementation Deps.koinAndroidViewModel\n" +
                "\n" +
                "    testImplementation Deps.jUnit\n" +
                "    androidTestImplementation Deps.androidTestRunner\n" +
                "    androidTestImplementation Deps.espresso\n" +
                "}\n"
    )

    class RepoBuildGradle : FileType(
        "build.gradle",
        "apply plugin: 'com.android.library'\n" +
                "apply plugin: 'kotlin-android-extensions'\n" +
                "apply plugin: 'kotlin-android'\n" +
                "apply plugin: \"kotlin-kapt\"\n" +
                "\n" +
                "android {\n" +
                "    compileSdkVersion 28\n" +
                "\n" +
                "    defaultConfig {\n" +
                "        minSdkVersion 21\n" +
                "        targetSdkVersion 28\n" +
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
                "    implementation Deps.appCompat\n" +
                "    implementation Deps.kotlinStdlib\n" +
                "\n" +
                "    implementation Deps.lifecycle\n" +
                "\n" +
                "    implementation Deps.rxJava\n" +
                "    implementation Deps.rxRelay\n" +
                "    implementation Deps.rxAndroid\n" +
                "\n" +
                "    implementation Deps.okHttp\n" +
                "    implementation Deps.okHttpInteceptor\n" +
                "\n" +
                "    implementation Deps.retrofit\n" +
                "    implementation Deps.retrofitRxJavaAdapter\n" +
                "    implementation Deps.retrofitConverterLoganSquare\n" +
                "\n" +
                "    implementation Deps.loganSquare\n" +
                "    kapt Deps.loganSquareCompiler\n" +
                "    implementation Deps.jacksonCore\n" +
                "\n" +
                "    implementation Deps.room\n" +
                "    implementation Deps.rxRoom\n" +
                "    kapt Deps.roomCompiler\n" +
                "\n" +
                "    debugImplementation Deps.debugDb\n" +
                "\n" +
                "    implementation Deps.koinCore\n" +
                "    implementation Deps.koinAndroid\n" +
                "    implementation Deps.koinCoreScope\n" +
                "    implementation Deps.koinAndroidViewModel\n" +
                "\n" +
                "    testImplementation Deps.jUnit\n" +
                "    androidTestImplementation Deps.androidTestRunner\n" +
                "    androidTestImplementation Deps.espresso\n" +
                "}\n" +
                "repositories {\n" +
                "    mavenCentral()\n" +
                "}\n"
    )

    class Deps : FileType(
        "Deps.kt",
        "import org.gradle.api.JavaVersion\n" +
                "\n" +
                "object General {\n" +
                "  val sourceCompatibility = JavaVersion.VERSION_1_8\n" +
                "  val targetCompatibility = JavaVersion.VERSION_1_8\n" +
                "}\n" +
                "\n" +
                "object Version {\n" +
                "  // project\n" +
                "  const val minSdk = 21\n" +
                "  const val targetSdk = 28\n" +
                "  const val buildToolsVersion = \"28.0.2\"\n" +
                "  const val compileSdkVersion = 28\n" +
                "  const val androidGradle = \"3.4.0-beta03\"\n" +
                "\n" +
                "  // android\n" +
                "  const val androidX = \"1.0.0\"\n" +
                "  const val androidKtx = \"1.0.0-rc02\"   //https://github.com/android/android-ktx\n" +
                "  const val androidLifecycle = \"2.0.0-alpha1\"\n" +
                "  const val multidex = \"2.0.0\"\n" +
                "  const val constraintLayout = \"2.0.0-alpha2\"\n" +
                "\n" +
                "  // kotlin\n" +
                "  const val kotlin = \"1.3.10\" // https://kotlinlang.org/\n" +
                "  const val coroutinesAndroid = \"1.0.1\" // https://kotlinlang.org/\n" +
                "\n" +
                "  //rxExtensions\n" +
                "  const val rxJava = \"2.2.0\"            //https://github.com/ReactiveX/RxJava\n" +
                "  const val rxRelay = \"2.0.0\"           //https://github.com/JakeWharton/RxRelay\n" +
                "  const val rxKotlin = \"2.2.0\"          //https://github.com/ReactiveX/RxKotlin\n" +
                "  const val rxAndroid = \"2.1.0\"         //https://github.com/ReactiveX/RxAndroid\n" +
                "  const val rxBinding = \"2.1.1\"         //https://github.com/JakeWharton/RxBinding\n" +
                "  const val rxBinding3 = \"3.0.0-alpha1\"         //https://github.com/JakeWharton/RxBinding\n" +
                "  const val rxNetwork = \"2.1.0\"         //https://github.com/pwittchen/ReactiveNetwork\n" +
                "  const val rxPermissions = \"0.10.2\"    //https://github.com/tbruyelle/RxPermissions\n" +
                "\n" +
                "  // dependency injection\n" +
                "  const val dagger = \"2.17\" //https://github.com/google/dagger\n" +
                "  const val koin = \"1.0.1\"  //https://github.com/InsertKoinIO/koin\n" +
                "\n" +
                "  // general\n" +
                "  const val mosby = \"3.1.0\" //https://github.com/sockeqwe/mosby\n" +
                "\n" +
                "  // network\n" +
                "  const val okHttp = \"3.11.0\" //https://github.com/square/okhttp\n" +
                "\n" +
                "  // serialization\n" +
                "  const val gson = \"2.8.5\"        //https://github.com/google/gson\n" +
                "  const val loganSquare = \"1.3.7\" //https://github.com/bluelinelabs/LoganSquare\n" +
                "  const val jacksonCore = \"2.9.7\" //https://github.com/bluelinelabs/LoganSquare\n" +
                "\n" +
                "  // retrofit\n" +
                "  const val retrofit = \"2.4.0\"                     //https://github.com/square/retrofit\n" +
                "  const val retrofitConverterGson =\n" +
                "    \"2.4.0\"        //https://github.com/square/retrofit/tree/master/retrofit-converters/gson\n" +
                "  const val retrofitConverterLoganSquare = \"1.4.1\" //https://github.com/mannodermaus/retrofit-logansquare\n" +
                "\n" +
                "  // apollo\n" +
                "  const val apollo = \"1.0.0-alpha\"      // https://github.com/apollographql/apollo-android\n" +
                "\n" +
                "  //persistence\n" +
                "  const val room = \"2.0.0-rc01\" //https://developer.android.com/topic/libraries/architecture/room\n" +
                "\n" +
                "  //lifecycle\n" +
                "  const val lifecycle = \"2.0.0-rc01\" //https://developer.android.com/topic/libraries/architecture/lifecycle\n" +
                "\n" +
                "  //navigation\n" +
                "  const val navigation = \"1.0.0-alpha11\" //https://developer.android.com/topic/libraries/architecture/navigation\n" +
                "\n" +
                "  //imageLoading\n" +
                "  const val glide = \"4.8.0\"  //https://github.comalpha07/bumptech/glide\n" +
                "  const val picasso = \"2.71828\"       //https://github.com/square/picasso\n" +
                "\n" +
                "  //profiling\n" +
                "  const val debugDb = \"1.0.4\"    //https://github.com/amitshekhariitbhu/Android-Debug-Database\n" +
                "  const val leakCanary = \"1.6.2\" //https://github.com/square/leakcanary\n" +
                "\n" +
                "  //social auth\n" +
                "  const val facebookAnalytics = \"[4,5)\"\n" +
                "  const val crashlytics = \"2.9.4@aar\"\n" +
                "  const val fabricPlugin = \"1.+\"\n" +
                "  const val simpleAuth = \"2.1.3\"  //https://github.com/jaychang0917/SimpleAuth\n" +
                "\n" +
                "  // firebase\n" +
                "  const val firebaseCore = \"16.0.1\"\n" +
                "  const val firebaseMessaging = \"17.1.0\"\n" +
                "\n" +
                "  //Google\n" +
                "  const val googleServices = \"4.0.1\"\n" +
                "  const val googleAuth = \"16.0.0\"\n" +
                "  const val googleMaps = \"16.0.0\"\n" +
                "  const val googleLocation = \"16.0.0\"\n" +
                "\n" +
                "  // tests\n" +
                "  const val jUnit = \"4.12\"\n" +
                "  const val androidTestRunner = \"1.0.0-rc01\"\n" +
                "  const val espresso = \"3.1.0-alpha4\"\n" +
                "  const val mockito = \"2.22.0\"\n" +
                "  const val mockitoAndroid = \"2.22.0\"\n" +
                "  const val mockitoKotlin = \"2.0.0-RC1\"\n" +
                "\n" +
                "  //ImageProcessing\n" +
                "  const val gpuImage = \"2.0.3\"\n" +
                "  const val exifinterface = \"1.0.0\"\n" +
                "  const val compressor = \"2.1.0\"\n" +
                "\n" +
                "  //Camera\n" +
                "  const val cameraView = \"2.0.0-beta03\"\n" +
                "  //Billing\n" +
                "  const val billing = \"1.2.1\"\n" +
                "  const val skeleton = \"1.1.2\"\n" +
                "  const val shimmerlayout = \"2.1.0\"\n" +
                "}\n" +
                "\n" +
                "object Deps {\n" +
                "\n" +
                "  // Android\n" +
                "  const val androidCore = \"androidx.core:core:\${Version.androidX}\"\n" +
                "  const val androidAnnotation = \"androidx.annotation:annotation:\${Version.androidX}\"\n" +
                "  const val androidLifecycle = \"androidx.lifecycle:lifecycle-runtime:\${Version.androidLifecycle}\"\n" +
                "  const val appCompat = \"androidx.appcompat:appcompat:\${Version.androidX}\"\n" +
                "  const val material = \"com.google.android.material:material:\${Version.androidX}\"\n" +
                "  const val palette = \"androidx.palette:palette:\${Version.androidX}\"\n" +
                "  const val constraintLayout = \"androidx.constraintlayout:constraintlayout:\${Version.constraintLayout}\"\n" +
                "  const val cardview = \"androidx.cardview:cardview:\${Version.androidX}\"\n" +
                "  const val multidex = \"androidx.multidex:multidex:\${Version.multidex}\"\n" +
                "  const val animatedVector = \"androidx.vectordrawable:vectordrawable-animated:\${Version.androidX}\"\n" +
                "\n" +
                "  const val androidKtx = \"androidx.core:core-ktx:\${Version.androidKtx}\"\n" +
                "  const val paletteKtx = \"androidx.palette:palette-ktx:\${Version.androidKtx}\"\n" +
                "  const val fragmentKtx = \"androidx.fragment:fragment-ktx:\${Version.androidKtx}\"\n" +
                "  const val collectionKtx = \"androidx.collection:collection-ktx:\${Version.androidKtx}\"\n" +
                "\n" +
                "  // Kotlin\n" +
                "  const val kotlinStdlib = \"org.jetbrains.kotlin:kotlin-stdlib-jdk7:\${Version.kotlin}\"\n" +
                "\n" +
                "  // Coroutines\n" +
                "  const val coroutines = \"org.jetbrains.kotlinx:kotlinx-coroutines-core:\${Version.coroutinesAndroid}\"\n" +
                "  const val coroutinesAndroid = \"org.jetbrains.kotlinx:kotlinx-coroutines-android:\${Version.coroutinesAndroid}\"\n" +
                "\n" +
                "  // RxExtension\n" +
                "  const val rxJava = \"io.reactivex.rxjava2:rxjava:\${Version.rxJava}\"\n" +
                "  const val rxRelay = \"com.jakewharton.rxrelay2:rxrelay:\${Version.rxRelay}\"\n" +
                "  const val rxKotlin = \"io.reactivex.rxjava2:rxkotlin:\${Version.rxKotlin}\"\n" +
                "  const val rxAndroid = \"io.reactivex.rxjava2:rxandroid:\${Version.rxAndroid}\"\n" +
                "\n" +
                "  const val rxBinding = \"com.jakewharton.rxbinding3:rxbinding:\${Version.rxBinding3}\"\n" +
                "  const val rxBindingCore = \"com.jakewharton.rxbinding3:rxbinding-core:\${Version.rxBinding3}\"\n" +
                "  const val rxBindingAppCompat = \"com.jakewharton.rxbinding3:rxbinding-appcompat:\${Version.rxBinding3}\"\n" +
                "  const val rxBindingDrawer = \"com.jakewharton.rxbinding3:rxbinding-drawerlayout:\${Version.rxBinding3}\"\n" +
                "  const val rxBindingLeanBack = \"com.jakewharton.rxbinding3:rxbinding-leanback:\${Version.rxBinding3}\"\n" +
                "  const val rxBindingRecyclerView = \"com.jakewharton.rxbinding3:rxbinding-recyclerview:\${Version.rxBinding3}\"\n" +
                "  const val rxBindingSlidingPanel = \"com.jakewharton.rxbinding3:rxbinding-slidingpanelayout:\${Version.rxBinding3}\"\n" +
                "  const val rxBindingSwipeRefresh = \"com.jakewharton.rxbinding3:rxbinding-swiperefreshlayout:\${Version.rxBinding3}\"\n" +
                "  const val rxBindingViewPager = \"com.jakewharton.rxbinding3:rxbinding-viewpager:\${Version.rxBinding3}\"\n" +
                "  const val rxBindingDesign = \"com.jakewharton.rxbinding3:rxbinding-material:\${Version.rxBinding3}\"\n" +
                "\n" +
                "  const val rxNetwork = \"com.github.pwittchen:reactivenetwork-rx2:\${Version.rxNetwork}\"\n" +
                "  const val rxPermissions = \"com.github.tbruyelle:rxpermissions:\${Version.rxPermissions}\"\n" +
                "  const val rxBindingKotlin = \"com.jakewharton.rxbinding2:rxbinding-kotlin:\${Version.rxBinding}\"\n" +
                "  const val rxBindingDesignKotlin = \"com.jakewharton.rxbinding2:rxbinding-design-kotlin:\${Version.rxBinding}\"\n" +
                "  const val rxBindingAppCompatV4Kotlin = \"com.jakewharton.rxbinding2:rxbinding-support-v4-kotlin:\${Version.rxBinding}\"\n" +
                "  const val rxBindingAppCompatV7Kotlin = \"com.jakewharton.rxbinding2:rxbinding-appcompat-v7:\${Version.rxBinding}\"\n" +
                "  const val rxBindingRecyclerViewV7Kotlin =\n" +
                "    \"com.jakewharton.rxbinding2:rxbinding-recyclerview-v7-kotlin:\${Version.rxBinding}\"\n" +
                "\n" +
                "  // Dependency Injection\n" +
                "  const val inject = \"javax.inject:javax.inject:1\"\n" +
                "  const val dagger = \"com.google.dagger:dagger:\${Version.dagger}\"\n" +
                "  const val daggerCompiler = \"com.google.dagger:dagger-compiler:\${Version.dagger}\"\n" +
                "  const val koinCore = \"org.koin:koin-core:\${Version.koin}\"\n" +
                "  const val koinAndroid = \"org.koin:koin-android:\${Version.koin}\"\n" +
                "  const val koinCoreScope = \"org.koin:koin-androidx-scope:\${Version.koin}\"\n" +
                "  const val koinAndroidViewModel = \"org.koin:koin-androidx-viewmodel:\${Version.koin}\"\n" +
                "  const val koinTests = \"org.koin:koin-test:\${Version.koin}\"\n" +
                "\n" +
                "  // General\n" +
                "  const val mosby = \"com.hannesdorfmann.mosby3:mvi:\${Version.mosby}\"\n" +
                "\n" +
                "  // Network\n" +
                "  const val okHttp = \"com.squareup.okhttp3:okhttp:\${Version.okHttp}\"\n" +
                "  const val okHttpInteceptor = \"com.squareup.okhttp3:logging-interceptor:\${Version.okHttp}\"\n" +
                "\n" +
                "  // Retrofit\n" +
                "  const val retrofit = \"com.squareup.retrofit2:retrofit:\${Version.retrofit}\"\n" +
                "  const val retrofitRxJavaAdapter = \"com.squareup.retrofit2:adapter-rxjava2:\${Version.retrofit}\"\n" +
                "  const val retrofitConverterGson = \"com.squareup.retrofit2:converter-gson:\${Version.retrofitConverterGson}\"\n" +
                "  const val retrofitConverterLoganSquare =\n" +
                "    \"com.github.aurae.retrofit2:converter-logansquare:\${Version.retrofitConverterLoganSquare}\"\n" +
                "\n" +
                "  // Apollo\n" +
                "  const val apollo = \"com.apollographql.apollo:apollo-runtime:\${Version.apollo}\"\n" +
                "  const val apolloAndroidSupport = \"com.apollographql.apollo:apollo-android-support:\${Version.apollo}\"\n" +
                "  const val apolloRxJavaSupport = \"com.apollographql.apollo:apollo-rx2-support:\${Version.apollo}\"\n" +
                "\n" +
                "  // Serialization\n" +
                "  const val gson = \"com.google.code.gson:gson:\${Version.gson}\"\n" +
                "  const val loganSquare = \"com.bluelinelabs:logansquare:\${Version.loganSquare}\"\n" +
                "  const val loganSquareCompiler = \"com.bluelinelabs:logansquare-compiler:\${Version.loganSquare}\"\n" +
                "  const val jacksonCore = \"com.fasterxml.jackson.core:jackson-core:\${Version.loganSquare}\"\n" +
                "\n" +
                "  // Persistence\n" +
                "  const val room = \"androidx.room:room-runtime:\${Version.room}\"\n" +
                "  const val roomCompiler = \"androidx.room:room-compiler:\${Version.room}\"\n" +
                "  const val rxRoom = \"androidx.room:room-rxjava2:\${Version.room}\"\n" +
                "\n" +
                "  // Lifecycle\n" +
                "  const val lifecycle = \"androidx.lifecycle:lifecycle-extensions:\${Version.lifecycle}\"\n" +
                "\n" +
                "  // Navigation\n" +
                "  const val navigationFragment = \"android.arch.navigation:navigation-fragment-ktx:\${Version.navigation}\"\n" +
                "  const val navigationUI = \"android.arch.navigation:navigation-ui-ktx:\${Version.navigation}\"\n" +
                "\n" +
                "  // ImageLoading\n" +
                "  const val glide = \"com.github.bumptech.glide:glide:\${Version.glide}\"\n" +
                "  const val glideCompiler = \"com.github.bumptech.glide:compiler:\${Version.glide}\"\n" +
                "  const val glideOkHttp3 = \"com.github.bumptech.glide:okhttp3-integration:\${Version.glide}\"\n" +
                "  const val picasso = \"com.squareup.picasso:picasso:\${Version.picasso}\"\n" +
                "\n" +
                "  // Profiling\n" +
                "  const val debugDb = \"com.amitshekhar.android:debug-db:\${Version.debugDb}\"\n" +
                "  const val leakCanaryDebug = \"com.squareup.leakcanary:leakcanary-android:\${Version.leakCanary}\"\n" +
                "  const val leakCanaryRelease = \"com.squareup.leakcanary:leakcanary-android-no-op:\${Version.leakCanary}\"\n" +
                "  const val leakCanaryFragment = \"com.squareup.leakcanary:leakcanary-support-fragment:\${Version.leakCanary}\"\n" +
                "\n" +
                "  // Social auth\n" +
                "  const val googleAuth = \"com.google.android.gms:play-services-auth:\${Version.googleAuth}\"\n" +
                "  const val socialAuth = \"com.jaychang:simpleauth:\${Version.simpleAuth}\"\n" +
                "  const val socialAuthFacebook = \"com.jaychang:simpleauth-facebook:\${Version.simpleAuth}\"\n" +
                "  const val socialAuthGoogle = \"com.jaychang:simpleauth-google:\${Version.simpleAuth}\"\n" +
                "  const val socialAuthInstagram = \"com.jaychang:simpleauth-instagram:\${Version.simpleAuth}\"\n" +
                "  const val socialAuthTwitter = \"com.jaychang:simpleauth-twitter:\${Version.simpleAuth}\"\n" +
                "\n" +
                "  // Facebook\n" +
                "  const val facebookAnalytics = \"com.facebook.android:facebook-android-sdk:\${Version.facebookAnalytics}\"\n" +
                "\n" +
                "  // Fabric\n" +
                "  const val fabricPlugin = \"io.fabric.tools:gradle:\${Version.fabricPlugin}\"\n" +
                "  const val crashlytics = \"com.crashlytics.sdk.android:crashlytics:\${Version.crashlytics}\"\n" +
                "\n" +
                "  // Firebase\n" +
                "  const val firebaseCore = \"com.google.firebase:firebase-core:\${Version.firebaseCore}\"\n" +
                "  const val firebaseMessaging = \"com.google.firebase:firebase-messaging:\${Version.firebaseMessaging}\"\n" +
                "\n" +
                "  //Maps\n" +
                "  const val googleMaps = \"com.google.android.gms:play-services-maps:\${Version.googleMaps}\"\n" +
                "  const val googleLocation = \"com.google.android.gms:play-services-location:\${Version.googleLocation}\"\n" +
                "\n" +
                "  // Tests\n" +
                "  const val jUnit = \"junit:junit:\${Version.jUnit}\"\n" +
                "  const val mockito = \"org.mockito:mockito-core:\${Version.mockito}\"\n" +
                "  const val mockitoKotlin = \"com.nhaarman.mockitokotlin2:mockito-kotlin:\${Version.mockitoKotlin}\"\n" +
                "  const val mockitoAndroid = \"org.mockito:mockito-android:\${Version.mockitoAndroid}\"\n" +
                "  const val mockitoInline = \"org.mockito:mockito-inline:+\"\n" +
                "  const val androidTestRunner = \"androidx.test:runner:\${Version.androidTestRunner}\"\n" +
                "  const val espresso = \"androidx.test.espresso:espresso-core:\${Version.espresso}\"\n" +
                "\n" +
                "  //ImageProcessing\n" +
                "  const val gpuImage = \"jp.co.cyberagent.android:gpuimage:\${Version.gpuImage}\"\n" +
                "  const val exifinterface = \"androidx.exifinterface:exifinterface:\${Version.exifinterface}\"\n" +
                "  const val compressor = \"id.zelory:compressor:\${Version.compressor}\"\n" +
                "\n" +
                "  //Camera\n" +
                "  const val cameraView = \"com.otaliastudios:cameraview:\${Version.cameraView}\"\n" +
                "\n" +
                "  //Billings\n" +
                "  const val billing = \"com.android.billingclient:billing:\${Version.billing}\"\n" +
                "\n" +
                "  const val skeleton = \"com.ethanhua:skeleton:\${Version.skeleton}\"\n" +
                "  const val shimmerlayout = \"io.supercharge:shimmerlayout:\${Version.shimmerlayout}\"\n" +
                "}"
    )
}