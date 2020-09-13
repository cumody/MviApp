package com.mahmoudshaaban.mviapp.ui

import com.mahmoudshaaban.util.DataState

interface DataStateListener {

      fun onDataStateChanged(datastate : DataState<*>?)
 }