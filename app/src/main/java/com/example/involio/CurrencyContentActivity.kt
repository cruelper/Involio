package com.example.involio

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.involio.databinding.ActivityStockContentBinding
import androidx.appcompat.widget.Toolbar
import classes.network.ApiClient
import classes.network.utils.getJWT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import com.google.android.material.textfield.TextInputEditText
import android.app.DatePickerDialog
import android.widget.ProgressBar
import classes.network.dto.*
import com.example.involio.ui.tabbs.CurrencySectionsPagerAdapter
import java.util.*


class CurrencyContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockContentBinding
    lateinit var currencyInfo: StockInfoDto
    private var dateOfPurchase: Long = 946684800000 // 01.01.2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = intent.extras!!.getString("nameCurrency")
        supportActionBar!!.subtitle = intent.extras!!.getString("id")


        setData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.stock_toolbar_menu, menu)
        return true
    }

    private fun setAdapterAndToolbar(){
        val sectionsPagerAdapter = CurrencySectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        viewPager.offscreenPageLimit = 3
        viewPager.currentItem = 1
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            startPurchaseDialog()
        }
    }

    private fun setData(){
        val responseCall: Call<CurrencyInfoDto> = ApiClient.getUserService().getCurrencyInfo(
            intent.extras!!.getString("id")!!, getJWT(applicationContext)
        )
        responseCall.enqueue(object : Callback<CurrencyInfoDto> {
            override fun onResponse(call: Call<CurrencyInfoDto>?, response: Response<CurrencyInfoDto>?) {
                if (response?.isSuccessful!!) {
                    findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                    Toast.makeText(this@CurrencyContentActivity, "Данные получены успешно!", Toast.LENGTH_SHORT).show()
                    val response = response.body()!!
                    currencyInfo = StockInfoDto(
                        nameExchangeSource = "",
                        nameOtherExchanges = emptyList(),
                        currentPrice = response.currentPriceInRuble,
                        currencySign = "₽",
                        inPortfolio = response.inPortfolio,
                        dayInterval = response.dayInterval,
                        weekInterval = response.weekInterval,
                        monthInterval = response.monthInterval,
                        yearInterval = response.yearInterval,
                        fullInterval = response.fullInterval,
                        nameCompany = "",
                        nameCountry = "",
                        descriptionCompany = "",
                        branch = emptyList(),
                        sector = emptyList(),
                        transactions = emptyList(),
                        dividends = emptyList(),
                    )
                    setAdapterAndToolbar()
                } else {
                    Toast.makeText(this@CurrencyContentActivity, "Ошибка при получении данных!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CurrencyInfoDto>?, t: Throwable?) {
                Toast.makeText(this@CurrencyContentActivity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startPurchaseDialog(){
        // get prompts.xml view
        val li = LayoutInflater.from(applicationContext)
        val promptsView: View = li.inflate(R.layout.buy_dialog_fragment, null)

        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView)

        // set dialog message
        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton("Добавить в портфель", null)
            .setNegativeButton("Отмена",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        // create alert dialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()

        // add data on spinner
        val portfoliosSpinner: AppCompatSpinner = promptsView.findViewById(R.id.spinner_portfolios)
        portfoliosSpinner.adapter = ArrayAdapter<String>(
            this@CurrencyContentActivity,
            R.layout.list_of_spinner,
            intent.extras!!.get("portfoliosName") as Array<String>
        )

        val currencySpinner: AppCompatSpinner = promptsView.findViewById(R.id.spinner_currency)
        currencySpinner.adapter = ArrayAdapter<String>(
            this@CurrencyContentActivity,
            R.layout.list_of_spinner,
            listOf("₽", "$", "€")
        )

        // add listener on editText
        val dateEditText: TextInputEditText = promptsView.findViewById(R.id.date_text_field)
        dateEditText.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val mYear = calendar.get(Calendar.YEAR)
            val mMonth = calendar.get(Calendar.MONTH)
            val mDay = calendar.get(Calendar.DAY_OF_MONTH)

            //show dialog
            val datePickerDialog = DatePickerDialog(this, R.style.dataPickerDialogStyle,
                { view, year, month, dayOfMonth ->
                    dateEditText.setText(dayOfMonth.toString() + "/" + (month + 1).toString() + "/" + year.toString())
                    dateOfPurchase = GregorianCalendar(year, month, dayOfMonth).timeInMillis
                },
                mYear,
                mMonth,
                mDay
            )

            datePickerDialog.datePicker.minDate = 946684800000 // 01.01.2000
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            datePickerDialog.show()
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.lightBar_color))
        }

        // show it
        alertDialog.show()

        val calendar = Calendar.getInstance()
        val curYear = calendar.get(Calendar.YEAR)
        val curMonth = calendar.get(Calendar.MONTH)
        val curDay = calendar.get(Calendar.DAY_OF_MONTH)
        dateOfPurchase = GregorianCalendar(curYear, curMonth, curDay).timeInMillis
        dateEditText.setText(curDay.toString() + "/" + (curMonth + 1).toString() + "/" + curYear.toString())

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val numberOfPortfolio = portfoliosSpinner.selectedItemPosition
            val portfolioDateOfCreation = (intent.extras!!.get("portfoliosDates") as LongArray)[numberOfPortfolio]
            val count = promptsView.findViewById<TextInputEditText>(R.id.count_text_field).text.toString().toIntOrNull()
            val price = promptsView.findViewById<TextInputEditText>(R.id.price_text_field).text.toString().toDoubleOrNull()
            val commission = promptsView.findViewById<TextInputEditText>(R.id.commission_text_field).text.toString().toDoubleOrNull()
            val currencyTicker = when(currencySpinner.selectedItemId){
                0L -> "rub"
                1L -> "usd"
                else -> "eur"
            }
            if (portfolioDateOfCreation >  dateOfPurchase)
                Toast.makeText(this@CurrencyContentActivity, "Ошибка! Выбранная дата предшествует дате создания портфеля", Toast.LENGTH_SHORT).show()
            else if (count == null || count <= 0)
                Toast.makeText(this@CurrencyContentActivity, "Количество должно быть целым числом!", Toast.LENGTH_SHORT).show()
            else if (price == null || price <= 0)
                Toast.makeText(this@CurrencyContentActivity, "Цена не является вещественным числом!", Toast.LENGTH_SHORT).show()
            else if (commission == null || commission <= 0)
                Toast.makeText(this@CurrencyContentActivity, "Коммиссия не является вещественным числом!", Toast.LENGTH_SHORT).show()
            else {
                queryToAddCurrencyToPortfolio(
                    CompositionOfPortfolioDto(
                        idPortfolio = (intent.extras!!.get("portfoliosId") as IntArray)[portfoliosSpinner.selectedItemPosition],
                        ticker = intent.extras!!.getString("id")!!,
                        idExchange = -1,
                        date = dateOfPurchase,
                        count = count,
                        priceOfUnit = price,
                        commission = commission,
                        currencyCommissionTicker = currencyTicker,
                    ))
                alertDialog.dismiss()
            }
        }
    }

    private fun queryToAddCurrencyToPortfolio(composition: CompositionOfPortfolioDto){
        val responseCall: Call<Boolean> = ApiClient.getUserService().addCurrencyToPortfolio(
            composition, getJWT(applicationContext)
        )
        responseCall.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                if (response?.isSuccessful!! && response.body() == true)
                    Toast.makeText(this@CurrencyContentActivity, "Добавление валюты прошло успешно!", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@CurrencyContentActivity, "Ошибка! Попробуйте добавить валюту снова", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Toast.makeText(this@CurrencyContentActivity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

