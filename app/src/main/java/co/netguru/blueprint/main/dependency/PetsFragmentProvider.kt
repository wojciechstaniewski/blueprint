package co.netguru.blueprint.main.dependency

import co.netguru.blueprint.main.view.PetsScreenFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PetsScreenFragmentProvider {

    @ContributesAndroidInjector(modules = arrayOf(PetsScreenFragmentModule::class))
    internal abstract fun providePetScreenFragmentFactory(): PetsScreenFragment
}