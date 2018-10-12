package cz.josefadamcik.activityjournal.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import org.koin.androidx.viewmodel.ext.android.ViewModelStoreOwnerDefinition
import org.koin.androidx.viewmodel.ext.android.viewModelByClass
import org.koin.core.parameter.ParameterDefinition
import org.koin.core.parameter.emptyParameterDefinition


/**
 * Lazy getByClass a viewModel instance shared with Activity
 *
 * @param key - ViewModel Factory key (if have several instances from same ViewModel)
 * @param name - Koin BeanDefinition name (if have several ViewModel beanDefinition of the same type)
 * @param from - ViewModelStoreOwner that will store the viewModel instance. Examples: "parentFragment", "activity". Default: "activity"
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T : ViewModel> Fragment.sharedViewModelForNestFragment(
        key: String? = null,
        name: String? = null,
        noinline from: ViewModelStoreOwnerDefinition = { parentFragment },
        noinline parameters: ParameterDefinition = emptyParameterDefinition()
) = viewModelByClass(T::class, key, name, from, parameters)
