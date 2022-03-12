package com.example.involio

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.involio.ui.tabbs.SectionsPagerAdapter
import com.example.involio.databinding.ActivityStockContentBinding
import com.example.involio.R
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import classes.network.ApiClient
import classes.network.dto.SearchedElement
import classes.network.dto.StockInfoDto
import classes.network.utils.getJWT
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import classes.network.dto.PortfolioDto
import com.google.android.material.textfield.TextInputEditText
import android.widget.DatePicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.widget.EditText
import classes.network.dto.CompositionOfPortfolioDto
import com.google.android.material.textfield.TextInputLayout
import java.util.*


class StockContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockContentBinding
    lateinit var stockInfo: StockInfoDto
    private var dateOfPurchase: Long = 946684800000 // 01.01.2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.stock_toolbar_menu, menu)
        return true
    }

    private fun setAdapterAndToolbar(){
        binding = ActivityStockContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        viewPager.offscreenPageLimit = 4
        viewPager.currentItem = 1
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
//        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                //do stuff here
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = intent.extras!!.getString("nameCompany")
        supportActionBar!!.subtitle = intent.extras!!.getString("ticker") + " | " + intent.extras!!.getString("nameExchange")

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            startPurchaseDialog()
        }
    }

    private fun setData(){
        val responseCall: Call<StockInfoDto> = ApiClient.getUserService().getStockInfo(
            intent.extras!!.getString("ticker")!!, intent.extras!!.getInt("idExchange"), getJWT(applicationContext)
        )
        responseCall.enqueue(object : Callback<StockInfoDto> {
            override fun onResponse(call: Call<StockInfoDto>?, response: Response<StockInfoDto>?) {
                if (response?.isSuccessful!!) {
                    Toast.makeText(this@StockContentActivity, "Данные получены успешно!", Toast.LENGTH_SHORT).show()
                    stockInfo = response.body()!!
                    setAdapterAndToolbar()
                } else {
                    Toast.makeText(this@StockContentActivity, "Ошибка при получении данных!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StockInfoDto>?, t: Throwable?) {
                Toast.makeText(this@StockContentActivity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
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
            this@StockContentActivity,
            R.layout.list_of_spinner,
            intent.extras!!.get("portfoliosName") as Array<String>
        )

        val currencySpinner: AppCompatSpinner = promptsView.findViewById(R.id.spinner_currency)
        currencySpinner.adapter = ArrayAdapter<String>(
            this@StockContentActivity,
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
                Toast.makeText(this@StockContentActivity, "Ошибка! Выбранная дата предшествует дате создания портфеля", Toast.LENGTH_SHORT).show()
            else if (count == null || count <= 0)
                Toast.makeText(this@StockContentActivity, "Количество должно быть целым числом!", Toast.LENGTH_SHORT).show()
            else if (price == null || price <= 0)
                Toast.makeText(this@StockContentActivity, "Цена не является вещественным числом!", Toast.LENGTH_SHORT).show()
            else if (commission == null || commission <= 0)
                Toast.makeText(this@StockContentActivity, "Коммиссия не является вещественным числом!", Toast.LENGTH_SHORT).show()
            else {
                queryToAddStockToPortfolio(
                    CompositionOfPortfolioDto(
                    idPortfolio = (intent.extras!!.get("portfoliosId") as IntArray)[portfoliosSpinner.selectedItemPosition],
                    ticker = intent.extras!!.getString("ticker")!!,
                    idExchange = intent.extras!!.getInt("idExchange"),
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

    private fun queryToAddStockToPortfolio(composition: CompositionOfPortfolioDto){
        val responseCall: Call<Boolean> = ApiClient.getUserService().addStockToPortfolio(
            composition, getJWT(applicationContext)
        )
        responseCall.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {
                if (response?.isSuccessful!! && response.body() == true)
                    Toast.makeText(this@StockContentActivity, "Добавление акции прошло успешно!", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@StockContentActivity, "Ошибка! Попробуйте добавить акции снова", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Boolean>?, t: Throwable?) {
                Toast.makeText(this@StockContentActivity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

