这是一套以屏幕宽度的dpi与设计图的比例生成以dp为单位的屏幕适配方案</br>
使用方法如下：运行编译后的jar文件或导入idea运行，出现如下图界面</br>
![软件运行截图](http://git.oschina.net/uploads/images/2016/0927/173940_54b264f4_123246.png "软件运行截图")</br>
有<b>4个值需要填写，其中最重要的是“设计图宽度”</b>，在该输入框中输入ui设计图的宽度，比如我拿到的设计图为750x1334，那么这个框就是输入750。</br>
“最小生成宽度”为适配的最小屏幕宽度dpi，“最大生成宽度”为适配的最大屏幕宽度dpi，这两个值都代表着Android中sw-{n}dpi的值</br>
“生成宽度间隔”为每套方案的间隔，按照截图生成结果为下图所示</br>
![资源生成结果](http://git.oschina.net/uploads/images/2016/0927/174336_31489736_123246.png "资源生成结果")</br>
使用方法：如果设计图中字体大小为30px，那么直接引用</br>
android:textSize="@dimen/s30"</br>
</br>
如果设计图中控件左间距20px，则直接引用</br>
android:layout_marginLeft="@dimen/x20"</br>
</br>
优点：适配简单，使用方便，经测试，误差最大仅为：(生成宽度间隔-1)/设计图宽度</br>
缺点：生成的方案越多、设计图宽度越大，增加的apk大小也越大</br>
ps：经测试，本套适配方案在42寸1920x1080的广告机与老旧低分辨率平板上，都与常见手机一样，达到了与设计图高度一致的效果，（低分辨率大屏设备相当于正常手机等比放大效果）
