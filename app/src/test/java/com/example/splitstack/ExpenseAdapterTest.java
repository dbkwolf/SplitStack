package com.example.splitstack;

import com.example.splitstack.Adapter.ExpenseAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ExpenseAdapterTest {

    private ArrayList<ExpenseItem> exampleList = new ArrayList<>();
    private ExpenseAdapter expAdapter = new ExpenseAdapter(exampleList);
    private int expectedResult;
    private int result;


    @Test
    public void getItemCount() {
        System.out.println("getItemCount");
        for (int i = 0; i <4 ; i++) {
            exampleList.add(new ExpenseItem("title", "heya", "hoho", "huhu", "haha"));
        }
        expectedResult = 4;
        result = expAdapter.getItemCount();
        Assert.assertEquals(expectedResult, result);
    }
}

