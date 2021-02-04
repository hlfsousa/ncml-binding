package io.github.hlfsousa.ncml.io;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import ucar.nc2.CDMNode;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RuntimeConfigurationTest {

    private RuntimeConfiguration runtimeConfiguration;
    @Mock
    private CDMNode rootNode;
    @Mock
    private CDMNode g01;
    @Mock
    private CDMNode x01;
    
    @BeforeEach
    public void initializeConfiguration() {
        Map<String, String> runtimeProperties = new HashMap<>();
        runtimeProperties.put("simpleSub", "substituted"); // simple substitution
        runtimeProperties.put("regex.g_(\\d+)", "mapped_$1"); // mapped property
        runtimeProperties.put("regex.x_\\d+/mappedChild", "child"); // child of mapped property
        
        runtimeConfiguration = new RuntimeConfiguration(runtimeProperties);
        
        when(rootNode.getFullName()).thenReturn("");
        when(g01.getFullName()).thenReturn("g_01");
        when(x01.getFullName()).thenReturn("x_01");
    }
    
    @Test
    public void testSimpleSubstitution() throws Exception {
        assertThat(runtimeConfiguration.getRuntimeName(rootNode, "simpleSub"), is("substituted"));
    }

    @Test
    public void testMappedPropertySubstitution() throws Exception {
        assertThat(runtimeConfiguration.getRuntimeName(rootNode, "g_01"), is("mapped_01"));
    }

    @Test
    public void testMappedChildSubstitution() throws Exception {
        
        assertThat(runtimeConfiguration.getRuntimeName(x01, "mappedChild"), is("child"));
    }
    
}
