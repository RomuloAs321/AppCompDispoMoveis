package com.example.appreciclerandjson;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String[] ENDPOINTS = {"/posts", "/comments", "/albums", "/photos", "/todos", "/users"};

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<Comment> commentList;
    private List<Album> albumList;
    private List<Photo> photoList;
    private List<Todo> todoList;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        commentList = new ArrayList<>();
        albumList = new ArrayList<>();
        photoList = new ArrayList<>();
        todoList = new ArrayList<>();
        userList = new ArrayList<>();

        postAdapter = new PostAdapter(this, postList);
        recyclerView.setAdapter(postAdapter);

        fetchDataFromAPI();
    }

    private void fetchDataFromAPI() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        for (String endpoint : ENDPOINTS) {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    BASE_URL + endpoint,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            parseJsonResponse(response, endpoint);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Error fetching data: " + error.getMessage());
                        }
                    }
            );

            requestQueue.add(jsonArrayRequest);
        }
    }

    private void parseJsonResponse(JSONArray jsonArray, String endpoint) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // alterar endpoint atual e criar o objeto correspondente
                if (endpoint.equals("/posts")) {
                    int id = jsonObject.getInt("id");
                    int userId = jsonObject.getInt("userId");
                    String title = jsonObject.getString("title");
                    Post post = new Post(id, title);
                    postList.add(post);
                } else if (endpoint.equals("/comments")) {
                    int id = jsonObject.getInt("id");
                    int postId = jsonObject.getInt("postId");
                    String name = jsonObject.getString("name");
                    String email = jsonObject.getString("email");
                    String body = jsonObject.getString("body");
                    Comment comment = new Comment(id, postId, name, email, body);
                    commentList.add(comment);
                } else if (endpoint.equals("/albums")) {
                    int id = jsonObject.getInt("id");
                    int userId = jsonObject.getInt("userId");
                    String title = jsonObject.getString("title");
                    Album album = new Album(id, userId, title);
                    albumList.add(album);
                } else if (endpoint.equals("/photos")) {
                    int id = jsonObject.getInt("id");
                    int albumId = jsonObject.getInt("albumId");
                    String title = jsonObject.getString("title");
                    String url = jsonObject.getString("url");
                    String thumbnailUrl = jsonObject.getString("thumbnailUrl");
                    Photo photo = new Photo(id, albumId, title, url, thumbnailUrl);
                    photoList.add(photo);
                } else if (endpoint.equals("/todos")) {
                    int id = jsonObject.getInt("id");
                    int userId = jsonObject.getInt("userId");
                    String title = jsonObject.getString("title");
                    boolean completed = jsonObject.getBoolean("completed");
                    Todo todo = new Todo(id, userId, title, completed);
                    todoList.add(todo);
                } else if (endpoint.equals("/users")) {
                    int id = jsonObject.getInt("id");
                    String name = jsonObject.getString("name");
                    String username = jsonObject.getString("username");
                    String email = jsonObject.getString("email");
                    Address address = parseAddress(jsonObject.getJSONObject("address"));
                    String phone = jsonObject.getString("phone");
                    String website = jsonObject.getString("website");
                    Company company = parseCompany(jsonObject.getJSONObject("company"));
                    User user = new User(id, name, username, email, address, phone, website, company);
                    userList.add(user);
                }
            }
            // Se quiser mostrar apenas um tipo de dado na tela, troque a lista no adapter conforme necessário
            postAdapter.notifyDataSetChanged(); // Certifique-se de chamar isso apenas uma vez após todos os endpoints serem processados
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }
    }

    private Address parseAddress(JSONObject addressObj) throws JSONException {
        String street = addressObj.getString("street");
        String suite = addressObj.getString("suite");
        String city = addressObj.getString("city");
        String zipcode = addressObj.getString("zipcode");
        Geo geo = parseGeo(addressObj.getJSONObject("geo"));
        return new Address(street, suite, city, zipcode, geo);
    }

    private Geo parseGeo(JSONObject geoObj) throws JSONException {
        double lat = geoObj.getDouble("lat");
        double lng = geoObj.getDouble("lng");
        return new Geo(lat, lng);
    }

    private Company parseCompany(JSONObject companyObj) throws JSONException {
        String name = companyObj.getString("name");
        String catchPhrase = companyObj.getString("catchPhrase");
        String bs = companyObj.getString("bs");
        return new Company(name, catchPhrase, bs);
    }
}
