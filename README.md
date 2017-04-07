# XXRecyclerView
## 添加依赖
1.在project目录的build.gradle的allprojects节点添加
```java maven { url "https://jitpack.io" }```
如下:
```java
    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
    }
```
2.在自己Modul的build.gradle中添加```compile 'com.github.luoshihai:XXRecyclerView:v1.2.8''```
如下:
```java
dependencies {
	        compile 'com.github.luoshihai:XXRecyclerView:v1.2.8'
	}
```
## 开始使用

##### lirbrary包含一个XXRecycleView和CommonRecyclerAdapter
* XXRecycleView是一个可以加头加脚的recyclerview
* CommonRecyclerAdapter是recyclerview的万能适配器

##### XXRecycleView的api
* ```rv.setEmptyView(emptyView);``` 设置空布局
* ```rv.setLoadMoreEnabled(true);``` 设置上拉加载
* ```rv.setLoadMoreEnabled(false)``` 将上拉加载布局删除
* ```rv.setPullRefreshEnabled(true);```设置下拉刷新
* ```rv.setPullRefreshEnabled(false);```将下拉刷新布局删除
* ```rv.addHeaderView(headerView);```添加头  添加头是有顺序的  注意
* ```rv.addFooterView(footerView);```添加脚  添加脚也是有顺序的  注意
* ```java
 rv.setOnRefreshListener(new PullRefreshRecycleView.OnRefreshListener() {
            @Override
            public void onRefresh() {//正在刷新的到时候回调
            }
            @Override
            public void refreshEnd() {//刷新完成回调
            }
        });
```
* ```java
  rv.setOnLoadMoreListener(new XXRecycleView.OnLoadMoreListener() {
            @Override
            public void onLoad() {//正在加载回调
            }

            @Override
            public void loadEnd() { // 加载完成回调
            }
        });
 ```
*  ```rv.stopLoad();```停止加载
* ```rv.stopRefresh();```停止刷新

##### 如果想用自己的下拉刷新或者下拉加载布局

1. 写一个类继承RefreshViewCreator 和 LoadViewCreator里面回调几个状态
2. 调用 ```rv.setPullRefreshEnabled(false,myRefreshViewCreator);```
        ``` rv.setLoadMoreEnabled(false,false,myLoadViewCreator);```

##### CommonRecyclerAdapter的api
*  显示单重条目
```java
rv.setAdapter(new CommonRecyclerAdapter<String>(this, datas,android.R.layout.simple_list_item_1) {
            @Override
            public void convert(CommonViewHolder helper, String item, int position, boolean itemChanged) {
                //通过helper来设置界面内容 里面有很多方法  可以进去看代码
                //boolean itemChanged 是数据是否发生改变的回调 是通过每个条目数据的equals()方法来对比
                // 所以我们应该复写条目数据bean的equalse()方法  否则比较的是地址值  就会一直返回false
                helper.setText(android.R.id.text1, item);
            }

        });
 ```
* 显示多种条目
```Java
MultiTypeSupport<String> multiTypeSupport = new MultiTypeSupport<String>() {
            @Override
            public int getLayoutId(String item, int position) {
                if (position % 2 == 0) return android.R.layout.simple_list_item_1;
                return android.R.layout.simple_list_item_2;
            }
        };
        CommonRecyclerAdapter<String> adapter = new CommonRecyclerAdapter<String>(this, datas, multiTypeSupport) {
            @Override
            public void convert(CommonViewHolder holder, String s, int position, boolean isChanged) {
                if (position % 2 == 0) {
                    holder.setText(android.R.id.text1, s);
                } else {
                    holder.setText(android.R.id.text1, s);
                    holder.setText(android.R.id.text2, s);
                }

            }

        };
 ```
* 设置点击事件

```java
 adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(CommonViewHolder commonViewHolder, int position) {

            }
        });
```
```java
adapter.setOnItemLongClickListener(new CommonRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClickListener(CommonViewHolder commonViewHolder, int position) {

            }
        });
````
* ```adapter.getDatas();```拿到当前rv展示的数据
* ``` adapter.getItemCount();``` 拿到当前rv的条目数
*  操作数据的方法    注意: 操作数据只能通过adapter 其他方式都无效
```java
add(T elem) 添加条目
addAll(List<T> elem)  添加集合
set(T oldElem, T newElem)  根据旧数据替换
setAt(int index, T elem)   根据角标替换
remove(T elem) 删除数据
removeAt(int index) 删除某位置的数据
replaceAll(List<T> elem) 替换数据
contains(T elem) 是否包含数据
clear()  清空数据
```
