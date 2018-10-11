package cz.josefadamcik.activityjournal.test

import io.mockk.mockk
import io.mockk.mockkClass
import org.koin.test.KoinTest
import kotlin.reflect.KClass

/**
 * Declare & Create a mockK in Koin container for given type
 */
inline fun <reified T : Any> KoinTest.declareMockK(
        isFactory: Boolean = false,
        module: String? = null,
        binds: List<KClass<*>> = kotlin.collections.emptyList()
) {
    val clazz = T::class
    org.koin.core.Koin.logger.info("[mockk] declare mockk for $clazz")
    org.koin.standalone.StandAloneContext.loadKoinModules(
            org.koin.dsl.module.module(module ?: org.koin.dsl.path.Path.ROOT) {
                val def = if (!isFactory) {
                    single(override = true) {
                        mockkClass(clazz)
                    }
                } else {
                    factory(override = true) {
                        mockkClass(clazz)
                    }
                }
                binds.forEach { def.bind(it) }
            }
    )
}

