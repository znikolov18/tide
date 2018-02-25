# tide

Restful API of announcement voting system.

# Deployment

The API is currently deployed on Apache Tomcat web container, running on AWS instance. 
Sample URL to the deployment: http://18.197.65.207:8080/TideInterview/{endpoint}

# Endpoints
      
**Get Announcement**
----
  Returns json representation of announcement.

* **URL**
  /announcements/:announcementId

* **Method:**
  `GET`
  
*  **URL Params**
   **Required:** 
   `announcementId=[integer]`

* **Data Params**
  N/A

* **Success Response:**
  * **Code:** 200 <br />
    **Content:** `{"id": 1, "subject": "TestSubject1", "summary": "TestSummary1"}`
 
* **Error Response:**
  * **Code:** 404 NOT FOUND <br />
    **Sample Content:** `{"errors": ["announcement with id 100 could not be found"]}`
  
**Create Announcement**
----
  Adds a new announcement.

* **URL**
  /announcements

* **Method:**
  `POST`
  
*  **URL Params**
   **Required:**
   N/A

* **Data Params**
  **Required:**
  `subject=[string, min=2 max=100]`
  `summary=[string, min=2 max=500]`

* **Success Response:**
  * **Code:** 201 CREATED <br />
    **Sample Content:** N/A
 
* **Error Response:**
  * **Code:** 400 BAD REQUEST <br />
    **Sample Content:** {"errors": ["subject: must not be null"]}

**Get Announcement Vote Result**
----
  Returns json representation of announcement's vote result.

* **URL**
  /announcements/:announcementId/vote-result

* **Method:**
  `GET`
  
*  **URL Params**
   **Required:** 
   `announcementId=[integer]`

* **Data Params**
  N/A

* **Success Response:**
  * **Code:** 200 <br />
    **Content:** `{
        "announcementId": 1,
        "likes": 2,
        "dislikes": 1
    }`
 
* **Error Response:**
  * **Code:** 404 NOT FOUND <br />
    **Sample Content:** `{"errors": ["announcement with id 100 could not be found"]}`

**Get Vote**
----
  Returns json representation about a vote.

* **URL**
  /votes/:voteId

* **Method:**
  `GET`
  
*  **URL Params**
   **Required:**
   `voteId=[integer]`

* **Data Params**
  N/A

* **Success Response:**
  * **Code:** 200 <br />
    **Content:** `{
        "id": 8,
        "announcementId": 1,
        "announcementSubject": "TestSubject1",
        "userId": 2,
        "userName": "John",
        "positive": true
    }`
 
* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Sample Content:** {"errors": ["vote with id 183 could not be found"]}
    
 **Create Vote**
----
  Adds a new vote.

* **URL**
  /votes

* **Method:**
  `POST`
  
*  **URL Params**
   **Required:**
   N/A

* **Data Params**
  **Required:**
  `announcementId=[integer]`
  `userId=[integer]`
  `positive=[boolean]`

* **Success Response:**
  * **Code:** 201 CREATED <br />
    **Sample Content:** N/A
 
* **Error Response:**
  * **Code:** 400 BAD REQUEST <br />
    **Sample Content:** `{"errors": ["announcementId: must not be null"]}`
  * **Code:** 404 NOT FOUND <br />
    **Sample Content:** `{"errors": ["announcement with id 1000 could not be found"]}`
  * **Code:** 409 CONFLICT <bg />
    **Sample Content:** `{"errors": ["vote for announcement with id 1 by user with id 1 already exists"]}`
 
**Get Votes by Announcement**
----
  Returns json representation about an announcement's votes.

* **URL**

  /votes?announcementId=:announcementId

* **Method:**
  `GET`
  
*  **URL Params**
   **Required:**
   `announcementId=[integer]`

* **Data Params**
  N/A

* **Success Response:**
  * **Code:** 200 <br />
    **Content:** `{
    "votes": [
        {
            "id": 8,
            "announcementId": 1,
            "announcementSubject": "TestSubject1",
            "userId": 2,
            "userName": "John",
            "positive": true
        },
        {
            "id": 6,
            "announcementId": 1,
            "announcementSubject": "TestSubject1",
            "userId": 1,
            "userName": "Adam",
            "positive": true
        }
    ]}`
 
* **Error Response:**
  * **Code:** 404 NOT FOUND <br />
    **Sample Content:** {"errors": ["announcement with id 18 could not be found"]}
    


**Get User**
----
  Returns json representation of user.

* **URL**
  /users/:userId

* **Method:**
  `GET`
  
*  **URL Params**
   **Required:** 
   `userId=[integer]`

* **Data Params**
  N/A

* **Success Response:**
  * **Code:** 200 <br />
    **Content:** `{"id": 1, "name": "TestUser"}`
 
* **Error Response:**
  * **Code:** 404 NOT FOUND <br />
    **Sample Content:** `{"errors": ["user with id 100 could not be found"]}`
  
**Create User**
----
  Adds a new user.

* **URL**
  /users

* **Method:**
  `POST`
  
*  **URL Params**
   **Required:**
   N/A

* **Data Params**
  **Required:**
  `name=[string, min=2 max=45]`

* **Success Response:**
  * **Code:** 201 CREATED <br />
    **Sample Content:** N/A
 
* **Error Response:**
  * **Code:** 400 BAD REQUEST <br />
    **Sample Content:** {"errors": ["name: must not be null"]}