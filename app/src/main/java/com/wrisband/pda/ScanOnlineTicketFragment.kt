package com.wrisband.pda

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.wrisband.pda.databinding.FragmentScanOnlineTicketBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [ScanOnlineTicketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScanOnlineTicketFragment : Fragment() {

    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    companion object {
        @JvmStatic
        fun newInstance() = ScanOnlineTicketFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentScanOnlineTicketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = MainViewModel(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanOnlineTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        if(actionBar != null) actionBar.title = "Online Tickets"

        binding.code.doAfterTextChanged {
            val text = it.toString().trim()
            if(text.length >= 8) this.checkTicket(text)
        }

        // Ticket check response
        viewModel.booking.observe(requireActivity()) {
            val response = it?:return@observe
            if(response.data != null) {
                val model = response.data
                binding.code.text?.clear()

                if(model.event?.date == today) {
                    binding.event.text = model.event?.name
                    binding.caption.text = resources.getString(R.string.ticket_details, model.user?.name)
                }
                else {
                    binding.code.error = "Wrong date"
                }
            }
            else {
                binding.code.error = "Invalid"
            }

            //
            showToast(response.message)
            binding.progress.isVisible = false
        }

        // Load booking response
        viewModel.bookings.observe(requireActivity()) {
            val response = it?:return@observe
            showToast(response.message)
        }
    }

    private fun checkTicket(code: String) {
        val codes = code.split("#")
        if(codes.size == 2) {
            binding.progress.isVisible = true
            viewModel.checkTicket(codes[0], codes[1])
        }
    }

    private fun doImport() {
        viewModel.loadUsers()
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onCreateOptionsMenu(menu, inflater)",
        "androidx.fragment.app.Fragment")
    )
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_online_ticket, menu)
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onOptionsItemSelected(item)",
        "androidx.fragment.app.Fragment")
    )
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_events -> {
                this.doImport()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}