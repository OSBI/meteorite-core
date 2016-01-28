# API Examples

## Add user

To add a user to the platform you can post the following object:

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