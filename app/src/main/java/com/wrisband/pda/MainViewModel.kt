package com.wrisband.pda

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.wrisband.pda.data.api.APIEndpoints
import com.wrisband.pda.data.db.AppDatabase
import com.wrisband.pda.data.db.BookingDao
import com.wrisband.pda.data.models.Scanned
import com.wrisband.pda.data.db.ScannedDao
import com.wrisband.pda.data.models.APIResponse
import com.wrisband.pda.data.models.Booking
import com.wrisband.pda.data.models.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.thread

class MainViewModel(app: Application): AndroidViewModel(app) {

    private var dao: ScannedDao
    private var bookingDao: BookingDao
    private lateinit var api: APIEndpoints

    private val _bookings = MutableLiveData<APIResponse<List<Booking>>>()
    val bookings: LiveData<APIResponse<List<Booking>>> = _bookings

    private val _booking = MutableLiveData<APIResponse<Booking>>()
    val booking: LiveData<APIResponse<Booking>> = _booking

    init {
        val db = Room.databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration().build()

        dao = db.scannedDao()
        bookingDao = db.bookingDao()
        initRetrofit()
    }

    private val _scanned = MutableLiveData<Boolean?>()
    val scanned: LiveData<Boolean?> = _scanned

    private val _model = MutableLiveData<Scanned?>()
    val model: LiveData<Scanned?> = _model

    fun createOrUpdate(code: String) {
        thread(start = true) {
            val model = _model.value
            if(model == null) {
                dao.insert(Scanned(0, 1, code))
            }
            else {
                dao.updateCount(model.count + 1, model.id)
            }

            //
            _scanned.postValue(true)
        }
    }

    fun getScanned(day: String) {
        thread(start = true) {
            _model.postValue(dao.getByDate(day))
        }
    }

    private fun initRetrofit() {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

         this.api = Retrofit.Builder()
            .client(client).baseUrl("https://sol.twconline.co/api/")
            .addConverterFactory(GsonConverterFactory.create()).build().create(APIEndpoints::class.java)
    }

    fun loadUsers() {
        api.loadBookings().enqueue(object : Callback<APIResponse<List<Booking>>> {
            override fun onResponse(call: Call<APIResponse<List<Booking>>>, response: Response<APIResponse<List<Booking>>>) {
               if(response.isSuccessful) handleResponse(response.body()!!)
                else {
                    _bookings.value = APIResponse(
                        status = false,
                        message = "Unable to fetch bookings"
                    )
               }
            }

            override fun onFailure(call: Call<APIResponse<List<Booking>>>, t: Throwable) {
                _bookings.value = APIResponse(
                    status = false,
                    message = "Unable to fetch bookings"
                )
            }
        })
    }

    private fun handleResponse(response: APIResponse<List<Booking>>) {
        this._bookings.value = response
        val body = response.data!!
       thread(start = true) {
           bookingDao.insertAll(body)
       }
    }

    fun checkTicket(code: String, sn: String) {
        thread(start = true) {
            val model = bookingDao.getByCodeAndSN(sn, code)
            _booking.postValue(APIResponse(
                data = model,
                status = model != null,
                message = if (model != null) "Ticket found" else "Invalid code supplied")
            )
        }
    }
}