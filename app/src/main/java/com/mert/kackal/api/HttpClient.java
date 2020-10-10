//package com.mert.kackal.api;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.view.View;
//
//import com.mert.kackal.Constants;
//import com.mert.kackal.models.FoodModel;
//import com.mert.kackal.ui.foods.FoodsFragment;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.JSONTokener;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class HttpClient extends AsyncTask<Void, Void, List<FoodModel>> {
//    private Context context;
//    private List<FoodModel> foodModelList;
//    private FoodsFragment foodsFragment;
//    private static final String API_URL = "https://api.collectapi.com/food/calories?query=";
//
//    public static String getFoodQuery(String foodName) {
//        return API_URL + foodName;
//    }
//
//    public HttpClient(Context context, FoodsFragment fragment) {
//        this.context = context;
//        this.foodsFragment = fragment;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//        foodsFragment.progressBar_foods.setVisibility(View.VISIBLE);
//        foodsFragment.recyclerView.setVisibility(View.GONE);
//    }
//
//    @Override
//    protected List<FoodModel> doInBackground(Void... voids) {
//        foodModelList = new ArrayList<FoodModel>();
//        String[] foodNames = Constants.FOOD_NAMES;
//        Arrays.sort(foodNames);
//        for (String foodName : foodNames) {
//            try {
//                URL url = new URL(getFoodQuery(foodName));
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestProperty("authorization", Constants.API_KEY);
//                httpURLConnection.setRequestMethod("GET");
//                httpURLConnection.setRequestProperty("content-type", "application/json");
//
//                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//
//                    InputStream inputStream = httpURLConnection.getInputStream();
//                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                    String json = bufferedReader.readLine();
//                    JSONTokener jsonTokener = new JSONTokener(json);
//                    JSONObject jsonObject = new JSONObject(jsonTokener);
//
////                    Log.i("HttpRequestLog : ", jsonObject.toString());
////                    Log.i("HttpRequestLog : ", jsonObject.getString("result"));
////                    Log.i("HttpRequestLog : ", jsonObject.getJSONObject("result").getString("name"));
////                    Log.i("HttpRequestLog : ", jsonObject.getJSONObject("result").getString("img"));
//                    if (jsonObject.getBoolean("success")) {
//                        foodModelList.add(new FoodModel(jsonObject.getJSONObject("result").getString("name"), jsonObject.getJSONObject("result").getString("kcal"), jsonObject.getJSONObject("result").getString("weight")));
//
//                    } else {
//                        Log.i("HttpClient ", "Nesne Bulunamadı :" + foodName);
//                    }
//
//
//                } else {
//                    //Baglanti basarisiz oldugu kisim
//                    Log.i("HttpClient : ", "Connection failed : " + httpURLConnection.getResponseCode());
//                }
//            } catch (IOException e) {
//
//                //Baglantı hataso
//            } catch (JSONException e) {
//                e.printStackTrace();
//                //Json Hatası
//            }
//
//        }
//        return foodModelList;
//    }
//
//    @Override
//    protected void onPostExecute(List<FoodModel> foodModelList) {
//        super.onPostExecute(foodModelList);
//        foodsFragment.setFoodModelList(foodModelList);
//        foodsFragment.progressBar_foods.setVisibility(View.GONE);
//        foodsFragment.recyclerView.setVisibility(View.VISIBLE);
//
//    }
//    //    String imageUrl;
////    Context context;
////    FoodsFragment fragment;
////
////    public HttpClient(FoodsFragment fragment, Context context, TextView textView, ImageView imageView) {
////        super();
////        this.fragment = fragment;
////        this.context = context;
////        this.textView_foodName = textView;
////        this.imageView_foodImage = imageView;
////    }
////    public HttpClient(FoodsFragment fragment, Context context) {
////        super();
////        this.fragment = fragment;
////        this.context = context;
////    }
////
////    @Override
////    protected String doInBackground(String... strings) {
////        try {
////            URL url = new URL(strings[0]);
////            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
////            httpURLConnection.setRequestProperty("authorization", Constants.API_KEY);
////            httpURLConnection.setRequestMethod("GET");
////            httpURLConnection.setRequestProperty("content-type", "application/json");
////
////            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
////
////                InputStream inputStream = httpURLConnection.getInputStream();
////                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
////                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
////                String json = bufferedReader.readLine();
////                JSONTokener jsonTokener = new JSONTokener(json);
////                JSONObject jsonObject = new JSONObject(jsonTokener);
////                Log.i("HttpRequestLog : ",jsonObject.toString());
////                Log.i("HttpRequestLog : ",jsonObject.getString("result"));
////                Log.i("HttpRequestLog : ",jsonObject.getJSONObject("result").getString("name"));
////                Log.i("HttpRequestLog : ",jsonObject.getJSONObject("result").getString("img"));
//////                JSONArray jsonArray = new JSONArray(jsonTokener);
////                imageUrl = "jsonO";
////                return jsonObject.getString("name");
////
////
////            } else {
////                //Baglanti basarisiz oldugu kisim
////                return String.valueOf(HttpURLConnection.HTTP_BAD_REQUEST);
////            }
////        } catch (IOException e) {
////
////            //Baglantı hataso
////        } catch (JSONException e) {
////            e.printStackTrace();
////            //Json Hatası
////        }
////        return "fail";
////    }
////
////    @Override
////    protected void onPreExecute() {
////        super.onPreExecute();
////        Toast.makeText(context, "Start Request", Toast.LENGTH_LONG).show();
////    }
////
////    @Override
////    protected void onPostExecute(String s) {
////        super.onPostExecute(s);
////        fragment.foodModelList.add(new FoodModel(s, imageUrl, "None", 0.0f));
////
//////        if (imageUrl != "") {
//////            try {
//////                URL url = new URL(imageUrl);
//////                Bitmap bmp = null;
//////                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//////                imageView_foodImage.setImageBitmap(bmp);
//////            } catch (IOException e) {
//////                e.printStackTrace();
//////            }
//////        }
//////        textView_foodName.setText(s);
////        Toast.makeText(context, "End Request", Toast.LENGTH_LONG).show();
////    }
////
////    @Override
////    protected void onProgressUpdate(Integer... values) {
////        super.onProgressUpdate(values);
////    }
////
////    @Override
////    protected void onCancelled(String s) {
////        super.onCancelled(s);
////    }
////
////    @Override
////    protected void onCancelled() {
////        super.onCancelled();}
//
//
//}
//
//
