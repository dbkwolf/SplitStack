package com.example.splitstack;

import com.example.splitstack.Models.EventParentItem;
import com.example.splitstack.Models.TitleCreator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TitleCreatorTest {

    private ArrayList<EventParentItem> parentArrayList = new ArrayList<>();
    private TitleCreator titleCreator;
    private String expectedResult;
    private String result;

    @Test
    public void TitleCreator() {

    }

    @Test
    public void makeList() {
        System.out.println("MakeList");
        parentArrayList.add(new EventParentItem("spain", "id"));
        titleCreator = new TitleCreator();
        List<EventParentItem> test = titleCreator.makeList(parentArrayList);
        List<EventParentItem> parentExpectedList = Arrays.asList(new EventParentItem("spain", "id"));
        expectedResult = parentExpectedList.get(0).getTitle();
        result = test.get(0).getTitle();



        Assert.assertEquals(expectedResult, result);

    }
}
