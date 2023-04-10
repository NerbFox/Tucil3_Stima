package stima.tucil3.algo

import android.os.AsyncTask
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONObject
import stima.tucil3.ui.maps.MapsFragment
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MapsFetch(inContext: MapsFragment) : AsyncTask<String, Void, MutableList<MutableList<HashMap<String, String>>>>() {
    private var mapsContext: MapsFragment = inContext
    private var directionMode = "walking"


    private fun downloadUrl(urlString: String): String{
        val data: String
        var input: InputStream? = null
        var connection: HttpURLConnection? = null
        try {
            val url = URL(urlString)
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            input = connection.inputStream
            val reader = BufferedReader(InputStreamReader(input))
            val buffer = StringBuffer()
            var line = reader.readLine()
            while (line != null){
                buffer.append(line)
                line = reader.readLine()
            }
            data = buffer.toString()
            reader.close()
        } catch (e: Exception){
            throw e
        } finally {
            input?.close()
            connection?.disconnect()
        }
        return data
    }

    override fun doInBackground(vararg strings: String?): MutableList<MutableList<HashMap<String, String>>> {

        println("Tasking part 1")

        val text = downloadUrl(strings[0].toString())
        println(text)
        val jObject = JSONObject(text)

        return MapsUtil().parse(jObject)!!
    }

    override fun onPostExecute(result: MutableList<MutableList<HashMap<String, String>>>) {

        println("Tasking part 2")
        println(result)

        var points: ArrayList<LatLng?>
        var lineOptions: PolylineOptions? = null
        for (i in 0 until result.size) {
            println(i)

            points = ArrayList()
            lineOptions = PolylineOptions()
            val path = result[i]
            for (j in path.indices) {
                val point = path[j]
                val lat = point["lat"]!!.toDouble()
                val lng = point["lng"]!!.toDouble()
                val position = LatLng(lat, lng)
                points.add(position)
            }
            lineOptions.addAll(points)
            lineOptions.width(10f)
        }

        if (lineOptions != null) {

            println("Tasking part 3")
            mapsContext.onTaskDone(lineOptions)
        }
    }
}