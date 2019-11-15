package com.jambons.aed;

import java.util.ArrayList;

public class Card {
        private String mName;
        private int mDamage;
        private int mHealth;

        public Card(String name, int damage, int health) {
            mName = name;
            mDamage = damage;
            mHealth = health;
        }

        public String getName() {
            return mName;
        }

        public int getDamage() {
            return mDamage;
        }

        public int getHealth() {
            return mHealth;
        }

        private static int lastCardId = 0;

        public static ArrayList<Card> createCardsList(int numCards) {
            ArrayList<Card> cards = new ArrayList<Card>();

            for (int i = 1; i <= numCards; i++) {
                cards.add(new Card("Person " + ++lastCardId, (i + 1), (i + 3)));
            }

            return cards;
        }
    }

