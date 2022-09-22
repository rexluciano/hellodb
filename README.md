# Welcome to HelloDB
HelloDB is a simple JSON-based database that runs NoSQL like database in the cloud allowing you to insert, update, read, and delete documents right from your app without any server-side code.

### Scalable NoSQL Database for Android
You can use HelloDB for such operations:
* Low to medium query operation
* Basic query operation like `db.orderBy("name").get().addQueryEventListener(queryEventListener);`
* CRUD operation using HelloDB client's library.

HelloDB is currently only available for Java and REST API.
## Features
* Lightweight & Fast

Stores data in plain-text utilizing JSON format, no binary conversion needed to store or fetch the data. Default query cache layer.

* Schema free data storage

HelloDB does not require any schema, so you can insert any types of data you want.

* Query on nested properties

As it supports schema free data, so you can filter and use conditions on nested properties of the JSON documents.

* Serverless

Store, create, read, and delete documents without any server-side code using easy integrated query features such as:

**Insert** - create or add new data to the documents;
**Update** - modified current data from the documents without replacing all data;
**Read** - get current data from the documents, such reading `users`.
**Delete** - remove current data from the documents.
## Getting Started
**Initializing the HelloDB Instance.**
You must put this code inside of your Application class before any.
`HelloDB.initialize(`
`this,`
`new HelloDB.Config()`
`.serverUrl("https://api.hellodb.ga") //Default server`
`.projectId("Abcdef") //Project id`
`.username("xyzdfg") //Required username`
`.password("xyyzzzyz") //Required password);`

**Inserting new data to the documents.**

`private HelloDB db = HelloDB.getInstance();`
`private Query users = db.getReference("users");`

`//You need to use JSONObject`
`JSONObject data = new JSONObject();`
`data.put("name", "John Doe");`
`...`
`db.insert(data);`
`//or`
`db.document("custom_id").insert(data);`

And the response output will be:

`{`
`"id": "custom_id"`
`"name": "John Doe",`
`...`
`}`

**Updating current data from documents.**

`JSONObject data = new JSONObject();`
`data.put("name", "John Doe");`
`...`
`db.document("custom_id").update(data);`

**Delete custom documents or value.**

`db.document("custom_id").delete();`
`//or`
`db.document("custom_id").delete("value");`

**Getting documents with limited queries.**

`db.orderBy("name").limit(30).get();`

> HelloDB is currently under development, and some features may change in future updates.