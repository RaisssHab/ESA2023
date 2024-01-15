<h1>Practical Work #3. RESTful web-service </h1>
<h2>Task 1</h2>
Based on <a href="https://stackoverflow.com/questions/20040511/jersey-vs-spring-for-rest-webservices">this</a>, we have the following:

Jersey
Pros: Standard API (JAX-RS)
Cons: MVC support is less richer than Spring MVC.

Spring:
Pros: Many features, many documents, active community.
Cons: Many rules to learn.

Due to existing experience with Spring REST and its active community, our choice is Spring REST.

<h2>Task 2</h2>

We choose the application based on Spring (practice work 2).

We're going to have the following REST API. All input data can be both in xml/json format. All output data can be presented in xml/json/html formats. 

<h3>Lexicon</h3>

All requests are sent to "/lexicon".

GET:

input: no
output: a list of lexicons with each in the following format:

```
{
    "lexiconId": 8,
    "semantics": {
        "semanticId": 1,
        "translation": "translation1",
        "explanation": "explanation1"
    },
    "phrase": {
        "phraseId": 1,
        "phrase": "phrase1"
    },
    "text": "test of json input"
}
```

POST:

input: json/xml in the following format:

```
{
    "text": "test of json input",
    "semanticId": "1",
    "phraseId": "1"
}
```

output: added lexicon in json/xml/html.

DELETE:

input: parameter "id"

output: removed lexicon in json/xml/html

PUT:

input: with the following fields:

```
{
    "lexiconId": 1,
    "text": "test of json input",
    "semanticId": "1",
    "phraseId": "1"
}
```

output: removed lexicon in json/xml/html

<h3>Phrases</h3>

All requests are sent to "/phrases".

GET:

input: no
output: a list of phrases with each in the following format (json/xml/html):

```
{
    "phrase": [
        {
            "phraseId": 3,
            "phrase": "first phrase"
        }
    ]
}
```

POST:

input: json/xml in the following format:

```
{
    "phrase": "first phrase"
}
```

output: added phrase in json/xml/html.

DELETE:

input: parameter "id"

output: removed phrase in json/xml/html

PUT:

input: with the following fields:

```
{
    "phraseId": 3,
    "phrase": "first phrase"
}
```

output: removed phrase in json/xml/html

<h3>Semantics</h3>

All requests are sent to "/semantics".

GET:

input: no
output: a list of semantics with each in the following format:

```
"semantics": {
    "semanticId": 1,
    "translation": "translation1",
    "explanation": "explanation1"
}
```

POST:

input: json/xml in the following format:

```
{
    "translation": "translation1",
    "explanation": "explanation1"
}
```

output: added semantics in json/xml/html.

DELETE:

input: parameter "id"

output: removed semantics in json/xml/html

PUT:

input: with the following fields:

```
{
    "semanticId": 1,
    "translation": "translation1",
    "explanation": "explanation1"
}
```

output: removed semantics in json/xml/html


<h2>Task 3-6</h2>
Our business layer is pretty much as before with modifications to controller and entity classes and new helper classes.

For each controller method, the 'Accept' header is used to return output based on what output format is expected. Yet we prioritize text/html output for displaying in browsers.

```
@RequestHeader("Accept") String acceptHeader
```

To prepare response, a static method from implemented helper class is used:

```
return Helper.response(lexiconList, acceptHeader, "/lexicon.xslt", LexiconList.class);
```

It checks the 'Accept' header, and if it has 'text/html' format, then XSL transform is performed to get an html page. Otherwise the initial object is returned, which then converts either to json or xml automatically.

We had to implement the helper methods in order to work with XSL transformation. Though there is an built-in opportunity for doing transformation, it seemed to the author that it is very difficult to combine this transformation with other output formats, so own implementation was provided.

It consists of two parts.

First transform an object to XML. Jaxb2Marshaller is used on this purpose:

```
Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
marshaller.setClassesToBeBound(classesToBeBound);

StringWriter sw = new StringWriter();
marshaller.marshal(object, new StreamResult(sw));

String xmlString = sw.toString();
```

Then transformation to XSLT is applied:

```
// Transform XML to HTML using XSLT
StringWriter result = new StringWriter();

TransformerFactory transformerFactory = TransformerFactory.newInstance();

ResourceLoader resourceLoader = new DefaultResourceLoader();
// Obtain the Resource for the XSLT file
Resource resource = resourceLoader.getResource("classpath:" + xslString);

StreamSource xslt = new StreamSource(resource.getInputStream());
xslt.setSystemId(resource.getURI().toString());
Transformer transformer = transformerFactory.newTransformer(xslt);

StreamSource source = new StreamSource(new StringReader(xmlString));
transformer.transform(source, new StreamResult(result));

// Return the transformed HTML
return result.toString();
```

Note that we had to use ResourceLoader that can take input with "classpath:" prefix, which allows us to specify the location of an XSLT file in webapp. Then, we also set system ID to URI of the XLST source in order to make possible local references to files inside XSLT (to menu.xslt exactly):

```
<!-- Include menu.xslt -->
<xsl:include href="menu.xslt"/>
```

Additional helper classes are LexiconList, PhraseList, SemanticsList which are wrappers over List of Lexicon, Phrase, and Semantics objects. It is used for correct use of Jaxb2Marshaller when transforming objects to XML.

For each entity and helper classes (*List) we also apply the following annotations to provide transormations into XML and JSON:

```
@XmlRootElement(name = "semantics")

@XmlAccessorType(XmlAccessType.FIELD)

@JsonbProperty("semanticId")
```

<h2>Demonstration</h2>

We use Postman to make queries to the web server. On the example on Phrases, we'll show all operations and their outputs. We will differentiate accept and content type to make sure that implementation is correct.

Set json input, json output.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/76131eab-19a3-4554-8520-6d4aba2c482f)

Create a new phrase.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/cb3b1f80-4a4e-405a-9541-1e9c5ff11add)

Set xml output.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/91eab695-5967-4082-a72b-9e0e3fbe83a5)

Create a new phrase.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/47f70566-dddd-494e-98ee-a1d9cbf485e7)

Set html output.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/a02139ec-931a-4dea-b9be-c8dea57cecf1)

Create a new phrase.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/6597c73e-c253-482e-906a-dabffe4e47c6)

Switch to xml input. Create a new phrase.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/ba4d4d9f-2f43-494a-bc73-e0e252e7d183)

Show all phrases.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/4c4b2fd2-70f7-4404-9ee5-43f76b2f85ab)

Delete a phrase.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/0779bf6d-c7df-4a06-a957-aa7a642871da)

Update a phrase.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/8f5ac172-e332-47f0-a610-d92dbc957d5c)

Add a semantics.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/8b1c16bf-aa0c-4b6f-9cd5-6e6b7042a9a5)

Add a lexicon.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/7d2ea88b-0be8-4526-ac68-d26bca6896a2)

Lexicon, phrases and semantics pages on a web browser.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/3c92f535-eac5-48f2-b2b9-1b420fc3c3bd)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/eb5f38a2-98eb-435a-b7bb-bb0c72c23f09)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/62299c4c-4595-4776-97a4-caac1941e579)
