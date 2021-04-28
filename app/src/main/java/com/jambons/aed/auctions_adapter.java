package com.jambons.aed;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
 
    // ALL RECYCLER VIEW CODE BASED ON https://guides.codepath.com/android/using-the-recyclerview#using-the-recyclerview
    public class auctions_adapter extends RecyclerView.Adapter<com.jambons.aed.auctions_adapter.ViewHolder> {

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        private Context mContext ;
        private List<Auction> auctionList;

        //private List<Sheep> sheepData ;
        RequestOptions option;
        public auctions_adapter(Context mContext, List<Auction> auctions) {
            this.mContext = mContext;
            auctionList = auctions;
            FireUtils.getInstance(mContext);
            Log.wtf("Are there any Auction here", auctionList.toString());

            // Request option for Glide
            option = new RequestOptions().centerCrop().placeholder(R.drawable.splash_screen).error(R.drawable.sheep_dab);

        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            //public TextView nameTextView;
            //public TextView damageTextView;
            //public TextView healthTextView;
            public CardView auctionCardView;
            public ImageView auctionImageView;
            public TextView auctionSheepName;
            public TextView auctionTimeRemaining;
            public TextView auctionStatus;
            public TextView auctionCurrentPrice;
            public TextView auctionSeller;
            public TextView auctionCurrentPriceLabel;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                //nameTextView = (TextView) itemView.findViewById(R.id.tvCardName);
                //damageTextView = (TextView) itemView.findViewById(R.id.tvCardDmg);
                //healthTextView = (TextView) itemView.findViewById(R.id.tvCardHp);
                auctionCardView = (CardView) itemView.findViewById(R.id.auction_info);
                auctionImageView = (ImageView) itemView.findViewById(R.id.ivAuctionCharacter);
                auctionCurrentPrice = itemView.findViewById(R.id.tvViewAuctionsCurrentPrice);
                auctionSheepName = itemView.findViewById(R.id.tvViewAuctionsSheepName);
                auctionStatus = itemView.findViewById(R.id.tvAuctionStatus);
                auctionSeller = itemView.findViewById(R.id.tvAuctionSeller);
                auctionCurrentPriceLabel =itemView.findViewById(R.id.tvViewAuctionsCurrentPriceLabel);

            }
        }
         // Store a member variable for the cards


        // Pass in the Card array into the constructor
        // public deck_adapter(List<Auction> auctions) {
        //    auctionList = auctions;
        // }

        // Usually involves inflating a layout from XML and returning the holder
        @Override
        public com.jambons.aed.auctions_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View CardView = inflater.inflate(R.layout.view_auctions_item, parent, false);

            // Return a new holder instance
            com.jambons.aed.auctions_adapter.ViewHolder viewHolder = new com.jambons.aed.auctions_adapter.ViewHolder(CardView);
            viewHolder.auctionCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent viewAuctionIntent = new Intent(mContext,ViewAuction.class);
                    viewAuctionIntent.putExtra("auction_id",auctionList.get(viewHolder.getAdapterPosition()).getAuctionId());
                    viewAuctionIntent.putExtra("sheep_id",auctionList.get(viewHolder.getAdapterPosition()).getSheepId());
                    Log.wtf("WHERE IS TE SHEEP ID:", auctionList.get(viewHolder.getAdapterPosition()).getSheepId());
                    viewAuctionIntent.putExtra("seller_id",auctionList.get(viewHolder. getAdapterPosition()).getSeller());
                    viewAuctionIntent.putExtra("starting_price",auctionList.get(viewHolder.getAdapterPosition()).getStartingPrice());
                    viewAuctionIntent.putExtra("ending_price",auctionList.get(viewHolder.getAdapterPosition()).getEndingPrice());
                    viewAuctionIntent.putExtra("started_at",auctionList.get(viewHolder.getAdapterPosition()).getStartedAt());
                    viewAuctionIntent.putExtra("auction_status",auctionList.get(viewHolder.getAdapterPosition()).getAuctionStatus());
                    viewAuctionIntent.putExtra("auction_duration",auctionList.get(viewHolder.getAdapterPosition()).getDuration());
                    viewAuctionIntent.putExtra("auction_final_bid",auctionList.get(viewHolder.getAdapterPosition()).getFinalBidPrice());
                    viewAuctionIntent.putExtra("auction_winner_id",auctionList.get(viewHolder.getAdapterPosition()).getAuctionWinner());
                    viewAuctionIntent.putExtra("auction_sheep_name",auctionList.get(viewHolder.getAdapterPosition()).getAuctionSheepName());
                    viewAuctionIntent.putExtra("auction_sheep_uid",auctionList.get(viewHolder.getAdapterPosition()).getAuctionSheepUid());
                    viewAuctionIntent.putExtra("auction_sheep_image_link",auctionList.get(viewHolder.getAdapterPosition()).getAuctionSheepImage());
                //    viewSheepIntent.putExtra("sheep_dp",auctionList.get(viewHolder.getAdapterPosition()).getSheepDp());
                //    viewSheepIntent.putExtra("sheep_owner",auctionList.get(viewHolder.getAdapterPosition()).getSheepOwner());
                 //   viewSheepIntent.putExtra("sheep_imageLink",auctionList.get(viewHolder.getAdapterPosition()).getSheepImageLink());

                    //TODO - BAD CODING PRACTISE - IF SOLUTION FOUND APPLY
                    viewAuctionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(viewAuctionIntent);

                }
            });

            return viewHolder;
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(com.jambons.aed.auctions_adapter.ViewHolder viewHolder, int position) {
            // Get the data model based on position
            //ImageRequest sheepImageRequest = new ImageRequest(sheep.);
            Auction auction = auctionList.get(position);

            Log.wtf("auctionList Pos", auction.toString());
           // Log.wtf("SheepLinks", sheep.getSheepImageLink());
            //Log.wtf("Sheep Options", option.toString());

            ImageView sheepImage = viewHolder.auctionImageView;
            Glide.with(mContext).load(auction.getAuctionSheepImage()).apply(option).into(viewHolder.auctionImageView);

            TextView sheepName = viewHolder.auctionSheepName;
            sheepName.setText(auction.getAuctionSheepName());

            TextView tvCurrentPrice = viewHolder.auctionCurrentPrice;
            TextView tvCurrentPriceLabel = viewHolder.auctionCurrentPriceLabel;
            TextView tvAuctionStatus = viewHolder.auctionStatus;


            TextView tvAuctionSeller = viewHolder.auctionSeller;

            tvAuctionSeller.setText(FireUtils.getInstance().getUserNameById(auction.getSeller()));
            tvAuctionStatus.setText(auction.getAuctionStatus());

            if(auction.getAuctionStatus().equals("LIVE")){
                tvAuctionStatus.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                RestUtils.getInstance(mContext).getAuctionCurrentPrice(auction.getSheepId(), new SheepRestListener<String>() {
                    @Override
                    public void getResult(String response) {
                        String currentPriceString = response;

                        Log.wtf("What is the price", response);
                        tvCurrentPrice.setText(currentPriceString);
                    }
                });
            } else if(auction.getAuctionStatus().equals("COMPLETED")){
                tvAuctionStatus.setTextColor(mContext.getResources().getColor(R.color.colorSuccessGreen));
                tvCurrentPriceLabel.setText("Winning Bid:");
                tvCurrentPrice.setText(auction.getFinalBidPrice());
            } else if(auction.getAuctionStatus().equals("CANCELLED")){
                tvAuctionStatus.setTextColor(mContext.getResources().getColor(R.color.colorWarningRed));
                tvCurrentPriceLabel.setText("Auction Cancelled By Owner");
                tvCurrentPriceLabel.setTextColor(mContext.getResources().getColor(R.color.colorWarningRed));
                tvCurrentPrice.setVisibility(View.GONE);
            }


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
            return this.auctionList.size();
        }



    }


