package com.example.bottommenudemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;

    public static final String fragment1Tag = "fragment1";
    public static final String fragment2Tag = "fragment2";
    public static final String fragment3Tag = "fragment3";
    public static final String fragment4Tag = "fragment4";
    public static final String fragment5Tag = "fragment5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup) findViewById(R.id.activity_group_radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment1 = fm.findFragmentByTag(fragment1Tag);
                Fragment fragment2 = fm.findFragmentByTag(fragment2Tag);
                Fragment fragment3 = fm.findFragmentByTag(fragment3Tag);
                Fragment fragment4 = fm.findFragmentByTag(fragment4Tag);
                Fragment fragment5 = fm.findFragmentByTag(fragment5Tag);

                if (fragment1 != null) {
                    ft.hide(fragment1);
                }
                if (fragment2 != null) {
                    ft.hide(fragment2);
                }
                if (fragment3 != null) {
                    ft.hide(fragment3);
                }
                if (fragment4 != null) {
                    ft.hide(fragment4);
                }
                if (fragment5 != null) {
                    ft.hide(fragment5);
                }
                switch (checkedId) {
                    case R.id.order_process:
                        if (fragment1 == null) {
                            fragment1 = new Fragment1();
                            ft.add(R.id.container, fragment1, fragment1Tag);
                        } else {
                            ft.show(fragment1);
                        }
                        break;
                    case R.id.order_query:
                        if (fragment2 == null) {
                            fragment2 = new Fragment2();
                            ft.add(R.id.container, fragment2, fragment2Tag);
                        } else {
                            ft.show(fragment2);
                        }
                        break;
                    case R.id.merchant_manager:
                        if (fragment3 == null) {
                            fragment3 = new Fragment3();
                            ft.add(R.id.container, fragment3,
                                    fragment3Tag);
                        } else {
                            ft.show(fragment3);
                        }
                        break;
                    case R.id.setting:
                        if (fragment4 == null) {
                            fragment4 = new Fragment4();
                            ft.add(R.id.container, fragment4, fragment4Tag);
                        } else {
                            ft.show(fragment4);
                        }
                        break;
                    case R.id.qushi:
                        if (fragment5 == null) {
                            fragment5 = new Fragment5();
                            ft.add(R.id.container, fragment5, fragment5Tag);
                        } else {
                            ft.show(fragment5);
                        }
                        break;
                    default:
                        break;
                }
                ft.commit();
            }
        });
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new Fragment1();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, fragment1Tag).commit();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton mTab = (RadioButton) radioGroup.getChildAt(i);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag((String) mTab.getTag());
            FragmentTransaction ft = fm.beginTransaction();
            if (fragment != null) {
                if (!mTab.isChecked()) {
                    ft.hide(fragment);
                }
            }
            ft.commit();
        }
    }
}
