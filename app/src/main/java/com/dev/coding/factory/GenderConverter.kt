package com.dev.coding.factory
import android.support.core.di.Inject
import android.support.core.di.ShareScope

enum class Gender {
    Male,
    Female,
    Mixed,
    UnSpecify
}

@Inject(ShareScope.Singleton)
class GenderConverter {
    fun convert(gender: String?): Gender {
        return when (gender) {
            Gender.Male.name.lowercase() -> Gender.Male
            Gender.Female.name.lowercase() -> Gender.Female
            Gender.Mixed.name.lowercase() -> Gender.Mixed
            else -> Gender.UnSpecify
        }
    }
}