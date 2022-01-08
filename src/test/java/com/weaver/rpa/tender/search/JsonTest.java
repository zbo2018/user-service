package com.weaver.rpa.tender.search;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weaver.rpa.tender.search.util.ReadFromFile;
import org.junit.Test;

import java.util.List;

public class JsonTest {

    @Test
    public void cc(){
        byte[] read = ReadFromFile.read("C:\\Users\\Administrator\\Desktop\\2.json");
        String jsonStr = new String(read);
        List<Object> columnList = new Gson().fromJson(jsonStr, new TypeToken<List<Object>>() {}.getType());
        return;
    }
}
