# OnlineDictionary
A simple Java Dictionary.

### 程序概述  
  OnlineDictionary是Java程序设计课程的课后作业，能够支持多词典在线查询功能。此外，程序还实现了用户登录、点赞记录、结果排序、发送单词卡等功能。  
  该程序要求使用采用C/S模式开发，使用多种Java技术，包括多线程技术、数据库编程、正则表达式、网络编程等。  
  该程序由两人分工合作完成，通过Github管理源代码。  
  
### 程序结构  
 ![Structure](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/structure.png)
 
### 模块功能  
1. 客户端		(Client 类)  
   客户端模块主要负责控制UI界面的切换和向服务器发送请求包。客户端初始化创建与服务器的Socket连接，监听用户的操作并向服务器发送请求；自身能够根据服务器返回的响应进行UI界面的切换与输出。  
   客户端为每个客户维护一条与服务器的连接和一个UI界面容器，接口为5个switch函数负责切换到相应的用户界面。  
   ![code1](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/code1.png)  
2. 服务器		(Server 类)  
  服务器负责将客户端发送的所有请求分类进行处理，再将处理结果放入响应包中返回。此外，服务器还维护一个名为Dict的数据库。  
  服务器将请求包分为9种分类处理，生成9类不同的响应包返回。
  ![code2](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/code2.png)  
3. 数据库		(DbConnection 类)  
  数据库类构造时初始化数据库地址，用户名和密码。  
  本程序采用MySQL 数据库，配置如下：   
  驱动：	“com.mysql.jdbc.Driver”  
  URL：	"jdbc:mysql://localhost/dict"    
  在Dict数据库中维护3张表，分别为User， Words 和 Wordcard， 分别存储用户信息，单词点赞信息和各用户收件箱内容。  
  在服务器初始化数据库并成功连接以后，通过该类的3个接口函数进行查询和更新：  
  ![code3](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/code3.png)   
4. 请求/响应包	(RequestPackage/ResponsePackage 类)   
  请求包：请求包中存在一个String类型的变量负责表示请求包的种类，另存在一个String数组放置请求内容。   
  响应包：响应包中存在一个String类型的变量负责表示所响应的请求种类，以及一个String数字放置返回结果。   
  接口为get/set 函数：   
  ![code4](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/code4.png)   
  ![code5](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/code5.png)   
5. 网络查询		(OnlineFetch 类)    
  查询类负责获取相应单词的百度、必应和有道网页的html源码，通过正则匹配查询网页源码中单词释义的部分，随后截取所有释义放置在容器中通过接口返回。      
  OnlineFetch类的构造函数接收一个单词，随后通过私有变量拼接形成3个网络词典的网址，再将网页源代码通过URL类获取至本地，随后4个fetch函数获取释义和音标并存入私有变量：   
  ![code6](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/code6.png)    
  随后服务器通过调用作为接口的get函数获取所需要的信息：    
  ![code7](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/code7.png)    
  
### 实现原理
1. 页面切换   
  Client类控制5个界面，它们由5个fxml文件组成：   
  ![fxml files](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/fxml%20files.png)    
  它们分别表示登录界面，主界面，在线用户显示界面，收件箱界面和单词卡显示界面，这5个fxml文件各自存在一个Controller 类管理界面中的各类事件响应。   
  Client类主要功能是通过实现5个页面的切换来实现UI功能，5个switch函数各自使用FXMLLoader类加载相应的fxml文件放置在Stage容器中。    
  例如，当用户登录时输入正确的用户名和密码时，Client类接收到Server类返回的响应表示登录成功，便调用switchToMainboard() 函数加载主界面；如果服务器返回登录失败（如密码错误或用户名不存在），Client类则不进行跳转。    
2. 点赞记录   
  当用户点击主界面的心形图标时，客户端向服务器发送请求表示用户对某词典某单词进行点赞。服务器接收信息后，首先在数据库中查询点赞单词是否存在，若不存在则新建该条目。随后服务器进行数据库的更新操作，将相应的点赞记录加1，随后返回响应包表示点赞成功。客户端接收到该响应后将图标下的点赞数加1，表示点赞完成。   
  以下为查询和更新语句（以点赞百度为例）：   
  ![code8](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/code8.png)    
  ![code9](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/code9.png)   
3. 结果排序   
  当用户查询某个单词后，服务器不但返回单词的释义、音标等信息，还返回历史点赞数。客户端接收到该返回包后，根据各词典点赞数对放置在VBox 中的3块HBox进行重新排序后输出查询信息，实现结果排序功能：    
  ![sort results](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/sort%20results.png)   
4. 在线用户   
  当某用户点击查询在线用户时，客户端向服务器发送请求查询在线用户。服务器接收请求后在数据库中查询User表，将Online字段为1（在用户登录后，该字段会置为1）的用户结果全部存入ResponsePackage 包中返回。客户端接收返回包后将包中所有在线用户的用户名显示在ListView中。  
5. 发送/接收单词卡    
  当用户查询在线用户时，可以点击其中一位用户发送当前查询单词的单词卡。点击发送后，客户端对主界面的Pane进行截屏得到WritableImage 对象，再将其序列化（转化为BufferedImage对象后在转化为byte数组），将序列化结果 （byte数组）生成字符串放入RequestPackage对象中发送给服务器。    
  服务器得到该对象后，将String再解码为byte数组存入数据库的Wordcard表中，Wordcard存放各单词卡的发送接收信息，期中有一个字段的类型为longblob，可以存放转化为二进制类型的多媒体文件。    
  当接收方打开收件箱时，客户端返回所有发给他的邮件的信息（编号和发送人），当接收方点击查看，服务器从数据库中取出图片转化为String 放入响应包中发给接收方，随后接收方客户端解码生成WritableImage对象显示在对话框中。   
  序列化图片：   
  ![code10](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/code10.png)   
  
### 功能演示   
1. 登录注册   
  ![demo login](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/demo_login.png)   
2. 主界面   
  ![demo mainboard](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/demo_mainboard.png)    
3. 在线用户界面   
  ![demo onlineusers](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/demo_onlineusers.png)   
4. 收件箱界面   
  ![demo mails](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/demo_mails.png)   
5. 单词卡界面   
  ![demo wordcard](https://github.com/MrJxy/OnlineDictionary/blob/master/snapshot-folder/demo_wordcard.png)
 
