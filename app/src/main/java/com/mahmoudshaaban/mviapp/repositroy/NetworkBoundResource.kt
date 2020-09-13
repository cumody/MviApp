package com.mahmoudshaaban.mviapp.repositroy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.mahmoudshaaban.mviapp.ui.main.state.MainViewState
import com.mahmoudshaaban.util.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource<ResponseObject, ViewStateType> {

    protected val result = MediatorLiveData<DataState<ViewStateType>>()

    init {
        result.value = DataState.loading(true)

        GlobalScope.launch(IO) {

            delay(Constants.TESTING_NETWORK_DELAY)

            withContext(Main) {

                val apiResponse = createCall()
                result.addSource(apiResponse) { response ->
                    // we remove it becasue we want to stop looking at it
                    result.removeSource(apiResponse)

                    handleNetworkCall(response)

                }


            }
        }
    }

    fun handleNetworkCall(response: GenericApiResponse<ResponseObject>) {
        when (response) {
            is ApiSuccessResponse -> {

                handleApisuccessResponse(response)
            }
            is ApiErrorResponse -> {
                println("DEBUG : NetworkBoundResource: ${response.errorMessage}")
                onReturnError(response.errorMessage)

            }
            is ApiEmptyResponse -> {
                println("DEBUG : NetworkBoundResource: Http 204. Return NOTHING")
                onReturnError("Http 204. Return NOTHING")

            }
        }
    }

    fun onReturnError(message : String){
        result.value = DataState.error(message)
    }

    abstract fun handleApisuccessResponse(response : ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>


    fun asLiveData() = result as LiveData<DataState<MainViewState>>


}