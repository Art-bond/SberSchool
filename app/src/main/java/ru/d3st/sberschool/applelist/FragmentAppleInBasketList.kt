package ru.d3st.sberschool.applelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.d3st.sberschool.applelist.domain.AppleDataModel
import ru.d3st.sberschool.applelist.domain.BasketDataModel
import ru.d3st.sberschool.applelist.domain.TotalDataModel
import ru.d3st.sberschool.R
import ru.d3st.sberschool.databinding.FragmentAppleInBasketListBinding


class FragmentAppleInBasketList : Fragment() {

    private val dataList = mutableListOf<Any>()


    private var _binding: FragmentAppleInBasketListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var touchHelper: ItemTouchHelper? = null

    var basketIds: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppleInBasketListBinding.inflate(inflater, container, false)




        dataList.add(BasketDataModel(basketIds))
        basketIds += 1
        dataList.add(TotalDataModel(0))

        val adapter = DataAdapter(requireContext(), dataList, object : OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
                if (viewHolder != null) {
                    touchHelper?.startDrag(viewHolder)
                }
            }
        })
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvList.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)

        val res = ContextCompat.getDrawable(requireContext(), R.drawable.divider_drawable);

        if (res != null) {
            dividerItemDecoration.setDrawable(res)
        }
        binding.rvList.addItemDecoration(dividerItemDecoration)

        binding.removeAllBasket.setOnClickListener {
            val listApples: List<Any> = dataList.filter {
                it::class.java == AppleDataModel::class.java
            }
            val listBasket = dataList.filter {
                it::class.java == BasketDataModel::class.java
            }
            dataList.removeAll(listApples)
            dataList.removeAll(listBasket)
            basketIds=1
            binding.rvList.adapter?.notifyDataSetChanged()
        }

        binding.btnAddBasket.setOnClickListener {
            dataList.add(0, BasketDataModel(basketIds))
            basketIds += 1
            binding.rvList.adapter?.notifyDataSetChanged()
        }

        //init drag and swipe
        val callback: ItemTouchHelper.Callback = ApplesTouchHelperCallback(adapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper?.attachToRecyclerView(binding.rvList)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}