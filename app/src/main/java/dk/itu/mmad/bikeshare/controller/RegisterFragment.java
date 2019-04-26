package dk.itu.mmad.bikeshare.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dk.itu.mmad.bikeshare.R;
import dk.itu.mmad.bikeshare.model.User;
import dk.itu.mmad.bikeshare.util.ValidationUtils;
import io.realm.Realm;

public class RegisterFragment extends Fragment {

    private TextView mEmail;
    private TextView mRepeatEmail;
    private TextView mPassword;
    private TextView mRepeatPassword;

    private Button mRegisterButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedBundleState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        mEmail = (TextView) v.findViewById(R.id.email_text);
        mRepeatEmail = (TextView) v.findViewById(R.id.repeat_email_text);
        mPassword = (TextView) v.findViewById(R.id.password_text);
        mRepeatPassword = (TextView) v.findViewById(R.id.repeat_password_text);

        mRegisterButton = (Button) v.findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Validation
                final List<String> errorMsgList = new ArrayList<>();

                final String email = mEmail.getText().toString().trim();
                String emailRepeat = mRepeatEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                String passwordRepeat = mRepeatPassword.getText().toString().trim();

                if (!email.equals(emailRepeat)) {
                    errorMsgList.add("Emails do not match.");
                }

                if (!password.equals(passwordRepeat)) {
                    errorMsgList.add("Passwords do not match.");
                }

                if (!errorMsgList.isEmpty()) {
                    showDialogBox(view, errorMsgList);
                    return;
                }

                if (!ValidationUtils.isValidEmail(email)) {
                    errorMsgList.add("Invalid email.");
                }

                if (!ValidationUtils.isValidPassword(password)) {
                    errorMsgList.add("Password must contain at least 8 characters.");
                }

                if (!errorMsgList.isEmpty()) {
                    showDialogBox(view, errorMsgList);
                    return;
                }

                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            // Check if user exists
                            User user = bgRealm.where(User.class)
                                    .equalTo("mEmail", email)
                                    .findFirst();

                            if (user != null) {
                                throw new RuntimeException("An account already exists with this email.\nPlease try logging in.");
                            }

                            // Insert
                            user = new User(email, password);
                            bgRealm.insert(user);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            // Store user email in shared preferences
                            // https://stackoverflow.com/a/6186239
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            sharedPreferences.edit()
                                    .putString("user", email)
                                    .apply();

                            Toast.makeText(getContext(), "Registration successful, welcome to BikeShare", Toast.LENGTH_SHORT).show();

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new BikeShareFragment())
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            errorMsgList.add(error.getMessage());
                            showDialogBox(view, errorMsgList);
                        }
                    });
                }
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showDialogBox(View view, List<String> messageList) {
        String message = messageList.get(0);

        for (int i = 1; i < messageList.size(); i++) {
            message += "\n" + messageList.get(i);
        }

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
