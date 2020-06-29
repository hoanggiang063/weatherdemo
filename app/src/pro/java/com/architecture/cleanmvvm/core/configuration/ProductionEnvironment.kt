package com.architecture.cleanmvvm.core.configuration
//values in production environment
class ProductionEnvironment:EnvConfiguration {
    companion object {
        private const val API_URL = "https://api.openweathermap.org/"
        private const val API_KEY = "60c6fbeb4b93ac653c492ba806fc346d"
        private const val UNIT = "metric"
        private const val SHA1_SPINNING = "sha1/33E4E80807204C2B6182A3A14B591ACD25B5F0DB"
    }

    override fun getEnvironmentUrl(): String {
        return API_URL
    }

    override fun getEnvironmentApiKey(): String {
        return API_KEY
    }

    override fun getEnvironmentUnit(): String {
        return UNIT
    }

    override fun getSpinningKey(): String {
        return SHA1_SPINNING
    }
}