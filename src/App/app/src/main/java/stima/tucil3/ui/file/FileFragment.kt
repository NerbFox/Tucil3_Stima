package stima.tucil3.ui.file

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import stima.tucil3.databinding.FragmentFileBinding
import stima.tucil3.R
import stima.tucil3.algo.Algo
import stima.tucil3.algo.Input
import java.io.InputStream
import java.lang.Exception

class FileFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentFileBinding? = null
    private val binding get() = _binding!!

    private var selectedMatrix: Uri? = null
    private var selectedCoor: Uri? = null
    private var algoType: Int = 0
    private var startIdx: Int = 0
    private var goalIdx: Int = 0
    private val paths = arrayOf("UCS", "A*")
    private val runner: Algo = Algo()

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
                    binding.textCoorname.text = e.toString()
                }
            }
        }
        else if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_CANCELED){
            selectedMatrix = null
            binding.textMatrixname.text = resources.getString(R.string.unchosen_label)
        }

        if (requestCode == 2 && resultCode == AppCompatActivity.RESULT_OK) {
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
                }
                catch (e: Exception){
                    binding.textCoorname.text = e.toString()

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
        _binding = FragmentFileBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

                var result = ""

                for(i in runner.path){
                    result += runner.data.names[i]
                    if(i != runner.path.last()){
                        result += "\n"
                    }
                }

                binding.textResult.text = result
                binding.textDistance.text = runner.distanceD.toString()
            }
            else{
                binding.textResult.text = resources.getString(R.string.invalid_label)
                binding.textDistance.text = resources.getString(R.string.invalid_label)
            }
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paths)
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

    override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
        when(parent!!.id){
            R.id.algoDropdown ->{
                println("Chosen Algo is $position")
                algoType = position
            }
            R.id.startDropdown ->{
                println("Start is $position")
                startIdx = position
            }
            R.id.goalDropdown ->{
                println("goal is $position")
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