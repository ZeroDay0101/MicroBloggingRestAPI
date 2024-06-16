Spring boot simple rest api for micro-blogging with functionality like:

Creating and deleting users and profiles

Creating and deleting posts

Interacting with posts (like and comment)

Username and password login

Roles and additional functionality for admins

Sql database

etc.

Encryption keys for signing jwt in directory src\main\resources\encryptionKeys are only example ones and are visible to everyone.

You ought to generate your own keys if you're to use this code in your own project  

Public key should be in X509 format and Private key should be in PKCS8 with headers just like in example files.

|                  Log user in                  |  POST  |                  /login                  |
|:---------------------------------------------:|:------:|:----------------------------------------:|
| Exchange user refresh token for access token  |  POST  |           /login/refresh_token           |
|                   Add post                    |  POST  |                /api/post                 |
|                   Edit post                   | PATCH  |                /api/post                 |
|               Delete post by id               | DELETE |              /api/post?id=               |
|                Get post by id                 |  GET   |              /api/post?id=               |
|           Get all posts by user id            |  GET   |         /api/post/byProfile?id=          |
|                   Like post                   |  POST  |            /api/post/like?id=            |
|             Remove like from post             |  POST  |           /api/post/unlike?id=           |
|            Get comments by post id            |  GET   |     /api/post/comments/{id}/offset=?     |
|             Get likes by post id              |  GET   |      /api/post/likes/{id}/offset=?       |
|            Get profile by username            |  GET   |    /api/profile/byUsername/{username}    |
|               Get profile by id               |  GET   |          /api/profile/byId/{id}          |
|          Get profile id by username           |  GET   | /api/profile/getId/byUsername/{username} |
|                  Create user                  |  POST  |                /api/user                 |
|                  Delete user                  | DELETE |              /api/user/{id}              |
| Change username (Of currently logged in user) | PATCH  |          /api/user?newUsername=          |
|          Change username by user id           | PATCH  |       /api/user/?newUsername=/{id}       |
|             Change user password              | PATCH  |            /api/user/password            |
=======
To make this api work, expose database in cofigured default url jdbc:postgresql://localhost:5432/SocialMedia or change it to yours in application.properties
username=postgres
password=1234

>>>>>>> origin/master
