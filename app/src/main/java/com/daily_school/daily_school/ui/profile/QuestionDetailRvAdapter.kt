package com.daily_school.daily_school.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daily_school.daily_school.databinding.ProfileQuestionDetailRvItemBinding

//자주 묻는 질문에 대한 대답 화면 adapter 구현
class QuestionDetailRvAdapter (private val items : MutableList<QuestionDetailModel>) : RecyclerView.Adapter<QuestionDetailRvAdapter.ViewHolder>(){

    class ViewHolder(binding : ProfileQuestionDetailRvItemBinding) : RecyclerView.ViewHolder(binding.root){

        val detailTextView : TextView = binding.questionDetailTextVIew

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuestionDetailRvAdapter.ViewHolder {

        val view = ProfileQuestionDetailRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.detailTextView.text = item.questionContent

    }

    override fun getItemCount(): Int {
        return items.size
    }
}