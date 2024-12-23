package com.example.bbvaapp.utils

import android.content.Context
import com.example.bbvaapp.R
import com.example.bbvaapp.model.InputPasswordError
import com.example.bbvaapp.model.InputEmailError
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MessageResolver @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun getErrorEmail(error: InputEmailError): String = when (error) {
        InputEmailError.EMPTY -> context.getString(R.string.err_empty_field)
        InputEmailError.INVALID_EMAIL -> context.getString(R.string.err_invalid_email)
    }

    fun getErrorPassword(error: InputPasswordError): String = when (error) {
        InputPasswordError.EMPTY -> context.getString(R.string.err_empty_field)
        InputPasswordError.LEAST_THAN_8_CHARACTERS -> context.getString(R.string.err_min_8_characters)
        InputPasswordError.LEAST_ONE_NUMBER -> context.getString(R.string.err_least_one_number)
        InputPasswordError.LEAST_ONE_SPECIAL_CHARACTER -> context.getString(R.string.err_least_one_character)
        InputPasswordError.LEAST_ONE_UPPERCASE -> context.getString(R.string.err_least_one_uppercase)
    }
}
