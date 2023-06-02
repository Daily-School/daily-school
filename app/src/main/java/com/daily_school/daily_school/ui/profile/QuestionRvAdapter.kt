package com.daily_school.daily_school.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.ProfileQuestionRvItemBinding
import com.google.android.material.card.MaterialCardView

//자주 묻는 질문 adapter 구현
class QuestionRvAdapter (val context : Context, private val items : MutableList<QuestionModel>) : RecyclerView.Adapter<QuestionRvAdapter.ViewHolder>(){

    class ViewHolder(binding : ProfileQuestionRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val questionView : CardView = binding.questionLayout
        val questionTextView : TextView = binding.questionEachTextView
        val downBtn : ImageButton = binding.questionIcDown
        val questionRv : RecyclerView = binding.questionDetailRv
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionRvAdapter.ViewHolder {

        val view = ProfileQuestionRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: QuestionRvAdapter.ViewHolder, position: Int) {

        val item = items[position]

        holder.questionTextView.text = item.question

        // 질문 영역을 눌렀을 때
        holder.questionView.setOnClickListener{
            // 리사이클러 뷰의 상태가 VISIBLE 일 때
            if(holder.questionRv.visibility == View.VISIBLE){
                // visibilty를 GONE으로 바꿔줌
                holder.questionRv.visibility = View.GONE
                // icon의 방향을 down 방향으로 바꿔줌
                holder.downBtn.animate().apply {
                    duration = 200
                    rotation(0f)
                }
            }
            else{
                // visibility를 VISIBLE으로 바꿔줌
                holder.questionRv.visibility = View.VISIBLE
                // icon의 방향을 up 방향으로 바꿔줌
                holder.downBtn.animate().apply {
                    duration = 200
                    rotation(180f)
                }
            }
        }

        holder.questionRv.adapter = QuestionDetailRvAdapter(item.innerModel)
        holder.questionRv.layoutManager = GridLayoutManager(context, 1)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}