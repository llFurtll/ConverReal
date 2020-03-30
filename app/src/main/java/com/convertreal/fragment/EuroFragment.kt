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

class EuroFragment: Fragment() {

    private var _viewHolder = viewHolder()

    var fragment: FragmentActivity? = activity

    var data = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var data_atual = data.format(LocalDate.now())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.euro_fragment, container, false)

        this._viewHolder.dataAtual = view.findViewById(R.id.dataAtual)
        this._viewHolder.valorEuro = view.findViewById(R.id.valorEuroHoje)

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
                var getEuroAsyncTask = object : AsyncTask<Unit, Unit, List<JSONObject>>() {
                    override fun doInBackground(vararg params: Unit?): List<JSONObject> {
                        var euro = JSONArray(
                            URL("${getString(R.string.url_api)}/eur")
                                .readText()
                        ).getJSONObject(0)

                        return listOf(euro)
                    }

                    override fun onPostExecute(result: List<JSONObject>?) {
                        super.onPostExecute(result)

                        val euro = result?.get(0)

                        if (euro != null) {
                            this@EuroFragment._viewHolder.valorEuro.setText(
                                ("EUR ${String.format("%.2f", euro.getDouble("high"))}"
                                        ).toString()
                            )
                        } else {
                            Toast.makeText(
                                fragment, "Valor do Euro indisponível no momento!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                getEuroAsyncTask.execute()
            }
        } else {
            this._viewHolder.valorEuro.setText("Sem conexão com a Internet")
        }
    }

    private class viewHolder() {
        lateinit var dataAtual: TextView
        lateinit var valorEuro: TextView
    }
}