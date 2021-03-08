package io.github.hlfsousa.ncml.io.cami;

/*-
 * #%L
 * ncml-io
 * %%
 * Copyright (C) 2020 - 2021 Henrique L. F. de Sousa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import io.github.hlfsousa.ncml.io.ConvertUtils;
import io.github.hlfsousa.ncml.io.RuntimeConfiguration;
import io.github.hlfsousa.ncml.io.wrapper.NetcdfWrapper;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import ucar.ma2.*;
import ucar.nc2.Dimension;
import ucar.nc2.Group;
import ucar.nc2.Variable;


public class CommunityAtmosphericModelWrapper extends NetcdfWrapper implements CommunityAtmosphericModel {

    private class LatitudeWrapper implements LatitudeVariable<double[]> {

        private final Variable variable;
        private double[] value;
        
        public LatitudeWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

    }


    private class LongitudeWrapper implements LongitudeVariable<double[]> {

        private final Variable variable;
        private double[] value;
        
        public LongitudeWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

    }


    private class HybridLevelWrapper implements HybridLevelVariable<double[]> {

        private final Variable variable;
        private double[] value;
        
        public HybridLevelWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getPositive() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "positive")))
                    .map(positive -> positive.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setPositive(String positive) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getStandardName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "standard_name")))
                    .map(standardName -> standardName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setStandardName(String standardName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getFormulaTerms() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "formula_terms")))
                    .map(formulaTerms -> formulaTerms.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setFormulaTerms(String formulaTerms) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

    }


    private class TimeWrapper implements TimeVariable<double[]> {

        private final Variable variable;
        private double[] value;
        
        public TimeWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getCalendar() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "calendar")))
                    .map(calendar -> calendar.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setCalendar(String calendar) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

    }


    private class HybridACoefficientAtLayerInterfacesWrapper implements HybridACoefficientAtLayerInterfacesVariable<double[]> {

        private final Variable variable;
        private double[] value;
        
        public HybridACoefficientAtLayerInterfacesWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

    }


    private class HybridBCoefficientAtLayerInterfacesWrapper implements HybridBCoefficientAtLayerInterfacesVariable<double[]> {

        private final Variable variable;
        private double[] value;
        
        public HybridBCoefficientAtLayerInterfacesWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

    }


    private class HybridACoefficientAtLayerMidpointsWrapper implements HybridACoefficientAtLayerMidpointsVariable<double[]> {

        private final Variable variable;
        private double[] value;
        
        public HybridACoefficientAtLayerMidpointsWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

    }


    private class HybridBCoefficientAtLayerMidpointsWrapper implements HybridBCoefficientAtLayerMidpointsVariable<double[]> {

        private final Variable variable;
        private double[] value;
        
        public HybridBCoefficientAtLayerMidpointsWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

    }


    private class GaussWeightsWrapper implements GaussWeightsVariable<double[]> {

        private final Variable variable;
        private double[] value;
        
        public GaussWeightsWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

    }


    private class ReferencePressureWrapper implements ReferencePressureVariable<Double> {

        private final Variable variable;
        private Double value;
        
        public ReferencePressureWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public Double getValue() {
            if (value == null) {
            	Array ncArray;
            	DataType dataType = variable.getDataType();
            	if (dataType.isNumeric()) {
                    ncArray = getNumericArray(variable);
                } else {
                    try {
                        ncArray = variable.read();
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to read variable value", e);
                    }
                }
                value = convertUtils.toJavaObject(ncArray, Double.class);
            }
            return value;
        }

        public void setValue(Double value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return Optional.ofNullable(variable.getDimensions()).orElse(Collections.emptyList());
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException("This is a scalar variable");
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }

    private class DateWrittenWrapper implements DateWrittenVariable<char[][]> {

        private final Variable variable;
        private char[][] value;
        
        public DateWrittenWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public char[][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, char[][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(char[][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

    }


    private class TimeWrittenWrapper implements TimeWrittenVariable<char[][]> {

        private final Variable variable;
        private char[][] value;
        
        public TimeWrittenWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public char[][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, char[][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(char[][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

    }


    private class SpectralTruncationParameterMWrapper implements SpectralTruncationParameterMVariable<Integer> {

        private final Variable variable;
        private Integer value;
        
        public SpectralTruncationParameterMWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public Integer getValue() {
            if (value == null) {
            	Array ncArray;
            	DataType dataType = variable.getDataType();
            	if (dataType.isNumeric()) {
                    ncArray = getNumericArray(variable);
                } else {
                    try {
                        ncArray = variable.read();
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to read variable value", e);
                    }
                }
                value = convertUtils.toJavaObject(ncArray, Integer.class);
            }
            return value;
        }

        public void setValue(Integer value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return Optional.ofNullable(variable.getDimensions()).orElse(Collections.emptyList());
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException("This is a scalar variable");
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }

    private class SpectralTruncationParameterNWrapper implements SpectralTruncationParameterNVariable<Integer> {

        private final Variable variable;
        private Integer value;
        
        public SpectralTruncationParameterNWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public Integer getValue() {
            if (value == null) {
            	Array ncArray;
            	DataType dataType = variable.getDataType();
            	if (dataType.isNumeric()) {
                    ncArray = getNumericArray(variable);
                } else {
                    try {
                        ncArray = variable.read();
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to read variable value", e);
                    }
                }
                value = convertUtils.toJavaObject(ncArray, Integer.class);
            }
            return value;
        }

        public void setValue(Integer value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return Optional.ofNullable(variable.getDimensions()).orElse(Collections.emptyList());
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException("This is a scalar variable");
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }

    private class SpectralTruncationParameterKWrapper implements SpectralTruncationParameterKVariable<Integer> {

        private final Variable variable;
        private Integer value;
        
        public SpectralTruncationParameterKWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public Integer getValue() {
            if (value == null) {
            	Array ncArray;
            	DataType dataType = variable.getDataType();
            	if (dataType.isNumeric()) {
                    ncArray = getNumericArray(variable);
                } else {
                    try {
                        ncArray = variable.read();
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to read variable value", e);
                    }
                }
                value = convertUtils.toJavaObject(ncArray, Integer.class);
            }
            return value;
        }

        public void setValue(Integer value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return Optional.ofNullable(variable.getDimensions()).orElse(Collections.emptyList());
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException("This is a scalar variable");
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }

    private class BaseDayWrapper implements BaseDayVariable<Integer> {

        private final Variable variable;
        private Integer value;
        
        public BaseDayWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public Integer getValue() {
            if (value == null) {
            	Array ncArray;
            	DataType dataType = variable.getDataType();
            	if (dataType.isNumeric()) {
                    ncArray = getNumericArray(variable);
                } else {
                    try {
                        ncArray = variable.read();
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to read variable value", e);
                    }
                }
                value = convertUtils.toJavaObject(ncArray, Integer.class);
            }
            return value;
        }

        public void setValue(Integer value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return Optional.ofNullable(variable.getDimensions()).orElse(Collections.emptyList());
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException("This is a scalar variable");
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }

    private class SecondsOfBaseDayWrapper implements SecondsOfBaseDayVariable<Integer> {

        private final Variable variable;
        private Integer value;
        
        public SecondsOfBaseDayWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public Integer getValue() {
            if (value == null) {
            	Array ncArray;
            	DataType dataType = variable.getDataType();
            	if (dataType.isNumeric()) {
                    ncArray = getNumericArray(variable);
                } else {
                    try {
                        ncArray = variable.read();
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to read variable value", e);
                    }
                }
                value = convertUtils.toJavaObject(ncArray, Integer.class);
            }
            return value;
        }

        public void setValue(Integer value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return Optional.ofNullable(variable.getDimensions()).orElse(Collections.emptyList());
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException("This is a scalar variable");
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }

    private class BaseDateWrapper implements BaseDateVariable<Integer> {

        private final Variable variable;
        private Integer value;
        
        public BaseDateWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public Integer getValue() {
            if (value == null) {
            	Array ncArray;
            	DataType dataType = variable.getDataType();
            	if (dataType.isNumeric()) {
                    ncArray = getNumericArray(variable);
                } else {
                    try {
                        ncArray = variable.read();
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to read variable value", e);
                    }
                }
                value = convertUtils.toJavaObject(ncArray, Integer.class);
            }
            return value;
        }

        public void setValue(Integer value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return Optional.ofNullable(variable.getDimensions()).orElse(Collections.emptyList());
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException("This is a scalar variable");
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }

    private class SecondsOfBaseDateWrapper implements SecondsOfBaseDateVariable<Integer> {

        private final Variable variable;
        private Integer value;
        
        public SecondsOfBaseDateWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public Integer getValue() {
            if (value == null) {
            	Array ncArray;
            	DataType dataType = variable.getDataType();
            	if (dataType.isNumeric()) {
                    ncArray = getNumericArray(variable);
                } else {
                    try {
                        ncArray = variable.read();
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to read variable value", e);
                    }
                }
                value = convertUtils.toJavaObject(ncArray, Integer.class);
            }
            return value;
        }

        public void setValue(Integer value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return Optional.ofNullable(variable.getDimensions()).orElse(Collections.emptyList());
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException("This is a scalar variable");
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }

    private class TimestepWrapper implements TimestepVariable<Integer> {

        private final Variable variable;
        private Integer value;
        
        public TimestepWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public Integer getValue() {
            if (value == null) {
            	Array ncArray;
            	DataType dataType = variable.getDataType();
            	if (dataType.isNumeric()) {
                    ncArray = getNumericArray(variable);
                } else {
                    try {
                        ncArray = variable.read();
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to read variable value", e);
                    }
                }
                value = convertUtils.toJavaObject(ncArray, Integer.class);
            }
            return value;
        }

        public void setValue(Integer value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return Optional.ofNullable(variable.getDimensions()).orElse(Collections.emptyList());
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException("This is a scalar variable");
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }

    private class CurrentDayWrapper implements CurrentDayVariable<int[]> {

        private final Variable variable;
        private int[] value;
        
        public CurrentDayWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public int[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, int[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(int[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }


    private class CurrentSecondsOfCurrentDayWrapper implements CurrentSecondsOfCurrentDayVariable<int[]> {

        private final Variable variable;
        private int[] value;
        
        public CurrentSecondsOfCurrentDayWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public int[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, int[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(int[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }


    private class CurrentDateWrapper implements CurrentDateVariable<int[]> {

        private final Variable variable;
        private int[] value;
        
        public CurrentDateWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public int[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, int[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(int[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }


    private class CurrentSecondsOfCurrentDateWrapper implements CurrentSecondsOfCurrentDateVariable<int[]> {

        private final Variable variable;
        private int[] value;
        
        public CurrentSecondsOfCurrentDateWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public int[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, int[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(int[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }


    private class CurrentTimestepWrapper implements CurrentTimestepVariable<int[]> {

        private final Variable variable;
        private int[] value;
        
        public CurrentTimestepWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public int[] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, int[].class);
            }
            return value;
        }
        
        @Override
        public void setValue(int[] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

    }


    private class WindWrapper implements WindVariable<double[][][][]> {

        private final Variable variable;
        private double[][][][] value;
        
        public WindWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class TemperatureWrapper implements TemperatureVariable<double[][][][]> {

        private final Variable variable;
        private double[][][][] value;
        
        public TemperatureWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class SpecificHumidityWrapper implements SpecificHumidityVariable<double[][][][]> {

        private final Variable variable;
        private double[][][][] value;
        
        public SpecificHumidityWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class SurfacePressureWrapper implements SurfacePressureVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public SurfacePressureWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class SurfaceGeopotentialWrapper implements SurfaceGeopotentialVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public SurfaceGeopotentialWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getFromHires() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "from_hires")))
                    .map(fromHires -> fromHires.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setFromHires(String fromHires) {
            throw new UnsupportedOperationException();
        }

    }


    private class OrographyStandardDeviationWrapper implements OrographyStandardDeviationVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public OrographyStandardDeviationWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getFromHires() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "from_hires")))
                    .map(fromHires -> fromHires.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setFromHires(String fromHires) {
            throw new UnsupportedOperationException();
        }

    }


    private class LandOceanTransitionMaskWrapper implements LandOceanTransitionMaskVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public LandOceanTransitionMaskWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getFromHires() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "from_hires")))
                    .map(fromHires -> fromHires.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setFromHires(String fromHires) {
            throw new UnsupportedOperationException();
        }

    }


    private class PblHeightWrapper implements PblHeightVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public PblHeightWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class PerturbationTemperatureWrapper implements PerturbationTemperatureVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public PerturbationTemperatureWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class PerturbationSpecificHumidityWrapper implements PerturbationSpecificHumidityVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public PerturbationSpecificHumidityWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class CloudFractionWrapper implements CloudFractionVariable<double[][][][]> {

        private final Variable variable;
        private double[][][][] value;
        
        public CloudFractionWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class QCWATWrapper implements QCWATVariable<double[][][][]> {

        private final Variable variable;
        private double[][][][] value;
        
        public QCWATWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

    }


    private class TCWATWrapper implements TCWATVariable<double[][][][]> {

        private final Variable variable;
        private double[][][][] value;
        
        public TCWATWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

    }


    private class LCWATWrapper implements LCWATVariable<double[][][][]> {

        private final Variable variable;
        private double[][][][] value;
        
        public LCWATWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

    }


    private class TSICERADWrapper implements TSICERADVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public TSICERADWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

    }


    private class SecondaryTemperatureWrapper implements SecondaryTemperatureVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public SecondaryTemperatureWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class WaterEquivalentSnowDepthWrapper implements WaterEquivalentSnowDepthVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public WaterEquivalentSnowDepthWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class GridboxLandFractionWrapper implements GridboxLandFractionVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public GridboxLandFractionWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Double getFillValue() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "_FillValue")))
                    .map(fillValue -> (Double)fillValue.getNumericValue())
                    .orElse(null);
        }

        @Override
        public void setFillValue(Double fillValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getFromHires() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "from_hires")))
                    .map(fromHires -> fromHires.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setFromHires(String fromHires) {
            throw new UnsupportedOperationException();
        }

    }


    private class FractionOfSfcAreaCoveredBySeaIceWrapper implements FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public FractionOfSfcAreaCoveredBySeaIceWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class SeaIceThicknessWrapper implements SeaIceThicknessVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public SeaIceThicknessWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }


    private class TSOCNWrapper implements TSOCNVariable<double[][][]> {

        private final Variable variable;
        private double[][][] value;
        
        public TSOCNWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

    }


    private class GridBoxAveragedCondensateAmountWrapper implements GridBoxAveragedCondensateAmountVariable<double[][][][]> {

        private final Variable variable;
        private double[][][][] value;
        
        public GridBoxAveragedCondensateAmountWrapper(Variable variable) {
            this.variable = variable;
        }

        @Override
        public double[][][][] getValue() {
            if (value == null) {
                Array ncArray = getNumericArray(variable);
                value = convertUtils.toJavaObject(ncArray, double[][][][].class);
            }
            return value;
        }
        
        @Override
        public void setValue(double[][][][] value) {
            throw new UnsupportedOperationException();
        }

        public List<Dimension> getDimensions() {
            return variable.getDimensions();
        }

        public void setDimensions(List<Dimension> dimensions) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getLongName() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "long_name")))
                    .map(longName -> longName.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setLongName(String longName) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getUnits() {
            return Optional.ofNullable(variable.findAttribute(runtimeConfiguration.getRuntimeName(variable, "units")))
                    .map(units -> units.getStringValue())
                    .orElse(null);
        }

        @Override
        public void setUnits(String units) {
            throw new UnsupportedOperationException();
        }

    }



    public CommunityAtmosphericModelWrapper(Group group, RuntimeConfiguration runtimeConfiguration) {
        super(group, runtimeConfiguration);
    }

    private ConvertUtils convertUtils = ConvertUtils.getInstance();
    private Map<String, Object> variableCache = new HashMap<String,Object>();
    
    // additionalFields >>
    // << additionalFields

    @SuppressWarnings("unchecked")
    public LatitudeVariable<double[]> getLatitude() {
        return (LatitudeVariable<double[]>) variableCache.computeIfAbsent("lat",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(LatitudeWrapper::new)
                        .orElse(null));
    }

    public void setLatitude(LatitudeVariable<double[]> latitude) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public LongitudeVariable<double[]> getLongitude() {
        return (LongitudeVariable<double[]>) variableCache.computeIfAbsent("lon",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(LongitudeWrapper::new)
                        .orElse(null));
    }

    public void setLongitude(LongitudeVariable<double[]> longitude) {
        throw new UnsupportedOperationException();
    }

    public Map<String, HybridLevelVariable<double[]>> getHybridLevel() {
        return (Map<String, HybridLevelVariable<double[]>>) variableCache.computeIfAbsent("hybrid_level:lev|ilev", varName -> {
            Pattern regex = Pattern.compile(runtimeConfiguration.getRuntimeName(group,
                    varName.substring(varName.indexOf(':') + 1)));
            Map<String, HybridLevelVariable<double[]>> value = new LinkedHashMap<>();
            for (Variable variable : group.getVariables()) {
                Matcher matcher = regex.matcher(variable.getShortName());
                if (matcher.matches()) {
                    value.put(variable.getName(), new HybridLevelWrapper(variable));
                }
            }
            return value;
        });
    }
    
    public void setHybridLevel(Map<String, HybridLevelVariable<double[]>> hybridLevel) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public TimeVariable<double[]> getTime() {
        return (TimeVariable<double[]>) variableCache.computeIfAbsent("time",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(TimeWrapper::new)
                        .orElse(null));
    }

    public void setTime(TimeVariable<double[]> time) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public HybridACoefficientAtLayerInterfacesVariable<double[]> getHybridACoefficientAtLayerInterfaces() {
        return (HybridACoefficientAtLayerInterfacesVariable<double[]>) variableCache.computeIfAbsent("hyai",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(HybridACoefficientAtLayerInterfacesWrapper::new)
                        .orElse(null));
    }

    public void setHybridACoefficientAtLayerInterfaces(HybridACoefficientAtLayerInterfacesVariable<double[]> hybridACoefficientAtLayerInterfaces) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public HybridBCoefficientAtLayerInterfacesVariable<double[]> getHybridBCoefficientAtLayerInterfaces() {
        return (HybridBCoefficientAtLayerInterfacesVariable<double[]>) variableCache.computeIfAbsent("hybi",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(HybridBCoefficientAtLayerInterfacesWrapper::new)
                        .orElse(null));
    }

    public void setHybridBCoefficientAtLayerInterfaces(HybridBCoefficientAtLayerInterfacesVariable<double[]> hybridBCoefficientAtLayerInterfaces) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public HybridACoefficientAtLayerMidpointsVariable<double[]> getHybridACoefficientAtLayerMidpoints() {
        return (HybridACoefficientAtLayerMidpointsVariable<double[]>) variableCache.computeIfAbsent("hyam",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(HybridACoefficientAtLayerMidpointsWrapper::new)
                        .orElse(null));
    }

    public void setHybridACoefficientAtLayerMidpoints(HybridACoefficientAtLayerMidpointsVariable<double[]> hybridACoefficientAtLayerMidpoints) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public HybridBCoefficientAtLayerMidpointsVariable<double[]> getHybridBCoefficientAtLayerMidpoints() {
        return (HybridBCoefficientAtLayerMidpointsVariable<double[]>) variableCache.computeIfAbsent("hybm",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(HybridBCoefficientAtLayerMidpointsWrapper::new)
                        .orElse(null));
    }

    public void setHybridBCoefficientAtLayerMidpoints(HybridBCoefficientAtLayerMidpointsVariable<double[]> hybridBCoefficientAtLayerMidpoints) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public GaussWeightsVariable<double[]> getGaussWeights() {
        return (GaussWeightsVariable<double[]>) variableCache.computeIfAbsent("gw",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(GaussWeightsWrapper::new)
                        .orElse(null));
    }

    public void setGaussWeights(GaussWeightsVariable<double[]> gaussWeights) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public ReferencePressureVariable<Double> getReferencePressure() {
        return (ReferencePressureVariable<Double>) variableCache.computeIfAbsent("P0",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(ReferencePressureWrapper::new)
                        .orElse(null));
    }

    public void setReferencePressure(ReferencePressureVariable<Double> referencePressure) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public DateWrittenVariable<char[][]> getDateWritten() {
        return (DateWrittenVariable<char[][]>) variableCache.computeIfAbsent("date_written",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(DateWrittenWrapper::new)
                        .orElse(null));
    }

    public void setDateWritten(DateWrittenVariable<char[][]> dateWritten) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public TimeWrittenVariable<char[][]> getTimeWritten() {
        return (TimeWrittenVariable<char[][]>) variableCache.computeIfAbsent("time_written",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(TimeWrittenWrapper::new)
                        .orElse(null));
    }

    public void setTimeWritten(TimeWrittenVariable<char[][]> timeWritten) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public SpectralTruncationParameterMVariable<Integer> getSpectralTruncationParameterM() {
        return (SpectralTruncationParameterMVariable<Integer>) variableCache.computeIfAbsent("ntrm",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(SpectralTruncationParameterMWrapper::new)
                        .orElse(null));
    }

    public void setSpectralTruncationParameterM(SpectralTruncationParameterMVariable<Integer> spectralTruncationParameterM) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public SpectralTruncationParameterNVariable<Integer> getSpectralTruncationParameterN() {
        return (SpectralTruncationParameterNVariable<Integer>) variableCache.computeIfAbsent("ntrn",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(SpectralTruncationParameterNWrapper::new)
                        .orElse(null));
    }

    public void setSpectralTruncationParameterN(SpectralTruncationParameterNVariable<Integer> spectralTruncationParameterN) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public SpectralTruncationParameterKVariable<Integer> getSpectralTruncationParameterK() {
        return (SpectralTruncationParameterKVariable<Integer>) variableCache.computeIfAbsent("ntrk",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(SpectralTruncationParameterKWrapper::new)
                        .orElse(null));
    }

    public void setSpectralTruncationParameterK(SpectralTruncationParameterKVariable<Integer> spectralTruncationParameterK) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public BaseDayVariable<Integer> getBaseDay() {
        return (BaseDayVariable<Integer>) variableCache.computeIfAbsent("ndbase",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(BaseDayWrapper::new)
                        .orElse(null));
    }

    public void setBaseDay(BaseDayVariable<Integer> baseDay) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public SecondsOfBaseDayVariable<Integer> getSecondsOfBaseDay() {
        return (SecondsOfBaseDayVariable<Integer>) variableCache.computeIfAbsent("nsbase",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(SecondsOfBaseDayWrapper::new)
                        .orElse(null));
    }

    public void setSecondsOfBaseDay(SecondsOfBaseDayVariable<Integer> secondsOfBaseDay) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public BaseDateVariable<Integer> getBaseDate() {
        return (BaseDateVariable<Integer>) variableCache.computeIfAbsent("nbdate",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(BaseDateWrapper::new)
                        .orElse(null));
    }

    public void setBaseDate(BaseDateVariable<Integer> baseDate) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public SecondsOfBaseDateVariable<Integer> getSecondsOfBaseDate() {
        return (SecondsOfBaseDateVariable<Integer>) variableCache.computeIfAbsent("nbsec",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(SecondsOfBaseDateWrapper::new)
                        .orElse(null));
    }

    public void setSecondsOfBaseDate(SecondsOfBaseDateVariable<Integer> secondsOfBaseDate) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public TimestepVariable<Integer> getTimestep() {
        return (TimestepVariable<Integer>) variableCache.computeIfAbsent("mdt",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(TimestepWrapper::new)
                        .orElse(null));
    }

    public void setTimestep(TimestepVariable<Integer> timestep) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public CurrentDayVariable<int[]> getCurrentDay() {
        return (CurrentDayVariable<int[]>) variableCache.computeIfAbsent("ndcur",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(CurrentDayWrapper::new)
                        .orElse(null));
    }

    public void setCurrentDay(CurrentDayVariable<int[]> currentDay) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public CurrentSecondsOfCurrentDayVariable<int[]> getCurrentSecondsOfCurrentDay() {
        return (CurrentSecondsOfCurrentDayVariable<int[]>) variableCache.computeIfAbsent("nscur",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(CurrentSecondsOfCurrentDayWrapper::new)
                        .orElse(null));
    }

    public void setCurrentSecondsOfCurrentDay(CurrentSecondsOfCurrentDayVariable<int[]> currentSecondsOfCurrentDay) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public CurrentDateVariable<int[]> getCurrentDate() {
        return (CurrentDateVariable<int[]>) variableCache.computeIfAbsent("date",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(CurrentDateWrapper::new)
                        .orElse(null));
    }

    public void setCurrentDate(CurrentDateVariable<int[]> currentDate) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public CurrentSecondsOfCurrentDateVariable<int[]> getCurrentSecondsOfCurrentDate() {
        return (CurrentSecondsOfCurrentDateVariable<int[]>) variableCache.computeIfAbsent("datesec",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(CurrentSecondsOfCurrentDateWrapper::new)
                        .orElse(null));
    }

    public void setCurrentSecondsOfCurrentDate(CurrentSecondsOfCurrentDateVariable<int[]> currentSecondsOfCurrentDate) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public CurrentTimestepVariable<int[]> getCurrentTimestep() {
        return (CurrentTimestepVariable<int[]>) variableCache.computeIfAbsent("nsteph",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(CurrentTimestepWrapper::new)
                        .orElse(null));
    }

    public void setCurrentTimestep(CurrentTimestepVariable<int[]> currentTimestep) {
        throw new UnsupportedOperationException();
    }

    public Map<String, WindVariable<double[][][][]>> getWind() {
        return (Map<String, WindVariable<double[][][][]>>) variableCache.computeIfAbsent("wind:U|V", varName -> {
            Pattern regex = Pattern.compile(runtimeConfiguration.getRuntimeName(group,
                    varName.substring(varName.indexOf(':') + 1)));
            Map<String, WindVariable<double[][][][]>> value = new LinkedHashMap<>();
            for (Variable variable : group.getVariables()) {
                Matcher matcher = regex.matcher(variable.getShortName());
                if (matcher.matches()) {
                    value.put(variable.getName(), new WindWrapper(variable));
                }
            }
            return value;
        });
    }
    
    public void setWind(Map<String, WindVariable<double[][][][]>> wind) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public TemperatureVariable<double[][][][]> getTemperature() {
        return (TemperatureVariable<double[][][][]>) variableCache.computeIfAbsent("T",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(TemperatureWrapper::new)
                        .orElse(null));
    }

    public void setTemperature(TemperatureVariable<double[][][][]> temperature) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public SpecificHumidityVariable<double[][][][]> getSpecificHumidity() {
        return (SpecificHumidityVariable<double[][][][]>) variableCache.computeIfAbsent("Q",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(SpecificHumidityWrapper::new)
                        .orElse(null));
    }

    public void setSpecificHumidity(SpecificHumidityVariable<double[][][][]> specificHumidity) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public SurfacePressureVariable<double[][][]> getSurfacePressure() {
        return (SurfacePressureVariable<double[][][]>) variableCache.computeIfAbsent("PS",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(SurfacePressureWrapper::new)
                        .orElse(null));
    }

    public void setSurfacePressure(SurfacePressureVariable<double[][][]> surfacePressure) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public SurfaceGeopotentialVariable<double[][][]> getSurfaceGeopotential() {
        return (SurfaceGeopotentialVariable<double[][][]>) variableCache.computeIfAbsent("PHIS",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(SurfaceGeopotentialWrapper::new)
                        .orElse(null));
    }

    public void setSurfaceGeopotential(SurfaceGeopotentialVariable<double[][][]> surfaceGeopotential) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public OrographyStandardDeviationVariable<double[][][]> getOrographyStandardDeviation() {
        return (OrographyStandardDeviationVariable<double[][][]>) variableCache.computeIfAbsent("SGH",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(OrographyStandardDeviationWrapper::new)
                        .orElse(null));
    }

    public void setOrographyStandardDeviation(OrographyStandardDeviationVariable<double[][][]> orographyStandardDeviation) {
        throw new UnsupportedOperationException();
    }

    public Map<String, LandOceanTransitionMaskVariable<double[][][]>> getLandOceanTransitionMask() {
        return (Map<String, LandOceanTransitionMaskVariable<double[][][]>>) variableCache.computeIfAbsent("land_ocean_transition_mask:LANDM|LANDM_COSLAT", varName -> {
            Pattern regex = Pattern.compile(runtimeConfiguration.getRuntimeName(group,
                    varName.substring(varName.indexOf(':') + 1)));
            Map<String, LandOceanTransitionMaskVariable<double[][][]>> value = new LinkedHashMap<>();
            for (Variable variable : group.getVariables()) {
                Matcher matcher = regex.matcher(variable.getShortName());
                if (matcher.matches()) {
                    value.put(variable.getName(), new LandOceanTransitionMaskWrapper(variable));
                }
            }
            return value;
        });
    }
    
    public void setLandOceanTransitionMask(Map<String, LandOceanTransitionMaskVariable<double[][][]>> landOceanTransitionMask) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public PblHeightVariable<double[][][]> getPblHeight() {
        return (PblHeightVariable<double[][][]>) variableCache.computeIfAbsent("PBLH",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(PblHeightWrapper::new)
                        .orElse(null));
    }

    public void setPblHeight(PblHeightVariable<double[][][]> pblHeight) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public PerturbationTemperatureVariable<double[][][]> getPerturbationTemperature() {
        return (PerturbationTemperatureVariable<double[][][]>) variableCache.computeIfAbsent("TPERT",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(PerturbationTemperatureWrapper::new)
                        .orElse(null));
    }

    public void setPerturbationTemperature(PerturbationTemperatureVariable<double[][][]> perturbationTemperature) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public PerturbationSpecificHumidityVariable<double[][][]> getPerturbationSpecificHumidity() {
        return (PerturbationSpecificHumidityVariable<double[][][]>) variableCache.computeIfAbsent("QPERT",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(PerturbationSpecificHumidityWrapper::new)
                        .orElse(null));
    }

    public void setPerturbationSpecificHumidity(PerturbationSpecificHumidityVariable<double[][][]> perturbationSpecificHumidity) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public CloudFractionVariable<double[][][][]> getCloudFraction() {
        return (CloudFractionVariable<double[][][][]>) variableCache.computeIfAbsent("CLOUD",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(CloudFractionWrapper::new)
                        .orElse(null));
    }

    public void setCloudFraction(CloudFractionVariable<double[][][][]> cloudFraction) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public QCWATVariable<double[][][][]> getQCWAT() {
        return (QCWATVariable<double[][][][]>) variableCache.computeIfAbsent("QCWAT",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(QCWATWrapper::new)
                        .orElse(null));
    }

    public void setQCWAT(QCWATVariable<double[][][][]> qCWAT) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public TCWATVariable<double[][][][]> getTCWAT() {
        return (TCWATVariable<double[][][][]>) variableCache.computeIfAbsent("TCWAT",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(TCWATWrapper::new)
                        .orElse(null));
    }

    public void setTCWAT(TCWATVariable<double[][][][]> tCWAT) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public LCWATVariable<double[][][][]> getLCWAT() {
        return (LCWATVariable<double[][][][]>) variableCache.computeIfAbsent("LCWAT",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(LCWATWrapper::new)
                        .orElse(null));
    }

    public void setLCWAT(LCWATVariable<double[][][][]> lCWAT) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public TSICERADVariable<double[][][]> getTSICERAD() {
        return (TSICERADVariable<double[][][]>) variableCache.computeIfAbsent("TSICERAD",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(TSICERADWrapper::new)
                        .orElse(null));
    }

    public void setTSICERAD(TSICERADVariable<double[][][]> tSICERAD) {
        throw new UnsupportedOperationException();
    }

    public Map<String, SecondaryTemperatureVariable<double[][][]>> getSecondaryTemperature() {
        return (Map<String, SecondaryTemperatureVariable<double[][][]>>) variableCache.computeIfAbsent("secondary_temperature:TS|TSICE|TS1|TS2|TS3|TS4|TBOT", varName -> {
            Pattern regex = Pattern.compile(runtimeConfiguration.getRuntimeName(group,
                    varName.substring(varName.indexOf(':') + 1)));
            Map<String, SecondaryTemperatureVariable<double[][][]>> value = new LinkedHashMap<>();
            for (Variable variable : group.getVariables()) {
                Matcher matcher = regex.matcher(variable.getShortName());
                if (matcher.matches()) {
                    value.put(variable.getName(), new SecondaryTemperatureWrapper(variable));
                }
            }
            return value;
        });
    }
    
    public void setSecondaryTemperature(Map<String, SecondaryTemperatureVariable<double[][][]>> secondaryTemperature) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public WaterEquivalentSnowDepthVariable<double[][][]> getWaterEquivalentSnowDepth() {
        return (WaterEquivalentSnowDepthVariable<double[][][]>) variableCache.computeIfAbsent("SNOWHICE",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(WaterEquivalentSnowDepthWrapper::new)
                        .orElse(null));
    }

    public void setWaterEquivalentSnowDepth(WaterEquivalentSnowDepthVariable<double[][][]> waterEquivalentSnowDepth) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public GridboxLandFractionVariable<double[][][]> getGridboxLandFraction() {
        return (GridboxLandFractionVariable<double[][][]>) variableCache.computeIfAbsent("LANDFRAC",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(GridboxLandFractionWrapper::new)
                        .orElse(null));
    }

    public void setGridboxLandFraction(GridboxLandFractionVariable<double[][][]> gridboxLandFraction) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> getFractionOfSfcAreaCoveredBySeaIce() {
        return (FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]>) variableCache.computeIfAbsent("ICEFRAC",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(FractionOfSfcAreaCoveredBySeaIceWrapper::new)
                        .orElse(null));
    }

    public void setFractionOfSfcAreaCoveredBySeaIce(FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> fractionOfSfcAreaCoveredBySeaIce) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public SeaIceThicknessVariable<double[][][]> getSeaIceThickness() {
        return (SeaIceThicknessVariable<double[][][]>) variableCache.computeIfAbsent("SICTHK",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(SeaIceThicknessWrapper::new)
                        .orElse(null));
    }

    public void setSeaIceThickness(SeaIceThicknessVariable<double[][][]> seaIceThickness) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public TSOCNVariable<double[][][]> getTSOCN() {
        return (TSOCNVariable<double[][][]>) variableCache.computeIfAbsent("TSOCN",
                varName -> Optional.ofNullable(group.findVariable(runtimeConfiguration.getRuntimeName(group, varName)))
                        .map(TSOCNWrapper::new)
                        .orElse(null));
    }

    public void setTSOCN(TSOCNVariable<double[][][]> tSOCN) {
        throw new UnsupportedOperationException();
    }

    public Map<String, GridBoxAveragedCondensateAmountVariable<double[][][][]>> getGridBoxAveragedCondensateAmount() {
        return (Map<String, GridBoxAveragedCondensateAmountVariable<double[][][][]>>) variableCache.computeIfAbsent("grid_box_averaged_condensate_amount:CLDLIQ|CLDICE", varName -> {
            Pattern regex = Pattern.compile(runtimeConfiguration.getRuntimeName(group,
                    varName.substring(varName.indexOf(':') + 1)));
            Map<String, GridBoxAveragedCondensateAmountVariable<double[][][][]>> value = new LinkedHashMap<>();
            for (Variable variable : group.getVariables()) {
                Matcher matcher = regex.matcher(variable.getShortName());
                if (matcher.matches()) {
                    value.put(variable.getName(), new GridBoxAveragedCondensateAmountWrapper(variable));
                }
            }
            return value;
        });
    }
    
    public void setGridBoxAveragedCondensateAmount(Map<String, GridBoxAveragedCondensateAmountVariable<double[][][][]>> gridBoxAveragedCondensateAmount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getConventions() {
        return Optional.ofNullable(group.findAttribute(runtimeConfiguration.getRuntimeName(group, "Conventions")))
                .map(conventions -> conventions.getStringValue())
                .orElse(null);
    }

    @Override
    public void setConventions(String conventions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getLogname() {
        return Optional.ofNullable(group.findAttribute(runtimeConfiguration.getRuntimeName(group, "logname")))
                .map(logname -> logname.getStringValue())
                .orElse(null);
    }

    @Override
    public void setLogname(String logname) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getHost() {
        return Optional.ofNullable(group.findAttribute(runtimeConfiguration.getRuntimeName(group, "host")))
                .map(host -> host.getStringValue())
                .orElse(null);
    }

    @Override
    public void setHost(String host) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSource() {
        return Optional.ofNullable(group.findAttribute(runtimeConfiguration.getRuntimeName(group, "source")))
                .map(source -> source.getStringValue())
                .orElse(null);
    }

    @Override
    public void setSource(String source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCase() {
        return Optional.ofNullable(group.findAttribute(runtimeConfiguration.getRuntimeName(group, "case")))
                .map(_case -> _case.getStringValue())
                .orElse(null);
    }

    @Override
    public void setCase(String _case) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTitle() {
        return Optional.ofNullable(group.findAttribute(runtimeConfiguration.getRuntimeName(group, "title")))
                .map(title -> title.getStringValue())
                .orElse(null);
    }

    @Override
    public void setTitle(String title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getHistory() {
        return Optional.ofNullable(group.findAttribute(runtimeConfiguration.getRuntimeName(group, "history")))
                .map(history -> history.getStringValue())
                .orElse(null);
    }

    @Override
    public void setHistory(String history) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMakeRoss() {
        return Optional.ofNullable(group.findAttribute(runtimeConfiguration.getRuntimeName(group, "make_ross")))
                .map(makeRoss -> makeRoss.getStringValue())
                .orElse(null);
    }

    @Override
    public void setMakeRoss(String makeRoss) {
        throw new UnsupportedOperationException();
    }

    // additionalMethods >>
    // << additionalMethods

}
