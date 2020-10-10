package com.mert.kackal.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.v1.WriteResult;
import com.mert.kackal.MainActivity;
import com.mert.kackal.R;
import com.mert.kackal.models.FoodModel;
import com.mert.kackal.models.PostModel;
import com.mert.kackal.ui.foods.AddFoodFragment;
import com.mert.kackal.ui.foods.FoodsFragment;
import com.mert.kackal.ui.posts.AddPostFragment;
import com.mert.kackal.ui.posts.PostsFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class FireBaseClient {
    private static final String TAG = "FireBaseClient";


    public FireBaseClient() {

    }

    //Food
    public static class AddFoodDataTask extends AsyncTask<FoodModel, Void, String> {
        private FirebaseFirestore db;
        private AddFoodFragment fragment;
        private FoodModel foodModel;

        public AddFoodDataTask(AddFoodFragment fragment) {
            super();
            this.fragment = fragment;
            db = FirebaseFirestore.getInstance();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragment.selectVisibilty(View.GONE);
            fragment.progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(FoodModel... foodModels) {
            foodModel = foodModels[0];
            db.collection("foods")
                    .add(foodModel)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            onPostExecute("OK");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            onPostExecute("FAIL");
                        }
                    });
            return "NONE";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == "OK") {
                fragment.selectVisibilty(View.VISIBLE);
                fragment.progressBar.setVisibility(View.GONE);
                Toast.makeText(fragment.getActivity(), foodModel.getFoodName() + " Başarıyla kaydedildi.", Toast.LENGTH_LONG).show();
                fragment.clearInputs();
            } else if (s == "FAIL") {
                fragment.selectVisibilty(View.VISIBLE);
                fragment.progressBar.setVisibility(View.GONE);
                Toast.makeText(fragment.getActivity(), foodModel.getFoodName() + " Kaydedilemedi Bad", Toast.LENGTH_LONG).show();

            } else {

            }

        }
    }

    public static class UploadFoodImageTask extends AsyncTask<ImageView, String, String> {
        private AddFoodFragment fragment;
        private String imageUrl;
        private String foodName;
        private StorageReference mStorageRef;

        public UploadFoodImageTask(String foodName, AddFoodFragment fragment) {
            super();
            this.mStorageRef = FirebaseStorage.getInstance().getReference();
            this.fragment = fragment;
            this.foodName = foodName;
        }

        private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
            return bitmap;
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(ImageView... imageViews) {
            Bitmap bitmap;
            byte[] data;
            final StorageReference childRef = mStorageRef.child(foodName + ".png");
            imageViews[0].setDrawingCacheEnabled(true);
            imageViews[0].buildDrawingCache();
            if (imageViews[0].getDrawable().getClass() == VectorDrawable.class) {
                Log.i("FireBaseClient", "image view yanilenmemiş");
                bitmap = getBitmap((VectorDrawable) imageViews[0].getDrawable());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                data = baos.toByteArray();
            } else {
                bitmap = ((BitmapDrawable) imageViews[0].getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                data = baos.toByteArray();
            }

            UploadTask uploadTask = childRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("StrogeUpload", "Fail" + exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl = uri.toString();
                            Log.i("Upload Task Uri", uri.toString());
                            onPostExecute(imageUrl);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            onPostExecute("FAIL");
                        }
                    });
                }
            });
            return "X";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragment.selectVisibilty(View.GONE);
            fragment.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == "X") {

            } else if (s == "FAIL") {
                Toast.makeText(fragment.getActivity(), foodName + " Resim yüklemesinde hata oluştu", Toast.LENGTH_LONG).show();
            } else {
                fragment.selectVisibilty(View.VISIBLE);
                fragment.progressBar.setVisibility(View.GONE);
                fragment.saveFood(imageUrl);
            }
        }
    }

    public static class FirebaseGetFoodsTask extends AsyncTask<Void, Void, List<FoodModel>> {
        private FirebaseFirestore db;
        private FoodsFragment foodsFragment;

        public FirebaseGetFoodsTask(FoodsFragment foodsFragment) {
            this.foodsFragment = foodsFragment;
            db = FirebaseFirestore.getInstance();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            foodsFragment.progressBar_foods.setVisibility(View.VISIBLE);
            foodsFragment.recyclerView.setVisibility(View.GONE);
        }

        @Override
        protected List<FoodModel> doInBackground(Void... voids) {
            db.collection("foods")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<FoodModel> foodModelList = new ArrayList<FoodModel>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            foodModelList.add((FoodModel) document.toObject(FoodModel.class));
                        }

                        onPostExecute(foodModelList);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
            return null;
        }


        @Override
        protected void onPostExecute(List<FoodModel> foodModelList) {
            super.onPostExecute(foodModelList);
            if (foodModelList == null) {

            } else {
                foodsFragment.setFoodModelList(foodModelList);
                foodsFragment.progressBar_foods.setVisibility(View.GONE);
                foodsFragment.recyclerView.setVisibility(View.VISIBLE);
            }

        }
    }


    //Post
    public static class AddPostTask extends AsyncTask<PostModel, Void, String> {
        private FirebaseFirestore db;
        private AddPostFragment addPostFragment;
        private PostModel postModel;

        public AddPostTask(AddPostFragment addPostFragment) {
            this.addPostFragment = addPostFragment;
            db = FirebaseFirestore.getInstance();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            addPostFragment.selectVisibilty(View.GONE);
//            addPostFragment.post_add_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(final PostModel... postModels) {
            postModel = postModels[0];

            db.collection("posts")
                    .add(postModel)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            db.collection("posts").document(documentReference.getId()).update("post_id", documentReference.getId())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            onPostExecute("OK");
                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            onPostExecute("FAIL");
                        }
                    });
            return "NONE";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == "OK") {
                addPostFragment.changeFragment();
            } else if (s == "FAIL") {
                addPostFragment.selectVisibilty(View.VISIBLE);
                addPostFragment.post_add_progress.setVisibility(View.GONE);
                Toast.makeText(addPostFragment.getActivity(), "Başarısız , Lütfden internet bağlantınızı kontrol ediniz", Toast.LENGTH_LONG).show();

            } else {

            }
        }
    }

    public static class UploadPostImageTask extends AsyncTask<ImageView, String, String> {
        private AddPostFragment addPostFragment;
        private String img_url;
        private String post_image_id;
        private StorageReference storageReference;
        private boolean isAdded;

        public UploadPostImageTask(AddPostFragment addPostFragment, String post_image_id, Boolean isAdded) {
            this.isAdded = isAdded;
            this.addPostFragment = addPostFragment;
            this.post_image_id = post_image_id;
            this.storageReference = FirebaseStorage.getInstance().getReference();
        }

        private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            addPostFragment.selectVisibilty(View.GONE);
            addPostFragment.post_add_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(ImageView... imageViews) {

            if (!isAdded) {

                return "not";

            }

            Bitmap bitmap;
            byte[] data;
            final StorageReference childRef = storageReference.child(post_image_id + ".png");
            imageViews[0].setDrawingCacheEnabled(true);
            imageViews[0].buildDrawingCache();
            if (imageViews[0].getDrawable().getClass() == VectorDrawable.class) {
                Log.i("FireBaseClient", "image view yanilenmemiş");
                bitmap = getBitmap((VectorDrawable) imageViews[0].getDrawable());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                data = baos.toByteArray();
            } else {
                bitmap = ((BitmapDrawable) imageViews[0].getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                data = baos.toByteArray();
            }

            UploadTask uploadTask = childRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("StrogeUpload", "Fail" + exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            img_url = uri.toString();
                            Log.i("Upload Task Uri", uri.toString());
                            onPostExecute(img_url);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            onPostExecute("FAIL");
                        }
                    });
                }
            });
            return "X";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == "X") {

            } else if (s == "FAIL") {
                Toast.makeText(addPostFragment.getActivity(), " Resim yüklemesinde hata oluştu", Toast.LENGTH_LONG).show();
            } else if (s == "not") {
                addPostFragment.sendPost(null);
            } else {
//                addPostFragment.selectVisibilty(View.VISIBLE);
//                addPostFragment.post_add_progress.setVisibility(View.GONE);
                addPostFragment.sendPost(img_url);
            }
        }
    }

    public static class GetPostsTask extends AsyncTask<Void, Void, List<PostModel>> {
        private FirebaseFirestore db;
        private PostsFragment postsFragment;

        public GetPostsTask(PostsFragment postsFragment) {
            this.postsFragment = postsFragment;
            db = FirebaseFirestore.getInstance();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postsFragment.progressBar_posts.setVisibility(View.VISIBLE);
            postsFragment.recyclerView_posts.setVisibility(View.GONE);
        }

        @Override
        protected List<PostModel> doInBackground(Void... voids) {
            db.collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<PostModel> postModels = new ArrayList<PostModel>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            postModels.add((PostModel) document.toObject(PostModel.class));
                        }

                        onPostExecute(postModels);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(List<PostModel> postModels) {
            super.onPostExecute(postModels);
            if (postModels == null) {

            } else {
                postsFragment.setPostList(postModels);
                postsFragment.progressBar_posts.setVisibility(View.GONE);
                postsFragment.recyclerView_posts.setVisibility(View.VISIBLE);
            }
        }
    }

    public static class LikePostTask extends AsyncTask<PostModel, Void, String> {

        private FirebaseFirestore db;
        private PostsFragment postsFragment;

        public LikePostTask(PostsFragment postsFragment) {
            db = FirebaseFirestore.getInstance();
            this.postsFragment = postsFragment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(final PostModel... postModels) {
            DocumentReference docRef = db.collection("posts")
                    .document(postModels[0].getPost_id());

            docRef.update("likeCount", postModels[0].getLikeCount()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(postsFragment.getActivity(), "Liked", Toast.LENGTH_LONG).show();
                    onPostExecute("Liked");
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                postsFragment.updateList();
            }

        }
    }

    public static class DeletePostTask extends AsyncTask<PostModel, Void, String> {

        private PostModel postModel;
        private FirebaseFirestore db;
        private PostsFragment postsFragment;

        public DeletePostTask(PostsFragment postsFragment) {
            this.postsFragment = postsFragment;
            db = FirebaseFirestore.getInstance();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postsFragment.recyclerView_posts.setVisibility(View.GONE);
            postsFragment.progressBar_posts.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(PostModel... postModels) {
            postModel = postModels[0];
            DocumentReference ref = db.collection("posts").document(postModel.getPost_id());
            ref.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    onPostExecute("DELETED");
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == "DELETED") {
                postsFragment.deletePost(postModel);
                postsFragment.recyclerView_posts.setVisibility(View.VISIBLE);
                postsFragment.progressBar_posts.setVisibility(View.GONE);
            }
        }
    }

    //User
    public static class UploatUserProfilePhotoTask extends AsyncTask<ImageView, String, String> {
        private String img_url;
        private String post_image_id;
        private StorageReference storageReference;
        private MainActivity mainActivity;

        public UploatUserProfilePhotoTask(String post_image_id, MainActivity mainActivity) {
            this.post_image_id = post_image_id;
            this.mainActivity = mainActivity;
            this.storageReference = FirebaseStorage.getInstance().getReference();
        }

        private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
//
        }

        @Override
        protected String doInBackground(ImageView... imageViews) {
            Bitmap bitmap;
            byte[] data;
            final StorageReference childRef = storageReference.child(post_image_id + ".png");
            imageViews[0].setDrawingCacheEnabled(true);
            imageViews[0].buildDrawingCache();
            if (imageViews[0].getDrawable().getClass() == VectorDrawable.class) {
                Log.i("FireBaseClient", "image view yanilenmemiş");
                bitmap = getBitmap((VectorDrawable) imageViews[0].getDrawable());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                data = baos.toByteArray();
            } else {
                bitmap = ((BitmapDrawable) imageViews[0].getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                data = baos.toByteArray();
            }

            UploadTask uploadTask = childRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.i("StrogeUpload", "Fail" + exception.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            img_url = uri.toString();
                            Log.i("Upload Task Uri", uri.toString());
                            onPostExecute(img_url);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            onPostExecute("FAIL");
                        }
                    });
                }
            });
            return "X";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s == "X") {

            } else if (s == "FAIL") {
                //Toast.makeText(addPostFragment.getActivity(), " Resim yüklemesinde hata oluştu", Toast.LENGTH_LONG).show();
            } else {
                mainActivity.getUserPhoto(s);


            }
        }

    }

}
