package com.example.administrator.pic;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.administrator.pic.baidu.location.service.LocationService;
import org.apache.cordova.*;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CordovaInterface, SeekBar.OnSeekBarChangeListener {


    CordovaWebView cordovaWebView;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    protected int activityResultRequestCode;
    protected CordovaPlugin activityResultCallback;
    protected CordovaPreferences prefs = new CordovaPreferences();
    protected ArrayList<PluginEntry> pluginEntries = null;
    protected SystemWebView systemWebView;
    protected Button button;
    private AlertDialog.Builder builder;
    private AlertDialog.Builder industry;
    public final static int CODE = 1;
    private SlidingDrawer slidingDrawer;
    private LinearLayout linearLayout;
    private int[] pics;
    private String[] picsName;
    private String[] date;
    private String[] introduce;
    private String[] industries;
    private LayoutInflater mInflater;
    private LocationService locationService;
    private String permissionInfo;
    private final int SDK_PERMISSION_REQUEST = 127;
    private Toolbar toolbar;
    private String longtitude;
    private String latitude;
    private boolean bool = false;
    private Snackbar snackbar;
    private CoordinatorLayout container;
    private CompoundButton nav_cameraButton;
    private CompoundButton nav_galleryButton;
    private CompoundButton nav_slideshowButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getPersimmions();
        mInflater = LayoutInflater.from(this);
        pics = new int[]{
                R.drawable.wm1,
                R.drawable.ak2,
                R.drawable.aa3,
                R.drawable.mm4,
                R.drawable.mk5,
                R.drawable.ab6,
        };
        picsName = new String[]{
                "URU-Montevideo",
                "AUS-Karratha",
                "AUS-Port Augusta",
                "US-Montana",
                "US-Carson",
                "AUS-Broome"
        };
        date = new String[]{
                "2016.11.11",
                "2016.11.12",
                "2016.11.13",
                "2016.11.14",
                "2016.11.15",
                "2016.11.16",
        };
        introduce = new String[]{
                "玫瑰争妍的蒙得维的亚 (Montevideo) 是乌拉圭首都，位于拉普拉塔河下游，濒临南大西洋，与阿根廷首都布宜诺斯艾利斯隔河相望。这里气候温和，常年绿树成荫，鲜花盛开，素有\"南美的瑞士\"之称。",
                "卡拉萨坐落于西澳大利亚州，建立于1968年，虽然是一座新城镇，但设施齐全且现代化，整洁有序，因多种多样的户外运动而知名，深受户外运动爱好者的亲睐。",
                "澳大利亚南澳大利亚州港口城市。在斯潘塞湾北端，皮里港西北80公里。人口1.3万。东西铁路干线和中央铁路干线会合点，沿海贸易港口。出口羊毛、小麦。二十世纪五十年代新建巨大火力发电站，供应全州所需大部电力。",
                "蒙大拿州（英语：Montana）是美国西北部的一州，州名可能来自于西班牙语的montaña，此州经济上以农牧业为主，作物主要有燕麦、大麦和甜菜，亦有重要的采矿和伐木业。",
                "卡森市坐落在巍峨的内华达山脉下。这座美丽的小城绿树环绕，遍布着维多利亚式住宅和古老的市政建筑。",
                "布鲁姆位于西澳大利亚州首府珀斯北面2200公里处,是西澳大利亚州金伯利地区的主要城镇,也是著名的旅游圣地。",
        };

        industries = new String[]{"国土资源部", "环境保护部", "农业部", "住房和城乡建设部", "交通运输部", "国家林业局", "民政部", "中国地震局", "水利部", "国家统计局"};
        linearLayout = (LinearLayout) findViewById(R.id.id_gallery);
        container = (CoordinatorLayout) findViewById(R.id.coordinator);
        for (int i = 0; i < pics.length; i++) {
            View view = mInflater.inflate(R.layout.activity_index_gallery_item, linearLayout, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            ImageView imageView1 = (ImageView) view.findViewById(R.id.shadowpic);
            TextView textView1 = (TextView) view.findViewById(R.id.shadow);
            imageView.setImageResource(pics[i]);
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    systemWebView.loadUrl("javascript:getPicId(\"" + finalI + "\")");
                    slidingDrawer.close();
                    Snackbar.make(container, introduce[finalI], Snackbar.LENGTH_INDEFINITE).setAction("more..", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showSingleChoiceDialog(introduce[finalI]);
                        }
                    }).show();

                }
            });
            TextView textView = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
            textView.setText(picsName[i]);
            textView1.setText(date[i]);
            linearLayout.addView(view);
        }

        slidingDrawer = (SlidingDrawer) findViewById(R.id.sliding);
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
            }

        });

        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {

            @Override
            public void onDrawerClosed() {
            }

        });

        slidingDrawer.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {

            @Override
            public void onScrollEnded() {
            }

            @Override
            public void onScrollStarted() {
            }

        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        systemWebView.loadUrl("javascript:backHome()");
                        break;

                    case R.id.share:
                        Snackbar.make(container, "抱歉，分享功能尚未开放", Snackbar.LENGTH_LONG).show();
                        break;

                    case R.id.locate:
                        locationService.registerListener(mListener);
                        locationService.start();

                }
                return true;
            }

        });


        button = (Button) findViewById(R.id.compass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                systemWebView.loadUrl("javascript:compass(\"compass\")");
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        systemWebView = (SystemWebView) findViewById(R.id.cordovaWebView);
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(this);
        cordovaWebView = new CordovaWebViewImpl(new SystemWebViewEngine(systemWebView));
        cordovaWebView.init(this, parser.getPluginEntries(), parser.getPreferences());
        systemWebView.loadUrl("file:///android_asset/www/index.html");
        MenuItem nav_camera = navigationView.getMenu().findItem(R.id.layer1);
        MenuItem nav_gallery = navigationView.getMenu().findItem(R.id.layer2);
        MenuItem nav_slideshow = navigationView.getMenu().findItem(R.id.layer3);
        nav_cameraButton = (CompoundButton) MenuItemCompat.getActionView(nav_camera);
        nav_galleryButton = (CompoundButton) MenuItemCompat.getActionView(nav_gallery);
        nav_slideshowButton = (CompoundButton) MenuItemCompat.getActionView(nav_slideshow);

        nav_cameraButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    nav_cameraButton.setClickable(false);
            }
        });

        nav_galleryButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    nav_galleryButton.setClickable(false);
            }
        });

        nav_slideshowButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    nav_slideshowButton.setClickable(false);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationService = ((PicApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener);
        locationService.stop();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        /**
         * city life
         */
        if (id == R.id.layer1) {
            nav_cameraButton.setChecked(true);
            nav_galleryButton.setChecked(false);
            nav_slideshowButton.setChecked(false);
            systemWebView.loadUrl("javascript:selectLayers(\"0\")");
        } else if (id == R.id.layer2) {
            nav_cameraButton.setChecked(false);
            nav_galleryButton.setChecked(true);
            nav_slideshowButton.setChecked(false);
            systemWebView.loadUrl("javascript:selectLayers(\"1\")");
        } else if (id == R.id.layer3) {
            nav_cameraButton.setChecked(false);
            nav_galleryButton.setChecked(false);
            nav_slideshowButton.setChecked(true);
            systemWebView.loadUrl("javascript:selectLayers(\"2\")");
        }
        if (id == R.id.hangye) {
            showIndustryDialog();
        } else if (id == R.id.nav_send) {
            Snackbar.make(container, "抱歉，社交功能尚未开放" , Snackbar.LENGTH_LONG).show();
        } else if (id == R.id.userInfo) {

            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 0);
        }
        else if (id == R.id.order){
            Snackbar.make(container, "抱歉，订单功能尚未开放" , Snackbar.LENGTH_LONG).show();
        }else if (id == R.id.guide){
            Intent intent = new Intent().setClass(MainActivity.this, guideActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void startActivityForResult(CordovaPlugin cordovaPlugin, Intent intent, int i) {
        setActivityResultCallback(cordovaPlugin);
        try {
            startActivityForResult(intent, i);
        } catch (RuntimeException e) {
            activityResultCallback = null;
            throw e;
        }
    }

    @Override
    public void setActivityResultCallback(CordovaPlugin cordovaPlugin) {
        if (activityResultCallback != null) {
            activityResultCallback.onActivityResult(activityResultRequestCode, Activity.RESULT_CANCELED, null);
        }
        this.activityResultCallback = cordovaPlugin;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Object onMessage(String s, Object o) {
        if ("exit".equals(s)) {
            super.finish();
        }
        return null;
    }

    @Override
    public ExecutorService getThreadPool() {
        return threadPool;
    }

    @Override
    public void requestPermission(CordovaPlugin cordovaPlugin, int i, String s) {

    }

    @Override
    public void requestPermissions(CordovaPlugin cordovaPlugin, int i, String[] strings) {

    }

    @Override
    public boolean hasPermission(String s) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 0) {
            /**
             * get UserName
             */
            Bundle bundle = data.getExtras();
            final String userName = bundle.getString("userName");
            Log.e("---username----", userName);
            systemWebView.loadUrl("javascript:showAlert(\"" + userName + "\")");

        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, final int progress, boolean b) {

        systemWebView.loadUrl("javascript:seekBar(\"" + progress + "\")");

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void showSingleChoiceDialog(String string) {
        builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        builder.setTitle("详细信息:");
        builder.setMessage(string);
        builder.setPositiveButton("关闭", null);
        builder.setCancelable(true);
        builder.show();
    }

    private void showIndustryDialog() {
        industry = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        industry.setTitle("请选择行业:");
        final boolean checkable[] = new boolean[]{false, false, false, false, false, false, false, false, false, false};
        industry.setSingleChoiceItems(industries, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Snackbar.make(container, "您选择的行业是 ：" + industries[i], Snackbar.LENGTH_LONG).show();
                dialogInterface.dismiss();
            }
        });
        industry.setPositiveButton("关闭", null);
        industry.setCancelable(true);
        industry.show();
    }


    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }

            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                latitude = "" + location.getLatitude();
                longtitude = "" + location.getLongitude();
                systemWebView.loadUrl("javascript:setLocation(\"" + latitude + "," + longtitude + "\")");
                sb.append(location.getLatitude());
                sb.append("-");
                sb.append(location.getLongitude());
                logMsg(sb.toString());
                Snackbar.make(container, "您所在的经纬度是 ：" + sb.toString(), Snackbar.LENGTH_LONG).show();
                locationService.stop();

            }
        }
    };


    public void logMsg(String str) {
        Log.e("LOCATION", str);
    }
}