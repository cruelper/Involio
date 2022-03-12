package classes.network.dto

import kotlinx.serialization.Serializable

data class PortfolioDto(
    val id: Int,
    val name: String,
    val idBroker: Int,
    val idTypeBrokerAccount: Int,
    val dataOfCreation: Long
)