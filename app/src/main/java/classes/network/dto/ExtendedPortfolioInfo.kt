package classes.network.dto

import java.util.Date

data class ExtendedPortfolioInfoDto (
    var id: Int,
    var name: String,
    var nameBroker: String,
    var nameTypeOfBrokerAccount: String,

    var dateOfCreation: Long,

    // вкладка портфель
    var signs: Map<String, String>,
    var curPriceInRubl: Double,
    var curPriceInUsd: Double,
    var curPriceInEuro: Double,

    var rubInterval: ValuesInInterval,
    var usdInterval: ValuesInInterval,
    var euroInterval: ValuesInInterval,

    // вкладка анализ
    // стринга это название элемента, дабл - его часть в портфеле
    var assets: List<Pair<String, Double>>,
    var companies: List<Pair<String, Double>>,
    var branches: List<Pair<String, Double>>,
    var sectors: List<Pair<String, Double>>,
    var currencies: List<Pair<String, Double>>,

    // вкладка расчет налогов
    var tax: String,
)

data class ValuesInInterval(
    var monthInterval: List<Pair<Long, Double>>,
    var monthData: BasicValues,

    var yearInterval: List<Pair<Long, Double>>,
    var yearData: BasicValues,

    var allInterval: List<Pair<Long, Double>>,
    var allData: BasicValues,
)

data class BasicValues(
    var income: Double,
    var dividends: Double,
    var brokerCommission: Double,
    var depositsAndWithdrawalsDiff: Double,
)

data class IndicesInterval(
    // вкладка сравнение
    // данные для графика портфеля уже есть
    // берем только данные за все время
    var indicesIntervalsInRuble: Map<String, ValuesInInterval>,
    var indicesIntervalsInUsd: Map<String, ValuesInInterval>,
    var indicesIntervalsInEuro: Map<String, ValuesInInterval>,
)
