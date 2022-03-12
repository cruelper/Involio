package classes.network

import classes.network.dto.*
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface UserService {

    @POST("authenticate/")
    fun userAuth(@Body authRequest: AuthRequest): Call<AuthResponse>

    @POST("user-registration/")
    fun userRegistration(@Body myUserDto: MyUserDto): Call<Pair<String, Boolean>>

    @GET("user/portfolios/")
    fun getUserPortfolios(@Header("Authorization") token: String): Call<List<PortfolioDto>>

    @GET("/user/broker/")
    fun getAllBroker(@Header("Authorization") token: String): Call<List<BrokerDto>>

    @GET("/user/type-of-broker-account/")
    fun getAllTypeOfBrokerAccount(@Header("Authorization") token: String): Call<List<TypeOfBrokerAccountDto>>

    @POST("/user/portfolios/")
    fun createPortfolio(@Body portfolioDto: PortfolioDto, @Header("Authorization") token: String): Call<Boolean>

    @GET("/user/portfolios/{id}/group-by-company={YesOrNo}")
    fun getBasicPortfolioInfo(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Path("YesOrNo") YesOrNo: String
    ): Call<BasicPortfolioInfoDto>

    @GET("/user/currency")
    fun getAllCurrencyWithPrice(
        @Header("Authorization") token: String,
    ): Call<List<Pair<CurrencyDto, Double>>>

    @GET("/user/search/stock/{searchString}/{page}")
    fun searchStock(
        @Path("searchString") searchString: String,
        @Path("page") page: Int,
        @Header("Authorization") token: String,
    ): Call<Map<String, List<SearchedElement>>>

    @GET("/user/search/stock/{page}")
    fun getAllStock(
        @Path("page") page: Int,
        @Header("Authorization") token: String,
    ): Call<Map<String, List<SearchedElement>>>

    @GET("/user/stock/{ticker}/{idExchange}")
    fun getStockInfo(
        @Path("ticker") ticker: String,
        @Path("idExchange") idExchange: Int,
        @Header("Authorization") token: String,
    ): Call<StockInfoDto>

    @POST("/user/portfolios/add-stock")
    fun addStockToPortfolio(
        @Body composition: CompositionOfPortfolioDto,
        @Header("Authorization") token: String,
    ): Call<Boolean>

}