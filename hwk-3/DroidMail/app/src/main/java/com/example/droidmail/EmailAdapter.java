package com.example.droidmail;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.droidmail.emailutils.Email;

import java.util.ArrayList;

// Adapter to display message
public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {


    // holder for individual email view
    public static class ViewHolder extends  RecyclerView.ViewHolder {

        // TextView that holds individual email
        public TextView emailView;

        // Constructor
        public ViewHolder(View itemView) {
            super(itemView);
            emailView = (TextView) itemView.findViewById((R.id.emailRow));
        }
    }

    // private variables for Adapter
    private ArrayList<Email> emailList;
    private Resources mResources;

    // Constructor
    public EmailAdapter(ArrayList<Email> emails) {
        emailList = emails;
    }

    @NonNull
    public EmailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // get context and resources
        Context context = parent.getContext();
        mResources = context.getResources();

        // get email view from xml
        LayoutInflater inflater = LayoutInflater.from(context);
        View emailView = inflater.inflate(R.layout.inboxview_row, parent, false);

        return new ViewHolder(emailView);
    }

    public void onBindViewHolder (@NonNull EmailAdapter.ViewHolder holder, int position) {

        // holds what text to display
        String emailText;

        // special case for if inbox is empty
        if (emailList.size() == 0) {
            emailText = mResources.getString(R.string.empty_inbox_message);
        } else {
            // get email from array, set text to formatted email string
            Email email = emailList.get(position);
            emailText = mResources.getString(R.string.email_display, email.from, email.body);
        }

        // set text inside email view
        TextView textView = holder.emailView;
        textView.setText(emailText);
    }

    public int getItemCount() {

        // return 1 if inbox is empty
        // ensures adapter runs once to display empty inbox text
        // otherwise return proper size
        int size = emailList.size();
        return size == 0 ? 1 : size;
    }
}
