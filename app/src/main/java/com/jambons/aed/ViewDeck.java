package com.jambons.aed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jambons.aed.EthUtils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;

public class ViewDeck extends Fragment {

    ArrayList<Card> cards;
    EthUtils eth ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eth = new EthUtils(this.getContext(), this.getActivity());

        eth.connectToEthNetwork();
        //eth.createWallet();
        eth.getAddress();
        View view = inflater.inflate(R.layout.fragment_view_deck, container, false);
        RecyclerView rvCards = view.findViewById(R.id.rvCardList);

        // Initialize cards
        cards = Card.createCardsList(20);
        // Create adapter passing in the sample user data
        deck_adapter adapter = new deck_adapter(cards);
        // Attach the adapter to the recyclerview to populate items
        rvCards.setAdapter(adapter);
        // Set layout manager to position the items
       // LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        rvCards.setLayoutManager(new LinearLayoutManager(getActivity()));
        // That's all!


        return view;
       // return super.onCreateView(inflater, container, savedInstanceState);
    }



    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf("hey", "heyeyeye");
        setContentView(R.layout.fragment_view_deck);



        // ...
        // Lookup the recyclerview in activity layout
        RecyclerView rvCards = findViewById(R.id.rvCardList);

        // Initialize cards
        cards = Card.createCardsList(20);
        // Create adapter passing in the sample user data
        deck_adapter adapter = new deck_adapter(cards);
        // Attach the adapter to the recyclerview to populate items
        rvCards.setAdapter(adapter);
        // Set layout manager to position the items
        rvCards.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }

     */
}
