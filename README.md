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

