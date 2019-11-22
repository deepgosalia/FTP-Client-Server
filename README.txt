CN project by Deep Chetan Gosalia (UFID: 5697-9299)



Steps:
1. Compile javac FTPServer.java and run java FTPServer
2. Compile javac FTPClient.java and run java FTPClient   (Server can handle multiple clients)
3. In FTPClient: 
   ftpclient localhost 8000
4. Enter credentials:
   default username is client1 and password is abc1
5. Enter any of the below commands
   >dir   (This will print files in server directory)
   >get <file_name>
   >upload <file_name>
6. To add new user (make sure you are logged in any of current user)
   > add <user_name> <passsword>
