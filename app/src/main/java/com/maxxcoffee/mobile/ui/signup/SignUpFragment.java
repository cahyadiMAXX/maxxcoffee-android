package com.maxxcoffee.mobile.ui.signup;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.ui.activity.MainActivity;
import com.maxxcoffee.mobile.model.request.CheckValidEmailRequestModel;
import com.maxxcoffee.mobile.task.user.CheckValidEmailTask;
import com.maxxcoffee.mobile.task.extra.CityTask;
import com.maxxcoffee.mobile.util.Constant;
import com.maxxcoffee.mobile.util.PreferenceManager;
import com.maxxcoffee.mobile.util.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rio Swarawan on 5/3/2016.
 */
public class SignUpFragment extends Fragment {

    @Bind(R.id.first_name)
    EditText firstName;
    @Bind(R.id.last_name)
    EditText lastName;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.password_confirm)
    EditText passwordConfirm;

    private MainActivity activity;
    private int mDayPart = Utils.getDayPart();

    private boolean isEmailChecked = false;

    public SignUpFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setHeaderColor(true);
        isEmailChecked = true;
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ButterKnife.bind(this, view);
        activity.setTitle("Sign Up", true);

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus && !activity.isDrawerExpanded() && (activity.getActiveFragmentFlag() == MainActivity.SIGNUP)){
                    if(!email.getText().toString().equals("") && isValidEmail(email.getText().toString())){
                        /*final LoadingDialog progress = new LoadingDialog();
                        progress.show(getFragmentManager(), null);*/
                        final Dialog loading;
                        loading = new Dialog(getActivity());
                        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        loading.setContentView(R.layout.dialog_loading);
                        loading.setCancelable(false);
                        loading.show();

                        //check ke server
                        CheckValidEmailRequestModel body = new CheckValidEmailRequestModel();
                        body.setEmail(email.getText().toString());

                        CheckValidEmailTask cvtask = new CheckValidEmailTask(activity) {
                            @Override
                            public void onSuccess(String response) {
                                //progress.dismissAllowingStateLoss();
                                if (loading.isShowing())loading.dismiss();
                                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                                PreferenceManager.putBool(activity, Constant.PREFERENCE_REGISTER_IS_VALID_EMAIL, true);
                            }

                            @Override
                            public void onFailed() {
                                //progress.dismissAllowingStateLoss();
                                if (loading.isShowing())loading.dismiss();
                                PreferenceManager.putBool(activity, Constant.PREFERENCE_REGISTER_IS_VALID_EMAIL, false);
                                email.requestFocus();
                                email.setError(getActivity().getResources().getString(R.string.something_wrong));
                                //Toast.makeText(getActivity(), "Email already exists", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailed(String response) {
                                //progress.dismissAllowingStateLoss();
                                if (loading.isShowing())loading.dismiss();
                                PreferenceManager.putBool(activity, Constant.PREFERENCE_REGISTER_IS_VALID_EMAIL, false);
                                email.requestFocus();
                                email.setError(response);
                            }
                        };
                        cvtask.execute(body);
                    }
                }
            }
        });

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_FIRST_NAME, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_LAST_NAME, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_PHONE, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_EMAIL, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setBackground(mDayPart);

        String mFirstName = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_FIRST_NAME, "");
        String mLastName = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_LAST_NAME, "");
        String mEmail = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_EMAIL, "");
        String mPhone = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_PHONE, "");
        //String mPassword = PreferenceManager.getString(activity, Constant.PREFERENCE_REGISTER_PASSWORD, "");

        firstName.setText(mFirstName);
        lastName.setText(mLastName);
        email.setText(mEmail);
        phone.setText(mPhone);
        //password.setText(mPassword);
    }

    @OnClick(R.id.next)
    public void onNextClick() {
        if (!isFormValid())
            return;

        String mFirstName = firstName.getText().toString();
        String mLastName = lastName.getText().toString();
        String mEmail = email.getText().toString();
        String mPhone = phone.getText().toString();
        String mPassword = password.getText().toString();

        final Bundle bundle = new Bundle();
        bundle.putString("first-name", mFirstName);
        bundle.putString("last-name", mLastName);
        bundle.putString("email", mEmail);
        bundle.putString("phone", mPhone);
        bundle.putString("password", mPassword);

        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_FIRST_NAME, mFirstName);
        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_LAST_NAME, mLastName);
        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_EMAIL, mEmail);
        PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_PHONE, mPhone);
        //PreferenceManager.putString(activity, Constant.PREFERENCE_REGISTER_PASSWORD, mPassword);

        /*final LoadingDialog progress = new LoadingDialog();
        progress.show(getFragmentManager(), null);*/
        final Dialog loading;
        loading = new Dialog(getActivity());
        loading.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        loading.setContentView(R.layout.dialog_loading);
        loading.setCancelable(false);
        loading.show();

        CityTask task = new CityTask(activity) {
            @Override
            public void onSuccess(String json) {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                PreferenceManager.putString(activity, Constant.DATA_KOTA, json);
                activity.switchFragment(MainActivity.SIGNUP_INFO, bundle);
            }

            @Override
            public void onFailed() {
                //progress.dismissAllowingStateLoss();
                if (loading.isShowing())loading.dismiss();
                Toast.makeText(activity, "Failed to retrieve city data", Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    private boolean isFormValid() {
        String mFirstName = firstName.getText().toString();
        String mLastName = lastName.getText().toString();
        String mEmail = email.getText().toString();
        String mPhone = phone.getText().toString();
        String mPassword = password.getText().toString();
        String mPasswordConfirm = passwordConfirm.getText().toString();

        if (mFirstName.equals("")) {
            firstName.setError("Please verify your first name");
            Toast.makeText(activity, "Please verify your first name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mLastName.equals("")) {
            lastName.setError("Please verify your last name");
            Toast.makeText(activity, "Please verify your last name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEmail.equals("")) {
            email.requestFocus();
            email.setError("Please verify your email address");
            Toast.makeText(activity, "Please verify your email address", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            if(!isValidEmail(mEmail.toString())){
                email.requestFocus();
                email.setError("Please enter valid email address");
                Toast.makeText(activity, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                return false;
            }
            boolean isValid = PreferenceManager.getBool(activity, Constant.PREFERENCE_REGISTER_IS_VALID_EMAIL, false);
            if(!isValid){
                email.requestFocus();
                email.setError("Please enter valid email address");
                Toast.makeText(activity, "Please check your email address", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (mPhone.equals("")) {
            phone.setError("Please verify your phone number");
            Toast.makeText(activity, "Please verify your phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mPassword.equals("")) {
            password.setError("Please verify your password");
            Toast.makeText(activity, "Please verify your password", Toast.LENGTH_SHORT).show();
            return false;
        } else{
            Log.d("passwordasu", String.valueOf(mPassword.length()));
            if(mPassword.length() < 8){
                password.setError("Password must be at least 8 characters");
                Toast.makeText(activity, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (mPasswordConfirm.equals("")) {
            passwordConfirm.setError("Please verify your password confirmation");
            Toast.makeText(activity, "Please verify your password confirmation", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mPassword.equals(mPasswordConfirm)) {
            passwordConfirm.setError("Please verify your password confirmation");
            Toast.makeText(activity, "Please verify your password confirmation", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void setBackground(int dayPart) {
        int navbar = R.drawable.bg_morning_navbar;

        if (dayPart == Utils.MORNING) {
            navbar = R.drawable.bg_morning_navbar;
        } else if (dayPart == Utils.AFTERNOON) {
            navbar = R.drawable.bg_afternoon_navbar;
        } else if (dayPart == Utils.EVENING) {
            navbar = R.drawable.bg_evening_navbar;
        }
        activity.setRootBackground(navbar);
        activity.setNavbarBackground(navbar);
    }
}
