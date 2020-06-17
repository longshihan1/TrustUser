# 无Root抓包插件
## 实现原理
从Android7.0之后系统不再信任用户CA证书。主要限制在应用的targetSdkVersion >= 24时生效，如果targetSdkVersion < 24即使系统是7.0+依然会信任（用户证书）。也就是说即使安装了用户CA证书，在Android 7.0+的机器上，targetSdkVersion >= 24的应用的HTTPS包就抓不到了。对于普通的HTTP请求，可以使用一些抓包工具进行抓包，对于targetSdkVersion >= 24的Https请求，只需要信任相关的证书，也可以抓取Https请求。
我们通过修改运行状态的targetSdkVersion实现用户证书被信任，Https可以被抓包
## 用法
主要是给无Root场景下使用，安装太极，处于太极阴状态，在安装APP，同时需要安装抓包工具的证书(比如Charles)，即可实现抓包。

## 效果
![](https://user-gold-cdn.xitu.io/2020/6/17/172bff7612af2072?w=850&h=360&f=jpeg&s=141581)