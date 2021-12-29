# NcML-Java binding

NcML is the XML semantics of the Common Data Language, which is used as the implementation for reading and writing NetCDF files. This library provides the means to quickly transform a NetCDF file into a set of classes with the same semantics and then use these objects to read and write NetCDF files with that semantics.

NetCDF is maintained by Unidata: https://www.unidata.ucar.edu/software/netcdf/

## Compatibility

This release uses an older NetCDF version, compatible with NetCDF-C 4.6.3 to 4.7.4.

## Processing a NetCDF file

As a developer, you need not know absolutely anything about NetCDF files in order to read one. You don't need to know SQL or how the RDBMS stores data to use JPA, so why would have to learn NetCDF? But just as you need a database, you will need to install the [NetCDF tools](https://www.unidata.ucar.edu/downloads/netcdf/). Once installed, you will use the header file to generate your classes. To generate a header XML from some file (if the header was not provided), use the `ncdump` command:

```shell
$ ncdump -h -x my_file > my_file_header.xml
```

Now use that to generate all the code needed:

```java
URL schemaURL = new File("/path/to/my_file_header.xml").toURI().toURL(); // or however you wish
NCMLCodeGenerator generator = new NCMLCodeGenerator(schemaURL);
generator.setModelPackage("com.example");
generator.setRootGroupName("MyFile"); // the root group is anonymous, so give it a name
generator.generateSources(new File("src/main/java");
```

The default code generation will provide you with some pieces of data:

1. An annotated interface which corresponds to the header file. Any object that implements this interface can represent a file. If you choose so, a file can be read through a proxy that implements this interface, so that only the interface is required for interacting with NetCDF files.
2. A NetCDF wrapper object that wraps the `netcdf-java` library objects directly and implements the interface above. This allows for customization of the implementation, such as additional properties or methods.
3. A (plain Java) value object. You can use this object to create NetCDF content from scratch.
4. A runtime configuration properties file. You can use this properties file to change how the NetCDF files are read and written.

Additional templates can be provided in order to generate more classes or any other type of file from the NcML header. Then you can read your file with two simple lines:

```java
RuntimeConfiguration emptyConfig = new RuntimeConfiguration(Collections.emptyMap());
NetcdfReader<MyFile> reader = new NetcdfReader<>(MyFile.class, emptyConfig);
MyFile myFile = reader.read(new File("/path/to/my_file.nc"), true); // read-only
// done! Now you can read any property, e.g.:
String someAttribute = myFile.getSomeAttribute();
SomeVariable<double[]> someVariable = myFile.getSomeVariable();
double[] value = someVariable.getValue(); // variable attributes are also available
```

Further information is provided in the wiki and example projects. Issues can be submitted through the GitHub project at https://github.com/hlfsousa/ncml-binding. Thank you!
