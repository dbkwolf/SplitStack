package com.example.splitstack;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.splitstack.Adapter.MyAdapter;
import com.example.splitstack.Models.TitleChild;
import com.example.splitstack.Models.TitleCreator;
import com.example.splitstack.Models.TitleParent;

import java.util.ArrayList;
import java.util.List;

public class EventListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

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

        MyAdapter adapter = new MyAdapter(this, initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(adapter);
    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();
        System.out.println(titles.size());
        for (int i = 0; i < titles.size(); i++) {
            List<Object> childList = new ArrayList<>();
            childList.add(new TitleChild("expenses: 10000 SEK", "participants: "+120, "Helsingborg"));
            titles.get(i).setChildObjectList(childList);
            parentObject.add(titles.get(i));
        }

        return parentObject;
    }
}
