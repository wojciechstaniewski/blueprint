package co.netguru.blueprint.services.authentication.business

import co.netguru.blueprint.services.authentication.domain.ApiAuth
import co.netguru.blueprintlibrary.common.utils.HttpStatus
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class AuthenticationManagerImpl @Inject constructor(): AuthenticationManager {

    private var credentialsRepo: Map<String, String>? = null

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    init {
        val credentials = HashMap<String, String>()
        credentials["test1@netguru.pl"] = "Andr123!"
        credentials["test2@netguru.pl"] = "Andr123!"
        credentials["test3@netguru.pl"] = "Andr123!"
        credentialsRepo = Collections.unmodifiableMap(credentials)
    }

    override fun authenticateClient(userName: String, password: String): Single<ApiAuth> {

        val accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJIOE9MYUdmZWVmWlgtbW1zekk2QzhsTXRRbW5vNEg1VVA4MkE0UFZiR3pnIn0.eyJqdGkiOiJmN2Y0Y2QyYy0wNGJiLTQ0NDAtYWMwZC04ZDdlZjg5MzIwMDUiLCJleHAiOjE1MjY2NTU1MDUsIm5iZiI6MCwiaWF0IjoxNTI2NjM3NTA1LCJpc3MiOiJodHRwczovL2hlbGxvYXN0cmEub253ZWxvLmNvbS9hdXRoL3JlYWxtcy9jYW5kaWRhdGVzIiwiYXVkIjoiaGVsbG9hc3RyYS1jYW5kaWRhdGVzIiwic3ViIjoiZmVlYTllYTctNjI5YS00MDQyLTlmMTktNDNlODY2Y2MxMTEzIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiaGVsbG9hc3RyYS1jYW5kaWRhdGVzIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiNzQ3OTk0OWMtNWRlNS00NDQ3LWI3N2QtMDk4ZWZlNzU3ODkzIiwiYWNyIjoiMSIsImNsaWVudF9zZXNzaW9uIjoiNjc2OGZkZmUtZmIzMy00OTg0LWIwMmMtY2JiODc4ZDNjZjBlIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MS8qIiwiaHR0cDovL2xvY2FsaG9zdDo4MDgwLyoiLCJodHRwOi8vb250YWxlbnQtdHN0Lm9ud2Vsby5jb20vKiIsImh0dHBzOi8vb250YWxlbnQtdHN0Lm9ud2Vsby5jb20vKiIsImh0dHA6Ly8xMC4xMTIuMTEyLjgvKiIsImh0dHBzOi8vbG9jYWxob3N0OjMwMDAiLCJodHRwczovL2hlbGxvYXN0cmEub253ZWxvLmNvbS8qIiwiaHR0cHM6Ly8xMC4xMTIuMTEyLjgiLCJodHRwOi8vbG9jYWxob3N0OjMwMDAiLCJodHRwczovL2xvY2FsaG9zdDo4MDgwLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIiwiUl9DQU5ESURBVEVfUFJPRklMRSIsIlJfQ0FORElEQVRFX0FETUlOIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwibmFtZSI6IkpvaG4gS293YWxza3kiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0MSIsImdpdmVuX25hbWUiOiJKb2huIiwiZmFtaWx5X25hbWUiOiJLb3dhbHNreSJ9.C5eNs70gwV5Y7HoC-D1_J2ZVVjtqSztjo1ueWySWvnAt_D50VY9dfcnRyLhT_NQbphxVNAUn5Kduksb3LBIpZYPMbyJg3B-KaZtbm13PrfJIqbx4QRpLTe4Rebd9XrzqNFzBCm1l7Kl1jsJ8mhmhE2NuJddRHx_lStTXNIQNREG64teQsMwZhDr9KMLsk9FMmsFu2fqQlQZ8WlEZWwW6vDyrTHlxyMYY_Y_G_6_Q_oOHs05iyfY4h99Hnz4qGQrLB6eCSeHGVC4dzt8hQ0Dn4pmQ6VoTq9knttClVnaY1o_TGKn59BB009DS38YKdZyI2wesl70ridrO3BW-EMP2Ag"
        val expiresIn = 180000 //180 sec
        val refreshExpires = 180000 //180 sec
        val refreshToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJIOE9MYUdmZWVmWlgtbW1zekk2QzhsTXRRbW5vNEg1VVA4MkE0UFZiR3pnIn0.eyJqdGkiOiI4YzQyNDZiMC1kNGRjLTRmZGYtYTM5NS0xZTNkNjIwYjE2YTIiLCJleHAiOjE1MjY2MzkzMDUsIm5iZiI6MCwiaWF0IjoxNTI2NjM3NTA1LCJpc3MiOiJodHRwczovL2hlbGxvYXN0cmEub253ZWxvLmNvbS9hdXRoL3JlYWxtcy9jYW5kaWRhdGVzIiwiYXVkIjoiaGVsbG9hc3RyYS1jYW5kaWRhdGVzIiwic3ViIjoiZmVlYTllYTctNjI5YS00MDQyLTlmMTktNDNlODY2Y2MxMTEzIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6ImhlbGxvYXN0cmEtY2FuZGlkYXRlcyIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6Ijc0Nzk5NDljLTVkZTUtNDQ0Ny1iNzdkLTA5OGVmZTc1Nzg5MyIsImNsaWVudF9zZXNzaW9uIjoiNjc2OGZkZmUtZmIzMy00OTg0LWIwMmMtY2JiODc4ZDNjZjBlIiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIiwiUl9DQU5ESURBVEVfUFJPRklMRSIsIlJfQ0FORElEQVRFX0FETUlOIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fX0.OeUGO5lCz-OLeJDHEgl7w0YOHBHW3bNgN9-mjTqHwKaz1Yy7xcY_afwdaLWDmEWu0D_gjkvrKez5QRZ8P_IS9UCvibUZmltSQTF5lhCd9bdw4qvlExMXsAR2xZvQRJA6GfgVPCrAdKSqPyUL0fVuAHbitWxuJTpJCu70Ec44cGEjSJF8SHUo5m7Dek-TKhxyMwypoijGCEYAyySyCNLMeTWrZyZknnxDxH-7dP24cY8AK4v45IX9vbJzXUuVFj4puOLilcg3hBFCSzYwtI8UmhgeU75HqMjxru1Hz1a3UMqjy27UAUiDG0hTbkkXfoJFv0XYlXi90cKs7um2Z45K5g"
        val tokenType = "bearer"
        val idToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJIOE9MYUdmZWVmWlgtbW1zekk2QzhsTXRRbW5vNEg1VVA4MkE0UFZiR3pnIn0.eyJqdGkiOiI1OTM5Mjk5My0xYzRmLTQyMWQtYWUxMi05ZmNlMTQ5MDIzYzIiLCJleHAiOjE1MjY2NTU1MDUsIm5iZiI6MCwiaWF0IjoxNTI2NjM3NTA1LCJpc3MiOiJodHRwczovL2hlbGxvYXN0cmEub253ZWxvLmNvbS9hdXRoL3JlYWxtcy9jYW5kaWRhdGVzIiwiYXVkIjoiaGVsbG9hc3RyYS1jYW5kaWRhdGVzIiwic3ViIjoiZmVlYTllYTctNjI5YS00MDQyLTlmMTktNDNlODY2Y2MxMTEzIiwidHlwIjoiSUQiLCJhenAiOiJoZWxsb2FzdHJhLWNhbmRpZGF0ZXMiLCJhdXRoX3RpbWUiOjAsInNlc3Npb25fc3RhdGUiOiI3NDc5OTQ5Yy01ZGU1LTQ0NDctYjc3ZC0wOThlZmU3NTc4OTMiLCJhY3IiOiIxIiwibmFtZSI6IkpvaG4gS293YWxza3kiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ0ZXN0MSIsImdpdmVuX25hbWUiOiJKb2huIiwiZmFtaWx5X25hbWUiOiJLb3dhbHNreSJ9.Cf9F35uvQ1ElcUb9AW1C21uZ09V8WkG2wYtKuNt6sM97Kf6bgxx9JJN5N_W1_rgpdSktqOmjYfQfAdB19Dz0e8GCw-OvdieqKEA483ASD-rk6BHnAe0xsXDoQnlVAu3PBwPH4_Wgk54xI_YJty4bO28SbZaPb8NNPNJqQtTYym3fT0CVfGmBhxRK1EG5vPpfYhxWOHdyXCRd6JijfQEe3sb0iqDYrwNV2NUCzz-q3yCg6tyu9_A5VxAg70WW_KpaZDoJEaxxaXCf-7P5PLEnVaI6c3vJDfse081JrgsBeNm6qcWE5-NlgF_Rikm3s7WXbLWRsVzjar4cwLqkwW6JmA"
        val notBeforePolicy = "0"
        val sessionState = "7479949c-5de5-4447-b77d-098efe757893"

        if (credentialsRepo!!.contains(userName)) {
            if (password == credentialsRepo?.get(userName)) {
                return Single.just(ApiAuth(accessToken, expiresIn, refreshExpires, refreshToken, tokenType, idToken, notBeforePolicy, sessionState))
            }
        }

        val responseBody: ResponseBody = ResponseBody.create(MediaType.parse("text/plain"), "{\n" +
                "    \"error\": \"invalid_grant\",\n" +
                "    \"error_description\": \"Invalid user credentials\"\n" +
                "}")
        val response: Response<String> = Response.error(HttpStatus.UNAUTHORIZED.value(), responseBody)
        return Single.error(HttpException(response))
    }
}