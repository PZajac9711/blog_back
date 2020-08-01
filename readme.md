# Short description

This project allow you to manage your own blog.It provides functions like
- Adding a new user
- Authentication users
- Adding new posts
- Activate or Deactivate post
- Modifying posts
- Addition comments
- And more

It's not a full solution, you still need to create your own front-end application.

#Example (angular)
// tutaj wideo ale to jeszcze nie 

#Technology used in project
* Spring framework
* Hibernate
* H2 database
* JWT
#End points

* /login - used for generate JWT, status:200 if data is correct, status:401 in other situation
* /register - used for create users, status:201 if created, status:400 in other situation
* /random - used for getting random image from server, required header with JWT 
* /score - used to increase/reduce score for specific image, required header with JWT
* /page - used for getting pages, page = 0 gets five first images from DB, page = 1 gets 5-10....
* /uploadFile - allow you to send image from your pc on server. 


#Important
* Server automatically create and clear database after every run, if you wanna change it you need to modify application.properties
