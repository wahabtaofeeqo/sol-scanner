package com.wrisband.pda.data.api

import com.wrisband.pda.data.models.APIResponse
import com.wrisband.pda.data.models.Booking
import com.wrisband.pda.data.models.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface APIEndpoints {

    @GET("bookings")
    fun loadBookings(): Call<APIResponse<List<Booking>>>
}