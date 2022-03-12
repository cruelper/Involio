package classes.network.dto

data class BrokerDto (
    val id: Int,
    val name: String,
    val listExchange: List<ExchangeDto>,
)