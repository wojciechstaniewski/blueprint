package co.netguru.blueprint.authenticator


data class AuthenticationResult(val accountName: String? = null,
                                val accountType: String? = null,
                                val authToken: String? = null,
                                val password: String? = null,
                                val expireIn: String? = null,
                                val status: String? = null)