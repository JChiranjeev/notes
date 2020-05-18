package dev.jainchiranjeev.notes.utils;

import android.content.Context;

import io.github.mthli.knife.KnifeText;

public class TextStylist {
    private static TextStylist instance;
    private static KnifeText knifeText;
    Context context;

    private TextStylist(Context context) {
        this.context = context;
    }

    public static TextStylist getInstance(Context context) {
        if(instance == null) {
            instance = new TextStylist(context);
        }
        return instance;
    }

    public void setupBold(KnifeText knifeText) {
    }
}
