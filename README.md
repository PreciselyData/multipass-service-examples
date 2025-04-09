# multipass-service-examples

This demo project is a how-to sample for integrating multipass flows with Rest services.
In this demo, we have used spring-boot to set up the web server.

This project also, demonstrates how to use GA-SDK features as steps of the flow.
In this project, we are going to use Addressing Geocode and Addressing Verify as steps of the Flow.

## Defined Custom Operators

In this project two custom operators are defined:

1. CustomGeocodeOperator (Geocode)
2. CustomVerifyOperator (Verify)

### 1. CustomGeocodeOperator

CustomGeocodeOperator is the custom implementation of SimpleOperator interface from Multipass Builder.
The name of this operator is "Geocode" i.e. in "type":"Geocode" in the JSON definition of this Operator.
This operator has a few additional properties other than what are predefined.

Predefined Properties:

1. id: unique ID of the operator
2. description: brief description of what is the use case of this operator
3. nextNode: The id of the next operator to be executed in the flow

Additional Properties:

1. inputKey: This is the key within Global JSON where address (to be geocoded) is stored.
2. outputKey: This is the key where result of geocode will be stored within the Global JSON.
3. preferences: This is the JSON data for preferences if Geocode needs to happen with custom preferences. This is optional

Sample definition of Geocode operator:

    {
        "id": "geocode_address",
        "description": "This step runs the custom operator with functionality to geocode",
        "nextNode": "next_node_id",
        "inputKey": "request",
        "outputKey": "geocode_response",
        "type": "Geocode"
    }

Explanation:

If the input is given as:

    {
        "addressLines": ["1 Global Trot"],
        "country": "USA"
    }

Then Global JSON in memory will be created as:

    {
        "request": {
            "addressLines": ["1 Global Trot"],
            "country": "USA"
        }
    }

In this case the value of inputKey should be set as "request" as the address to be geocoded will be stored under this key.

outputKey is set to 'geocode_response' hence, the response is stored under key `geocode_response`.

After this geocode step runs, the global JSON would look like:

    {
        "request": {
            "addressLines": ["1 Global Trot"],
            "country": "USA"
        },
        "geocode_response": {
            "status": "OK",
            ......
        }
    }

## Setup

Required:

1. Java 17 in local environment
2. Maven installation
3. Export the following variables:
```shell

export MULITPASS_PATH_FLOWS=<path-to-folder-where-flow-JSONs-are-saved> #if not set then `src/main/resources/flows` will be used to read flow JSONs by default
export MULTIPASS_PATH_FILES_JAVA=<path-to-folder-where-java-files-are-saved> #in case Java operator is used
export DATA_PATH=<path-to-geocode-verify-data> #if the user wants to use Geocode/Verify operator
export RESOURCE_PATH=<path-to-GA-SDK-resources> #if the user wants to use Geocode/Verify operator
```

## How to Run

### 1. Build the JAR

Set the property `${sdk.repository}` to the repository available in GA-SDK.
This should be pointing to `{path-to-ga-sdk-build}/ga-sdk-dist-{version}/sdk/repository`.


Variable `{path-to-ga-sdk-build}` refers to the location where the extracted build is stored.

Variable `{version}` refers to the version of the GA-SDK build available.

If the build is stored on path `/Users/user.name/path/sdk/` and version of the GA-SDK build is `11.1.1250` then the following command should be executed.

    mvn clean install -s settings.xml -Dsdk.repository=/Users/user.name/path/sdk/ga-sdk-dist-11.1.1250/sdk/repository

### 2. Execute the JAR

    java -jar target/multipass-rest-api-demo-1.0-SNAPSHOT.jar

Check the logs and see if it displays:

    Started MultipassApplication in x.xxx seconds (process running for x.xxx)

## Execute Flows

Once the MultipassApplication starts running, Hit the following request:

    http://localhost:8080/multipass/{flowId}

Body JSON would be an array of addresses:

    [
        {
            "addressLines": ["1 Global Trot"],
            "country": "USA"
        }
    ]

## Sample Flows Added

### 1. sample_flow.json

This flow is very simple in terms of processing and only does two things:

1. Trim the addressLines
2. Checks if the country exists, if not then adds US as default country.

### 2. enhanced_flow.json

This flow utilises the custom Geocode Operator. The workflow is:
1. Geocode the input address as is.
2. Check score.
3. If score not good the geocode by making the address as single line
4. Check single line score.
5. If the single line score not good then compare multiline vs single line score
6. Return the response with the higher score.

### 3. preferences_enhanced_flow.json

This flow is same as the enhanced_flow, but it demonstrates how preferences can be filled in the JSON definition of Geocode operator.
