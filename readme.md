Custom check modules for checkstyle

See: http://checkstyle.sourceforge.net/writingchecks.html

Modules: 
* **ru.pva.code.style.checks.EmptyLineBeforeReturnCheck**
	* checks for empty lines before `return` or `throw` if the number of lines in the code block is greater than three;
	* parent module: `TreeWalker`.

##Usage
* Required Checkstyle version 6.19 (JDK 7)
* Add the required module to your Checkstyle configuration (`xml`), for example,
`<module name="ru.pva.code.style.checks.EmptyLineBeforeReturnCheck"/>`;
* Add `codestyles` as dependency to your `maven-checkstyle-plugin` plugin, for example in Maven,
```
<plugins>
	<plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-checkstyle-plugin</artifactId>
		<version>2.7</version>
		<dependencies>
			<dependency>
				<groupId>ru.pva</groupId>
				<artifactId>codestyles</artifactId>
				<version>0.0.1-SNAPSHOT</version>
			</dependency>
		</dependencies>
	</plugin>
</plugins>
```
* Run `maven-checkstyle-plugin` (see: https://maven.apache.org/plugins/maven-checkstyle-plugin/)