# Welcome to HelloDB
**HelloDB** is a *NoSQL* database that uses ~~JSON~~ as document storage service which is stores data as plain-text in json format. Each data is written to the server as single ~~JSON~~ file allowing to easily make queries faster, and more efficient on response.

In this way, queries will be more faster, and safer to make query operation easy to retrieve documents without making too much I/O operations. **HelloDB** combined all single ~~JSON~~ file into one document file on queries.

**HelloDB** is also running on a secure environment in the cloud, so you don't need to worry about server cost, technical support, and maintenance.

# Introduction
Short back history of HelloDB project.
> HelloDB is started back on AUGUST 16, 2022 by @rexluciano as local data storage for storing data as JSON documents. Before then, it was called as **~~NodeDB~~** and it was available on GitHub as java library.
# Getting Started
There's some queries available that you can use.
* `insert` - create or add new data to the documents.

* `update` - modifying or updating data from the documents.

* `delete` - removing or destroying data in the documents.

* `get` - retrieving or fetching data from documents.

**Query functions are** use to filtering data and taking query document action as needed.

* `id` - default unique id for each document. It use for finding or updating data that uses *id*.
* `limit` - use to filtered, and limited query to specific number of rows in the database.
* `orderBy` - it use to filter and order documents according to their values.
* `where` - find data along with use of `equalTo` to find specific documents in the database.
* `desc` - it use when `orderBy` is use then set to true if request query is descending or ascending to false.
### Security Access
HelloDB is uses basic authentication to work as required by the security rules to protect client documents from attacks. Also, end-to-end encryption is being use when transferring data between devices and servers over the internet.

`username` - required string encoded in base64 use to access HelloDB resources.

`password` - required string encoded in base64 use to access HelloDB resources.

### Applications
To start working with HelloDB, you must need to have register your app through the dashboard allowing you to use and access apis, such as REST API and client api library.

`serverUrl` - use to access HelloDB API and backend resources. This could be a default domain like `https://api.hellodb.ga` where all resources are hosted.

`projectId` - required when accessing HelloDB Project which is use to define your resources on the client side.

`apiKey` - use in REST API to access external api to work without authenticating outside application.

## Client Libraries
HelloDB is currently available for Android as client api library. To be able to use it on other platforms, you need to use external library such as `curl` for REST API.

# Implementation in Android
Add the JitPack repository in your root `build.gradle` file.
```Gradle
allprojects {
  maven { url "https://jitpack.io" }
}
```
Add the required dependencies.
```Gradle
dependencies {
  implementation "com.github.rexluciano:hellodb:v1.0.0"
  implementation "com.github.rexluciano:worker:v1.0.0"
}
```
### **Basic use:**
```Java
public class YourApplicationName extends Application {

    @Override
    public void onCreate() {
    super.onCreate();
    //Required before using HelloDB
    HelloDB.initialize(
        this,
        new HelloDB.Config()
            .serverUrl("https://api.hellodb.ga")
            .projectId("abcdefg")
            .username("abc")
            .password("abc")
            .build());
    //Or use 
    HelloDB.initialize(this);
    //And create required string resources in your string.xml file.
    }

}
```
**Create this string resources:**

`hellodb_url`, `hellodb_project_id`, `hellodb_username`, `hellodb_password`
```XML
<string name="hellodb_url">https://api.hellodb.ga</string>
<string name="hellodb_project_id">abcdefg</string>
<string name="hellodb_username">abc</string>
<string name="hellodb_password">abc</string>
```

`username` is a 16 characters length.

`password` is a 16 characters length.

`project_id` is 6 characters length or higher.

`server_url` is ~~https\://api.hellodb.ga~~

To protect your backend from unauthorized access, add your SHA-1 signature to your application settings in the dashboard.

## Configure Rules
Also, HelloDB available with database access rules. To set it up, just create rules like this:
```JSON
{
  "rules" : {
    "path": "dogs",
    "allow_read": true,
    "allow_write": true
  },
  "version": 1
}
```
Then, go to https\://app.hellodb.ga/project/{projectId}/{tableName}/rules and upload your rules here.

## Insert query method
```Java
Query dogs = HelloDB.getInstance().getReference("dogs");

try {
  JSONObject ob = new JSONObject();
  ob.put("dog_name", "George");
  ob.put("dog_type", "bulldog");
  ob.put("reg_id", "#45-AAC");
  dogs.insert(ob).addOnCompleteTaskListener(listener);
} catch (JSONException je) {
   Log.e(TAG, je.getMessage());
  //Failed to insert query.
}
```
## Retrieve documents
```Java
Query dogs = HelloDB.getInstance().getReference("dogs");

dogs.orderBy("dog_type").limit(100).get().addQueryEventListener(listener);
```
Then, documents will be fetch in raw JSON array, such as:
```JSON
[
  {
    "id": 1,
    "dog_name": "George",
    "dog_type": "bulldog",
    "reg_id": "#45-AAC"
  }
]
```
### Dashboard
The dashboard where all of your project are hosted. Currently, dashboard isn't yet available but we're working on it to provide your needs.

### Release Notes
| SDK Version     | Platform | Library         |
| --------------- | -------- | --------------- |
| 1.0.0 or higher | Android  | Android Library |
| 1.0.0 or higher | Desktop  | Not Available   |

|   |
| - |
|   |


## License
```
Copyright 2022 REX LUCIANO

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```