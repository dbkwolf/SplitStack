package com.example.splitstack.Models;

import java.util.ArrayList;
import java.util.List;

public class TitleCreator {
    static TitleCreator titleCreator;

    List<EventParentItem> titleParents;


    public TitleCreator(){
//        titleParents = new ArrayList<>();
//        for (int i = 1; i < 100; i++) {
//            EventParentItem title = new EventParentItem(String.format("Event name",i));
//            titleParents.add(title);
//        }
    }

//    public static TitleCreator get(Context context){
//        if(titleCreator==null){
//            titleCreator = new TitleCreator(context);
//        }
//        return titleCreator;
//    }

    public List<EventParentItem> makeList(ArrayList<EventParentItem> parentList){

        titleParents = new ArrayList<>();

        for (int i = 0; i < parentList.size(); i++) {
            EventParentItem eventParentItem = new EventParentItem(parentList.get(i).getTitle());
            System.out.println(eventParentItem.getTitle());
            titleParents.add(eventParentItem);

        }
        return titleParents;
    }

    public List<EventParentItem> getAll() {
        return titleParents;
    }
}
