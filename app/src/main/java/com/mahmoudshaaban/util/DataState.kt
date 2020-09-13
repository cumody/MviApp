package com.mahmoudshaaban.util

data class DataState<T>(
    var message: Event<String>? = null,
    var loading: Boolean? = null,
    var data: Event<T>? = null
) {

    companion object {
        // we will return different methods for different cases
        fun <T> error(message: String): DataState<T> {
            return DataState(
                // in case error data we has no data to be returned
                message = Event(message), loading = false, data = null
            )
        }

        fun <T> loading(isloading: Boolean): DataState<T> {
            return DataState(message = null, loading = isloading, data = null)
        }

        fun <T> data(message: String? = null, data: T? = null): DataState<T> {
            return DataState(message = Event.messageEvent(message), loading = false, data = Event.dataEvent(data))
        }



    }

    override fun toString(): String {
        return "DataState(message=$message, loading=$loading, data=$data)"
    }


}