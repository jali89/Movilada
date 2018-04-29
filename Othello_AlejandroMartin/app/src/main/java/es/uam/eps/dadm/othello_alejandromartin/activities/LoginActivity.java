package es.uam.eps.dadm.othello_alejandromartin.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.uam.eps.dadm.othello_alejandromartin.R;
import es.uam.eps.dadm.othello_alejandromartin.model.RoundRepository;
import es.uam.eps.dadm.othello_alejandromartin.model.RoundRepositoryFactory;

/**
 * Created by jalij on 22/03/2017.
 */

public class LoginActivity  extends AppCompatActivity implements View.OnClickListener {

    private RoundRepository repository;
    private EditText usernameEditText;
    private EditText passwordEditText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!OthelloPreferenceActivity.getPlayerName(this).
                equals(OthelloPreferenceActivity.PLAYERNAME_DEFAULT)){
            startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
            finish();
            return;
        }
        usernameEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        Button newUserButton = (Button) findViewById(R.id.new_user_button);
        loginButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        newUserButton.setOnClickListener(this);

        repository = RoundRepositoryFactory.createRepository(LoginActivity.this);
        if (repository == null)
            Toast.makeText(LoginActivity.this, R.string.repository_opening_error,
                    Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        final String playername = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        RoundRepository.LoginRegisterCallback loginRegisterCallback =
                new RoundRepository.LoginRegisterCallback() {
                    @Override
                    public void onLogin(String playerId) {
                        OthelloPreferenceActivity.setPlayerUUID(LoginActivity.this, playerId);
                        OthelloPreferenceActivity.setPlayerName(LoginActivity.this, playername);
                        OthelloPreferenceActivity.setPlayerPassword(LoginActivity.this, password);
                        startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
                        finish();
                    }
                    @Override
                    public void onError(String error) {
                        new ErrorDialog().show(LoginActivity.this.getSupportFragmentManager(), "ALERT DIALOG");

                    }
                };
        switch (v.getId()) {
            case R.id.login_button:
                repository.login(playername, password, loginRegisterCallback);
                break;
            case R.id.cancel_button:
                finish();
                break;
            case R.id.new_user_button:
                repository.register(playername, password, loginRegisterCallback);
                break;
        }
    }

}
