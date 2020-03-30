package com.convertreal.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.convertreal.R
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class CalculatorFragment: Fragment() {

    private var _viewHolder = viewHolder()

    companion object {
        private const val COMPOUND_DRAWABLE_RIGHT_INDEX = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(
            R.layout.calculator_fragment,
            container,
            false)

        this._viewHolder.resultDolar = view.findViewById(R.id.text_dolar)
        this._viewHolder.resultEuro = view.findViewById(R.id.text_euro)
        this._viewHolder.valueReal = view.findViewById(R.id.edit_valor)
        this._viewHolder.calculateReal = view.findViewById(R.id.button_calculate)

        this.clearValues()

        this._viewHolder.calculateReal.setOnClickListener { calculateReal(it) }
        this._viewHolder.valueReal.makeClearableEditText(null, null)

        return view
    }

    fun clearValues() {
        this._viewHolder.resultDolar.setText("")
        this._viewHolder.resultEuro.setText("")
    }

    fun calculateReal(view: View) {
        var cm: ConnectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo = cm.activeNetworkInfo

        if (networkInfo != null) {
            if (
                networkInfo.type == ConnectivityManager.TYPE_WIFI  ||
                networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                var calculateRealAsyncTask = object : AsyncTask<Unit, Unit, List<JSONObject>>() {
                    override fun doInBackground(vararg params: Unit?): List<JSONObject> {

                        var dolar = JSONArray(
                            URL("${getString(R.string.url_api)}/usd")
                                .readText()
                        ).getJSONObject(0)

                        var euro = JSONArray(
                            URL("${getString(R.string.url_api)}/eur")
                                .readText()
                        ).getJSONObject(0)

                        return listOf<JSONObject>(dolar, euro)
                    }

                    override fun onPostExecute(result: List<JSONObject>?) {
                        super.onPostExecute(result)

                        val dolar = result!![0]
                        val euro = result!![1]

                        if (view.getId() == R.id.button_calculate) {
                            var value: String =
                                this@CalculatorFragment._viewHolder.valueReal.text.toString()
                            if (value.equals("")) {
                                Toast.makeText(activity, "Informe um valor!", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                var real: Double = value.toDouble()

                                this@CalculatorFragment._viewHolder.resultDolar.setText(
                                    "US$ ${String.format("%.2f", (real / dolar.getDouble("high")))}"
                                ).toString()

                                this@CalculatorFragment._viewHolder.resultEuro.setText(
                                    "EUR ${String.format("%.2f", (real / euro.getDouble("high")))}"
                                ).toString()
                            }
                        }
                    }
                }
                calculateRealAsyncTask.execute()
            }
        } else {
            Toast.makeText(activity,
                "Você não está conectado a Internet",
                Toast.LENGTH_LONG).show()
        }
    }


    fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }

    fun EditText.makeClearableEditText(
        onIsNotEmpty: (() -> Unit)?,
        onClear: (() -> Unit)?,
        clearDrawable: Drawable) {

        val updateRightDrawable = {
            this.setCompoundDrawables(
                null, null,
                if(text.isNotEmpty()) clearDrawable else null, null)
        }
        updateRightDrawable()

        this.doAfterTextChanged {
            if (text.isNotEmpty()) {
                onIsNotEmpty?.invoke()
            }
            updateRightDrawable()
        }

        this.onRightDrawableClicked {
            this.text.clear()
            this.setCompoundDrawables(null, null, null, null)
            onClear?.invoke()
            this.requestFocus()
        }
    }

    fun EditText.makeClearableEditText(onIsNotEmpty: (() -> Unit)?, onCleared: (() -> Unit)?) {
        compoundDrawables[COMPOUND_DRAWABLE_RIGHT_INDEX]?.let {
                clearDrawable ->
            makeClearableEditText(onIsNotEmpty, onCleared, clearDrawable)
        }
    }

    private class viewHolder {
        lateinit var resultDolar: TextView
        lateinit var resultEuro: TextView
        lateinit var valueReal: EditText
        lateinit var calculateReal: Button
    }
}