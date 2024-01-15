<h1>Practical Work #2. Application using Spring Framework </h1>
<h2>Task 1</h2>
We use the same models and the same script as before.

<h2>Task 2</h2>
The data layer is the same as it was before. But we have additional configuration files and modify already existing ones.

We create mvc-config.xml configuration file that 
- enables annotation-driven mode;
- specifies a base package to look for components;
- specifies rules for view files;
- defines EntityManagerFactory bean that heavily relies on persistence.xml we created before;
- defines TransactionManager.

As for persistence.xml, the only modification was related to transactions:

```
<properties>
    <property name="hibernate.transaction.jta.platform" value="org.hibernate.service.jta.platform.internal.SunOneJtaPlatform"/>
</properties>
```

We also rewrote web.xml:
- included config location;
- added ContextLoaderListener;
- configured dispatcher.

<h2>Task 3</h2>
Our business layer is pretty much as before, but uses @Service and @Transactional annotations for each Service class.

<h2>Task 4</h2>

As for web layer, it has the same logic as Servlets before, but using controllers allowed to contain logic regarding Lexicons, Phrases and Semanthics within its controllers. JSP files are the same as before.

We use the following annotations:
- @Controller to mark a controller;
- @Autowired to provide a service instance:
```
@Autowired
private LexiconService lexiconService;

@Autowired
private SemanticsService semanticsService;

@Autowired
private PhraseService phrasesService;
```
- @GetMapping to specify an url to which the method refers;
- @RequestParam for each method parameter to specify which request parameter a method parameter corresponds to.

Return types are strings. We have two specific types of returns:
- the name of a JSP file which should be displayed;
- a redirect to another page:
```
return "redirect:/lexicon";
```

We also used a Model class object as a method parameter to specify attributes to be provided to JSP files:

```
model.addAttribute("lexiconList", lexiconList);
model.addAttribute("allSemantics", allSemantics);
model.addAttribute("allPhrases", allPhrases);
```

<h2>Task 5</h2>
<h2>Demonstration and Issues</h2>
The demonstration is the same as before. The only thing we would like to point out is that there's a problem with adding records containing Russian symbols. For example:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/2605c012-5588-48ac-83ef-3edbef9d8661)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/df792514-7dee-4d84-882d-f2c2dffe06b5)

But it wasn't the same with the previous application:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/8236b369-afff-4fc5-babf-8193a0d8429a)

Note that we can see Russian letters from the database correctly, but get a mess when trying to persist entries with them:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/c4a3d4df-f8f7-4d2b-913c-480b35ba2dcd)

A lot of attemps were taken to resolve the issue, but with no success so far.
