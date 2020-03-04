package com.jambons.aed;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

// ALL RECYCLER VIEW CODE BASED ON https://guides.codepath.com/android/using-the-recyclerview#using-the-recyclerview
public class deck_adapter extends RecyclerView.Adapter<deck_adapter.ViewHolder> {

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        private Context mContext ;
    private List<Sheep> sheepDeck;
     //private List<Sheep> sheepData ;
    RequestOptions option;
        public deck_adapter(Context mContext, List<Sheep> sheepList) {
            this.mContext = mContext;
            sheepDeck = sheepList;
            Log.wtf("Are there any sheep here", sheepDeck.toString());

            // Request option for Glide
            option = new RequestOptions().centerCrop().placeholder(R.drawable.splash_screen).error(R.drawable.sheep_dab);

        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            //public TextView nameTextView;
            //public TextView damageTextView;
            //public TextView healthTextView;
            public ImageView sheepImageView;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                //nameTextView = (TextView) itemView.findViewById(R.id.tvCardName);
                //damageTextView = (TextView) itemView.findViewById(R.id.tvCardDmg);
                //healthTextView = (TextView) itemView.findViewById(R.id.tvCardHp);
                sheepImageView = (ImageView) itemView.findViewById(R.id.ivCharacter);

            }
        }
    // Store a member variable for the cards


    // Pass in the Card array into the constructor
   // public deck_adapter(List<Sheep> sheepList) {
    //    sheepDeck = sheepList;
   // }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public deck_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View CardView = inflater.inflate(R.layout.view_deck_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(CardView);
        viewHolder.sheepImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent viewSheepIntent = new Intent(mContext,ViewSheep.class);
                viewSheepIntent.putExtra("sheep_id",sheepDeck.get(viewHolder.getAdapterPosition()).getSheepId());
                viewSheepIntent.putExtra("sheep_name",sheepDeck.get(viewHolder.getAdapterPosition()).getSheepName());
                viewSheepIntent.putExtra("sheep_uid",sheepDeck.get(viewHolder.getAdapterPosition()).getSheepUID());
                viewSheepIntent.putExtra("sheep_hp",sheepDeck.get(viewHolder.getAdapterPosition()).getSheepHp());
                viewSheepIntent.putExtra("sheep_dp",sheepDeck.get(viewHolder.getAdapterPosition()).getSheepDp());
                viewSheepIntent.putExtra("sheep_owner",sheepDeck.get(viewHolder.getAdapterPosition()).getSheepOwner());
                viewSheepIntent.putExtra("sheep_imageLink",sheepDeck.get(viewHolder.getAdapterPosition()).getSheepImageLink());
                viewSheepIntent.putExtra("sheep_mint_time",sheepDeck.get(viewHolder.getAdapterPosition()).getSheepMintedDate());

                //TODO - BAD CODING PRACTISE - IF SOLUTION FOUND APPLY
                viewSheepIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                mContext.startActivity(viewSheepIntent);

            }
        });

        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(deck_adapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        //ImageRequest sheepImageRequest = new ImageRequest(sheep.);
        Sheep sheep = sheepDeck.get(position);
        Log.wtf("SheepDeck Pos", sheep.toString());
        Log.wtf("SheepLinks", sheep.getSheepImageLink());
        Log.wtf("Sheep Options", option.toString());

        ImageView sheepImage = viewHolder.sheepImageView;
        Glide.with(mContext).load(sheep.getSheepImageLink()).apply(option).into(viewHolder.sheepImageView);
        ///sheepImage.setOnClickListener(new View.OnClickListener() {
          //  @Override
           // public void onClick(View v) {
                //Intent W = new Intent(getApplicationContext(),ViewSheep.class);
                //startActivity(W);
                //break;
                //Toast.makeText(this,"MEME SHEEP", Toast.LENGTH_LONG);
          //  }
        //});
        // Set item views based on your views and data model
        //TextView textViewName = viewHolder.nameTextView;
        //textViewName.setText(Card.getName());

        //TextView textViewDmg = viewHolder.damageTextView;
        //textViewDmg.setText(String.valueOf(Card.getDamage()));

        //TextView textViewHp = viewHolder.healthTextView;
        //textViewHp.setText(String.valueOf(Card.getHealth()));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return this.sheepDeck.size();
    }



    }

