import com.softmintindia.app.network.models.*
import com.softmintindia.app.data.ApiService
import com.softmintindia.app.domain.models.CheckTxnStatusResponse
import com.softmintindia.app.domain.models.PgsdkInitRequest
import com.softmintindia.app.domain.models.PgsdkInitResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Call

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService
    private lateinit var retrofit: Retrofit

    @Before
    fun setup() {
        // Start the mock server
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Initialize Retrofit with the MockWebServer base URL
        retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))  // Base URL will be from MockWebServer
            .addConverterFactory(GsonConverterFactory.create())  // Use Gson converter
            .client(OkHttpClient()) // Optionally configure OkHttpClient
            .build()

        // Create the ApiService instance
        apiService = retrofit.create(ApiService::class.java)
    }

    @Test
    fun testPgsdkInitialize() {
        // Simulate the response for pgsdkInitialize
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{ \"status\": 200, \"message\": \"Success\", \"data\": { \"sellerID\": \"123\", \"companyName\": \"Softmint\", \"rrn\": \"abc123\", \"amount\": \"1000\", \"vpa\": \"user@softmint\", \"qrURL\": \"http://example.com\", \"txnID\": \"txn123\", \"remark\": \"Test\", \"status\": \"Success\" }}")

        mockWebServer.enqueue(mockResponse)

        // Call the API method
        val call: Call<PgsdkInitResponse> = apiService.pgsdkInitialize(
            deviceId = "device123",
            requestBody = PgsdkInitRequest("SELL123", "100.00", "Purchase", "MERCHANTTOKEN@123456789")
        )

        // Execute the call
        val response = call.execute()

        // Assert that the response is successful
        assert(response.isSuccessful)
        assert(response.body()?.status?.toInt() == 200)
        assert(response.body()?.message == "Success")
    }

    @Test
    fun testCheckTxnStatus() {
        // Simulate the response for checkTxnStatus
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\"status\": 200, \"message\": \"Success\", \"data\": {\"name\": \"Test User\", \"amount\": \"1000\", \"date\": \"2024-12-30\", \"time\": \"10:00\", \"txnID\": \"txn123\", \"remark\": \"Test Remark\", \"status\": \"Completed\"}}")

        mockWebServer.enqueue(mockResponse)

        // Call the API method
        val call: Call<CheckTxnStatusResponse> = apiService.checkTxnStatus()

        // Execute the call
        val response = call.execute()

        // Assert that the response is successful
        assert(response.isSuccessful)
        assert(response.body()?.status?.toInt() == 200)
        assert(response.body()?.message == "Success")
    }

    @After
    fun tearDown() {
        // Shut down the mock server after tests
        mockWebServer.shutdown()
    }
}
