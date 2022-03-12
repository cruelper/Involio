package classes.network.dto

data class BasicPortfolioInfoDto(
    var id: Int,
    var name: String,
    var nameBroker: String,
    var nameTypeOfBrokerAccount: String,

    var inRuble: PortfolioPrice,
    var inUSD: PortfolioPrice,
    var inEuro: PortfolioPrice,

    var changeSP500InRuble: ChangePrice,
    var changeSP500InUSD: ChangePrice,
    var changeSP500InEURO: ChangePrice,

    var changeIMOEXInRuble: ChangePrice,
    var changeIMOEXInUSD: ChangePrice,
    var changeIMOEXInEURO: ChangePrice,

    var stocksInPortfolio: List<StockInPortfolio>,

)

data class ChangePrice(
    val priceChangeOnDay: Double,
    val priceChangeOnYear: Double,
    val priceChangeOnAllTime: Double,
)

data class PortfolioPrice(
    val currentPrice: Double,
    val changePrice: ChangePrice,
    val currencySign: String,
)

data class StockInPortfolio(
    var name: String,
    var ticker: String,
    var count: Int,
    var currentUnitPrice: Double,
    var partOfPortfolio: Double,
    var changePrice: ChangePrice,
)
