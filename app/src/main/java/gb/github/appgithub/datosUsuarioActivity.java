package gb.github.appgithub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Adapter.ReposAdapter;
import Api.APIClient;
import Api.GitHubRepoEndPoint;
import Api.GitHubUserEndPoints;
import Modelo.GitHubRepo;
import Modelo.GitHubUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class datosUsuarioActivity extends AppCompatActivity {
    ImageView avatarImg;
    TextView userNameTV;
    TextView followersTV;
    TextView userNameTVUno;
    TextView followingTV;
    TextView logIn;
    TextView email;
    Button btn_perfil;

    Bundle extras;
    String newString;

    Bitmap myImage;
    RecyclerView mRecyclerView;
    List<GitHubRepo> myDataSource = new ArrayList<>();
    RecyclerView.Adapter myAdapter;
    public static final String direccion = "https://github.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);

        avatarImg = (ImageView) findViewById(R.id.avatar);
        userNameTV = (TextView) findViewById(R.id.username);
        followersTV = (TextView) findViewById(R.id.followers);
        followingTV = (TextView) findViewById(R.id.following);
        logIn = (TextView) findViewById(R.id.logIn);
        email = (TextView) findViewById(R.id.email);
        //
        btn_perfil = findViewById(R.id.btn_perfil);


        extras = getIntent().getExtras();
        newString = extras.getString("usuario");

        System.out.println(newString);
        loadData();
        //


        userNameTVUno =  findViewById(R.id.userNameTV);
        userNameTV.setText("Usuario: " + newString);

        mRecyclerView=  findViewById(R.id.repos_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ReposAdapter(myDataSource, R.layout.list_item_repo,
                getApplicationContext());

        mRecyclerView.setAdapter(myAdapter);

        loadRepositories();
        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse( direccion + newString);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }



    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }
            return null;
        }
    }

    public void loadData() {
        final GitHubUserEndPoints apiService =
                APIClient.getClient().create(GitHubUserEndPoints.class);

        Call<GitHubUser> call = apiService.getUser(newString);
        call.enqueue(new Callback<GitHubUser>() {

            @Override
            public void onResponse(Call<GitHubUser> call, Response<GitHubUser>
                    response) {

                ImageDownloader task = new ImageDownloader();

                try {
                    myImage = task.execute(response.body().getAvatar()).get();

                } catch (Exception e) {

                    e.printStackTrace();

                }

                avatarImg.setImageBitmap(myImage);
                //avatarImg.getLayoutParams().height=220;
                //avatarImg.getLayoutParams().width=220;

                if(response.body().getName() == null){
                    userNameTV.setText("No name provided");
                } else {
                    userNameTV.setText("Usuario: " + response.body().getName());
                }

                followersTV.setText("Seguidores: " + response.body().getFollowers());
                followingTV.setText("Siguiendo: " + response.body().getFollowing());
                logIn.setText("Iniciar sesión: " + response.body().getLogin());

                if(response.body().getEmail() == null){
                    email.setText("No hay correo electrónico");
                } else{
                    email.setText("Email: " + response.body().getEmail());
                }



            }

            @Override
            public void onFailure(Call<GitHubUser> call, Throwable t) {
                System.out.println("Ha ocurrido un error!" + t.toString());
            }
        });
    }

    public void loadRepositories(){
        GitHubRepoEndPoint apiService =
                APIClient.getClient().create(GitHubRepoEndPoint.class);

        Call<List<GitHubRepo>> call = apiService.getRepo(newString);
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {

                myDataSource.clear();
                myDataSource.addAll(response.body());
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {
                // Log error here since request failed
                Log.e("Repostitorios", t.toString());
            }

        });
    }
}
