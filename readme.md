# nevada.api
Social service API<br>
♦️Some features:♦️
- Likes 👍 and dislikes 👎
- Friends 👥
- Access modifiers🔓 (e.g. 'For all', 'Friends only', 'Only me')
- Comments ✍️
# Security model
Instead of passing passcode/username pair, token sended in Authorization header of request. It is much like oAuth tokens
(but it is not!).
<br>
Typical authorization flow:
<br>
User registers new profile(all applications that have token with scope `ACCOUNT READ/WRITE` allowed to register new users) ->
user is asked which information she or he gives to this application -> token is issued on this application and user ->
user can perform requests on endpoints (at least, on those he or she is allowed).
<br>
There are several access scopes:
- `PHOTO`
- `MESSAGE`
- `FRIENDS `
- `ACCOUNT`
- `NOTIFICATION`(currently unused)
- `APPLICATION`
- `STREAM`
- `PERSON_INFO` 

<br>All of them has one of the following levels:
- `NONE`
- `READ`
- `READ/WRITE`
<br>
Less restrictive include stronger ones, for example `READ/WRITE` also includes `READ` and `NONE`.
In order to access some of endpoints you have to have token with one or more scopes and
appropriate for endpoind access level. For example to send a message you may want to have `MESSAGE READ/WRITE` token.

# SQL Schema
![schema](https://raw.githubusercontent.com/username/projectname/branch/path/to/img.png)

# Example request
Besides controller classes all endpoints list also enumerated in uri.txt.<br>
 <br>
```diff
- GET /profile/{id}
Returns profile by identifier.
+ Token:
May be accessed without token, if request was done by token holder additional fields
will be returned.
+ Accept header:
application/vnd.nevada.profile+json;version=%version%
OR application/vnd.nevada.profile+xml;version=%version%
+ Versions:
1.0
+ Example:
GET /profile/{id}
Accept: application/vnd.nevada.profile+json;version=1.0
Authorization:token
```
# License
😈😈😈 **It is free!!** 😈😈😈
