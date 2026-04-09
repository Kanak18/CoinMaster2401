package com.coinmasterspinlink.cmspinmaster;

import android.content.Context; // Import the Context
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class SpinAdapter extends RecyclerView.Adapter<SpinAdapter.SpinViewHolder> {
    private List<RewardsResponse.Reward> rewards;
    private Context context; // Store the context

    public SpinAdapter(Context context, List<RewardsResponse.Reward> rewards) { // Add context parameter
        this.context = context; // Initialize the context
        this.rewards = rewards;
    }

    @NonNull
    @Override
    public SpinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reward, parent, false);
        return new SpinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpinViewHolder holder, int position) {
        RewardsResponse.Reward reward = rewards.get(position);
        Log.d("SpinAdapter", "onBindViewHolder position: " + position + ", title: " + reward.getTitle());
        holder.titleTextView.setText(reward.getTitle());
        holder.authorTextView.setText(reward.getAuthorName());
        holder.dateTextView.setText(reward.getPostDate());

        Log.d("SpinAdapter", "Author image URL: " + reward.getAuthorPic());

        // Load author image using Glide
        if (reward.getAuthorPic() != null && !reward.getAuthorPic().isEmpty()) {
            Glide.with(context)
                    .load(reward.getAuthorPic())
                    .placeholder(R.drawable.ic_coin)
                    .error(R.drawable.ic_coin)
                    .into(holder.authorImageView);
        } else {
            holder.authorImageView.setImageResource(R.drawable.ic_coin);
        }

        // Set click listener on the entire item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpinRewardActivity.class);
            intent.putExtra("title", reward.getTitle());
            intent.putExtra("author", reward.getAuthorName());
            intent.putExtra("date", reward.getPostDate());
            intent.putExtra("authorPic", reward.getAuthorPic());
            intent.putExtra("link", reward.getLink());
            context.startActivity(intent);
        });

        holder.btnAction.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpinRewardActivity.class);
            intent.putExtra("title", reward.getTitle());
            intent.putExtra("author", reward.getAuthorName());
            intent.putExtra("date", reward.getPostDate());
            intent.putExtra("authorPic", reward.getAuthorPic());
            intent.putExtra("link", reward.getLink());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        Log.d("SpinAdapter", "getItemCount: " + rewards.size());
        return rewards.size();
    }

    static class SpinViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        TextView dateTextView;
        ImageView authorImageView;
        MaterialButton btnAction;

        public SpinViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            authorImageView = itemView.findViewById(R.id.authorImageView);
            btnAction = itemView.findViewById(R.id.btnAction);
        }
    }

}
