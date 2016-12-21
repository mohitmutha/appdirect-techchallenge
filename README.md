AppDirect Tech Challenge
===============================
The following notifications are integrated
- Subscription Create
- Subscription Cancel
- Assign User
- Unassign User

Open id authentication with AppDirect is also implemented

*The following are the notification end points*
GET /appdirect/subscription/create?url={url}
GET /appdirect/subscription/cancel?url={url}
GET /appdirect/user/assign?url={url}
GET /appdirect/user/unassign?url={url}

*The following protected endpoints are accessible after authentication*
GET /accounts
GET /accounts/{id}
GET /accounts/{id}/users

The application is currently live at http://13.55.56.16/

Requirements for building the application
---
- Java 8
- Maven (3.x)
- Docker (optional)

*Test are not implemented*
