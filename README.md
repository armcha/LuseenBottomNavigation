# Material Bottom Navigation

BottomNavigationView

Created according by Google [guideLine][1]
[1]: https://www.google.com/design/spec/components/bottom-navigation.html#bottom-navigation-usage
[2]: http://luseen.com/
By [Luseen][2] Technologies 



![](ScreenShots/gifView2.gif)


Download
--------

Gradle:
```groovy
compile 'com.github.armcha:LuseenBottomNavigation:1.1'
```
Maven:
```xml
<dependency>
  <groupId>com.github.armcha</groupId>
  <artifactId>LuseenBottomNavigation</artifactId>
  <version>0.0.1</version>
  <type>pom</type>
</dependency>
```
Usage
-----

Add the BottomNavigationView to your layout

```xml
 <com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView
        android:id="@+id/bottomNavigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true" />
```

Add BottomNavigation items 

```java
  BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
  
  BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                ("Record", getResources().getColor(R.color.firstColor), R.drawable.ic_mic_black_24dp);
  BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("Like", getResources().getColor(R.color.secondColor), R.drawable.ic_favorite_black_24dp); 
                
  bottomNavigationView.addTab(bottomNavigationItem);
  bottomNavigationView.addTab(bottomNavigationItem1);
```

Then set your `OnBottomNavigationItemClickListener`

```java
 
 bottomNavigationView.setOnBottomNavigationItemClickListener(new BottomNavigationView.OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                Toast.makeText(MainActivity.this, "Item " +index +" clicked", Toast.LENGTH_SHORT).show();
            }
        });
```

Cusomize
--------

```java
 bottomNavigationView.isWithText(true);
```
![](ScreenShots/gifView.gif)
```java
 bottomNavigationView.isColoredBackground(false);
 bottomNavigationView.setItemActiveColorWithoutColoredBackground(yourColor);
```
![](ScreenShots/gifView3.gif)

 
## Contact 

Pull requests are more than welcome.

Please fell free to contact me if there is any problem when using the library.

- **email**: armcha01@gmail.com
- **facebook**: https://web.facebook.com/chatikyana
 
License
--------


          Copyright 2016 Arman Chatikyan

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.
    
