package com.example.buzzhub.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.buzzhub.MainActivity;
import com.example.buzzhub.R;
import com.example.buzzhub.editprofileActivity;
import com.example.buzzhub.emailchange_Activity;
import com.example.buzzhub.landingActivity;
import com.example.buzzhub.profile_password_changeActivity;

public class ProfileFragment extends Fragment {

    ImageView setting,editprofile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setting = view.findViewById(R.id.profile_setting);
        editprofile = view.findViewById(R.id.edit_profile);

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), editprofileActivity.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });
        return view;
    }

    public void showBottomDialog(){
        final Dialog dialog = new Dialog(this.requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout);

        LinearLayout emailcng = dialog.findViewById(R.id.emailchange);
        LinearLayout passcng = dialog.findViewById(R.id.passwordchange);
        LinearLayout bookmarks = dialog.findViewById(R.id.savedbookmark);
        LinearLayout logout = dialog.findViewById(R.id.logout);
        LinearLayout deleteacc = dialog.findViewById(R.id.delete_account);
        ImageView cancel = dialog.findViewById(R.id.cancel_button);

        emailcng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), emailchange_Activity.class);
                startActivity(intent);
            }
        });

        passcng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), profile_password_changeActivity.class);
                startActivity(intent);
            }
        });

        bookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "bookmarks", Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutdialog();
            }
        });

        deleteacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletaccdialog();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations =R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public void logoutdialog(){
        final Dialog logoutdialog = new Dialog(this.requireContext());
        logoutdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logoutdialog.setContentView(R.layout.logout_dialog);

        Button agree = logoutdialog.findViewById(R.id.logout_yes_btn);
        Button disagree = logoutdialog.findViewById(R.id.logout_no_btn);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), landingActivity.class);
                //You should right your logout code here to delete the details in local app database
                startActivity(intent);
                getActivity().finish();
            }
        });

        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutdialog.dismiss();
            }
        });
        logoutdialog.show();
        logoutdialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        logoutdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutdialog.getWindow().getAttributes().windowAnimations =R.style.DialogAnimation;
        logoutdialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void deletaccdialog(){
        final Dialog deletedialog = new Dialog(this.requireContext());
        deletedialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deletedialog.setContentView(R.layout.delete_account_dialog);

        Button agree = deletedialog.findViewById(R.id.delete_yes_btn);
        Button disagree = deletedialog.findViewById(R.id.delete_no_btn);

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                //You should right your delete account code here to delete the account in the database and also in local app database
                startActivity(intent);
                getActivity().finish();
            }
        });

        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletedialog.dismiss();
            }
        });
        deletedialog.show();
        deletedialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        deletedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deletedialog.getWindow().getAttributes().windowAnimations =R.style.DialogAnimation;
        deletedialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
    }
}