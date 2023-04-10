package stima.tucil3.algo

import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import org.json.JSONObject


class MapsUtil {
    fun getUrl(origin: LatLng, dest: LatLng): String{
        val originString = "origin=" + origin.latitude + "," + origin.longitude
        val destString = "destination=" + dest.latitude + "," + dest.longitude
        val modeString = "mode=walking"
        val parameters = originString + "&" + destString + "&" + modeString
        val output = "json"
        val url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyDVMrOzvvAszmFXVHZqf_dfLs-wfwTDz5M";
        return url
    }

    fun toMeters(latLng: LatLng): Pair<Double, Double>{
        return Pair(latLng.latitude * 111.139, latLng.longitude * 111.139)
    }

    private fun decodePoly(data: String): MutableList<LatLng>{
        val poly: MutableList<LatLng> = ArrayList()
        var idx = 0
        val len = data.length
        var lat = 0
        var lng = 0

        while (idx < len){
            var b: Int
            var shift = 0
            var result = 0
            do{
                b = data[idx++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if ((result and  1) != 0){
                (result shr 1).inv()
            } else{
                result shr 1
            }
            lat += dlat

            shift = 0
            result = 0
            do {
                b = data[idx++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if ((result and  1) != 0){
                (result shr 1).inv()
            } else{
                result shr 1
            }
            lng += dlng

            val p = LatLng((lat.toDouble()/1E5),(lng.toDouble()/1E5))
            poly.add(p)
        }

        return poly
    }

    fun parse(jObject: JSONObject): MutableList<MutableList<HashMap<String, String>>>? {
        val routes: MutableList<MutableList<HashMap<String, String>>> = ArrayList()
        val jRoutes: JSONArray
        var jLegs: JSONArray
        var jSteps: JSONArray
        jRoutes = jObject.getJSONArray("routes")
        for (i in 0 until jRoutes.length()) {
            jLegs = (jRoutes[i] as JSONObject).getJSONArray("legs")
            val path: MutableList<HashMap<String, String>> = ArrayList()
            for (j in 0 until jLegs.length()) {
                jSteps = (jLegs[j] as JSONObject).getJSONArray("steps")
                for (k in 0 until jSteps.length()) {
                    var polyline = ""
                    polyline = ((jSteps[k] as JSONObject)["polyline"] as JSONObject)["points"] as String
                    val list: List<LatLng> = decodePoly(polyline)
                    for (l in list.indices) {
                        val hm: HashMap<String, String> = HashMap()
                        hm["lat"] = java.lang.Double.toString(list[l].latitude)
                        hm["lng"] = java.lang.Double.toString(list[l].longitude)
                        path.add(hm)
                    }
                }
                routes.add(path)
            }
        }
        return routes
    }
}