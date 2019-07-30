package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utilitaire.ParseRelativeDate;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
   Context context;
   List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewtype) {
       View view= LayoutInflater.from(context).inflate(R.layout.item_tweet,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
     Tweet tweet = (Tweet) tweets.get(i);
     viewHolder.tvName.setText(tweet.user.name);
     String since_ = ParseRelativeDate.getRelativeTimeAgo(tweet.createAt);
     since_= since_.substring(0,since_.indexOf("."));
     viewHolder.tvSince.setText(since_);
     viewHolder.tvBody.setText( tweet.body);
     viewHolder.tvScreenName.setText(" @".concat(tweet.user.screenName));
        Glide.with(context).load(tweet.user.profileImageUrl).into(viewHolder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addTweets(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    // definir le viewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView  tvScreenName;
        TextView tvBody;
        TextView tvName;
        TextView tvSince;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            this.tvScreenName = itemView.findViewById(R.id.tvScreenName);
            this.tvBody = itemView.findViewById(R.id.tvBody);
            this.tvName = itemView.findViewById(R.id.tvName);
            this.tvSince = itemView.findViewById(R.id.tvSince);
        }
    }
}
