package com.example.splitstack;

import android.content.Context;
import com.example.splitstack.Models.ParentItem;
import com.example.splitstack.Models.TitleCreator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TitleCreatorTest {

    private ArrayList<ParentItem> parentArrayList = new ArrayList<>();
    private TitleCreator titleCreator;
    private String expectedResult;
    private String result;

    @Test
    public void TitleCreator() {

    }

    @Test
    public void makeList() {
        System.out.println("MakeList");
        parentArrayList.add(new ParentItem("spain"));
        titleCreator = new TitleCreator();
        List<ParentItem> test = titleCreator.makeList(parentArrayList);
        List<ParentItem> parentExpectedList = Arrays.asList(new ParentItem("spain"));
        expectedResult = parentExpectedList.get(0).getTitle();
        result = test.get(0).getTitle();



        Assert.assertEquals(expectedResult, result);

    }
}
