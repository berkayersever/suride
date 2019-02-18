package com.example.myfirstapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Emre on 28-May-18.
 */

public class CommentCursorAdapter extends CursorAdapter{

    public CommentCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.custom_row_comments_list, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Ride.Comment comment = new Ride.Comment(cursor);

        try {
//            TextView cID = (TextView) view.findViewById(R.id.commentID);
            TextView cText = (TextView) view.findViewById(R.id.commentText);
            TextView cOwner = (TextView) view.findViewById(R.id.commentOwner);
            TextView cDate = (TextView) view.findViewById(R.id.commentDate);

//            cID.setText(Long.toString(comment.getId()));
            String commentText = comment.getText();

            if (commentText.equals( "I have joined the ride.")){
                cText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                cText.setTypeface(cText.getTypeface(), Typeface.BOLD);
            } else {
                cText.setTextColor(Color.BLACK);
                cText.setTypeface(null, Typeface.NORMAL);

            }
            cText.setText(commentText);
            cOwner.setText(comment.getOwnerUsername());
            cDate.setText(new SimpleDateFormat("HH:mm").format(comment.getDate()));

        } catch (Exception e) {
            System.out.println(e.toString());
        }


    }

}
