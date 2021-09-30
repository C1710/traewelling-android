package de.traewelling.ui.include.status

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.traewelling.api.TraewellingApi
import de.traewelling.api.models.status.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatusCardViewModel(
    val status: Status
) : ViewModel() {

    private val _liked = MutableLiveData(status.liked)
    val liked: LiveData<Boolean> get() = _liked

    fun handleFavoriteClick(status: Status) {
        if (liked.value!!)
            deleteFavorite(status)
        else
            createFavorite(status)
    }

    private fun createFavorite(status: Status) {
        TraewellingApi.checkInService.createFavorite(status.id)
            .enqueue(object: Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful)
                        _liked.value = true
                    else
                        Log.e("StatusCardViewModel", response.toString())
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e("StatusCardViewModel", t.stackTraceToString())
                }
            })
    }

    private fun deleteFavorite(status: Status) {
        TraewellingApi.checkInService.deleteFavorite(status.id)
            .enqueue(object: Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful)
                        _liked.value = false
                    else
                        Log.e("StatusCardViewModel", response.toString())
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e("StatusCardViewModel", t.stackTraceToString())
                }
            })
    }
}