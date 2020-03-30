package com.convertreal.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.convertreal.R
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DollarFragment: Fragment() {

    private var _viewHolder = viewHolder()

    var fragment: FragmentActivity? = activity

    var data = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var data_atual = data.format(LocalDate.now())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        var view: View = inflater.inflate(R.layout.dollar_fragment, container, false)

        this._viewHolder.dataAtual = view.findViewById(R.id.dataAtual)
        this._viewHolder.valorDollar = view.findViewById(R.id.valorDolarHoje)

        this.insertValues()

        return view
    }

    fun insertValues() {
        this._viewHolder.dataAtual.setText("${data_atual}")

        var cm: ConnectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo = cm.activeNetworkInfo

        if (networkInfo != null) {
            if (
                networkInfo.type == ConnectivityManager.TYPE_WIFI ||
                networkInfo.type == ConnectivityManager.TYPE_MOBILE) {

                var getDolarAsyncTask = object : AsyncTask<Unit, Unit, List<JSONObject>>() {
                    override fun doInBackground(vararg params: Unit?): List<JSONObject> {
                        var dolar = JSONArray(
                            URL("${getString(R.string.url_api)}/usd")
                                .readText()
                        ).getJSONObject(0)

                        return listOf(dolar)
                    }

                    override fun onPostExecute(result: List<JSONObject>?) {
                        super.onPostExecute(result)

                        val dolar = result?.get(0)

                        if (dolar != null) {
                            this@DollarFragment._viewHolder.valorDollar.setText(
                                ("US$ ${String.format("%.2f", dolar.getDouble("high"))}")
                                    .toString()
                            )
                        } else {
                            Toast.makeText(
                                fragment, "Valor do Dólar indisponível no momento!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                getDolarAsyncTask.execute()
            }
        } else {
            this._viewHolder.valorDollar.setText("Sem conexão com a Internet")
        }
    }

    private class viewHolder {
        lateinit var dataAtual: TextView
        lateinit var valorDollar: TextView
    }
}