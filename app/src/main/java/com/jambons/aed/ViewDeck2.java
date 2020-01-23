package com.jambons.aed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ViewDeck2 extends AppCompatActivity {
    ArrayList<Card> cards;
    EthUtils eth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deck2);

        //eth = new EthUtils(this.getContext(), this.getActivity());

 //       eth.connectToEthNetwork();
        //eth.createWallet();
        //eth.getAddress();


        //View view = inflater.inflate(R.layout.fragment_view_deck, container, false);
        RecyclerView rvCards = this.findViewById(R.id.rvCardList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCards.getContext(),
                DividerItemDecoration.VERTICAL);
        rvCards.addItemDecoration(dividerItemDecoration);
        // Initialize cards
        cards = Card.createCardsList(10);
        // Create adapter passing in the sample user data
        deck_adapter adapter = new deck_adapter(cards);
        // Attach the adapter to the recyclerview to populate items
        rvCards.setAdapter(adapter);
        // Set layout manager to position the items
        // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        rvCards.setLayoutManager(new LinearLayoutManager(this));
        // That's all!


        //return view;
        // return super.onCreateView(inflater, container, savedInstanceState);
    }
}
