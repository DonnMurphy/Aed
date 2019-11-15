package com.jambons.aed;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// ALL RECYCLER VIEW CODE BASED ON https://guides.codepath.com/android/using-the-recyclerview#using-the-recyclerview
public class deck_adapter extends RecyclerView.Adapter<deck_adapter.ViewHolder> {

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            public TextView nameTextView;
            public TextView damageTextView;
            public TextView healthTextView;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                nameTextView = (TextView) itemView.findViewById(R.id.tvCardName);
                damageTextView = (TextView) itemView.findViewById(R.id.tvCardDmg);
                healthTextView = (TextView) itemView.findViewById(R.id.tvCardHp);
            }
        }
    // Store a member variable for the cards
    private List<Card> mCards;

    // Pass in the Card array into the constructor
    public deck_adapter(List<Card> cards) {
        mCards = cards;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public deck_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View CardView = inflater.inflate(R.layout.view_deck_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(CardView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(deck_adapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Card Card = mCards.get(position);

        // Set item views based on your views and data model
        TextView textViewName = viewHolder.nameTextView;
        textViewName.setText(Card.getName());

        TextView textViewDmg = viewHolder.damageTextView;
        textViewDmg.setText(String.valueOf(Card.getDamage()));

        TextView textViewHp = viewHolder.healthTextView;
        textViewHp.setText(String.valueOf(Card.getHealth()));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mCards.size();
    }



    }

