# -
This is the demo for chatting in the same local area network and you can achieve Voice Dictation...
## 项目介绍
本项目是基于java开发的聊天室（添加了语音转写的功能）。有用户注册、用户登陆、用户聊天、语音转写、显示聊天室用户列表等局域网客户端通信功能。如果服务器还没有启动，则客户端的操作是不会有任何响应的。本项目可以解决部分局域网通信的基本需求。
## 项目展示
![](img-folder/chatting-1.PNG)
![](img-folder/chatting-2.PNG)
![](img-folder/chatting-3.PNG)
## 项目部署
首先将项目导入eclipse中，将socket中的ip改为作为服务器端主机的ip，访问数据库的ip改为作为数据存储的主机的ip，com.iflytek.util包中的版本号根据自己申请的讯飞账号的appid更改。

## 使用的知识点   
##### Socket实现TCP一对多的通信    
Java提供了较多的Socket相关的API，Socekt编程的时候可以方便地调用API来实现通信，在客户端ping通的前提下（需要关闭防火墙等操作），以其中一台主机为服务端，其余主机作为客户端去访问服务端的IP地址和端口，即可完成同一局域网下的通信。     

服务器不断监听请求：  

	ServerSocket serve=new ServerSocket(端口)；
	while(true){
		Socket socket=server.accept();    
		....
	}
	public void run(){
		try{
			InputStreamReader reader=new InputStreamReader(socket.getInputStream());
			BufferedReader buffer_reader=new BufferedReader(reader);
			PrintWriter writer=new PrintWriter(socket.getOutputStream());
			String request=buffer_reader.readLine();
			String line="";
			writer.println(line);
			writer.flush();
			writer.close();
			buffer_reader.close();
			socket.close();
		}catch()....

客户端向服务端发送请求:

	Socket socket=new Socket(IP,port);
	InputStreamReader reader=new InputStreamReader(socket.getInputStream());     
	BufferedReader buffer_reader=new BufferedReader(reader);   	 
	PrintWriter writer=new PrintWriter(socket.getOutputStream());    
	String readline="";
	writer.println(readline);
	writer.flush();
	String response=buffer_reader.readLine();
	writer.close();
	buffer_reader.close();
	socket.close();				

#### 多线程
多线程使应用程序可以同时进行不同的操作，处理不同的事件。在多线程机制中，不同的线程处理不同的任务，他们之间互不干涉，不会由于一处等待影响其他部分，这样容易实现网络上的实时交互操作。多线程保证了较高的执行效率。服务端不断为客户端的请求创建线程就用到了多线程技术。

#### 数据库
用户名和密码信息存储在数据库中，根据输入的用户名和密码与数据库的用户名和密码作匹配，都相等就向服务器发起socket请求。Java项目中实现与数据库的连接需要导入JDBC包（在jdbc-download文件夹中下载解压，并在项目中加入jar包），JDBC是java设计者为数据库编程提供的一组接口。这组接口对开发者来说访问数据库的工具。该项目使用的数据库为microsoft sql server,使用前得配置数据库的端口打开。
连接数据库

	conn = DriverManager.getConnection(url, user, password);  					         
	stmt = conn.createStatement(); 
	rs = stmt.executeQuery(sql);

#### swing框架    
界面设计使用swing框架，在eclipse中可以通过下载windowsbuilder来简化代码步骤。(Swing采用MVC设计模式）   

#### 语音转写  
使用讯飞开放平台提供的sdk，[详细请见https://www.xfyun.cn/doc/asr/voicedictation/Java-SDK.html#_1%E3%80%81%E7%AE%80%E4%BB%8B](https://www.xfyun.cn/doc/asr/voicedictation/Java-SDK.html#_1%E3%80%81%E7%AE%80%E4%BB%8B)将官方demo中需要的类提取出来使用          

#### hashmap与hashset的使用
将在线用户用hashmap存储，键是用户名，值是对象（包括用户名和socket）        
选中用户用hashset存储，如果在线用户包含选中用户，则向选中用户发送信息。   


......


