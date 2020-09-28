assertNotNull(netcdf, "no input provided");
assertTrue(netcdf.history == "2004-09-15 17:04:29 GMT by mars2netcdf-0.92", "/@history");
assertTrue(netcdf.conventions == "CF-1.0", "/@Conventions");
assertNotNull(netcdf.longitude, "/longitude");
assertTrue(netcdf.longitude.longName == "longitude", "/longitude/@long_name");
assertTrue(netcdf.longitude.units == "degrees_east", "/longitude/@units");
assertTrue(netcdf.longitude.value != null, "/longitude (value)");
assertEquals(netcdf.longitude.dimensions.size(), 1, "/longitude (shape)");
assertEquals(netcdf.longitude.dimensions[0].length, 144, "/longitude (shape[0] length)");
assertNotNull(netcdf.time, "/time");
//assertTrue(arrayEquals(netcdf.getTime().getValue().getShape(), [ 62 ]), "/time (shape)");
assertTrue(arrayEquals(netcdf.time.value,
   [898476, 898482, 898500, 898506, 898524, 898530, 898548, 898554, 
    898572, 898578, 898596, 898602, 898620, 898626, 898644, 898650, 898668, 
    898674, 898692, 898698, 898716, 898722, 898740, 898746, 898764, 898770, 
    898788, 898794, 898812, 898818, 898836, 898842, 898860, 898866, 898884, 
    898890, 898908, 898914, 898932, 898938, 898956, 898962, 898980, 898986, 
    899004, 899010, 899028, 899034, 899052, 899058, 899076, 899082, 899100, 
    899106, 899124, 899130, 899148, 899154, 899172, 899178, 899196, 899202]));
