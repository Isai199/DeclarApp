package com.isadev.declarapp

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isadev.declarapp.Utility.getMonth
import com.isadev.declarapp.Utility.setHeightLinearLayout

class DeclarationsAdapter(
        private val declarationsList: ArrayList<Declaration>,
        private val listener: OnItemClickListener): RecyclerView.Adapter<DeclarationsAdapter.MyViewHolder>() {
    private lateinit var context: Context
    private var minimized = true
    private var docName = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.card_declaration, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val declaration: Declaration = declarationsList[position]
        holder.tvTitle.text = getMonth(declaration.month!!)

        setHeightLinearLayout(holder.lyData, 0)
        holder.lyData.translationY = -400F
        holder.ivTriangle.rotation = 0F
        holder.ivTriangle.setOnClickListener {
            if (minimized) {
                setHeightLinearLayout(holder.lyData, 400)
                ObjectAnimator.ofFloat(holder.lyData, "translationY", 0f, 20F).apply {
                    duration = 300L
                    start()
                }
                holder.ivTriangle.rotation = 90F
                minimized = false
            } else {
                setHeightLinearLayout(holder.lyData, 0)
                holder.lyData.translationY = -400F
                holder.ivTriangle.rotation = 0F
                minimized = true
            }
        }

        holder.tvIntermediary.text = "$ ${declaration.intermediary}"
        holder.tvCash.text = "$ ${declaration.cash}"
        holder.tvIsr.text = "$ ${declaration.isr}"
        holder.tvTotalSat.text = "$ ${declaration.total_sat}"




        if (declaration.status == "pagado") {
            holder.cbStatus.isChecked = true
            holder.tvStatus.text = declaration.status
        } else {
            holder.cbStatus.isChecked = false
            holder.tvStatus.text = declaration.status
        }

        docName = declaration.year.toString() + ("0"+declaration.month)
        holder.cbStatus.setOnClickListener {
            if (holder.cbStatus.isChecked) {
                BDdeclarations("declarations")
                    .updateDeclaration("status", "pagado", docName)
                holder.cbStatus.isChecked = true
                holder.tvStatus.text = declaration.status
                listener.showMessage("La declaracion ha sido actualizada")
            } else {
                BDdeclarations("declarations")
                    .updateDeclaration("status", "pendiente", docName)
                holder.cbStatus.isChecked = false
                holder.tvStatus.text = declaration.status
                listener.showMessage("La declaracion ha sido actualizada")
            }
        }


        holder.lyDelete.setOnClickListener { listener.showPopup {
            BDdeclarations("declarations").deleteDeclaration(docName)
            declarationsList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, declarationsList.size)
            listener.showMessage("La declaracion ha sido eliminada")

        } }
    }

    override fun getItemCount(): Int {
        return declarationsList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.card_declaration_tv_date)
        val ivTriangle: ImageView = itemView.findViewById(R.id.card_declaration_iv_triangle)
        val lyData: LinearLayout = itemView.findViewById(R.id.card_declaration_ly_data)

        val tvIntermediary: TextView = itemView.findViewById(R.id.card_declaration_tv_intermediary_value)
        val tvCash: TextView = itemView.findViewById(R.id.card_declaration_tv_cash_value)
        val tvIsr: TextView = itemView.findViewById(R.id.card_declaration_tv_isr_value)
        val tvStatus: TextView = itemView.findViewById(R.id.card_declaration_tv_status_value)
        val tvTotalSat: TextView = itemView.findViewById(R.id.card_declaration_tv_total_sat_value)

        val cbStatus: CheckBox = itemView.findViewById(R.id.card_declaration_tv_status_value)

        val lyDelete: LinearLayout = itemView.findViewById(R.id.card_declaration_ly_delete)
    }

    interface OnItemClickListener {
        fun showMessage(message: String)
        fun showPopup(callback: () -> Unit)
    }
}