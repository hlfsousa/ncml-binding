package hsousa.ncml.io.write;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import hsousa.ncml.annotation.CDLDimension;
import hsousa.ncml.annotation.CDLDimensions;
import hsousa.ncml.annotation.CDLGroup;
import hsousa.ncml.annotation.CDLRoot;
import hsousa.ncml.annotation.CDLVariable;
import hsousa.ncml.declaration.Variable;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.IndexIterator;
import ucar.nc2.Dimension;
import ucar.nc2.Group;
import ucar.nc2.NetcdfFile;

public class NetcdfWriterTest {
    
    private static final Random RNG = new Random();

    @CDLRoot
    @CDLDimensions({
            @CDLDimension(name = "globalDimension", unlimited = true)
    })
    public static interface IRootGroup {

        @CDLGroup(name = "childGroup")
        IChildGroup getChildGroup();

        void setChildGroup(ChildGroup childGroup);

        @CDLVariable(name = "rootVariable", type = Float.class, shape = "globalDimension")
        IArrayVariable getRootVariable();

        void setRootVariable(IArrayVariable rootVariable);

    }
    
    public static class RootGroup implements IRootGroup {
        
        private IChildGroup childGroup;
        private IArrayVariable rootVariable;
        
        @Override
        public IChildGroup getChildGroup() {
            return childGroup;
        }
        
        @Override
        public void setChildGroup(ChildGroup childGroup) {
            this.childGroup = childGroup;
        }
        
        @Override
        public IArrayVariable getRootVariable() {
            return rootVariable;
        }
        
        @Override
        public void setRootVariable(IArrayVariable rootVariable) {
            this.rootVariable = rootVariable;
        }
        
    }
    
    @CDLDimensions({
        @CDLDimension(name = "localDimension", length = 30)
    })
    public static interface IChildGroup {
        
        @CDLVariable(name = "childVariable", shape = { "globalDimension", "localDimension"}, type = Integer.class)
        IArrayVariable getChildVariable();
        
        void setChildVariable(IArrayVariable childVariable);
    }
    
    public static class ChildGroup implements IChildGroup {
        
        private IArrayVariable childVariable;
        
        public IArrayVariable getChildVariable() {
            return childVariable;
        }
        
        public void setChildVariable(IArrayVariable childVariable) {
            this.childVariable = childVariable;
        }
        
    }
    
    public static interface IArrayVariable extends Variable<Array> {
        
    }
    
    public static class ArrayVariable implements IArrayVariable {
        
        private Array value;
        private List<Dimension> dimensions;
        
        public Array getValue() {
            return value;
        }
        
        public void setValue(Array value) {
            this.value = value;
        }
        
        public List<Dimension> getDimensions() {
            return dimensions;
        }
        
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }
        
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testCollectDimensions() throws Exception {
        Dimension globalDimension = new Dimension("globalDimension", 20, true, true, false);
        RootGroup root = new RootGroup();
        ChildGroup childGroup = new ChildGroup();
        root.setChildGroup(childGroup);
        ArrayVariable childVariable = new ArrayVariable();
        childGroup.setChildVariable(childVariable);
        Dimension localDimension = new Dimension("localDimension", 10);
        childVariable.setDimensions(Arrays.asList(globalDimension, localDimension));
        {
            Array array = Array.factory(DataType.INT, new int[] { 20, 10 });
            for (IndexIterator iterator = array.getIndexIterator(); iterator.hasNext(); ) {
                iterator.setIntNext(RNG.nextInt(1000));
            }
            childVariable.setValue(array);
        }
        
        ArrayVariable rootVariable = new ArrayVariable();
        {
            Array array = Array.factory(DataType.FLOAT, new int[] { 20 });
            for (IndexIterator iterator = array.getIndexIterator(); iterator.hasNext(); ) {
                iterator.setFloatNext((float)Math.abs(RNG.nextDouble() * 1000));
            }
            rootVariable.setDimensions(Arrays.asList(globalDimension));
            rootVariable.setValue(array);
        }
        
        root.setRootVariable(rootVariable);
        
        // write file
        NetcdfWriter writer = new NetcdfWriter();
        File outFile = new File("target/dimensionTest.nc");
        NetcdfFile netcdf = writer.write(root, outFile);
        Dimension actualGlobalDim = netcdf.getRootGroup().findDimension(globalDimension.getShortName());
        assertThat(actualGlobalDim.getLength(), is(globalDimension.getLength()));

        Group actualChildGroup = netcdf.getRootGroup().findGroup("childGroup");
        Dimension actualLocalDim = actualChildGroup.findDimension(localDimension.getShortName());
        assertThat(actualLocalDim.getLength(), is(localDimension.getLength()));
        
        netcdf.close();
    }

    

}
