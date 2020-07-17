package hsousa.ncml.io.read;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Proxy;

import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;
import ucar.unidata.io.RandomAccessFile;

public class NetcdfReader<T> {

    private final Class<T> rootType;

    public NetcdfReader(Class<T> rootType) {
        this.rootType = rootType;
    }
    
    @SuppressWarnings("unchecked")
    public T create() {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { rootType },
                new GroupHandler(null, false));
    }

    @SuppressWarnings("unchecked")
    public T read(File file, boolean readOnly) throws IOException {
        NetcdfFile netcdf;
        if (readOnly) {
            netcdf = NetcdfFiles.open(file.getAbsolutePath());
        } else {
            RandomAccessFile raf = new RandomAccessFile(file.getAbsolutePath(), "rw");
            netcdf = NetcdfFiles.open(raf, file.getAbsolutePath(), null, null);
        }
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { rootType },
                new GroupHandler(netcdf.getRootGroup(), readOnly));
    }

}
