package dev.jainchiranjeev.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import dev.jainchiranjeev.notes.databinding.ActivityMainBinding;
import dev.jainchiranjeev.notes.fragments.FragmentMain;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FragmentManager manager;
    FragmentTransaction transaction;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().setBackgroundDrawableResource(R.drawable.background_gradient_main_activity);

//        Load Home fragment
        FragmentMain fragmentMain = new FragmentMain();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(binding.crflMainActivity.getId(), fragmentMain);
        transaction.commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
