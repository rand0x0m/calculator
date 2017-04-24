# calculator

Embedable calculator library. Takes an math expression as String as input and returns the result.    

#### NOTE1 : It was build for fun. MUST NOT be used for commercial use!
#### NOTE2 : It is based on Math library of Java. And is accurate as this library is. Keep in mind that Java has max. 64-bit floating point (speaking for primitive types).  
#### This is a pre-release version.


## Build

In order to use this in your projects, after you cloned the repositorie, navigate to calculator/ directory and execute 
```shell
mvn install
```
to add it to your local Maven repositories. You can now use it in your Maven project by adding these lines
to its pom.xml file.

```xml
<dependencies>
	<dependency>
		<groupId>com.random1312</groupId>
		<artifactId>calculator</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
</dependencies>
```


## License

MIT License

Copyright (c) 2017 random1312

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
