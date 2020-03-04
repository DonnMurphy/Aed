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
    private String auctionPostResponse = "AUCTION CREATED???";
    private String registerSheepResponse = "Sheep Registered???";
    private String bidOnAuctionResponse = "Bid Successful???";
    private String currentPriceResponse = "N/A";
    ArrayList<Sheep> allSheepArray;
    Sheep specifcSheep;

    ArrayList<Auction> allAuctionsArray;
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
                        Log.wtf("get all sheep Response:", response.toString());
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

    public Sheep getSheepByIdAHHHHH(String sheepId){
        String sheepInfoUrl = "https:/vast-brook-68973.herokuapp.com/sheep/" + sheepId;
        //RequestQueue requestQueue = Volley.newRequestQueue(appContext);
        JsonObjectRequest sheepInfoRequest = new JsonObjectRequest(
                Request.Method.GET,
                sheepInfoUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.wtf("get all sheep Response:", response.toString());
                        //try {
                            sheepData = response.toString();
                            specifcSheep = gson.fromJson(sheepData, Sheep.class);
                       // } catch (JSONException e) {
                       //     e.printStackTrace();
                        //}
                        //txtSheepResults.setText(sheepData);
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
        requestQueue.add(sheepInfoRequest);
        return specifcSheep;
    }
    public String registerSheepRequest(String userId, String tokenId,final SheepRestListener<String> listener) {
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
                    Log.wtf("get all sheep Response:", response.toString());
                    //try {
                    //sheepData = response.toString();
                    //specifcSheep = gson.fromJson(sheepData, Sheep.class);
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

    public String releaseSheepRequest(String userId, String tokenId,final SheepRestListener<String> listener) {
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
                    Log.wtf("get all sheep Response:", response.toString());
                    //try {
                    //sheepData = response.toString();
                    //specifcSheep = gson.fromJson(sheepData, Sheep.class);
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
        return registerSheepResponse;
    }

    public String postBidRequest(String userId, String tokenId, String bidAmount,final SheepRestListener<String> listener) {
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


    public String createNewAuction(String userId, String tokenId, String startingPrice, String endingPrice, String auctionDuration,final SheepRestListener<String> listener) {
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
                    Log.wtf("get all sheep Response:", response.toString());
                    //try {
                    //sheepData = response.toString();
                    //specifcSheep = gson.fromJson(sheepData, Sheep.class);
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
                        }
                    }

            );
            requestQueue.add(auctionCreationRequest);
        } catch (JSONException e) {
            //TODO ERROR HERE
        }
        //TODO THIS RETURNS NOTHING
        return auctionPostResponse;
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

                            //for (int x=0; x < response.length(); x++){
                            currentPriceResponse = tempSheep;
                            listener.getResult(tempSheep);

                        }catch (JSONException e){
                            Log.wtf("SOMETHING WENT WRONG ", e.toString());
                            e.printStackTrace();
                        }
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
        requestQueue.add(currentPriceRequest);
        return currentPriceResponse;
    }




    public String createNewAuctionAAAAGH(String userId, String tokenId, String startingPrice, String endingPrice, String auctionDuration,final SheepRestListener<String> listener){
        String createAuctionUrl = "https:/vast-brook-68973.herokuapp.com/auctions";
        JsonObject o = new JsonParser().parse("{\"\": \"A\"}").getAsJsonObject();
        StringRequest auctionCreationRequest = new StringRequest(Request.Method.POST, createAuctionUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject postResponseObj = new JSONObject(response);
                    Log.wtf("RESPONSE FROM POST", postResponseObj.toString());
                } catch (Exception e) {
                    Log.wtf("RESPONSE FROM POST", "RESPONSE FAILED");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("POST ERROR","Post Data : Response Failed");
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params=new HashMap<String, String>();
                params.put("userId", userId);
                params.put("sheepId",tokenId);
                params.put("auctionDuration",auctionDuration);
                params.put("startingPrice",startingPrice);
                params.put("endingPrice",endingPrice);
                listener.getResult(params.toString());
                auctionPostResponse = params.toString();
                return params;
            }

            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(auctionCreationRequest);
        //TODO THIS DOES NOTHING
        return auctionPostResponse;
    }


    public ArrayList<Auction> getAllAuctions(final SheepRestListener<ArrayList<Auction>> listener) {
        String allAuctionsUrl = "https:/vast-brook-68973.herokuapp.com/auctions";

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
                        Log.wtf("NAME CHECK", tempAllAuctionsArray.get(1).getSheepId());
                        //for (int x=0; x < response.length(); x++){
                        allAuctionsArray = tempAllAuctionsArray;
                        listener.getResult(allAuctionsArray);
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
                        Log.wtf("Error with Get All Auctions", error.toString());
                        auctionData = error.toString();
                    }
                }

        );
        requestQueue.add(allSheepRequest);
        return allAuctionsArray;
    }
}

