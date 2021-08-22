package ru.d3st.sberschool.applelist

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import ru.d3st.sberschool.R
import ru.d3st.sberschool.applelist.domain.AppleDataModel
import ru.d3st.sberschool.applelist.domain.BasketDataModel
import ru.d3st.sberschool.applelist.domain.TotalDataModel


const val TAG = "DataAdapter"

class DataAdapter(
    private val context: Context,
    val adapterDataList: MutableList<Any>,
    val dragStartListener: OnStartDragListener
) :
    RecyclerView.Adapter<BaseViewHolder<*>>(), ItemTouchHelperAdapter {

    companion object {
        private const val TYPE_APPLE = 0
        private const val TYPE_BASKET = 1
        private const val TYPE_TOTAL = 2
    }

    inner class AppleViewHolder(itemView: View) : BaseViewHolder<AppleDataModel>(itemView),
        ItemTouchHelperViewHolder {
        private val btnEatApple: Button = itemView.findViewById(R.id.btn_eat_apple)

        override fun bind(item: AppleDataModel) {
            btnEatApple.setOnClickListener {
                adapterDataList.removeAt(adapterPosition)

                notifyItemRemoved(adapterPosition)
                notifyItemChanged(adapterDataList.lastIndex)

            }

        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onItemSelected() {
            itemView.scaleX = 1.1f
            itemView.scaleY = 1.1f
            itemView.transitionAlpha = .9f
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onItemClear() {
            itemView.scaleX = 1.0f
            itemView.scaleY = 1.0f
            itemView.transitionAlpha = 1.0f

        }
    }


    inner class BasketViewHolder(itemView: View) : BaseViewHolder<BasketDataModel>(itemView) {
        private val btnAddApple: Button = itemView.findViewById(R.id.btn_basket)

        override fun bind(item: BasketDataModel) {
            btnAddApple.setOnClickListener {


                var countApples = 0
                val applesInBasket = adapterDataList.drop(adapterPosition.plus(1))
                if (applesInBasket.size >= 3) {
                    applesInBasket.take(3).forEach {
                        if (it::class.java == AppleDataModel::class.java) {
                            countApples++
                        }
                    }
                }
                if (countApples < 3) {
                    val apple = AppleDataModel(item.id, 1)
                    adapterDataList.add(adapterPosition + 1, apple)

                } else {
                    Toast.makeText(context, "Too much", Toast.LENGTH_SHORT).show()
                }
                notifyDataSetChanged()
            }
        }
    }

    inner class TotalViewHolder(itemView: View) : BaseViewHolder<TotalDataModel>(itemView) {
        private val tvTotal: TextView = itemView.findViewById(R.id.tv_result)
        override fun bind(item: TotalDataModel) {

            tvTotal.text = adapterDataList.filter {
                it::class.java == AppleDataModel::class.java
            }.size.toString()

        }
    }

    //--------onCreateViewHolder: inflate layout with view holder-------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_APPLE -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_apple, parent, false)
                AppleViewHolder(view)
            }
            TYPE_BASKET -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_basket, parent, false)
                BasketViewHolder(view)
            }
            TYPE_TOTAL -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.item_total, parent, false)
                TotalViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = adapterDataList[position]

        when (holder) {
            is AppleViewHolder -> holder.bind(element as AppleDataModel)
            is BasketViewHolder -> holder.bind(element as BasketDataModel)
            is TotalViewHolder -> holder.bind(element as TotalDataModel)
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val comparable = adapterDataList[position]
        return when (comparable) {
            is AppleDataModel -> TYPE_APPLE
            is BasketDataModel -> TYPE_BASKET
            is TotalDataModel -> TYPE_TOTAL
            else -> throw IllegalArgumentException("Invalid type of data $position")
        }
    }

    override fun getItemCount(): Int {
        return adapterDataList.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (toPosition > 0 && toPosition < adapterDataList.size.minus(1)) {

            if (fromPosition < toPosition) {
                downMove(toPosition, fromPosition)
            } else {
                toUpMove(toPosition, fromPosition)
            }

        }
    }




    override fun onItemDismiss(position: Int) {
        val comparable = adapterDataList[position]
        when (comparable) {
            is AppleDataModel -> {
                adapterDataList.removeAt(position)
                notifyItemRemoved(position)
            }
            is BasketDataModel -> removeApplesInBasket(position)

        }

    }
    private fun toUpMove(toPosition: Int, fromPosition: Int) {
        val toBasketIndex = adapterDataList.take(toPosition).indexOfLast {
            it::class.java == BasketDataModel::class.java
        }
        var countApples = 0
        val applesInBasket = adapterDataList.drop(toBasketIndex.plus(1))
        if (applesInBasket.size >= 3) {
            applesInBasket.take(3).forEach {
                if (it is AppleDataModel) {
                    countApples++
                }
            }
        }
        if (countApples < 3) {
            adapterDataList.swap(fromPosition, toPosition)
        } else {
            Toast.makeText(context, "Too Much", Toast.LENGTH_SHORT).show()
        }
    }

    private fun downMove(toPosition: Int, fromPosition: Int) {
        val toBasketIndex = adapterDataList.take(toPosition + 1).indexOfLast {
            it is BasketDataModel
        }
        var countApples = 0
        val applesInBasket = adapterDataList.drop(toBasketIndex.plus(1))
        if (applesInBasket.size >= 3) {
            applesInBasket.take(3).forEach {
                if (it::class.java == AppleDataModel::class.java) {
                    countApples++
                }
            }
        }
        if (countApples < 3) {
            adapterDataList.swap(fromPosition, toPosition)
        } else {
            Toast.makeText(context, "Too Much", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeApplesInBasket(position: Int) {
        adapterDataList.removeAt(position)
        notifyItemRemoved(position)

        while (adapterDataList[position] is AppleDataModel){
            adapterDataList.removeAt(position)
            notifyItemRemoved(position)
        }

    }

    private fun MutableList<Any>.swap(index1: Int, index2: Int) {
        val tmp = this[index1] // 'this' даёт ссылку на список
        this[index1] = this[index2]
        this[index2] = tmp
        notifyItemMoved(index1, index2)

    }
}