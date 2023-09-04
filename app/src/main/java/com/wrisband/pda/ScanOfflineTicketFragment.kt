package com.wrisband.pda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.wrisband.pda.databinding.FragmentScanOfflineTicketBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScanOfflineTicketFragment : Fragment() {

    companion object {
        fun newInstance() = ScanOfflineTicketFragment()
    }

    private lateinit var today: String
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentScanOfflineTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainViewModel(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentScanOfflineTicketBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(Date())
        today = date.replace("-", "", true)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        if(actionBar != null) actionBar.title = "Offline Tickets"

        binding.code.doAfterTextChanged {
            val text = it.toString()
            if(text.length >= 8) {
                if (text != today) showToast("Code is not valid")
                else viewModel.createOrUpdate(text)
            }
        }

        viewModel.model.observe(requireActivity()) {
            val model = it?:return@observe
            binding.count.text = model.count.toString()
        }

        viewModel.scanned.observe(requireActivity()) {
            val res = it?:return@observe

            if(res) { // update
                viewModel.getScanned(today)
                binding.code.text?.clear()
                showToast("Scanned successfully")
            }
            else showToast("Not successful")
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}