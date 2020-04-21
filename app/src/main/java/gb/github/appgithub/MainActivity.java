package gb.github.appgithub;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private EditText nombreusuario;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.text_titulo);
        // Casteamos los elementos que usaremos en este caso el input y el boton
        login = findViewById(R.id.btn_login);
        nombreusuario =  findViewById(R.id.input_username);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, datosUsuarioActivity.class);
                if(nombreusuario.length() == 0)
                {
                    Toast.makeText(MainActivity.this, "Por favor ingrese un nombre de usuario", Toast.LENGTH_SHORT).show();
                }else
                    {
                        intent.putExtra("usuario",nombreusuario.getText().toString().trim());
                        startActivity(intent);
                        getBorrar();
                    }
            }
        });
    }

    private void getBorrar() {
        nombreusuario.setText("");
    }
}


