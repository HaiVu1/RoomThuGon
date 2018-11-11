package com.h2r.roomthugon;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.h2r.roomthugon.data.UserResponsitory;
import com.h2r.roomthugon.data.dao.UserDatabase;
import com.h2r.roomthugon.data.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ListView listUser;
    private FloatingActionButton fab;

    //adapter
    private List<User> arrUser = new ArrayList<>();
    private ArrayAdapter adapter;
    // database
    private CompositeDisposable compositeDisposable;
    private UserResponsitory userResponsitory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compositeDisposable = new CompositeDisposable();
        initView();
        UserDatabase userDatabase = UserDatabase.getInstance(this);
        userResponsitory = UserResponsitory.getInstance(userDatabase.userDAO());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                        User user = new User("Toan", " Doan " + new Random().nextInt());

                        userResponsitory.insertUser(user);
                        e.onComplete();
                    }
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {
                                //no ops
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                //onGetAllUserFailure(throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                loadData();
                            }
                        });

                compositeDisposable.add(disposable);
            }

        });
    }

    private void loadData() {
        Disposable disposable = userResponsitory.getALlUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        onGetAllUserSuccess(users);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MainActivity.this,""+throwable.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }
    private void onGetAllUserSuccess(List<User> user) {
        arrUser.clear();
        arrUser.addAll(user);
        adapter.notifyDataSetChanged();
    }
    private void initView() {
        listUser = findViewById(R.id.list_user);
        fab = findViewById(R.id.fab);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrUser);
        listUser.setAdapter(adapter);
    }
}
