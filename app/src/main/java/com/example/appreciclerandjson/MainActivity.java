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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
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
                // Aqui você precisa verificar o endpoint atual e criar o objeto correspondente
                if (endpoint.equals("/posts")) {
                    int id = jsonObject.getInt("id");
                    String title = jsonObject.getString("title");
                    Post post = new Post(id, title);
                    postList.add(post);
                } else if (endpoint.equals("/comments")) {
                    // Parse e adicione os comentários
                } else if (endpoint.equals("/albums")) {
                    // Parse e adicione os álbuns
                } else if (endpoint.equals("/photos")) {
                    // Parse e adicione as fotos
                } else if (endpoint.equals("/todos")) {
                    // Parse e adicione as tarefas
                } else if (endpoint.equals("/users")) {
                    // Parse e adicione os usuários
                }
            }
            postAdapter.notifyDataSetChanged(); // Certifique-se de chamar isso apenas uma vez após todos os endpoints serem processados
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON response: " + e.getMessage());
        }
    }
}
