package uniffi.database

public actual open class Persistence : Disposable, PersistenceInterface {
    /**
     * This constructor can be used to instantiate a fake object. Only used for tests. Any
     * attempt to actually use an object constructed this way will fail as there is no
     * connected Rust object.
     */
    actual constructor(noPointer: NoPointer) {
        TODO("Not yet implemented")
    }

    actual constructor() {
        TODO("Not yet implemented")
    }

    actual override fun destroy() {
    }

    actual override fun close() {
    }

    actual override fun connect(credential: Credential) {
    }

    actual override fun disconnect() {
    }

    /**
     * Execute a SQL query and return the results
     * (wrapper function for backward compatibility)
     */
    actual override suspend fun execute(
        sql: String,
        args: List<Entry>
    ): String {
        TODO("Not yet implemented")
    }

    actual override suspend fun synchronise() {
    }

    public actual companion object


}