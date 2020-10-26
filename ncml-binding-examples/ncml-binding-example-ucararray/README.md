# Example Project: custom templates
This example shows how to customize templates to generate code to your liking. For other operational details, check the other examples.

## Location of templates
These templates are based on the default templates found in `io.github.hsousa:ncml-schemagen` in package/directory `templates`. In this example project the new templates are at `src/main/resources`, but they need only be in the classpath during generation.

## What is changed
The default generation deals with Java arrays. That may be a more natural way of processing data for Java programmers, but it comes at a cost: arrays are copied to memory in their entirety; if the array is large and only a small portion of it is required, this may be an overhead. This applies mostly when reading data directly from NetCDF files (using the `NetcdfWrapper` implementation). In this example, we modify the original templates to use as variable value a raw `ucar.ma2.Array` for non-scalar variables and attributes.

## Other changes
The standard code is deals with the classes generated for the default templates. Extention points are provided for customizations. In particular, it is worth mentioning class `io.github.hlfsousa.ncml.io.DataCopier`. If you wish to use this with custom templates and you happen to set a particular naming for the value object class (assuming one is generated), you need to either provide a locator for such classes, or extend the class and override method `locateValueObjectFor(...)`.
