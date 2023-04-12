package stima.tucil3.ui.maps

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import stima.tucil3.R
import stima.tucil3.algo.Algo
import stima.tucil3.algo.Input
import stima.tucil3.databinding.FragmentMapsBinding
import java.io.InputStream


class MapsFragment : Fragment(), OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    private var map: GoogleMap? = null

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var selectedMatrix: Uri? = null
    private var selectedCoor: Uri? = null
    private var algoType: Int = 0
    private var startIdx: Int = 0
    private var goalIdx: Int = 0
    private val runner: Algo = Algo()


    private var lastPick: Int = 0

    private val markerList: MutableList<Marker> = ArrayList()
    private val lineList: MutableList<Polyline> = ArrayList()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK) {
            selectedMatrix = data?.data // The URI with the location of the file
            selectedMatrix?.let { uri ->
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
                val fileContents = inputStream?.readBytes()?.toString(Charsets.UTF_8) ?: ""

                try {
                    val validator = Input()
                    validator.readMatrix(fileContents)

                    binding.textMatrixname.text = fileContents
                }
                catch (e: Exception){
                    binding.textMatrixname.text = e.message
                }
            }
        }
        else if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_CANCELED){
            selectedMatrix = null
            binding.textMatrixname.text = resources.getString(R.string.unchosen_label)
        }

        if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK) {
            map!!.clear()
            markerList.clear()
            lineList.clear()

            selectedCoor = data?.data // The URI with the location of the file
            selectedCoor?.let { uri ->
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
                val fileContents = inputStream?.readBytes()?.toString(Charsets.UTF_8) ?: ""

                try {
                    val validator = Input()
                    validator.readCoor(fileContents)

                    binding.textCoorname.text = fileContents

                    val startAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, validator.names)
                    startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.startDropdown.adapter = startAdapter

                    val goalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, validator.names)
                    goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.goalDropdown.adapter = goalAdapter

                    for(i in 0 until validator.names.size){
                        val coor = validator.coordinates[i]
                        val place = MarkerOptions().position(LatLng(coor.first, coor.second)).title(validator.names[i]).alpha(0.5F)
                        markerList.add(map!!.addMarker(place)!!)
                    }

                    map!!.animateCamera(CameraUpdateFactory.newLatLng(LatLng(validator.coordinates[0].first, validator.coordinates[0].second)))

                    map!!.setOnMarkerClickListener { marker ->
                        println(lastPick)
                        if(lastPick == 0) binding.startDropdown.setSelection(markerList.indexOf(marker))
                        else binding.goalDropdown.setSelection(markerList.indexOf(marker))
                        true
                    }
                }
                catch (e: Exception){
                    binding.textCoorname.text = e.message

                    val startAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("No valid file chosen"))
                    startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.startDropdown.adapter = startAdapter

                    val goalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("No valid file chosen"))
                    goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.goalDropdown.adapter = goalAdapter
                }
            }
        }
        else if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_CANCELED){
            map!!.clear()
            markerList.clear()
            lineList.clear()

            selectedCoor = null
            binding.textCoorname.text = resources.getString(R.string.unchosen_label)

            val startSelector: Spinner = binding.startDropdown
            val startAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("No valid file chosen"))
            startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            startSelector.adapter = startAdapter

            val goalSelector: Spinner = binding.goalDropdown
            val goalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("No valid file chosen"))
            goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            goalSelector.adapter = goalAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        //maps
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //text initializations
        binding.textMatrixname.text = resources.getString(R.string.unchosen_label)
        binding.textCoorname.text = resources.getString(R.string.unchosen_label)

        //buttons
        binding.buttonGetmatrix.setOnClickListener{
            val intent = Intent()
                .setType("text/plain")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 1)
        }

        binding.buttonGetcoordinates.setOnClickListener{
            val intent = Intent()
                .setType("text/plain")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 2)
        }

        binding.buttonRun.setOnClickListener{
            for(line in lineList) line.remove()
            lineList.clear()
            try{
                if(selectedMatrix != null && selectedCoor != null){
                    var matrixContents: String? = null
                    var coorContents: String? = null
                    selectedMatrix?.let { uri ->
                        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
                        matrixContents = inputStream?.readBytes()?.toString(Charsets.UTF_8) ?: ""
                    }
                    selectedCoor?.let { uri ->
                        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
                        coorContents = inputStream?.readBytes()?.toString(Charsets.UTF_8) ?: ""
                    }
                    runner.runAlgo(matrixContents!!, coorContents!!, algoType, startIdx, goalIdx)

                    // P.S ini butuh akun billing, for now pake vektor aja dulu :(
                    //println("Tasking start!")
                    //MapsFetch(this@MapsFragment).execute(MapsUtil().getUrl(place1.position, place2.position))

                    //oke ini vektor
                    val latLngList = ArrayList<LatLng>()

                    var result = ""

                    for(i in runner.path){
                        latLngList.add(LatLng(runner.data.coordinates[i].first, runner.data.coordinates[i].second))

                        result += runner.data.names[i]
                        if(i != runner.path.last()){
                            result += "\n"
                        }
                    }

                    lineList.add(map!!.addPolyline(PolylineOptions().addAll(latLngList).color(Color.GREEN)))

                    binding.textResult.text = result
                    binding.textDistance.text = runner.distanceD.toString()
                }
                else{
                    binding.textResult.text = resources.getString(R.string.invalid_label) + ", File is null"
                    binding.textDistance.text = resources.getString(R.string.invalid_label) + ", File is null"
                }
            } catch (e: Exception){
                binding.textResult.text = resources.getString(R.string.invalid_label) + ", " + e.message
                binding.textDistance.text = resources.getString(R.string.invalid_label) + ", " + e.message
            }
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("UCS", "A*"))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.algoDropdown.adapter = adapter
        binding.algoDropdown.onItemSelectedListener = this

        val startAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("No valid file chosen"))
        startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.startDropdown.adapter = startAdapter
        binding.startDropdown.onItemSelectedListener = this

        val goalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("No valid file chosen"))
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.goalDropdown.adapter = goalAdapter
        binding.goalDropdown.onItemSelectedListener = this

        return root
    }

    fun onTaskDone(vararg values: Any){
        println("Task Done!")
        map!!.addPolyline(values[0] as PolylineOptions)
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0

        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-6.890530517416122, 107.60979931154648), 16.5F))
    }

    override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
        when(parent!!.id){
            R.id.algoDropdown ->{
                println("Chosen Algo is $position")
                algoType = position
            }
            R.id.startDropdown ->{
                lastPick = 0
                for(line in lineList) line.remove()
                lineList.clear()
                println("Start is $position")
                try {
                    if(markerList.isNotEmpty()){
                        val mark: Marker = markerList[position]
                        if(goalIdx != startIdx) markerList[startIdx].alpha = 0.5F
                        mark.showInfoWindow()
                        mark.alpha = 1F

                        map!!.animateCamera(CameraUpdateFactory.newLatLng(LatLng(mark.position.latitude, mark.position.longitude)))
                    }
                } catch (e: Exception){
                    println(e.message)
                } finally {
                    startIdx = position
                }
            }
            R.id.goalDropdown ->{
                lastPick = 1
                for(line in lineList) line.remove()
                lineList.clear()
                println("goal is $position")
                try {
                    if(markerList.isNotEmpty()){
                        val mark: Marker = markerList[position]
                        if(goalIdx != startIdx) markerList[goalIdx].alpha = 0.5F
                        mark.showInfoWindow()
                        mark.alpha = 1F

                        map!!.animateCamera(CameraUpdateFactory.newLatLng(LatLng(mark.position.latitude, mark.position.longitude)))
                    }
                } catch (e: Exception){
                    println(e.message)
                } finally {
                    goalIdx = position
                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}