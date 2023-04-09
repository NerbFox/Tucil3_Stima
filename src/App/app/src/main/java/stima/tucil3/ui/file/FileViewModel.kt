package stima.tucil3.ui.file

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Chosen file"
    }
    val text: LiveData<String> = _text
}