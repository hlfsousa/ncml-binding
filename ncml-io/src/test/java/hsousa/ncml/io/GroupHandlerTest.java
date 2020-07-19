package hsousa.ncml.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import hsousa.ncml.io.read.GroupHandler;
import ucar.nc2.Attribute;
import ucar.nc2.Group;
import ucar.nc2.Variable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GroupHandlerTest {

    private static final String DESCRIPTION_ATTRIBUTE_VALUE = "description attribute value";
    @Mock
    private Group rootGroup;
    @Mock
    private Group childGroup;
    @Mock
    private Attribute description;
    @Mock
    private Variable someVariable;

    @BeforeEach
    public void setupNetcdf() throws Exception {
        when(rootGroup.findGroup("child_group")).thenReturn(childGroup);
        when(childGroup.getShortName()).thenReturn("child_group");
        when(rootGroup.findAttribute("description")).thenReturn(description);
        when(description.getStringValue()).thenReturn(DESCRIPTION_ATTRIBUTE_VALUE);
        when(childGroup.findVariable("some_variable")).thenReturn(someVariable);
    }

    @Test
    public void testGetGroup() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        TestFile testFile = (TestFile) Proxy.newProxyInstance(classLoader,
                new Class<?>[] { TestFile.class }, new GroupHandler(rootGroup, true));
        assertThat(testFile.getChildGroup(), is(notNullValue()));
        assertTrue(testFile.getChildGroup() == testFile.getChildGroup(), "Each call returns a different instance");
    }

    @Test
    public void testGetVariable() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ChildGroup child = (ChildGroup) Proxy.newProxyInstance(classLoader,
                new Class<?>[] { ChildGroup.class }, new GroupHandler(childGroup, true));
        assertThat(child.getSomeVariable(), is(notNullValue()));
        assertTrue(child.getSomeVariable() == child.getSomeVariable(), "Each call returns a different instance");
    }

    @Test
    public void testGetAttribute() {
        TestFile testFile = (TestFile) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] { TestFile.class },
                new GroupHandler(rootGroup, true));
        assertThat(testFile.getDescription(), is(DESCRIPTION_ATTRIBUTE_VALUE));
    }

}
