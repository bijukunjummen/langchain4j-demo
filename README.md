# Langchain4j chat demonstration

This is a simple demonstration of a chat agent using langchain4j and Spring Boot.

The agent has information about the National Parks in the USA and provides informative 
answers about them based on the user questions.

This is a snippet of a conversation:
```text
data:{
    "from": "USER",
    "payload": "I stay in Oregon, any interesting places to visit around"
}

data:{
    "from": "SYSTEM",
    "payload": "Crater Lake National Park is a must-visit if you're in Oregon! It's known for its stunning blue lake and picturesque landscapes. It's definitely worth checking out!"
}

data:{
    "from": "USER",
    "payload": "Anything north of here?"
}

data:{
    "from": "SYSTEM",
    "payload": "There are several national parks north of Oregon that you might find interesting. One option is Olympic National Park in Washington. It offers diverse ecosystems including mountains, rainforests, and beaches. Another option is North Cascades National Park, also in Washington, which is known for its rugged mountains and pristine wilderness. Would you like more information about these parks or any other specific requests?"
}

data:{
    "from": "USER",
    "payload": "Anything east of here?"
}

data:{
    "from": "SYSTEM",
    "payload": "Yes, there are several national parks east of Oregon that you might find interesting. One option is Glacier National Park in Montana. It's known for its stunning alpine scenery, glaciers, and abundant wildlife. Another option is Yellowstone National Park, which spans across Wyoming, Montana, and Idaho. It's famous for its geothermal features such as geysers and hot springs, as well as its diverse wildlife and stunning landscapes. Would you like more information about these parks or any other specific requests?"
}
```

## Running the application

Start it the following way:
```sh
OPENAPI_APIKEY=<openapi_key> ./gradlew bootRun
```

## Testing the agent
Run the following command:

```sh
http http://localhost:8080/conversation/session
```

and copy the session id. In both windows set the SESSIONID env variable:

```sh
export SESSIONID=<copied sessionid>
```


Now, start streaming the conversation:

```sh
http http://localhost:8080/conversation/ Cookie:"JSESSIONID=$SESSIONID" 
```

In another window chat with the agent:

```shell
http http://localhost:8080/conversation/add Cookie:JSESSIONID=$SESSIONID  message=="I stay in Oregon, any interesting places to visit around"
```

The responses will show up in the streaming window.