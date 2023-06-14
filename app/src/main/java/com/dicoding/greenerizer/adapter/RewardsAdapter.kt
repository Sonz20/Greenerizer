package com.dicoding.greenerizer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.data.response.RewardsResponseItem
import com.dicoding.greenerizer.ui.rewards.RewardsFragmentDirections


class RewardsAdapter(
    private val listRewards: List<RewardsResponseItem>,
) : RecyclerView.Adapter<RewardsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleJudul: TextView = view.findViewById(R.id.title_judul)
        val imageRewards: ImageView = view.findViewById(R.id.image_rewards)
        val rewardPoint: TextView = view.findViewById(R.id.value_reward)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rewards, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listRewards.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rewardsItem = listRewards[position]
        holder.titleJudul.text = rewardsItem.namareward
        holder.rewardPoint.text = rewardsItem.hargareward.toString()

        Glide.with(holder.itemView)
            .load(rewardsItem.urlgambar)
            .into(holder.imageRewards)

        holder.itemView.setOnClickListener {
            val toDetailRewards =
                RewardsFragmentDirections.actionFragmentRewardsToDetailRewardsFragment(
                    rewardsItem.urlgambar,
                    rewardsItem.namareward,
                    rewardsItem.hargareward,
                    rewardsItem.deskripsi,
                )
            holder.itemView.findNavController().navigate(toDetailRewards)
        }

    }
}