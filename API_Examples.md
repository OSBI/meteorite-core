# API Examples


## Get Existing Users

http://localhost:8181/cxf/rest/core/user

Returns a string list of id's and usernames:

    [
       {
          "id":1,
          "username":"admin"
       },
       {
          "id":2,
          "username":"smith"
       }
    ]

## Add user (or update)

To add a user to the platform you can POST the following object:

    {
    "@class": "bi.meteorite.core.security.rest.objects.UserObj",
    "username": "test2",
    "password": "test2",
    "orgId": 0,
    "email": "test@test.com",
    "roles": null
    }

to http://localhost:8181/cxf/rest/core/user

The @class property is mandatory to specify the concrete class that implements the interface used in the server API.

To update include the user id.

## Update a user

To add a user to the platform you can PUT the following object:

    {
    "@class": "bi.meteorite.core.security.rest.objects.UserObj",
    "username": "test2",
    "password": "test2",
    "orgId": 0,
    "email": "test@test.com",
    "roles": null,
    "id": 1
    }

to http://localhost:8181/cxf/rest/core/user

The @class property is mandatory to specify the concrete class that implements the interface used in the server API.
 

##  Get User

To get user details for a user: 

http://localhost:8181/cxf/rest/core/user/lookup/{id}

id is the user id.

    {
       "@class":"bi.meteorite.objects.UserImpl",
       "id":1,
       "username":"admin",
       "password":"admin",
       "roles":[
          {
             "id":1,
             "role":"ROLE_ADMIN",
             "userId":null
          },
          {
             "id":2,
             "role":"ROLE_USER",
             "userId":null
          }
       ],
       "orgId":0,
       "email":null
    }
    
    
## Delete a User

Send a DELETE to 
http://localhost:8181/cxf/rest/core/user/{id}

and it will remove the user and associated roles.