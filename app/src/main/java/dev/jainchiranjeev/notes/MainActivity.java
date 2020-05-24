package dev.jainchiranjeev.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import dev.jainchiranjeev.notes.databinding.ActivityMainBinding;
import dev.jainchiranjeev.notes.fragments.FragmentAllNotes;
import dev.jainchiranjeev.notes.fragments.FragmentNoteEditor;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FragmentManager manager;
    FragmentTransaction transaction;
    View view;
    Bundle bundle;

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

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if(action.equals("dev.jainchiranjeev.notes.MainActivity.NewNoteFragment")) {
            FragmentNoteEditor noteEditor = new FragmentNoteEditor();
            manager = getSupportFragmentManager();
            transaction = manager.beginTransaction();
            bundle = new Bundle();
            bundle.putBoolean("IsNewNote", true);
            noteEditor.setArguments(bundle);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(binding.crflMainActivity.getId(), noteEditor);
            transaction.commit();
        } else if(action.equals(Intent.ACTION_SEND) && type != null) {
            if(type.contains("text/plain")) {
                FragmentNoteEditor noteEditor = new FragmentNoteEditor();
                manager = getSupportFragmentManager();
                transaction = manager.beginTransaction();
                bundle = new Bundle();
                bundle.putBoolean("IsNewNote", true);
                bundle.putBoolean("IsSharedNote", true);
                bundle.putString("SharedContent",intent.getStringExtra(Intent.EXTRA_TEXT));
                noteEditor.setArguments(bundle);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(binding.crflMainActivity.getId(), noteEditor);
                transaction.commit();
            }
        } else {
//        Load Home fragment
            FragmentAllNotes fragmentAllNotes = new FragmentAllNotes();
            manager = getSupportFragmentManager();
            transaction = manager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.replace(binding.crflMainActivity.getId(), fragmentAllNotes);
            transaction.commit();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
