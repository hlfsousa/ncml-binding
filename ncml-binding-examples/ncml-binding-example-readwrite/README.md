# Example Project: read, process and write file
This is an example with the main steps in the design of reading and writing NetCDF files. This file describes each step and where each step is taken, in terms of code and configuration.

## Dependencies
All dependencies are taken from group ID `io.github.hlfsousa`. In this project, we are going to use two artifacts: `ncml-io`, and `ncml-schemagen` (in addition to testing tools). The latter is needed as code dependency because we are going to do some pre-processing of the NcML header. Otherwise, it would be a plugin dependency only. Check these dependencies out at `pom.xml`.

At runtime, as you read and write NetCDF files, you are going to need the library installed. If you have not, check out [Getting and Building netCDF](https://www.unidata.ucar.edu/software/netcdf/docs/getting_and_building_netcdf.html).

## The header file
We need to start from the NcML. NetCDF files are self-describing, so there are three possibilities here:

1. You receive a sample NetCDF file. If that is the case, generate the NcML header: `ncdump -h -x sample_file.nc`
2. You receive a sample CDL file (NetCDF as text). This may be a full file or maybe just the header. Whatever the case, generate a NetCDF file: `ncgen -o sample_file.nc sample_file.cdl`; then generate the NcML header (see above).
3. You receive a sample NcML header. Great! This is exactly what we need.

We already have an NcML header file. We are going to use a file from OPeNDAP, [cami_0000-09-01_64x128_L26_c030918.nc](http://test.opendap.org/opendap/coverage/cami_0000-09-01_64x128_L26_c030918.nc.html). It has been copied to the parent project and the NcML header has been generated as above.

## Design improvements
The default header file describes the NetCDF semantics, but not necessarily how it is supposed to be mapped to object. For instance, variables `lev` and `ilev` are related. They are both hybrid levels, with mostly the same attributes. The model could have one type to map both variables. The same goes for other variables, such as `hyai`, `hybi`, `hyam`, and `hybm`.

We have a solution for that. `io.github.hlfsousa.ncml.schemagen.improvements.HeaderTransformer` drives the transformation of the header into one that can be mapped to something more compact and expressive with the same semantics. Such improvements could be done in a separate project not for delivery, but for brevity this example includes the transformation class in the test code: `io.github.hlfsousa.ncml.example.readwrite.HeaderTransformation`. This class contains a main method and it is meant to be executed on demand (from your IDE). Improving a header is an iterative process, check the Javadoc of the aforementioned class to check all decisions that were made in this example.

When the NcML header (and eventual associated properties file) describes the desired model, it's time to generate it.

## Generate the model
Model generation can also be an iterative process, should the code not look sharp. Generating code is as simple as running a Java class. See `pom.xml` for the task. The classes are generated at `src/main/java` because they can be customized with additional code.

## Read a file
Once the model is generated you are ready to read files. This is really simple: instantiate a reader with the desired target type, and provide a file for it:

```java
NetcdfReader<CommunityAtmosphericModel> reader =  new NetcdfReader<>(CommunityAtmosphericModel.class);
boolean readOnly = true;  // this flag determines the implementation to use
CommunityAtmosphericModel cami = reader.read(file, readOnly);
```

At this point, all data in the file is available through the interface.

## Write a new file
Only a new file can be written, even if using data being read from another file. Though `netcdf-java` supports editing a file, this feature is not available yet from the mapping. Data from a file can be modified in memory and a new file is written with the modified data.

A file is created from the interface that was generated from the NcML header. The interface can be populated from scratch, or by modifying data from an existing file. Writing a file, however the data was created, is as simple as reading:

```java
NetcdfWriter writer = new NetcdfWriter(true);
NetcdfFile netcdf = writer.write(cami, new File("/path/to/file.nc"));
```

### File data from scratch
It is often the case that some of the data in the file will always be the same. Attribute conventions (`scale_factor`, `add_offset`) and other variable attributes, global attributes and others may be obviously derived from the header that was received above. In the process of improving the data model though, some of that data may have been discarded. All of this is also taken care of.

You will notice that one of the generated classes is `CommunityAtmosphericModelInitializer`. Every group and variable gets one initializer class, which does the above. The code initially generated is able to assign default values. It can be modified and customized to assign more values or have different initializations (for mapped variables, for instance). And if
a value has already been assigned to the variable, its dimensions are initialized too. So a variable can be created as simply as this:

```java
// create the variable as value object
ReferencePressureVO refPressure = new ReferencePressureVO();
// set its value (the same goes for arrays)
refPressure.setValue(20d);
// initialize remaining attributes and dimensions
ReferencePressureInitializer.initialize(refPressure);
```

See `DataProcessor` for more examples, including mapped variables and custom initializers.

## Future-proofing the code
At some point in the future, after the code is generated, the intended file format may change without changing semantics. For instance, there may be a requirement to change variable name from `lat` to `latitude`. By default, when the code is generated you will get a file named "ncml-binding.properties" at the root of the project. This location (file path relative to project root) can be set through property `propertiesLocation` in `cami_archetype.properties`. This file contains mappings for the NetCDF elements and allows for some future-proofing. Should you want to change the name of the elements, this properties file can be used.

To use these properties (from whether the generated file or another source), load the file and use it when instantiating the required `io.github.hlfsousa.ncml.io.RuntimeConfiguration`. Make sure the file is available for future modification. When any change is needed, you can add an entry with the full name of the node as the key, and the new name as value. You can also use a regular expression. For details, check the project Wiki.

You can also use an empty `RuntimeConfiguration` and just use the names as provided in the header without the possibility of future adjustments.

## Freedom of choice
Additional details can be seen in the code. As part of the repository, this project can be built from source. The generated files are not included in the source code to better illustrate the process. Run the build, open the sources in your IDE, check the test class for how to use a writer and write your own customizations. Compare with the effort of writing a file directly and provide feedback at GitHub. There are new features coming up, and what users say will have a lot of weight in upcoming decisions.
