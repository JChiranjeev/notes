package dev.jainchiranjeev.notes.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import dev.jainchiranjeev.notes.R;
import dev.jainchiranjeev.notes.databinding.FragmentAboutBinding;

public class FragmentAbout extends Fragment implements View.OnClickListener {

    FragmentAboutBinding binding;

    View view;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        context = getContext();

        Glide.with(view).load(R.drawable.ic_app_icon).fitCenter().into(binding.ivAboutAppIcon);
        Glide.with(view).load(R.drawable.ic_github).fitCenter().into(binding.ibGithub);
        Glide.with(view).load(R.drawable.ic_linkedin).fitCenter().into(binding.ibLinkedin);
        Glide.with(view).load(R.drawable.ic_stackoverflow).fitCenter().into(binding.ibSo);
        Glide.with(view).load(R.drawable.ic_twitter).fitCenter().into(binding.ibTwitter);
        Glide.with(view).load(R.drawable.ic_website).fitCenter().into(binding.ibWebsite);
        Glide.with(view).load(R.drawable.ic_email).fitCenter().into(binding.ibSendEmail);
        Glide.with(view).load(R.drawable.ic_github).fitCenter().into(binding.ibGlideGithub);
        Glide.with(view).load(R.drawable.ic_website).fitCenter().into(binding.ibGlideWebsite);
        Glide.with(view).load(R.drawable.ic_github).fitCenter().into(binding.ibMsvGithub);
        Glide.with(view).load(R.drawable.ic_website).fitCenter().into(binding.ibMsvWebsite);

        binding.ibGithub.setOnClickListener(this);
        binding.ibLinkedin.setOnClickListener(this);
        binding.ibSo.setOnClickListener(this);
        binding.ibTwitter.setOnClickListener(this);
        binding.ibWebsite.setOnClickListener(this);
        binding.ibSendEmail.setOnClickListener(this);
        binding.ibGlideGithub.setOnClickListener(this);
        binding.ibGlideWebsite.setOnClickListener(this);
        binding.ibMsvGithub.setOnClickListener(this);
        binding.ibMsvWebsite.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        builder.addDefaultShareMenuItem();
        CustomTabsIntent intent = builder.build();
        String url;
        switch (view.getId()) {
            case R.id.ib_linkedin:
                url = context.getString(R.string.url_linkedin);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_website:
                url = context.getString(R.string.url_website);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_so:
                url = context.getString(R.string.url_so);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_twitter:
                url = context.getString(R.string.url_twitter);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_github:
                url = context.getString(R.string.url_github);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_send_email:
                url = context.getString(R.string.url_email);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_glide_github:
                url = context.getString(R.string.url_glide_github);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_glide_website:
                url = context.getString(R.string.url_glide_website);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_msv_github:
                url = context.getString(R.string.url_msv_github);
                intent.launchUrl(context, Uri.parse(url));
                break;
            case R.id.ib_msv_website:
                url = context.getString(R.string.url_msv_website);
                intent.launchUrl(context, Uri.parse(url));
                break;
        }
    }
}
