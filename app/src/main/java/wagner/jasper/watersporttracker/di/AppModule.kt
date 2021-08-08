package wagner.jasper.watersporttracker.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideLocationData() = "String"

    @Provides
    fun provideFusedLocationProvider(@ApplicationContext appContext: Context) =
        LocationServices.getFusedLocationProviderClient(appContext)
}