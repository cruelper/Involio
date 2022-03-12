package com.example.involio.ui.home

import android.R.attr
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.example.involio.R
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import classes.network.ApiClient
import classes.network.dto.BrokerDto
import classes.network.dto.TypeOfBrokerAccountDto
import classes.network.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import android.text.TextUtils
import classes.network.dto.PortfolioDto
import com.example.involio.BottomMenuActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText


class CreatingPortfolioActivity : AppCompatActivity() {

    private lateinit var typesOfBrokerAccount: List<TypeOfBrokerAccountDto>
    private lateinit var listOfBroker: List<BrokerDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creating_portfolio)
        title = "Создание портфеля"

        openMessageIfNoOnePortfolio()
        setBrokers()
        setTypesOfBrokerAccount()
        setupDataPicker()
        setupListenerToButOfCreate()
    }

    private fun openMessageIfNoOnePortfolio(){
        if (intent.extras?.getBoolean("isNoOnePortfolio") == true) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@CreatingPortfolioActivity)
            builder.setTitle("Создание первого портфеля!")
                .setMessage("Прежде чем начать работать с приложением, необходимо создать хотя бы один портфель. " +
                        "В последующем, в него можно будет добавлять желаемые акции и валюту.")
                .setIcon(R.drawable.ic_bags_picture)
                .setCancelable(false)
                .setNegativeButton("Ок",
                    DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
            val alert: AlertDialog = builder.create()
            alert.show()
        }
    }

    private fun setBrokers(){
        var brokerResponseCall: Call<List<BrokerDto>> = ApiClient.getUserService().getAllBroker(getJWT(applicationContext))
        brokerResponseCall.enqueue(object : Callback<List<BrokerDto>> {
            override fun onResponse(call: Call<List<BrokerDto>>?, response: Response<List<BrokerDto>>?) {
                listOfBroker= response!!.body()!!
                val brokersAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@CreatingPortfolioActivity, R.layout.list_of_spinner, listOfBroker.map { it.name }
                )
                val brokersSpinner = findViewById<AppCompatSpinner>(R.id.spinner_broker)
                brokersSpinner.adapter = brokersAdapter
            }
            override fun onFailure(call: Call<List<BrokerDto>>?, t: Throwable?) {
                Toast.makeText(this@CreatingPortfolioActivity, "Ошибка загрузки брокеров!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setTypesOfBrokerAccount(){
        var typeAccountResponseCall: Call<List<TypeOfBrokerAccountDto>> = ApiClient.getUserService().getAllTypeOfBrokerAccount(getJWT(applicationContext))
        typeAccountResponseCall.enqueue(object : Callback<List<TypeOfBrokerAccountDto>> {
            override fun onResponse(call: Call<List<TypeOfBrokerAccountDto>>?, response: Response<List<TypeOfBrokerAccountDto>>?) {
                typesOfBrokerAccount = response!!.body()!!
                val typeOfBrokerAccountAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this@CreatingPortfolioActivity, R.layout.list_of_spinner, typesOfBrokerAccount.map { it.name }
                )
                val typeOfBrokerAccountSpinner = findViewById<AppCompatSpinner>(R.id.spinner_type_broker_account)
                typeOfBrokerAccountSpinner.adapter = typeOfBrokerAccountAdapter
            }
            override fun onFailure(call: Call<List<TypeOfBrokerAccountDto>>?, t: Throwable?) {
                Toast.makeText(this@CreatingPortfolioActivity, "Ошибка загрузки типов брокерских счетов!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupDataPicker(){
        val dataPicker: DatePicker = findViewById(R.id.datePicker_portfolioCreation)
        dataPicker.minDate = 946684800000 // 01.01.2000
        dataPicker.maxDate = System.currentTimeMillis()
    }

    private fun setupListenerToButOfCreate(){
        val but: Button = findViewById(R.id.but_create_portfolio)

        but.setOnClickListener(View.OnClickListener {
            val nameTextView: TextInputEditText = findViewById(R.id.name_portfolio_text_field)
            val typeSpinner: AppCompatSpinner = findViewById(R.id.spinner_type_broker_account)
            val brokerSpinner: AppCompatSpinner = findViewById(R.id.spinner_broker)
            val datePicker: DatePicker = findViewById(R.id.datePicker_portfolioCreation)

            if (TextUtils.isEmpty(nameTextView.text.toString()))
                Toast.makeText(this@CreatingPortfolioActivity, "Не введено название портфеля!", Toast.LENGTH_SHORT).show()
            else{
                val calendar = Calendar.getInstance()
                calendar[datePicker.year, datePicker.month] = datePicker.dayOfMonth

                var portfolioResponseCall: Call<Boolean> = ApiClient.getUserService().createPortfolio(
                    PortfolioDto(
                        0,
                        nameTextView.text.toString(),
                        listOfBroker.find { it.name == brokerSpinner.selectedItem.toString() }!!.id,
                        typesOfBrokerAccount.find { it.name == typeSpinner.selectedItem.toString() }!!.id,
                        calendar.timeInMillis
                        ),
                    getJWT(applicationContext)
                )
                portfolioResponseCall.enqueue(object : Callback<Boolean> {
                    override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                        if (response?.isSuccessful!!){
                            if (response?.body()!!){
                                Toast.makeText(this@CreatingPortfolioActivity, "Портфель создан успешно!", Toast.LENGTH_SHORT).show()
//                                findViewById<BottomNavigationView>(R.id.nav_view).selectedItemId = R.id.navigation_home
                                val intent: Intent = Intent(this@CreatingPortfolioActivity, BottomMenuActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            }
                            else Toast.makeText(this@CreatingPortfolioActivity, "Ошибка создания портфеля!", Toast.LENGTH_SHORT).show()
                        }
                        else Toast.makeText(this@CreatingPortfolioActivity, "Ошибка создания портфеля!", Toast.LENGTH_SHORT).show()
                    }
                    override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                        Toast.makeText(this@CreatingPortfolioActivity, "Ответ от сервера не получен!", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })
    }


}