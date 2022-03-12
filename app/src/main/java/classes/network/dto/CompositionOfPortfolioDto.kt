package classes.network.dto

data class CompositionOfPortfolioDto(
    val idPortfolio: Int,
    val ticker: String,
    val idExchange: Int,
    val date: Long,
    val count: Int,
    val priceOfUnit: Double,
    val commission: Double,
    val currencyCommissionTicker: String,
)
