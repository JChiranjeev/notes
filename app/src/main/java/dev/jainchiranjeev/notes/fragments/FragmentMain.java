package dev.jainchiranjeev.notes.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.databinding.FragmentMainBinding;

public class FragmentMain extends Fragment {

    View view;
    FragmentMainBinding binding;
    BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();

//        View Bindings
        bottomNavigationView = binding.bnvFragmentMain;

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_notes:
                        Snackbar.make(binding.snackbarMainFragment, "Notes", Snackbar.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_item_books:
                        Snackbar.make(binding.snackbarMainFragment, "Books", Snackbar.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_item_journal:
                        Snackbar.make(binding.snackbarMainFragment, "Journal", Snackbar.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
