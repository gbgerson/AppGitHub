package gb.github.appgithub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private EditText nombreusuario;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Casteamos los elementos que usaremos en este caso el input y el boton
        login = findViewById(R.id.btn_login);
        nombreusuario =  findViewById(R.id.input_username);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, datosUsuarioActivity.class);
                intent.putExtra("usuario",nombreusuario.getText().toString());
                startActivity(intent);
            }
        });
    }
}


