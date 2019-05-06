package com.example.splitstack.Models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TitleCreator {
    static TitleCreator titleCreator;
    List<TitleParent> titleParents;


    public TitleCreator(Context context){
        titleParents = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            TitleParent title = new TitleParent(String.format("Event name",i));
            titleParents.add(title);
        }
    }

    public static TitleCreator get(Context context){
        if(titleCreator==null){
            titleCreator = new TitleCreator(context);
        }
        return titleCreator;
    }

    public List<TitleParent> getAll() {
        return titleParents;
    }
}
