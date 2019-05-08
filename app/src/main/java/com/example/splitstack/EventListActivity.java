package com.example.splitstack;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.splitstack.Adapter.MyAdapter;
import com.example.splitstack.Models.TitleChild;
import com.example.splitstack.Models.TitleCreator;
import com.example.splitstack.Models.TitleParent;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TabLayout tabLayout;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MyAdapter)recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_view);



        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tabLayout = findViewById(R.id.tabLayout);

        MyAdapter adapter = new MyAdapter(this, initData(1));
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){

                }
                if(tab.getPosition()==1){

                }
                if(tab.getPosition()==2){

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private List<ParentObject> initData(int tabnumber) {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();
        System.out.println(titles.size());
        if(tabnumber == 1) {
            for (int i = 0; i < titles.size(); i++) {
                List<Object> childList = new ArrayList<>();
                childList.add(new TitleChild("expenses: 10000 SEK", "participants: " + 120, "Helsingborg"));
                titles.get(i).setChildObjectList(childList);
                parentObject.add(titles.get(i));
            }
        } else if(tabnumber==2){
            for (int i = 0; i < titles.size(); i++) {
                List<Object> childList = new ArrayList<>();
                childList.add(new TitleChild("expenses: 50 SEK", "participants: " + 120, "Helsingborg"));
                titles.get(i).setChildObjectList(childList);
                parentObject.add(titles.get(i));
            }
        }

        return parentObject;
    }

    @Override
    public void onBackPressed(){
        MyAdapter adapter = new MyAdapter(this, initData(2));
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);
    }



    public void testMethod(View view){
        System.out.println("THE TEST IS WORKING");
    }
}
