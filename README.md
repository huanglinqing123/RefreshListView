# listview下拉刷新
### 使用方法
#### maven：
#####   
      <dependency>
       <groupId>com.huanglinqing</groupId>
       <artifactId>RefreshListview</artifactId>
       <version>1.0.0</version>
       <type>pom</type>
      </dependency>
##### Gradle：
###### compile 'com.huanglinqing:RefreshListview:1.0.0'
##### 版本原因若报v27错误，可在app build.gradle中添加:
#####
   allprojects {
      repositories {
        jcenter()
        maven { url "https://maven.google.com" }
     }
 }
 ######
 在xml布局中：
#####
 <listview.huanglinqing.com.refreshlistview.Relistview<br>
         &emsp;&emsp;android:id="@+id/list"<br>
        &emsp;&emsp;android:layout_width="match_parent"<br>
        &emsp;&emsp;android:layout_height="wrap_content"><br>
</listview.huanglinqing.com.refreshlistview.Relistview><br>

#####
java代码中使用：

 relistview.setOnRefreshListener(new Relistview.OnRefreshListener() {
            @Override
            public void onRefresh() {
                
            }

        });

在onRefresh方法中添加刷新执行的方法即可。
#####
效果图如下所示：
#####
![alt text](http://oh5h550zc.bkt.clouddn.com/TIM%E5%9B%BE%E7%89%8720180525090609.jpg "title")
