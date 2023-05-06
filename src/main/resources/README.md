My Notes
 # gradle task -> 'Run Plugin' compiles jars to 
    /home/patrickjaja/development/intellij_spryker_plugin/build/idea-sandbox/plugins/intellij_spryker_plugin/lib/
 # HotSwap classes without recompile Run -> Debugging Actions -> Reload Changed Class
 # Add custom dependencys to build.gradle.kts i.e. `implementation("com.googlecode.json-simple:json-simple:1.1.1")`
 # Refresh Gradle dependencys after change in `build.gradle.kts` click on Gradle -> right click -> Refresh Gradle Dependencys (intellij updated automatically)

# example chatgpt prompt for extending vendor classes
# very early stage
Provide me with example code for deriving the class <%ClassName%> in my project context. To do that we’ll need to have the module in the project and to derive the <%ModuleName%>BusinessFactory and overwrite the create<%ClassName%> method and then the derived class itself in the likewise path as the original class only difference is that the project namespace is used instead of the Spryker namespace. E.g. original namespace: \Spryker\Zed\Calculation\Business\Model\Calculator will result in the new classes namespace: \<%ProjectName%>\Zed\Calculation\Business\Model\Calculator.
Namespace for the class that is to be extended: \<%ProjectName%>\<%ApplicationName%>\<%ModuleName%>/<%InnerPath%>/<%ClassName%>

Provide the Response in the following form:
Write the classes (the factory and the class that is to be extended) one after the other starting with the class name and then providing the sources for the respective class. Each class name is enclosed in text markers „//start-class-name:“ before the class-name and „//end-class-name“ after the class name.
The source code is enclosed in it’s own text markers: Before the source code the text „//start-source-code:“ marks the beginning of the source code and „//end-source-code“ ends the source code. Important: All markers need to be present to make the result possible to parse.


Example: //start-class-name:<%ApplicationName%>\<%ModuleName%>\<%InnerPath%>\<%ModuleName%>BusinessFactory.php//end-class-name
//start-source-code:Provide source code for the factory here//end-source-code

//start-class-name:<%ProjectName%><%ApplicationName%>/<%ModuleName%>/<%InnerPath%>/<ClassName>//end-class-name
//start-source-code:Provide Source Code for derived class here//end-source-code

# example for context to be used for prompt 
Spryker.Any.Any.Any.Any.Any

# example Prompt type which is used for the UI to represent the action
Extend vendor class using ChatGpt