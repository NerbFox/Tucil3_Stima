package stima.tucil3.ui.maps

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import stima.tucil3.databinding.FragmentMapsBinding
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import stima.tucil3.R
import stima.tucil3.algo.Algo
import stima.tucil3.algo.Input
import stima.tucil3.ui.file.FileViewModel
import java.io.InputStream
import java.lang.Exception

class MapsFragment : Fragment(), OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    //TODO: Implement

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var selectedMatrix: Uri? = null
    private var selectedCoor: Uri? = null
    private var algoType: Int = 0
    private var startIdx: Int = 0
    private var goalIdx: Int = 0
    private val paths = arrayOf("UCS", "A*")

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

                    val fileViewModel =
                        ViewModelProvider(this)[FileViewModel::class.java]
                    val fileName: TextView = binding.textMatrixname
                    fileViewModel.text.observe(viewLifecycleOwner) {
                        fileName.text = fileContents
                    }
                }
                catch (e: Exception){
                    val fileViewModel =
                        ViewModelProvider(this)[FileViewModel::class.java]
                    val fileName: TextView = binding.textCoorname
                    fileViewModel.text.observe(viewLifecycleOwner) {
                        fileName.text = e.toString()
                    }
                }
            }
        }
        else if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_CANCELED){
            selectedMatrix = null
            val fileViewModel =
                ViewModelProvider(this)[FileViewModel::class.java]
            val fileName: TextView = binding.textMatrixname
            fileViewModel.text.observe(viewLifecycleOwner) {
                fileName.text = resources.getString(R.string.unchosen_label)
            }
        }

        if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK) {
            selectedCoor = data?.data // The URI with the location of the file
            selectedCoor?.let { uri ->
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
                val fileContents = inputStream?.readBytes()?.toString(Charsets.UTF_8) ?: ""

                try {
                    val validator = Input()
                    validator.readCoor(fileContents)

                    val fileViewModel =
                        ViewModelProvider(this)[FileViewModel::class.java]
                    val fileName: TextView = binding.textCoorname
                    fileViewModel.text.observe(viewLifecycleOwner) {
                        fileName.text = fileContents
                    }

                    val startSelector: Spinner = binding.startDropdown
                    val startAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, validator.names)
                    startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    startSelector.adapter = startAdapter

                    val goalSelector: Spinner = binding.goalDropdown
                    val goalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, validator.names)
                    goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    goalSelector.adapter = goalAdapter
                }
                catch (e: Exception){
                    val fileViewModel =
                        ViewModelProvider(this)[FileViewModel::class.java]
                    val fileName: TextView = binding.textCoorname
                    fileViewModel.text.observe(viewLifecycleOwner) {
                        fileName.text = e.toString()
                    }

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
        }
        else if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_CANCELED){
            selectedCoor = null
            val fileViewModel =
                ViewModelProvider(this)[FileViewModel::class.java]
            val fileName: TextView = binding.textCoorname
            fileViewModel.text.observe(viewLifecycleOwner) {
                fileName.text = resources.getString(R.string.unchosen_label)
            }

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
        //file
        val fileViewModel =
            ViewModelProvider(this)[FileViewModel::class.java]

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root



        //maps
        val mapsViewModel =
            ViewModelProvider(this)[MapsViewModel::class.java]
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //text initializations
        val matrixName: TextView = binding.textMatrixname
        val coorName: TextView = binding.textCoorname
        fileViewModel.text.observe(viewLifecycleOwner) {
            matrixName.text = resources.getString(R.string.unchosen_label)
            coorName.text = resources.getString(R.string.unchosen_label)
        }

        //buttons
        val matrixButton: Button = binding.buttonGetmatrix
        matrixButton.setOnClickListener{
            val intent = Intent()
                .setType("text/plain")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 1)
        }

        val coorButton: Button = binding.buttonGetcoordinates
        coorButton.setOnClickListener{
            val intent = Intent()
                .setType("text/plain")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 2)
        }

        val runButton: Button = binding.buttonRun
        runButton.setOnClickListener{
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
                Algo().runAlgo(matrixContents!!, coorContents!!, algoType, startIdx, goalIdx)
            }
        }

        val modeSelector: Spinner = binding.algoDropdown
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paths)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        modeSelector.adapter = adapter
        modeSelector.onItemSelectedListener = this

        val startSelector: Spinner = binding.startDropdown
        val startAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("No valid file chosen"))
        startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        startSelector.adapter = startAdapter
        startSelector.onItemSelectedListener = this

        val goalSelector: Spinner = binding.goalDropdown
        val goalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOf("No valid file chosen"))
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        goalSelector.adapter = goalAdapter
        goalSelector.onItemSelectedListener = this

        return root
    }

    override fun onMapReady(p0: GoogleMap) {}

    override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
        when(parent!!.id){
            R.id.algoDropdown ->{
                println("Chosen Algo is " + position.toString())
                algoType = position
            }
            R.id.startDropdown ->{
                println("Start is " + position.toString())
                startIdx = position
            }
            R.id.goalDropdown ->{
                println("goal is " + position.toString())
                goalIdx = position
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}