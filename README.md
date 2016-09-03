# Material-Design-login

---

This is a Material-Design-login Demo. You can set the state(Sign-Up or Login)  by slip events in this demo, or you can create other animations when progress callback.

<img src="/Users/Weiwu/Movies/demo_show1.gif" width=300></img>
<img src="/Users/Weiwu/Movies/demo_show2.gif" width=300></img>

## How To Implements

CatchScrollLayout is responsible for monitoring external sliding events. 
You can set the area of monitored by setting the height of CatchScrollLayout (“match_parent” usually).

```xml

<yellow5a5.materialdesignlogin.View.CatchScrollLayout
    android:id="@+id/catch_sroll_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center_horizontal"
    android:orientation="vertical"
    android:clickable="true">
    
    <!--add the view like this-->
    
    <yellow5a5.materialdesignlogin.View.SignUpContainer
        android:id="@+id/sign_up_container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
    

</yellow5a5.materialdesignlogin.View.CatchScrollLayout>
```

Set the IScrollCallBack do anything you want.

```java

private IScrollCallBack mIScrollCallBack;

public interface IScrollCallBack {
    void onScrollProcess(int process, boolean isLeft);
}

public void setIScrollCallBack(IScrollCallBack l) {
    mIScrollCallBack = l;
}
```

You can change the number of segments by UnderlineDevider. Set it in xml (app:devide_count) or call the method (setDevider). UnderlineDevider is independent, you can use it in any other place.

```xml

<yellow5a5.materialdesignlogin.View.UnderlineDevider
    android:id="@+id/underline_v"
    android:layout_width="match_parent"
    android:layout_height="2dp"
    android:layout_gravity="bottom"
    app:devide_color="#ffffff"
    app:devide_count="2" />
```


## Demo－Introduction

You can set the state(Sign-Up or Login)  by slip events. SignUpContainer should contains the logic of logining and registering. It's easy to add other task you wanted or change the method in callback.

```java

mCatchScrollLayout.setIScrollCallBack(new CatchScrollLayout.IScrollCallBack() {
    @Override
    public void onScrollProcess(int process, boolean isLeft) {
        if (!isLeft){
            process = 100 - process;
        }
        mSignUpContainer.setAnimProportion(process);
    }
});

mSignUpContainer.setIConfirmCallBack(new SignUpContainer.IConfirmCallBack() {
    @Override
    public void goNext() {
        //TODO
    }
});
```


## License

    Copyright 2016 Yellow5A5
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
