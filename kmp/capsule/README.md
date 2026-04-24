Capsule
=======

A lightweight framework for writing presentation logic in Compose with built-in `retain` integration and [Metro](https://github.com/ZacSweers/metro) support.

## Writing a Presenter

A presenter extends `Presenter<UiEvent, UiState>` and implements a `present()` Composable function that returns the current UI state.

The presenter uses [Molecule](https://github.com/cashapp/molecule) to turn `present()` into a `StateFlow<UiState>` which can be consumed from the UI layer.

A `MoleculeContext` needs to be passed in via the constructor to configure the coroutine context and recomposition mode used to run Molecule.

Use Compose state primitives (`remember`, `mutableStateOf`, `LaunchedEffect`, etc.) inside `present()`. Use the built-in `CollectEvent {}` helper to collect UI events from the `eventSink`.

```kt
class MyPresenter(
    private val dataLayer: DataLayer,
    moleculeContext: MoleculeContext,
) : Presenter<MyUiEvent, MyUiState>(moleculeContext) {
    @Composable
    override fun present(): MyUiState {
        var uiState by remember { mutableStateOf<MyUiState>(MyUiState.Loading) }
        LaunchedEffect(Unit) {
            uiState = loadData()
        }
        CollectEvent { event ->
            when (event) {
                MyUiEvent.Refresh -> {
                    uiState = MyUiState.Loading
                    uiState = loadData()
                }
            }
        }
        return uiState
    }

    private suspend fun loadData(): MyUiState = try {
        MyUiState.Content(dataLayer.load())
    } catch (e: DataError) {
        MyUiState.Error
    }

    // called when the presenter is retired (e.g. screen popped from backstack)
    override fun onCleared() {
        // release resources
    }
}
```

## Dependency Injection

Capsule integrates with Metro for dependency injection via multibinding. Add `@PresenterKey` and `@ContributesIntoMap` to contribute a presenter to the graph:

```kt
@PresenterKey
@ContributesIntoMap(AppScope::class)
class MyPresenter(
    private val dataLayer: DataLayer,
    moleculeContext: MoleculeContext,
) : Presenter<MyUiEvent, MyUiState>(moleculeContext) { ... }
```

For presenters that require runtime parameters, use assisted injection:

```kt
@AssistedInject
class MyPresenter(
    @Assisted private val id: String,
    private val dataLayer: DataLayer,
    moleculeContext: MoleculeContext,
) : Presenter<MyUiEvent, MyUiState>(moleculeContext) {
    @Composable
    override fun present(): MyUiState { ... }

    @AssistedFactory
    @PresenterAssistedFactoryKey
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : PresenterAssistedFactory {
        fun create(id: String): MyPresenter
    }
}
```

### Dependency graph wiring

In the app module, provide the `PresenterFactory` via `LocalPresenterFactory`:

```kt
CompositionLocalProvider(
    LocalPresenterFactory provides appGraph.presenterFactory,
) {
    // app content
}
```

The dependency graph should extend `PresenterGraph` to expose the factory:

```kt
@DependencyGraph(AppScope::class)
interface AppGraph : PresenterGraph { ... }
```

`MoleculeContext` can be also provided via the dependency graph:

```kt
@Provides
private fun provideMoleculeContext(): MoleculeContext = object : MoleculeContext {
    override val coroutineContext get() = AndroidUiDispatcher.Main
    override val recompositionMode get() = RecompositionMode.ContextClock
}
```

A concrete `PresenterFactory` binding should be contributed to the graph:

```kt
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class MyPresenterFactory(
    override val presenterProviders: Map<KClass<out Presenter<*, *>>, () -> Presenter<*, *>>,
    override val assistedFactoryProviders: Map<KClass<out PresenterAssistedFactory>, () -> PresenterAssistedFactory>,
) : PresenterFactory()
```

### Injecting retained Presenters in Compose

To inject a retained presenter instance in a screen Composable:

```kt
val presenter = retainPresenter<MyPresenter>()
val uiState by presenter.states.collectAsState()
val eventSink = presenter.eventSink
```

To inject an assisted presenter with runtime parameters:

```kt
val presenter = assistedRetainPresenter<MyPresenter, MyPresenter.Factory> {
    create(id)
}
val uiState by presenter.states.collectAsState()
val eventSink = presenter.eventSink
```

Presenter instances are retained across configuration changes and backstack navigation via Compose's `retain` API and Nav3 integration.

## Testing

The `capsule:testing` module provides a  `TestDispatcher.asMoleculeContext()` extension for creating a test-friendly `MoleculeContext`.

Presenter tests can be written with [Turbine](https://github.com/cashapp/turbine):

```kt
class MyPresenterTest {
    private val testDispatcher = StandardTestDispatcher()
    private val dataLayer = FakeDataLayer()
    private val presenter = MyPresenter(
        dataLayer = dataLayer,
        moleculeContext = testDispatcher.asMoleculeContext(),
    )

    @Test
    fun `emits Content state when refresh succeeds`() = runTest {
        presenter.states.test {
            assertEquals(MyUiState.Loading, awaitItem())
            assertEquals(MyUiState.Content("data"), awaitItem())

            dataLayer.result = "updated"
            presenter.eventSink(MyUiEvent.Refresh)

            assertEquals(MyUiState.Loading, awaitItem())
            assertEquals(MyUiState.Content("updated"), awaitItem())
        }
    }   
}
```
