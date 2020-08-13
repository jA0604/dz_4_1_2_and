package com.androidakbar.dz_4_1_2_and;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences spList;
    private List<Map<String, String>> content;
    private SimpleAdapter simAdapter;
    private SwipeRefreshLayout swrList;
    private String LIST_ITEMS_NAME;
    private String LIST_ITEMS_KEY;
    private String[] txt;
    private Set<String> txtSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar appToolbar = findViewById(R.id.app_toolbar);
        appToolbar.setTitle(R.string.name_dz);
        appToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryText));

        LIST_ITEMS_NAME = getResources().getString(R.string.str_sp_name);
        LIST_ITEMS_KEY = getResources().getString(R.string.str_sp_key);


        spList = getSharedPreferences(LIST_ITEMS_NAME, MODE_PRIVATE);

        content = new ArrayList<>();
        txt = getResources().getStringArray(R.array.sa_description);
        txtSet = spList.getStringSet(LIST_ITEMS_KEY, new HashSet<String>());

        if(txtSet != null) {
            if (txtSet.size() == 0) {
                Collections.addAll(txtSet, txt);
                spList.edit().putStringSet(LIST_ITEMS_KEY, txtSet).apply();
            }
            for (String s : txtSet) {
                Map<String, String> iRow = new HashMap<>();
                iRow.put("description", s);
                iRow.put("num_letters", String.valueOf(s.length()));
                content.add(iRow);
            }
        }


        String[] from = new String[]{"description", "num_letters"};
        int[] to = new int[]{R.id.txt_description, R.id.txt_num_letters};
        simAdapter = new SimpleAdapter(this, content, R.layout.list_item, from, to);

        ListView lstView = findViewById(R.id.lst_view);

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                content.remove(i);
                simAdapter.notifyDataSetChanged();
            }
        });


        swrList = findViewById(R.id.swr_refresh_list);

        swrList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                content.clear();
                if(txtSet != null) {
                    if (txtSet.size() == 0) {
                        Collections.addAll(txtSet, txt);
                        spList.edit().putStringSet(LIST_ITEMS_KEY, txtSet).apply();
                    }
                    for (String s : txtSet) {
                        Map<String, String> iRow = new HashMap<>();
                        iRow.put("description", s);
                        iRow.put("num_letters", String.valueOf(s.length()));
                        content.add(iRow);
                    }
                }
                simAdapter.notifyDataSetChanged();
                swrList.setRefreshing(false);

            }
        });

        lstView.setAdapter(simAdapter);

    }
}