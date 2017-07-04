# android-zxingLibrary


**更新日志**

- compile 1.3 2016/08/02 优化扫描API

- compile 1.4 2016/08/03 修复扫描时二维码拉伸问题

- compile 1.5 2016/08/05 更新控制闪光灯API

- compile 1.6 2016/08/08 更新生成带logo二维码时logo不带边距可能存在的问题

- compile 1.7 2016/08/09 修改默认扫描框的大小，适配不同分辨率手机，修改自定义扫描框属性类型

- compile 1.8 2016/08/10 修复解析二维码图片时可能存在的OOM问题

- compile 1.9 2016/09/07 Library库中删除Application，在demo库中的Application执行初始化操作

- compile 2.0 2016/10/12 测试Demo中添加Android M权限处理，代码库添加自定义属性支持小圆点是否展示

- compile 2.1 2016/11/22 修复扫描中的一些bug

- compile 2.2 2017/07/04 更新zxing包，修复一些已知的bug

**使用说明**

- 可打开默认二维码扫描页面

- 支持对图片Bitmap的扫描功能

- 支持对UI的定制化操作

- 支持对条形码的扫描功能

- 支持生成二维码操作

- 支持控制闪光灯开关

**使用方式：**


- **集成默认的二维码扫描页面**

在具体介绍该扫描库之前我们先看一下其具体的使用方式，看看是不是几行代码就可以集成二维码扫描的功能。

- 在module的build.gradle中执行compile操作

```
compile 'cn.yipianfengye.android:zxing-library:2.2'
```

- 在demo Application中执行初始化操作
```
@Override
    public void onCreate() {
        super.onCreate();

        ZXingLibrary.initDisplayOpinion(this);
    }
```

- 在代码中执行打开扫描二维码界面操作

```
/**
         * 打开默认二维码扫描界面
         */
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
```
这里的REQUEST_CODE是我们定义的int型常量。

- 在Activity的onActivityResult方法中接收扫描结果

```
/**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
```

怎么样是不是很简单？下面我们可以来看一下具体的执行效果：

**执行效果：**

![image](https://github.com/yipianfengye/android-zxingLibrary/blob/master/images/ezgif.com-video-to-gif%20(2)%2015.33.08.gif)

但是这样的话是不是太简单了，如果我想选择图片解析呢？别急，对二维码图片的解析也是支持的

- **集成对二维码图片的解析功能**

- 调用系统API打开图库

```
Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
intent.addCategory(Intent.CATEGORY_OPENABLE);
intent.setType("image/*");
startActivityForResult(intent, REQUEST_IMAGE);
```

- 在Activity的onActivityResult方法中获取用户选中的图片并调用二维码图片解析API
```
if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片

                    CodeUtils.analyzeBitmap(mBitmap, new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });

                    if (mBitmap != null) {
                        mBitmap.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
```

**执行效果**

![image](http://img.blog.csdn.net/20160727170831543)

有了默认的二维码扫描界面，也有了对二维码图片的解析，可能有的同学会说如果我想定制化显示UI怎么办呢？没关系也支持滴。

- **定制化显示扫描UI**

由于我们的扫描组件是通过Fragment实现的，所以能够很轻松的实现扫描UI的定制化。

- 在新的Activity中定义Layout布局文件

```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/activity_second"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <Button
        android:id="@+id/second_button1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="取消"
        android:layout_marginTop="20dp"
        android:layout_marginLeft="20dp"
        android:layout_marginRight="20dp"
        android:layout_marginBottom="10dp"
        android:layout_gravity="bottom|center_horizontal"
        />

    <FrameLayout
        android:id="@+id/fl_my_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        ></FrameLayout>

</FrameLayout>
```

启动id为fl_my_container的FrameLayout就是我们需要替换的扫描组件，也就是说我们会将我们定义的扫描Fragment替换到id为fl_my_container的FrameLayout的位置。而上面的button是我们添加的一个额外的控件，在这里你可以添加任意的控件，各种UI效果等。具体可以看下面在Activity的初始化过程。

- 在Activity中执行Fragment的初始化操作

```
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        
        captureFragment.setAnalyzeCallback(analyzeCallback);
        /**
         * 替换我们的扫描控件
         */ getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

```

其中analyzeCallback是我们定义的扫描回调函数，其具体的定义：

```
/**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            SecondActivity.this.setResult(RESULT_OK, resultIntent);
            SecondActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            SecondActivity.this.setResult(RESULT_OK, resultIntent);
            SecondActivity.this.finish();
        }
    };
```
仔细看的话，你会发现我们调用了CondeUtils.setFragmentArgs方法，该方法主要用于修改扫描界面扫描框与透明框相对位置的，与若不调用的话，其会显示默认的组件效果，而如果调用该方法的话，可以修改扫描框与透明框的相对位置等UI效果，我们可以看一下my_camera布局文件的实现。

```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent" >

    <SurfaceView
        android:id="@+id/preview_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        />

    <com.uuzuche.lib_zxing.view.ViewfinderView
        android:id="@+id/viewfinder_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:inner_width="200dp"
        app:inner_height="200dp"
        app:inner_margintop="150dp"
        app:inner_corner_color="@color/scan_corner_color"
        app:inner_corner_length="30dp"
        app:inner_corner_width="5dp"
        app:inner_scan_bitmap="@drawable/scan_image"
        app:inner_scan_speed="10"
        app:inner_scan_iscircle="false"
        />

</FrameLayout>
```

上面我们自定义的扫描控件的布局文件，下面我们看一下默认的扫描控件的布局文件：

```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent" >

    <SurfaceView
        android:id="@+id/preview_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        />

    <com.uuzuche.lib_zxing.view.ViewfinderView
        android:id="@+id/viewfinder_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        />

</FrameLayout>
```

可以发现其主要的区别就是在自定义的扫描控件中多了几个自定义的扫描框属性：

```
<declare-styleable name="innerrect">
        <attr name="inner_width" format="dimension"/><!-- 控制扫描框的宽度 -->
        <attr name="inner_height" format="dimension"/><!-- 控制扫描框的高度 -->
        <attr name="inner_margintop" format="dimension" /><!-- 控制扫描框距离顶部的距离 -->
        <attr name="inner_corner_color" format="color" /><!-- 控制扫描框四角的颜色 -->
        <attr name="inner_corner_length" format="dimension" /><!-- 控制扫描框四角的长度 -->
        <attr name="inner_corner_width" format="dimension" /><!-- 控制扫描框四角的宽度 -->
        <attr name="inner_scan_bitmap" format="reference" /><!-- 控制扫描图 -->
        <attr name="inner_scan_speed" format="integer" /><!-- 控制扫描速度 -->
        <attr name="inner_scan_iscircle" format="boolean" /><!-- 控制小圆点是否展示 -->
    </declare-styleable>
```

通过以上几个属性我们就可以定制化的显示我们的扫描UI了，比如定制化微信扫描UI：

**执行效果**

![image](https://github.com/yipianfengye/android-zxingLibrary/blob/master/images/ezgif.com-video-to-gif%20(3)%2015.33.08.gif)

当然了如果以上的以上，你还是对定制化UI方面不太满意，可以直接下载我的项目，然后引入lib-zxing module作为你的module，直接修改其代码。

- **生成二维码图片**

- 生成带Logo的二维码图片：

```
/**
         * 生成二维码图片
         */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textContent = editText.getText().toString();
                if (TextUtils.isEmpty(textContent)) {
                    Toast.makeText(ThreeActivity.this, "您的输入为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                editText.setText("");
                mBitmap = CodeUtils.createImage(textContent, 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                imageView.setImageBitmap(mBitmap);
            }
        });
```

- 生成不带logo的二维码图片

```
/**
         * 生成不带logo的二维码图片
         */
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textContent = editText.getText().toString();
                if (TextUtils.isEmpty(textContent)) {
                    Toast.makeText(ThreeActivity.this, "您的输入为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                editText.setText("");
                mBitmap = CodeUtils.createImage(textContent, 400, 400, null);
                imageView.setImageBitmap(mBitmap);
            }
        });
```

- 执行效果

![image](https://github.com/yipianfengye/android-zxingLibrary/blob/master/images/ezgif.com-video-to-gif%20(5).gif)

- 支持控制闪光灯

```
/**
 * 打开闪光灯
 */
CodeUtils.isLightEnable(true);

/**
 * 关闭闪光灯
 */
 CodeUtils.isLightEnable(false);
```

也可以参考我的博客：<a href="http://blog.csdn.net/qq_23547831/article/details/52037710">几行代码快速集成二维码扫描库</a>
