package easyrun.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import easyrun.bean.UserBean;
import easyrun.util.R;
import easyrun.util.ServerData;

public class MainActivity extends Activity implements View.OnClickListener{
    private ResideMenu resideMenu;
    private MainActivity mContext;
    private ResideMenuItem itemHome;         //主界面
    private ResideMenuItem user_upload_pic;  //用户上传照片（积分兑换）
    private ResideMenuItem itemSettings;     //个人设置
    private ResideMenuItem shopping;         //易跑商城
    private ResideMenuItem find_Picture;     //照片查询
    private ResideMenuItem master_upload_pic;//摄影师上传照片
    private ResideMenuItem marathon_register;//赛事报名

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;

    private UserBean user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        //接收LoginActivity传来的账号密码,并保存在UserBean中
        Bundle bundle = getIntent().getExtras();
        user = new UserBean();
        if(bundle!=null){
            user.setAccount(bundle.getString("account"));
            user.setPassword(bundle.getString("password"));
        }

        //设置ActionBar
        ActionBar ab = getActionBar();

        ab.setCustomView(R.layout.shopping_actionbar_search);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        EditText search = (EditText) ab.getCustomView().findViewById(R.id.search);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(MainActivity.this, "Search triggered",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
                |  ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);



        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //ActionBar左侧导航栏
        String[] values = new String[]{
                "Stop Animation (Back icon)",
                "Stop Animation (Home icon)",
                "Start Animation",
                "Change Color",
                "GitHub Page",
                "Share",
                "Rate"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        mDrawerToggle.setAnimateEnabled(false);
                        drawerArrow.setProgress(1f);
                        break;
                    case 1:
                        mDrawerToggle.setAnimateEnabled(false);
                        drawerArrow.setProgress(0f);
                        break;
                    case 2:
                        mDrawerToggle.setAnimateEnabled(true);
                        mDrawerToggle.syncState();
                        break;
                    case 3:
                        if (drawerArrowColor) {
                            drawerArrowColor = false;
                            drawerArrow.setColor(R.color.ldrawer_color);
                        } else {
                            drawerArrowColor = true;
                            drawerArrow.setColor(R.color.drawer_arrow_second_color);
                        }
                        mDrawerToggle.syncState();
                        break;
                    case 4:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/IkiMuhendis/LDrawer"));
                        startActivity(browserIntent);
                        break;
                    case 5:
                        /*Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        share.putExtra(Intent.EXTRA_SUBJECT,
                            getString(R.string.app_name));
                        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_description) + "\n" +
                            "GitHub Page :  https://github.com/IkiMuhendis/LDrawer\n" +
                            "Sample App : https://play.google.com/store/apps/details?id=" +
                            getPackageName());
                        startActivity(Intent.createChooser(share,
                            getString(R.string.app_name)));*/
                        break;
                    case 6:
                        String appUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
                        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl));
                        startActivity(rateIntent);
                        break;
                }

            }
        });

        mContext = this;
        setUpMenu();
        if( savedInstanceState == null )
            changeFragment(new HomeFragment());

        ab.hide();
    }

    private void setUpMenu() {

        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.index_bg);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.65f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "主界面");
        itemSettings = new ResideMenuItem(this, R.drawable.icon_settings, "设置");
        user_upload_pic = new ResideMenuItem(this,R.drawable.upload, "我要上传");
        find_Picture = new ResideMenuItem(this,R.drawable.icon_find_pic, "照片查询");
        shopping = new ResideMenuItem(this,R.drawable.icon_shopping, "易跑商城");
        //master_upload_pic = new ResideMenuItem(this,R.drawable.upload, "摄影师上传");
        marathon_register = new ResideMenuItem(this,R.drawable.icon_register, "赛事报名");


        itemHome.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        user_upload_pic.setOnClickListener(this);
        shopping.setOnClickListener(this);
        find_Picture.setOnClickListener(this);
        //master_upload_pic.setOnClickListener(this);
        marathon_register.setOnClickListener(this);

        //左边项
        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(marathon_register, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(user_upload_pic, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(find_Picture, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(master_upload_pic, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(shopping, ResideMenu.DIRECTION_LEFT);

        //右边项
        resideMenu.addMenuItem(itemSettings, ResideMenu.DIRECTION_RIGHT);
        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        /**点击左边菜单栏事件*//*
        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        *//**点击右边菜单栏事件*//*
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });*/
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    /**点击事件，选择要打开的页面项*/
    @Override
    public void onClick(View view) {

        if (view == itemHome){
            HomeFragment homeFragment = new HomeFragment();
            //传递用户信息
            Bundle bundle = new Bundle();
            bundle.putParcelable("userInfo",user);
            homeFragment.setArguments(bundle);
            changeFragment(homeFragment);
        }else if (view == itemSettings){
            changeFragment(new SettingsFragment());
        }else if (view == user_upload_pic){
            if (!ServerData.checkNetwork(MainActivity.this)) {// 检测网络
                Toast toast = Toast.makeText(MainActivity.this,"网络未连接", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, NonetActivity.class);
                startActivity(intent);
            }
            else {
                UserUploadPicFragment userUploadPicFragment = new UserUploadPicFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("userInfo", user);
                userUploadPicFragment.setArguments(bundle);
                changeFragment(userUploadPicFragment);
            }
        }else if (view == find_Picture){
            if (!ServerData.checkNetwork(MainActivity.this)) {// 检测网络
                Toast toast = Toast.makeText(MainActivity.this,"网络未连接", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, NonetActivity.class);
                startActivity(intent);
            }
            else {
                FindPictureFragment findPictureFragment = new FindPictureFragment();
                //传递用户信息
                Bundle bundle = new Bundle();
                bundle.putParcelable("userInfo", user);
                findPictureFragment.setArguments(bundle);
                changeFragment(findPictureFragment);
            }
        }else if (view == shopping){
            changeFragment(new ShoppingFragment());
        }else if (view == marathon_register){
            MarathonRegisterFragment marathonRegisterFragment=new MarathonRegisterFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelable("userInfo",user);
            marathonRegisterFragment.setArguments(bundle);
            changeFragment(marathonRegisterFragment);
        }

        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
