package dev.jainchiranjeev.notes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.databinding.FragmentMainBinding;

public class FragmentMain extends Fragment implements View.OnClickListener {

    View view;
    Context context;
    FragmentMainBinding binding;
    BottomNavigationView bottomNavigationView;
    FragmentManager manager;
    FragmentTransaction transaction;
    FragmentAllNotes fragmentAllNotes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();

        context = getContext();

        fragmentAllNotes = new FragmentAllNotes();
        Bundle bundle = new Bundle();
        bundle.putBoolean("IsArchivesView",false);
        fragmentAllNotes.setArguments(bundle);
        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(binding.crflMainFragment.getId(), fragmentAllNotes);
        transaction.commit();
        fragmentAllNotes = null;
        manager = null;

//        View Bindings
        bottomNavigationView = binding.bnvFragmentMain;

        binding.ibAboutButton.setOnClickListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_notes:
                        fragmentAllNotes = new FragmentAllNotes();
                        manager = getFragmentManager();
                        transaction = manager.beginTransaction();
                        transaction.replace(binding.crflMainFragment.getId(), fragmentAllNotes);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        transaction.commit();
                        return true;
                    case R.id.menu_item_books:
                        Snackbar.make(binding.snackbarMainFragment, "Notes", Snackbar.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.ib_about_button:
                binding.ibAboutButton.setTransitionName("transition_about_icon");
                binding.crflMainFragment.findViewById(R.id.tv_notes_toolbar).setTransitionName("transition_about_title");
                FragmentAbout fragmentAbout = new FragmentAbout();
                manager = getFragmentManager();
                transaction = manager.beginTransaction();

                fragmentAbout.setSharedElementEnterTransition(TransitionInflater.from(context).inflateTransition(R.transition.transition_basic));
                fragmentAbout.setSharedElementReturnTransition(TransitionInflater.from(context).inflateTransition(R.transition.transition_basic));

                transaction.addSharedElement(binding.ibAboutButton,"transition_about_icon");
                transaction.addSharedElement(binding.crflMainFragment.findViewById(R.id.tv_notes_toolbar),"transition_about_title");
                transaction.replace(R.id.crfl_main_activity, fragmentAbout);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }
}
