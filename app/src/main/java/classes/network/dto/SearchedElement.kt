package classes.network.dto

data class SearchedElement(
    var ticker: String,
    var idExchange: Int,
    var nameExchange: String,
    var curPrice: Double,
    var signCurrency: String,
)