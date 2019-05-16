package com.example.splitstack.Models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TitleCreator {
    static TitleCreator titleCreator;
    List<ParentItem> titleParents;


    public TitleCreator(){
//        titleParents = new ArrayList<>();
//        for (int i = 1; i < 100; i++) {
//            ParentItem title = new ParentItem(String.format("Event name",i));
//            titleParents.add(title);
//        }
    }

//    public static TitleCreator get(Context context){
//        if(titleCreator==null){
//            titleCreator = new TitleCreator(context);
//        }
//        return titleCreator;
//    }

    public List<ParentItem> makeList(ArrayList<ParentItem> parentList){
        titleParents = new ArrayList<>();
        for (int i = 0; i < parentList.size(); i++) {
            ParentItem parentItem = new ParentItem(parentList.get(i).getTitle());
            System.out.println(parentItem.getTitle());
            titleParents.add(parentItem);

        }
        return titleParents;
    }

    public List<ParentItem> getAll() {
        return titleParents;
    }
}
