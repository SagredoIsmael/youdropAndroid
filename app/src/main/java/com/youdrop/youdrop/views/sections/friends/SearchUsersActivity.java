package com.youdrop.youdrop.views.sections.friends;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.youdrop.youdrop.controllers.SearchUsersController;
import com.youdrop.youdrop.data.Publication;
import com.youdrop.youdrop.data.User;
import com.youdrop.youdrop.views.adapters.ContentRecyclerViewAdapter;
import com.youdrop.youdrop.views.adapters.UserRecyclerViewAdapter;
import com.youdrop.youdrop.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchUsersActivity extends AppCompatActivity implements SearchUsersView {

    SearchUsersController controller;
    private UserRecyclerViewAdapter adapter;

    @BindView(R.id.search)
    EditText search;

    @BindView(R.id.list_search_users)
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);
        controller = new SearchUsersController(this);
        ButterKnife.bind(this);
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                controller.search(search.getText().toString());
                return true;
            }
        });

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter =new UserRecyclerViewAdapter(new ArrayList<User>(), controller,this);
        list.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void addUsersToList(List<User> notifications) {
        adapter.getmValues().addAll(notifications);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearList() {
        adapter.getmValues().clear();
        adapter.notifyDataSetChanged();
    }
}
