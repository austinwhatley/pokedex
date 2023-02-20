package com.whatley.pokedex.util

//Generic class that wraps around any object, take any data type, and a string
sealed class CallResponse<T>(val data: T? = null, val message: String? = null) {

    //This data that is taken will be a success response with data
    //returns the data and no message
    class Success<T>(data: T): CallResponse<T>(data, null)

    //If an error occurs, we cannot assume that there is data available
    //we will take the error message and thows an error message that the view
    //model can utilize.
    class Error<T>(data: T? = null, message: String) : CallResponse<T>(data, message)

    //This can be used to show loading progress. Will be hidden once we
    //get a success or failed response
    class Loading<T>(data: T? = null) : CallResponse<T>()
}

