package dk.itu.mmad.bikeshare.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.User;
import dk.itu.mmad.bikeshare.util.ValidationUtils;
import io.realm.Realm;

public class LoginFragment extends Fragment {

    private TextView mEmail;
    private TextView mPassword;

    private Button mLoginButton;
    private Button mRegisterButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedBundleState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mEmail = (TextView) v.findViewById(R.id.email_text);
        mPassword = (TextView) v.findViewById(R.id.password_text);

        mLoginButton = (Button) v.findViewById(R.id.login_button);
        mRegisterButton = (Button) v.findViewById(R.id.register_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String errorMsg = "Invalid username and/or password.";
                final String email = mEmail.getText().toString().trim();

                if (!ValidationUtils.isValidEmail(email) ||
                        !ValidationUtils.isValidPassword(mPassword.getText().toString().trim())) {
                    showDialogBox(view, errorMsg);
                    return;
                }

                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            User user = bgRealm.where(User.class)
                                    .equalTo("mEmail", mEmail.getText().toString().trim())
                                    .and()
                                    .equalTo("mPassword", mPassword.getText().toString().trim())
                                    .findFirst();

                            if (user == null) {
                                throw new RuntimeException("User not found.");
                            }


                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            // Store user email in shared preferences
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            sharedPreferences.edit()
                                    .putString("user", email)
                                    .apply();

                            Toast.makeText(getContext(), "Login successful, welcome to BikeShare", Toast.LENGTH_SHORT).show();

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new BikeShareFragment())
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            showDialogBox(view, error.getMessage());
                        }
                    });
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showDialogBox(View view, String message) {
        new AlertDialog.Builder(view.getContext())
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }
}
