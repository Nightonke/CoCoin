#CoCoin

CoCoin是一款记账APP，有记账、多种方式显示支出占比和支出变化、云同步、智能提醒等功能。

###Note

仅供学习之用，请勿用于商业用途。

#下载

####APK地址：

http://beta.qq.com/m/wggf

或者直接从github上下载:

https://github.com/Nightonke/CoCoin/blob/master/APK/CoCoin%20V1.1.0.apk

####或者二维码：
![扫描下载](https://github.com/Nightonke/CoCoin/blob/master/APK/CoCoin%20V1.1.0.jpg)

#介绍

Gif都有点大，请注意流量。

####记录

 - 在记录支出时，可以选择标签、添加备注。
 - 为了记录方便，记录之前并不用解锁CoCoin，但是查看账本则需要输入密码，密码会在第一次使用CoCoin的时候输入。
 - 手指上下滑（或者摇一摇）可以打开关闭输入密码界面，手指在输入框左滑可以输入备注。

![记录](https://github.com/Nightonke/CoCoin/blob/master/Gif/%E8%AE%B0%E5%BD%95.gif)

####今日视图

 - 在记录界面输入密码之后便可以进入到账本，账本默认界面是今日视图。
 - 在今日视图中，记录有今天、昨天、本周、上周、这个月、上个月、今年、去年的支出状况。
 - 在今天和昨天页面中，有饼图总览、以及各个支出的列表显示，饼图按照标签分类支出，点击饼块可以查看该标签下、该时间点的支出详情。
 - 在其他页面中，有饼图总览和柱状图概况，可以看到周、月、年的支出变化、支出占比。同样的，点击饼块或者图柱都可以看到特定标签、特定时间段下的支出详情。

![今日视图](https://github.com/Nightonke/CoCoin/blob/master/Gif/%E4%BB%8A%E6%97%A5%E8%A7%86%E5%9B%BE.gif)

####范围视图

 - 在账本右滑打开抽屉菜单，点击可选范围视图即可进入。
 - 点击“从”和“到”设置起始日期和结束日期，便可以显示区间内的支出情况，比如图中选取2016年1月4日到2016年1月13日，那么便会显示4日到13日之间的所有支出，注意在录制的时候时间为11日，所以显示上界为11日。

![范围视图](https://github.com/Nightonke/CoCoin/blob/master/Gif/%E8%8C%83%E5%9B%B4%E8%A7%86%E5%9B%BE.gif)

####标签视图

 - 在账本右滑打开抽屉菜单，点击标签视图即可进入。
 - 标签视图有多个页面，每个页面显示不同的标签的支出总况。
 - 同样的，点击柱状图可以查看详细情况。
 - 右滑打开抽屉菜单，可以快速导航到某个标签页面。

![标签视图](https://github.com/Nightonke/CoCoin/blob/master/Gif/%E6%A0%87%E7%AD%BE%E8%A7%86%E5%9B%BE.gif)

####月视图

 - 在账本右滑打开抽屉菜单，点击月视图即可进入。
 - 月视图显示每月支出总况。
 - 右滑打开抽屉菜单，可以快速导航到某个月页面，同时也可以看到每个月的支出总额。

![月视图](https://github.com/Nightonke/CoCoin/blob/master/Gif/%E6%9C%88%E8%A7%86%E5%9B%BE.gif)

####列表视图

 - 在账本右滑打开抽屉菜单，点击列表视图即可进入。
 - 列表视图列出了记录以来所有的记录。
 - 最右边的滑动条可以快速滑动。
 - 对某个项左滑可以编辑。
 - 对某个项右滑可以删除（在一定时间内可撤销）。

![列表视图](https://github.com/Nightonke/CoCoin/blob/master/Gif/%E5%88%97%E8%A1%A8%E8%A7%86%E5%9B%BE.gif)

####设置界面

 - 在账本右滑打开抽屉菜单，点击设置即可进入。
 - 点击头像或者第一个卡片（CardView）都可以进行注册、登录或者是登出。这会在注册介绍中详细介绍。
 - 如果打开每月支出限定，你可以：
  - 设定每月最大支出限额，这会在账本中提醒。
  - 如果开启颜色提醒，那么如果当月记录的支出总额超过了下一行中的数字（也就是“超过此数额时提醒”），记录界面的颜色主题会变化，以示提醒（这在Gif中有演示）。
  - 可以自己设定提醒颜色。
  - 可以限制自己在超出每月限额时不可记录。
 - 可以为你的账本起名，名字会在账本中显示（代替CoCoin的默认账本名，也就是CoCoin）。
 - 可以修改账本密码。
 - 可以对标签进行排序，排序之后，在记录界面、账本界面的标签顺序都会改变。
 - 如果打开在标签视图中显示图片，那么在标签视图中，将会对相应的标签显示相应的图片。当然打开会造成额外的内存消耗。
 - 如果选择甜甜圈饼图，那么饼图是空心的。
 - 以上所有设置，在登录之后，都会同步到云端。下次再登录会询问用户希望将本地新设置上传到云端还是希望将云端的旧设置同步到本地。

![设置](https://github.com/Nightonke/CoCoin/blob/master/Gif/%E8%AE%BE%E7%BD%AE.gif)

####云端同步

 - CoCoin将用户的记录同步到云端，在登录情况下，用户对记录的一切操作都会同步。
 - 通过填写与其他用户不同的邮箱即可注册。
 - 注意，每台手机只能注册一个用户（绑定了手机的设备id）。
 - 新用户注册时，会收到一封邮件提醒注册成功。

![云端同步](https://github.com/Nightonke/CoCoin/blob/master/Gif/%E6%B3%A8%E5%86%8C.gif)

####语言

 - CoCoin支持中文英文，在不同系统语言下显示不同语言。

![语言](https://github.com/Nightonke/CoCoin/blob/master/Gif/%E8%AF%AD%E8%A8%80.gif)

####颜色提醒演示

![颜色提醒](https://github.com/Nightonke/CoCoin/blob/master/Gif/%E9%A2%9C%E8%89%B2%E6%8F%90%E9%86%92.gif)

####首次安装CoCoin设置账本密码演示

![首次设置密码](https://github.com/Nightonke/CoCoin/blob/master/Gif/%E8%AE%BE%E7%BD%AE%E5%AF%86%E7%A0%81.gif)

####其他

 - 使用了腾讯的Bugly，可以收到崩溃的数据统计。

#Todo

 - 性能优化
 - 云端同步显示提示，让用户知道同步情况
 - 优化数据库读取速度
 - 加入自定义标签功能

#License

> The MIT License (MIT)
> 
> Copyright (c) 2016 Nightonke
> 
> Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
> 
> The above copyright notice and this permission notice shall be
> included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
