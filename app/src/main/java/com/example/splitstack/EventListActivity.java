package com.example.splitstack;

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
    MyAdapter adapter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MyAdapter) recyclerView.getAdapter()).onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_view);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getTabPostition();
        adapter = new MyAdapter(this, initData(2));
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);
    }

    private List<ParentObject> initData(int tabNumber) {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();
        System.out.println(titles.size());
        if (tabNumber == 1) {
            System.out.println("tab 1");
            for (int i = 0; i < titles.size(); i++) {
                List<Object> childList = new ArrayList<>();
                childList.add(new TitleChild("expenses: 10000 SEK", "participants: " + 120, "Helsingborg"));
                titles.get(i).setChildObjectList(childList);
                parentObject.add(titles.get(i));
            }
        }
        if (tabNumber == 2) {
            System.out.println("tab 2");
            for (int i = 0; i < titles.size(); i++) {
                List<Object> childList = new ArrayList<>();
                childList.add(new TitleChild("expenses: 56 SEK", "participants: " + 120, "Helsingborg"));
                titles.get(i).setChildObjectList(childList);
                parentObject.add(titles.get(i));
            }
        }
        if (tabNumber == 3) {
            System.out.println("tab 3");
            for (int i = 0; i < titles.size(); i++) {
                List<Object> childList = new ArrayList<>();
                childList.add(new TitleChild("expenses: 561121 SEK", "participants: " + 120, "Helsingborg"));
                titles.get(i).setChildObjectList(childList);
                parentObject.add(titles.get(i));
            }
        }

        return parentObject;
    }

    public void pastFillList(View view) {
        initData(1);
        System.out.println("tab 1");
    }


    public void futureFillList(View view) {
        System.out.println("tab 3");
        initData(3);
    }

    public void currentFillList(View view) {
        initData(2);
    }

    public void getTabPostition() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(getApplicationContext(), "Pos onTabSelected: " + tab.getPosition(), Toast.LENGTH_SHORT).show();
                if (tab.getPosition() == 0) {
                    initData(1);
                }
                if (tab.getPosition() == 1) {
                    initData(2);
                }
                if (tab.getPosition() == 2) {
                    initData(3);
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
}
