package com.codepath.nytimesapp.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimesapp.Models.Article;
import com.codepath.nytimesapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivThumbnail);
        imageView.setImageResource(0);
        TextView textView = (TextView) convertView.findViewById(R.id.tvTitle);

        textView.setText(article.getHeadline());

        String pictureUrl = article.getThumbnail();

        if (!TextUtils.isEmpty(pictureUrl)) {
            Picasso.with(getContext()).load(pictureUrl).into(imageView);
        } else {
            Picasso.with(getContext()).load(R.drawable.logo).resize(150, 0).into(imageView);
        }
        return convertView;
    }
}
