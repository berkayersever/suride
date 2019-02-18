package com.example.myfirstapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Emre on 05-May-18.
 */

public class CommentsAdapter extends ArrayAdapter<Ride.Comment> {

//    private List<Ride.Comment> commentsList;

    public CommentsAdapter(@NonNull Context context, List<Ride.Comment> resource) {
        super(context, R.layout.custom_row_comments_list, resource);
//        commentsList = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater dummyInflater = LayoutInflater.from(getContext());
        View customView = dummyInflater.inflate(R.layout.custom_row_comments_list, parent, false);

        Ride.Comment singleCommentItem = getItem(position);

//        TextView cID = (TextView) customView.findViewById(R.id.commentID);
        TextView cText = (TextView) customView.findViewById(R.id.commentText);
        TextView cOwner = (TextView) customView.findViewById(R.id.commentOwner);
        TextView cDate = (TextView) customView.findViewById(R.id.commentDate);

//        cID.setText(Integer.toString(position));
        cText.setText(singleCommentItem.getText());
        cOwner.setText(singleCommentItem.getOwnerUsername());
        cDate.setText(new SimpleDateFormat("HH:mm").format(singleCommentItem.getDate()));

        return customView;
    }
}
