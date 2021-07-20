package ru.d3st.sberschool.startscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import ru.d3st.sberschool.R
import ru.d3st.sberschool.databinding.FragmentStartBinding


class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)

        binding.btnToApples.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToAppleInBasketListFragment()
            findNavController().navigate(action)
        }
        binding.btnToSpeedometer.setOnClickListener {
            val action = StartFragmentDirections.actionStartFragmentToSpeedFragment()
            findNavController().navigate(action)
        }




        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}