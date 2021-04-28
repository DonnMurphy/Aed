package com.jambons.aed;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestUtils {
    private static final String TAG = "NetworkManager";
    private static RestUtils instance = null;

    private static final String prefixURL = "http://some/url/prefix/";

    //for Volley API
    public RequestQueue requestQueue;

    private RestUtils(Context context)
    {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        //other stuf if you need
    }

    public static synchronized RestUtils getInstance(Context context)
    {
        if (null == instance)
            instance = new RestUtils(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized RestUtils getInstance()
    {
        if (null == instance)
        {
            throw new IllegalStateException(RestUtils.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }



    // OLD STUFF
    private String sheepData = "WOw";
    private String auctionData = "AUCTIONS";

    private JSONObject registerSheepResponse;
    private JSONObject releaseSheepResponse;
    private JSONObject bidOnAuctionResponse;
    private String currentPriceResponse = "N/A";
    private JSONObject createAuctionResponse;
    ArrayList<Sheep> allSheepArray;
    Sheep specifcSheep;

    ArrayList<Auction> allAuctionsArray;
    ArrayList<Auction> liveAuctionsArray;
    ArrayList<Auction> ownerAuctionsArray;
    Auction specifcAuction;
    Gson gson = new Gson();

    public ArrayList<Sheep> getAllSheep(final SheepRestListener<ArrayList<Sheep>> listener) {
        String allSheepUrl = "https:/vast-brook-68973.herokuapp.com/sheep";

        Type sheepListType = new TypeToken<ArrayList<Sheep>>(){}.getType();

        //RequestQueue requestQueue = Volley.newRequestQueue(appContext);

        JsonArrayRequest allSheepRequest = new JsonArrayRequest(
                Request.Method.GET,
                allSheepUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //try {
                        Log.wtf("get all sheep Response:", response.toString());
                        sheepData = response.toString();
                        ArrayList<Sheep> tempAllSheepArray = gson.fromJson(sheepData, sheepListType);
                        Log.wtf("NAME CHECK", tempAllSheepArray.get(1).getSheepName());
                        allSheepArray = tempAllSheepArray;
                        listener.getResult(allSheepArray);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("Error with Get All Sheep", error.toString());
                        sheepData = error.toString();
                    }
                }

        );
        requestQueue.add(allSheepRequest);
        return allSheepArray;
    }

    public ArrayList<Sheep> getSheepByOwner(String ownerId,final SheepRestListener<ArrayList<Sheep>> listener) {
        String allSheepUrl = "https:/vast-brook-68973.herokuapp.com/sheep/owner/" + ownerId;

        Type sheepListType = new TypeToken<ArrayList<Sheep>>(){}.getType();

        //RequestQueue requestQueue = Volley.newRequestQueue(appContext);

        JsonArrayRequest allSheepRequest = new JsonArrayRequest(
                Request.Method.GET,
                allSheepUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //try {
                        Log.wtf("get all sheep Response:", response.toString());
                        sheepData = response.toString();
                        ArrayList<Sheep> tempAllSheepArray = gson.fromJson(sheepData, sheepListType);
                        //TODO ERROR HERE IF DECK IS 1 or less
                        //Log.wtf("NAME CHECK", tempAllSheepArray.get(0).getSheepName());
                        //for (int x=0; x < response.length(); x++){
                        allSheepArray = tempAllSheepArray;
                        listener.getResult(allSheepArray);
                        //        JSONObject sheep = response.getJSONObject(x);
                        //         sheepData.concat("\n" + sheep.getString("0"));
                        //          Log.wtf("Sheep:", String.valueOf(x)+ " Name: " + sheep.getString("0"));


                        //}


                        // sheepData = response.toString();
                        //} catch (JSONException e) {
                        //    e.printStackTrace();
                        //}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("Error with Get All Sheep", error.toString());
                        sheepData = error.toString();
                    }

                }

        );
        requestQueue.add(allSheepRequest);
        return allSheepArray;
    }

    public Sheep getSheepById(String sheepId, final SheepRestListener<Sheep> listener) {
        String sheepInfoUrl = "https:/vast-brook-68973.herokuapp.com/sheep/" + sheepId;

        //Type sheepListType = new TypeToken<ArrayList<Sheep>>(){}.getType();

        //RequestQueue requestQueue = Volley.newRequestQueue(appContext);

        JsonObjectRequest specificSheepRequest = new JsonObjectRequest(
                Request.Method.GET,
                sheepInfoUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //try {
                        Log.wtf("get specifc sheep Response:", response.toString());
                        sheepData = response.toString();
                        Sheep tempSheep = gson.fromJson(sheepData, Sheep.class);
                        Log.wtf("NAME CHECK", tempSheep.getSheepName());
                        //for (int x=0; x < response.length(); x++){
                        specifcSheep = tempSheep;
                        listener.getResult(specifcSheep);
                        //        JSONObject sheep = response.getJSONObject(x);
                        //         sheepData.concat("\n" + sheep.getString("0"));
                        //          Log.wtf("Sheep:", String.valueOf(x)+ " Name: " + sheep.getString("0"));


                        //}


                        // sheepData = response.toString();
                        //} catch (JSONException e) {
                        //    e.printStackTrace();
                        //}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("Error with Get Specific Sheep", error.toString());
                        sheepData = error.toString();
                    }
                }

        );
        requestQueue.add(specificSheepRequest);
        return specifcSheep;
    }


    public JSONObject registerSheepRequest(String userId, String tokenId,final SheepRestListener<JSONObject> listener) {
        String registerSheepUrl = "https:/vast-brook-68973.herokuapp.com/sheep/register";
        //JsonObject o = new JsonParser().parse("{\"\": \"A\"}").getAsJsonObject();
        try {
            JSONObject registerSheepBody = new JSONObject();
            registerSheepBody.put("user_id", userId);
            registerSheepBody.put("sheep_id", tokenId);
            Log.wtf("THE JSON BODY REGISTER", registerSheepBody.toString());
            JsonObjectRequest registerSheepRequest = new JsonObjectRequest(Request.Method.POST, registerSheepUrl, registerSheepBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf("Register sheep Response:", response.toString());
                    //try {
                    //sheepData = response.toString();
                    //specifcSheep = gson.fromJson(sheepData, Sheep.class);
                    registerSheepResponse = response;
                    listener.getResult(registerSheepResponse);
                    try {
                        //JSONObject postResponseObj = new JSONObject(response);
                        Log.wtf("RESPONSE FROM POST", response.toString());
                    } catch (Exception e) {
                        Log.wtf("RESPONSE FROM POST", "RESPONSE FAILED");
                    }
                    // } catch (JSONException e) {
                    //     e.printStackTrace();
                    //}
                    //txtSheepResults.setText(sheepData);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.wtf("Error with Sheep Registration", error.toString());
                            error.printStackTrace();
                            //sheepData = error.toString();
                        }
                    }

            );
            requestQueue.add(registerSheepRequest);
        } catch (JSONException e) {
            //TODO ERROR HERE
        }
        //TODO THIS RETURNS NOTHING
        return registerSheepResponse;
    }

    public JSONObject releaseSheepRequest(String userId, String tokenId,final SheepRestListener<JSONObject> listener) {
        String registerSheepUrl = "https:/vast-brook-68973.herokuapp.com/sheep/release";
        //JsonObject o = new JsonParser().parse("{\"\": \"A\"}").getAsJsonObject();
        try {
            JSONObject registerSheepBody = new JSONObject();
            registerSheepBody.put("user_id", userId);
            registerSheepBody.put("sheep_id", tokenId);
            Log.wtf("THE JSON BODY REGISTER", registerSheepBody.toString());
            JsonObjectRequest auctionCreationRequest = new JsonObjectRequest(Request.Method.POST, registerSheepUrl, registerSheepBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf("Release sheep Response:", response.toString());
                    //try {
                    //sheepData = response.toString();
                    //specifcSheep = gson.fromJson(sheepData, Sheep.class);
                    releaseSheepResponse = response;
                    listener.getResult(releaseSheepResponse);
                    try {
                        //JSONObject postResponseObj = new JSONObject(response);
                        Log.wtf("RESPONSE FROM POST", response.toString());
                    } catch (Exception e) {
                        Log.wtf("RESPONSE FROM POST", "RESPONSE FAILED");
                    }
                    // } catch (JSONException e) {
                    //     e.printStackTrace();
                    //}
                    //txtSheepResults.setText(sheepData);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.wtf("Error with Sheep Release", error.toString());
                            //sheepData = error.toString();
                        }
                    }

            );
            requestQueue.add(auctionCreationRequest);
        } catch (JSONException e) {
            //TODO ERROR HERE
        }
        //TODO THIS RETURNS NOTHING
        return releaseSheepResponse;
    }

    public JSONObject postBidRequest(String userId, String tokenId, String bidAmount,final SheepRestListener<JSONObject> listener) {
        String postBidUrl = "https:/vast-brook-68973.herokuapp.com/auctions/bid";
        //JsonObject o = new JsonParser().parse("{\"\": \"A\"}").getAsJsonObject();
        try {
            JSONObject registerSheepBody = new JSONObject();
            registerSheepBody.put("bidder_id", userId);
            registerSheepBody.put("sheep_id", tokenId);
            registerSheepBody.put("bid_amount", bidAmount);
            Log.wtf("THE JSON BODY BID", registerSheepBody.toString());
            JsonObjectRequest auctionCreationRequest = new JsonObjectRequest(Request.Method.POST, postBidUrl, registerSheepBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf("bid on sheep response:", response.toString());
                    //try {
                    //sheepData = response.toString();
                    //specifcSheep = gson.fromJson(sheepData, Sheep.class);
                    bidOnAuctionResponse = response;
                    listener.getResult(bidOnAuctionResponse);
                    try {
                        //JSONObject postResponseObj = new JSONObject(response);
                        Log.wtf("RESPONSE FROM BID", response.toString());
                    } catch (Exception e) {
                        Log.wtf("RESPONSE FROM BID", "RESPONSE FAILED");
                    }
                    // } catch (JSONException e) {
                    //     e.printStackTrace();
                    //}
                    //txtSheepResults.setText(sheepData);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.wtf("Error with Auction Bid", error.toString());
                            //sheepData = error.toString();
                        }
                    }

            );
            requestQueue.add(auctionCreationRequest);
        } catch (JSONException e) {
            //TODO ERROR HERE
        }
        //TODO THIS RETURNS NOTHING
        return bidOnAuctionResponse;
    }

    public JSONObject postTransferRequest(String fromUserId, String toUserId, String tokenId,final SheepRestListener<JSONObject> listener) {
        String postTransferUrl = "https:/vast-brook-68973.herokuapp.com/sheep/" + tokenId + "/transfer";
        //JsonObject o = new JsonParser().parse("{\"\": \"A\"}").getAsJsonObject();
        try {
            JSONObject transferSheepBody = new JSONObject();
            transferSheepBody.put("from_id", fromUserId);
            transferSheepBody.put("sheep_id", tokenId);
            transferSheepBody.put("to_id", toUserId);
            Log.wtf("THE JSON BODY TRANSFER", transferSheepBody.toString());
            JsonObjectRequest sheepTransferRequest = new JsonObjectRequest(Request.Method.POST, postTransferUrl, transferSheepBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf("Transfer Sheep Response:", response.toString());
                    //try {
                    //sheepData = response.toString();
                    //specifcSheep = gson.fromJson(sheepData, Sheep.class);
                    try {
                        //JSONObject postResponseObj = new JSONObject(response);
                        Log.wtf("RESPONSE FROM TRANSFER", response.toString());
                    } catch (Exception e) {
                        Log.wtf("RESPONSE FROM TRANSFER", "RESPONSE FAILED");
                    }
                    // } catch (JSONException e) {
                    //     e.printStackTrace();
                    //}
                    //txtSheepResults.setText(sheepData);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.wtf("Error with Auction Bid", error.toString());
                            //sheepData = error.toString();
                        }
                    }

            );
            requestQueue.add(sheepTransferRequest);
        } catch (JSONException e) {
            //TODO ERROR HERE
        }
        //TODO THIS RETURNS NOTHING
        return bidOnAuctionResponse;
    }

    public JSONObject cancelAuctionRequest(String userId, String tokenId,final SheepRestListener<JSONObject> listener) {
        String postBidUrl = "https:/vast-brook-68973.herokuapp.com/auctions/cancel";
        //JsonObject o = new JsonParser().parse("{\"\": \"A\"}").getAsJsonObject();
        try {
            //TODO MAKE SURE USER ID IS EQUAL TO SELLER ID JUST IN CASE
            JSONObject cancelAuctionBody = new JSONObject();
            cancelAuctionBody.put("seller_id", userId);
            cancelAuctionBody.put("sheep_id", tokenId);
            Log.wtf("THE JSON BODY Cancel", cancelAuctionBody.toString());
            JsonObjectRequest auctionCancellationRequest = new JsonObjectRequest(Request.Method.POST, postBidUrl, cancelAuctionBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf("bid on sheep response:", response.toString());
                    //try {
                    //sheepData = response.toString();
                    //specifcSheep = gson.fromJson(sheepData, Sheep.class);
                    bidOnAuctionResponse = response;
                    listener.getResult(bidOnAuctionResponse);
                    try {
                        //JSONObject postResponseObj = new JSONObject(response);


                        Log.wtf("RESPONSE FROM BID", response.toString());
                    } catch (Exception e) {
                        Log.wtf("RESPONSE FROM BID", "RESPONSE FAILED");
                    }
                    // } catch (JSONException e) {
                    //     e.printStackTrace();
                    //}
                    //txtSheepResults.setText(sheepData);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.wtf("Error with Auction Bid", error.toString());
                            //sheepData = error.toString();
                        }
                    }

            );
            requestQueue.add(auctionCancellationRequest);
        } catch (JSONException e) {
            //TODO ERROR HERE
        }
        //TODO THIS RETURNS NOTHING
        return bidOnAuctionResponse;
    }


    public JSONObject createNewAuction(String userId, String tokenId, String startingPrice, String endingPrice, String auctionDuration,final SheepRestListener<JSONObject> listener) {
        String createAuctionUrl = "https:/vast-brook-68973.herokuapp.com/auctions";
        //JsonObject o = new JsonParser().parse("{\"\": \"A\"}").getAsJsonObject();
        try {
            JSONObject createAuctionBody = new JSONObject();
            createAuctionBody.put("user_id", userId);
            createAuctionBody.put("sheep_id", tokenId);
            createAuctionBody.put("auction_duration", auctionDuration);
            createAuctionBody.put("starting_price", startingPrice);
            createAuctionBody.put("ending_price", endingPrice);
            Log.wtf("THE JSON BODY", createAuctionBody.toString());
            JsonObjectRequest auctionCreationRequest = new JsonObjectRequest(Request.Method.POST, createAuctionUrl, createAuctionBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.wtf("Create Auction Response:", response.toString());
                    //try {cre
                    //sheepData = response.toString();
                    //specifcSheep = gson.fromJson(sheepData, Sheep.class);
                    createAuctionResponse = response;
                    listener.getResult(createAuctionResponse);
                    try {
                        //JSONObject postResponseObj = new JSONObject(response);
                        Log.wtf("RESPONSE FROM POST", response.toString());
                    } catch (Exception e) {
                        Log.wtf("RESPONSE FROM POST", "RESPONSE FAILED");
                    }
                    // } catch (JSONException e) {
                    //     e.printStackTrace();
                    //}
                    //txtSheepResults.setText(sheepData);
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.wtf("Error with Create Auction", error.toString());
                            //sheepData = error.toString();
                            //TODO - CONVERT TO A JSON OBJECT AND CALL THE LISTENER>GET RESULT
                        }
                    }

            );
            requestQueue.add(auctionCreationRequest);
        } catch (JSONException e) {
            //TODO ERROR HERE
        }
        //TODO THIS RETURNS NOTHING
        return createAuctionResponse;
    }

    public String getAuctionCurrentPrice(String tokenId, final SheepRestListener<String> listener) {
        String getCurrentPriceUrl = "https:/vast-brook-68973.herokuapp.com/auctions/" + tokenId + "/price";
        Log.wtf("CURRENT PRICE LINK: ", getCurrentPriceUrl);
        JsonObjectRequest currentPriceRequest = new JsonObjectRequest(
                Request.Method.GET,
                getCurrentPriceUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.wtf("get current price:", response.toString());
                            sheepData = response.toString();
                            String tempSheep = response.getString("price");
                            currentPriceResponse = tempSheep;
                            listener.getResult(tempSheep);

                        }catch (JSONException e){
                            Log.wtf("SOMETHING WENT WRONG Current Price", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("Error with Get Specific Sheep", error.toString());
                        sheepData = error.toString();
                    }
                }

        );
        requestQueue.add(currentPriceRequest);
        return currentPriceResponse;
    }





    public ArrayList<Auction> getLiveAuctions(final SheepRestListener<ArrayList<Auction>> listener) {
        String liveAuctionsUrl = "https:/vast-brook-68973.herokuapp.com/auctions/live";

        Type auctionsListType = new TypeToken<ArrayList<Auction>>(){}.getType();
        JsonArrayRequest allSheepRequest = new JsonArrayRequest(
                Request.Method.GET,
                liveAuctionsUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //try {
                        Log.wtf("get live auctions Response:", response.toString());
                        auctionData = response.toString();
                        ArrayList<Auction> tempLiveAuctionsArray = gson.fromJson(auctionData, auctionsListType);
                        //Log.wtf("NAME CHECK", tempAllAuctionsArray.get(1).getSheepId());
                        //for (int x=0; x < response.length(); x++){
                        liveAuctionsArray = tempLiveAuctionsArray;
                        listener.getResult(liveAuctionsArray);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("Error with Get All Auctions", error.toString());
                        auctionData = error.toString();
                    }
                }

        );
        requestQueue.add(allSheepRequest);
        return liveAuctionsArray;
    }

    public ArrayList<Auction> getAllAuctions(final SheepRestListener<ArrayList<Auction>> listener) {
        String allAuctionsUrl = "https:/vast-brook-68973.herokuapp.com/auctions/";

        Type auctionsListType = new TypeToken<ArrayList<Auction>>(){}.getType();

        //RequestQueue requestQueue = Volley.newRequestQueue(appContext);

        JsonArrayRequest allSheepRequest = new JsonArrayRequest(
                Request.Method.GET,
                allAuctionsUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //try {
                        Log.wtf("get all auctions Response:", response.toString());
                        auctionData = response.toString();
                        ArrayList<Auction> tempAllAuctionsArray = gson.fromJson(auctionData, auctionsListType);
                        allAuctionsArray = tempAllAuctionsArray;
                        listener.getResult(allAuctionsArray);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("Error with Get All Auctions", error.toString());
                        auctionData = error.toString();
                    }
                }

        );
        requestQueue.add(allSheepRequest);
        return allAuctionsArray;
    }

    public ArrayList<Auction> getAuctionsByOwner(String ownerId, final SheepRestListener<ArrayList<Auction>> listener) {
        String ownerAuctionsUrl = "https:/vast-brook-68973.herokuapp.com/auctions/owner/" + ownerId;

        Type auctionsListType = new TypeToken<ArrayList<Auction>>(){}.getType();

        //RequestQueue requestQueue = Volley.newRequestQueue(appContext);

        JsonArrayRequest allSheepRequest = new JsonArrayRequest(
                Request.Method.GET,
                ownerAuctionsUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //try {
                        Log.wtf("get auctions By Owner Response:", response.toString());
                        auctionData = response.toString();
                        ArrayList<Auction> tempOwnerAuctionsArray = gson.fromJson(auctionData, auctionsListType);
                        ownerAuctionsArray = tempOwnerAuctionsArray;
                        listener.getResult(ownerAuctionsArray);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.wtf("Error with Get All Auctions", error.toString());
                        auctionData = error.toString();
                    }
                }

        );
        requestQueue.add(allSheepRequest);
        return ownerAuctionsArray;
    }
}

