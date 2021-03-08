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

// imports >>
// DEFAULT IMPORTS
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ucar.nc2.Dimension;
// << imports

public class CommunityAtmosphericModelVO implements CommunityAtmosphericModel {
    public static class LatitudeVO implements LatitudeVariable<double[]> {

        private double[] value;
        private List<Dimension> dimensions;

        public LatitudeVO() {}

        @Override
        public double[] getValue() {
            return value;
        }

        @Override
        public void setValue(double[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

    }


    public static class LongitudeVO implements LongitudeVariable<double[]> {

        private double[] value;
        private List<Dimension> dimensions;

        public LongitudeVO() {}

        @Override
        public double[] getValue() {
            return value;
        }

        @Override
        public void setValue(double[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

    }


    public static class HybridLevelVO implements HybridLevelVariable<double[]> {

        private double[] value;
        private List<Dimension> dimensions;

        public HybridLevelVO() {}

        @Override
        public double[] getValue() {
            return value;
        }

        @Override
        public void setValue(double[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

        private String positive;

        @Override
        public String getPositive() {
            return positive;
        }

        @Override
        public void setPositive(String positive) {
            this.positive = positive;
        }

        private String standardName;

        @Override
        public String getStandardName() {
            return standardName;
        }

        @Override
        public void setStandardName(String standardName) {
            this.standardName = standardName;
        }

        private String formulaTerms;

        @Override
        public String getFormulaTerms() {
            return formulaTerms;
        }

        @Override
        public void setFormulaTerms(String formulaTerms) {
            this.formulaTerms = formulaTerms;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

    }


    public static class TimeVO implements TimeVariable<double[]> {

        private double[] value;
        private List<Dimension> dimensions;

        public TimeVO() {}

        @Override
        public double[] getValue() {
            return value;
        }

        @Override
        public void setValue(double[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

        private String calendar;

        @Override
        public String getCalendar() {
            return calendar;
        }

        @Override
        public void setCalendar(String calendar) {
            this.calendar = calendar;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

    }


    public static class HybridACoefficientAtLayerInterfacesVO implements HybridACoefficientAtLayerInterfacesVariable<double[]> {

        private double[] value;
        private List<Dimension> dimensions;

        public HybridACoefficientAtLayerInterfacesVO() {}

        @Override
        public double[] getValue() {
            return value;
        }

        @Override
        public void setValue(double[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

    }


    public static class HybridBCoefficientAtLayerInterfacesVO implements HybridBCoefficientAtLayerInterfacesVariable<double[]> {

        private double[] value;
        private List<Dimension> dimensions;

        public HybridBCoefficientAtLayerInterfacesVO() {}

        @Override
        public double[] getValue() {
            return value;
        }

        @Override
        public void setValue(double[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

    }


    public static class HybridACoefficientAtLayerMidpointsVO implements HybridACoefficientAtLayerMidpointsVariable<double[]> {

        private double[] value;
        private List<Dimension> dimensions;

        public HybridACoefficientAtLayerMidpointsVO() {}

        @Override
        public double[] getValue() {
            return value;
        }

        @Override
        public void setValue(double[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

    }


    public static class HybridBCoefficientAtLayerMidpointsVO implements HybridBCoefficientAtLayerMidpointsVariable<double[]> {

        private double[] value;
        private List<Dimension> dimensions;

        public HybridBCoefficientAtLayerMidpointsVO() {}

        @Override
        public double[] getValue() {
            return value;
        }

        @Override
        public void setValue(double[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

    }


    public static class GaussWeightsVO implements GaussWeightsVariable<double[]> {

        private double[] value;
        private List<Dimension> dimensions;

        public GaussWeightsVO() {}

        @Override
        public double[] getValue() {
            return value;
        }

        @Override
        public void setValue(double[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

    }


    public static class ReferencePressureVO implements ReferencePressureVariable<Double> {

        private List<Dimension> dimensions;
        private Double value;

        @Override
        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }

    public static class DateWrittenVO implements DateWrittenVariable<char[][]> {

        private char[][] value;
        private List<Dimension> dimensions;

        public DateWrittenVO() {}

        @Override
        public char[][] getValue() {
            return value;
        }

        @Override
        public void setValue(char[][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

    }


    public static class TimeWrittenVO implements TimeWrittenVariable<char[][]> {

        private char[][] value;
        private List<Dimension> dimensions;

        public TimeWrittenVO() {}

        @Override
        public char[][] getValue() {
            return value;
        }

        @Override
        public void setValue(char[][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

    }


    public static class SpectralTruncationParameterMVO implements SpectralTruncationParameterMVariable<Integer> {

        private List<Dimension> dimensions;
        private Integer value;

        @Override
        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }

    public static class SpectralTruncationParameterNVO implements SpectralTruncationParameterNVariable<Integer> {

        private List<Dimension> dimensions;
        private Integer value;

        @Override
        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }

    public static class SpectralTruncationParameterKVO implements SpectralTruncationParameterKVariable<Integer> {

        private List<Dimension> dimensions;
        private Integer value;

        @Override
        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }

    public static class BaseDayVO implements BaseDayVariable<Integer> {

        private List<Dimension> dimensions;
        private Integer value;

        @Override
        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }

    public static class SecondsOfBaseDayVO implements SecondsOfBaseDayVariable<Integer> {

        private List<Dimension> dimensions;
        private Integer value;

        @Override
        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }

    public static class BaseDateVO implements BaseDateVariable<Integer> {

        private List<Dimension> dimensions;
        private Integer value;

        @Override
        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }

    public static class SecondsOfBaseDateVO implements SecondsOfBaseDateVariable<Integer> {

        private List<Dimension> dimensions;
        private Integer value;

        @Override
        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }

    public static class TimestepVO implements TimestepVariable<Integer> {

        private List<Dimension> dimensions;
        private Integer value;

        @Override
        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }

    public static class CurrentDayVO implements CurrentDayVariable<int[]> {

        private int[] value;
        private List<Dimension> dimensions;

        public CurrentDayVO() {}

        @Override
        public int[] getValue() {
            return value;
        }

        @Override
        public void setValue(int[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }


    public static class CurrentSecondsOfCurrentDayVO implements CurrentSecondsOfCurrentDayVariable<int[]> {

        private int[] value;
        private List<Dimension> dimensions;

        public CurrentSecondsOfCurrentDayVO() {}

        @Override
        public int[] getValue() {
            return value;
        }

        @Override
        public void setValue(int[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }


    public static class CurrentDateVO implements CurrentDateVariable<int[]> {

        private int[] value;
        private List<Dimension> dimensions;

        public CurrentDateVO() {}

        @Override
        public int[] getValue() {
            return value;
        }

        @Override
        public void setValue(int[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }


    public static class CurrentSecondsOfCurrentDateVO implements CurrentSecondsOfCurrentDateVariable<int[]> {

        private int[] value;
        private List<Dimension> dimensions;

        public CurrentSecondsOfCurrentDateVO() {}

        @Override
        public int[] getValue() {
            return value;
        }

        @Override
        public void setValue(int[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }


    public static class CurrentTimestepVO implements CurrentTimestepVariable<int[]> {

        private int[] value;
        private List<Dimension> dimensions;

        public CurrentTimestepVO() {}

        @Override
        public int[] getValue() {
            return value;
        }

        @Override
        public void setValue(int[] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

    }


    public static class WindVO implements WindVariable<double[][][][]> {

        private double[][][][] value;
        private List<Dimension> dimensions;

        public WindVO() {}

        @Override
        public double[][][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class TemperatureVO implements TemperatureVariable<double[][][][]> {

        private double[][][][] value;
        private List<Dimension> dimensions;

        public TemperatureVO() {}

        @Override
        public double[][][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class SpecificHumidityVO implements SpecificHumidityVariable<double[][][][]> {

        private double[][][][] value;
        private List<Dimension> dimensions;

        public SpecificHumidityVO() {}

        @Override
        public double[][][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class SurfacePressureVO implements SurfacePressureVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public SurfacePressureVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class SurfaceGeopotentialVO implements SurfaceGeopotentialVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public SurfaceGeopotentialVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

        private String fromHires;

        @Override
        public String getFromHires() {
            return fromHires;
        }

        @Override
        public void setFromHires(String fromHires) {
            this.fromHires = fromHires;
        }

    }


    public static class OrographyStandardDeviationVO implements OrographyStandardDeviationVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public OrographyStandardDeviationVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

        private String fromHires;

        @Override
        public String getFromHires() {
            return fromHires;
        }

        @Override
        public void setFromHires(String fromHires) {
            this.fromHires = fromHires;
        }

    }


    public static class LandOceanTransitionMaskVO implements LandOceanTransitionMaskVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public LandOceanTransitionMaskVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

        private String fromHires;

        @Override
        public String getFromHires() {
            return fromHires;
        }

        @Override
        public void setFromHires(String fromHires) {
            this.fromHires = fromHires;
        }

    }


    public static class PblHeightVO implements PblHeightVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public PblHeightVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class PerturbationTemperatureVO implements PerturbationTemperatureVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public PerturbationTemperatureVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class PerturbationSpecificHumidityVO implements PerturbationSpecificHumidityVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public PerturbationSpecificHumidityVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class CloudFractionVO implements CloudFractionVariable<double[][][][]> {

        private double[][][][] value;
        private List<Dimension> dimensions;

        public CloudFractionVO() {}

        @Override
        public double[][][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class QCWATVO implements QCWATVariable<double[][][][]> {

        private double[][][][] value;
        private List<Dimension> dimensions;

        public QCWATVO() {}

        @Override
        public double[][][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

    }


    public static class TCWATVO implements TCWATVariable<double[][][][]> {

        private double[][][][] value;
        private List<Dimension> dimensions;

        public TCWATVO() {}

        @Override
        public double[][][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

    }


    public static class LCWATVO implements LCWATVariable<double[][][][]> {

        private double[][][][] value;
        private List<Dimension> dimensions;

        public LCWATVO() {}

        @Override
        public double[][][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

    }


    public static class TSICERADVO implements TSICERADVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public TSICERADVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

    }


    public static class SecondaryTemperatureVO implements SecondaryTemperatureVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public SecondaryTemperatureVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class WaterEquivalentSnowDepthVO implements WaterEquivalentSnowDepthVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public WaterEquivalentSnowDepthVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class GridboxLandFractionVO implements GridboxLandFractionVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public GridboxLandFractionVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

        private Double fillValue;

        @Override
        public Double getFillValue() {
            return fillValue;
        }

        @Override
        public void setFillValue(Double fillValue) {
            this.fillValue = fillValue;
        }

        private String fromHires;

        @Override
        public String getFromHires() {
            return fromHires;
        }

        @Override
        public void setFromHires(String fromHires) {
            this.fromHires = fromHires;
        }

    }


    public static class FractionOfSfcAreaCoveredBySeaIceVO implements FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public FractionOfSfcAreaCoveredBySeaIceVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class SeaIceThicknessVO implements SeaIceThicknessVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public SeaIceThicknessVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    public static class TSOCNVO implements TSOCNVariable<double[][][]> {

        private double[][][] value;
        private List<Dimension> dimensions;

        public TSOCNVO() {}

        @Override
        public double[][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

    }


    public static class GridBoxAveragedCondensateAmountVO implements GridBoxAveragedCondensateAmountVariable<double[][][][]> {

        private double[][][][] value;
        private List<Dimension> dimensions;

        public GridBoxAveragedCondensateAmountVO() {}

        @Override
        public double[][][][] getValue() {
            return value;
        }

        @Override
        public void setValue(double[][][][] value) {
            this.value = value;
        }

        @Override
        public List<Dimension> getDimensions() {
            return dimensions;
        }

        @Override
        public void setDimensions(List<Dimension> dimensions) {
            this.dimensions = dimensions;
        }

        private String longName;

        @Override
        public String getLongName() {
            return longName;
        }

        @Override
        public void setLongName(String longName) {
            this.longName = longName;
        }

        private String units;

        @Override
        public String getUnits() {
            return units;
        }

        @Override
        public void setUnits(String units) {
            this.units = units;
        }

    }


    // additionalFields >>
    // << additionalFields

    private LatitudeVariable<double[]> latitude;

    public LatitudeVariable<double[]> getLatitude() {
        return latitude;
    }

    public void setLatitude(LatitudeVariable<double[]> latitude) {
        this.latitude = latitude;
    }

    private LongitudeVariable<double[]> longitude;

    public LongitudeVariable<double[]> getLongitude() {
        return longitude;
    }

    public void setLongitude(LongitudeVariable<double[]> longitude) {
        this.longitude = longitude;
    }

    private Map<String, HybridLevelVariable<double[]>> hybridLevel;

    public Map<String, HybridLevelVariable<double[]>> getHybridLevel() {
        return hybridLevel;
    }

    public void setHybridLevel(Map<String, HybridLevelVariable<double[]>> hybridLevel) {
        this.hybridLevel = hybridLevel;
    }

    private TimeVariable<double[]> time;

    public TimeVariable<double[]> getTime() {
        return time;
    }

    public void setTime(TimeVariable<double[]> time) {
        this.time = time;
    }

    private HybridACoefficientAtLayerInterfacesVariable<double[]> hybridACoefficientAtLayerInterfaces;

    public HybridACoefficientAtLayerInterfacesVariable<double[]> getHybridACoefficientAtLayerInterfaces() {
        return hybridACoefficientAtLayerInterfaces;
    }

    public void setHybridACoefficientAtLayerInterfaces(HybridACoefficientAtLayerInterfacesVariable<double[]> hybridACoefficientAtLayerInterfaces) {
        this.hybridACoefficientAtLayerInterfaces = hybridACoefficientAtLayerInterfaces;
    }

    private HybridBCoefficientAtLayerInterfacesVariable<double[]> hybridBCoefficientAtLayerInterfaces;

    public HybridBCoefficientAtLayerInterfacesVariable<double[]> getHybridBCoefficientAtLayerInterfaces() {
        return hybridBCoefficientAtLayerInterfaces;
    }

    public void setHybridBCoefficientAtLayerInterfaces(HybridBCoefficientAtLayerInterfacesVariable<double[]> hybridBCoefficientAtLayerInterfaces) {
        this.hybridBCoefficientAtLayerInterfaces = hybridBCoefficientAtLayerInterfaces;
    }

    private HybridACoefficientAtLayerMidpointsVariable<double[]> hybridACoefficientAtLayerMidpoints;

    public HybridACoefficientAtLayerMidpointsVariable<double[]> getHybridACoefficientAtLayerMidpoints() {
        return hybridACoefficientAtLayerMidpoints;
    }

    public void setHybridACoefficientAtLayerMidpoints(HybridACoefficientAtLayerMidpointsVariable<double[]> hybridACoefficientAtLayerMidpoints) {
        this.hybridACoefficientAtLayerMidpoints = hybridACoefficientAtLayerMidpoints;
    }

    private HybridBCoefficientAtLayerMidpointsVariable<double[]> hybridBCoefficientAtLayerMidpoints;

    public HybridBCoefficientAtLayerMidpointsVariable<double[]> getHybridBCoefficientAtLayerMidpoints() {
        return hybridBCoefficientAtLayerMidpoints;
    }

    public void setHybridBCoefficientAtLayerMidpoints(HybridBCoefficientAtLayerMidpointsVariable<double[]> hybridBCoefficientAtLayerMidpoints) {
        this.hybridBCoefficientAtLayerMidpoints = hybridBCoefficientAtLayerMidpoints;
    }

    private GaussWeightsVariable<double[]> gaussWeights;

    public GaussWeightsVariable<double[]> getGaussWeights() {
        return gaussWeights;
    }

    public void setGaussWeights(GaussWeightsVariable<double[]> gaussWeights) {
        this.gaussWeights = gaussWeights;
    }

    private ReferencePressureVariable<Double> referencePressure;

    public ReferencePressureVariable<Double> getReferencePressure() {
        return referencePressure;
    }

    public void setReferencePressure(ReferencePressureVariable<Double> referencePressure) {
        this.referencePressure = referencePressure;
    }

    private DateWrittenVariable<char[][]> dateWritten;

    public DateWrittenVariable<char[][]> getDateWritten() {
        return dateWritten;
    }

    public void setDateWritten(DateWrittenVariable<char[][]> dateWritten) {
        this.dateWritten = dateWritten;
    }

    private TimeWrittenVariable<char[][]> timeWritten;

    public TimeWrittenVariable<char[][]> getTimeWritten() {
        return timeWritten;
    }

    public void setTimeWritten(TimeWrittenVariable<char[][]> timeWritten) {
        this.timeWritten = timeWritten;
    }

    private SpectralTruncationParameterMVariable<Integer> spectralTruncationParameterM;

    public SpectralTruncationParameterMVariable<Integer> getSpectralTruncationParameterM() {
        return spectralTruncationParameterM;
    }

    public void setSpectralTruncationParameterM(SpectralTruncationParameterMVariable<Integer> spectralTruncationParameterM) {
        this.spectralTruncationParameterM = spectralTruncationParameterM;
    }

    private SpectralTruncationParameterNVariable<Integer> spectralTruncationParameterN;

    public SpectralTruncationParameterNVariable<Integer> getSpectralTruncationParameterN() {
        return spectralTruncationParameterN;
    }

    public void setSpectralTruncationParameterN(SpectralTruncationParameterNVariable<Integer> spectralTruncationParameterN) {
        this.spectralTruncationParameterN = spectralTruncationParameterN;
    }

    private SpectralTruncationParameterKVariable<Integer> spectralTruncationParameterK;

    public SpectralTruncationParameterKVariable<Integer> getSpectralTruncationParameterK() {
        return spectralTruncationParameterK;
    }

    public void setSpectralTruncationParameterK(SpectralTruncationParameterKVariable<Integer> spectralTruncationParameterK) {
        this.spectralTruncationParameterK = spectralTruncationParameterK;
    }

    private BaseDayVariable<Integer> baseDay;

    public BaseDayVariable<Integer> getBaseDay() {
        return baseDay;
    }

    public void setBaseDay(BaseDayVariable<Integer> baseDay) {
        this.baseDay = baseDay;
    }

    private SecondsOfBaseDayVariable<Integer> secondsOfBaseDay;

    public SecondsOfBaseDayVariable<Integer> getSecondsOfBaseDay() {
        return secondsOfBaseDay;
    }

    public void setSecondsOfBaseDay(SecondsOfBaseDayVariable<Integer> secondsOfBaseDay) {
        this.secondsOfBaseDay = secondsOfBaseDay;
    }

    private BaseDateVariable<Integer> baseDate;

    public BaseDateVariable<Integer> getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(BaseDateVariable<Integer> baseDate) {
        this.baseDate = baseDate;
    }

    private SecondsOfBaseDateVariable<Integer> secondsOfBaseDate;

    public SecondsOfBaseDateVariable<Integer> getSecondsOfBaseDate() {
        return secondsOfBaseDate;
    }

    public void setSecondsOfBaseDate(SecondsOfBaseDateVariable<Integer> secondsOfBaseDate) {
        this.secondsOfBaseDate = secondsOfBaseDate;
    }

    private TimestepVariable<Integer> timestep;

    public TimestepVariable<Integer> getTimestep() {
        return timestep;
    }

    public void setTimestep(TimestepVariable<Integer> timestep) {
        this.timestep = timestep;
    }

    private CurrentDayVariable<int[]> currentDay;

    public CurrentDayVariable<int[]> getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(CurrentDayVariable<int[]> currentDay) {
        this.currentDay = currentDay;
    }

    private CurrentSecondsOfCurrentDayVariable<int[]> currentSecondsOfCurrentDay;

    public CurrentSecondsOfCurrentDayVariable<int[]> getCurrentSecondsOfCurrentDay() {
        return currentSecondsOfCurrentDay;
    }

    public void setCurrentSecondsOfCurrentDay(CurrentSecondsOfCurrentDayVariable<int[]> currentSecondsOfCurrentDay) {
        this.currentSecondsOfCurrentDay = currentSecondsOfCurrentDay;
    }

    private CurrentDateVariable<int[]> currentDate;

    public CurrentDateVariable<int[]> getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(CurrentDateVariable<int[]> currentDate) {
        this.currentDate = currentDate;
    }

    private CurrentSecondsOfCurrentDateVariable<int[]> currentSecondsOfCurrentDate;

    public CurrentSecondsOfCurrentDateVariable<int[]> getCurrentSecondsOfCurrentDate() {
        return currentSecondsOfCurrentDate;
    }

    public void setCurrentSecondsOfCurrentDate(CurrentSecondsOfCurrentDateVariable<int[]> currentSecondsOfCurrentDate) {
        this.currentSecondsOfCurrentDate = currentSecondsOfCurrentDate;
    }

    private CurrentTimestepVariable<int[]> currentTimestep;

    public CurrentTimestepVariable<int[]> getCurrentTimestep() {
        return currentTimestep;
    }

    public void setCurrentTimestep(CurrentTimestepVariable<int[]> currentTimestep) {
        this.currentTimestep = currentTimestep;
    }

    private Map<String, WindVariable<double[][][][]>> wind;

    public Map<String, WindVariable<double[][][][]>> getWind() {
        return wind;
    }

    public void setWind(Map<String, WindVariable<double[][][][]>> wind) {
        this.wind = wind;
    }

    private TemperatureVariable<double[][][][]> temperature;

    public TemperatureVariable<double[][][][]> getTemperature() {
        return temperature;
    }

    public void setTemperature(TemperatureVariable<double[][][][]> temperature) {
        this.temperature = temperature;
    }

    private SpecificHumidityVariable<double[][][][]> specificHumidity;

    public SpecificHumidityVariable<double[][][][]> getSpecificHumidity() {
        return specificHumidity;
    }

    public void setSpecificHumidity(SpecificHumidityVariable<double[][][][]> specificHumidity) {
        this.specificHumidity = specificHumidity;
    }

    private SurfacePressureVariable<double[][][]> surfacePressure;

    public SurfacePressureVariable<double[][][]> getSurfacePressure() {
        return surfacePressure;
    }

    public void setSurfacePressure(SurfacePressureVariable<double[][][]> surfacePressure) {
        this.surfacePressure = surfacePressure;
    }

    private SurfaceGeopotentialVariable<double[][][]> surfaceGeopotential;

    public SurfaceGeopotentialVariable<double[][][]> getSurfaceGeopotential() {
        return surfaceGeopotential;
    }

    public void setSurfaceGeopotential(SurfaceGeopotentialVariable<double[][][]> surfaceGeopotential) {
        this.surfaceGeopotential = surfaceGeopotential;
    }

    private OrographyStandardDeviationVariable<double[][][]> orographyStandardDeviation;

    public OrographyStandardDeviationVariable<double[][][]> getOrographyStandardDeviation() {
        return orographyStandardDeviation;
    }

    public void setOrographyStandardDeviation(OrographyStandardDeviationVariable<double[][][]> orographyStandardDeviation) {
        this.orographyStandardDeviation = orographyStandardDeviation;
    }

    private Map<String, LandOceanTransitionMaskVariable<double[][][]>> landOceanTransitionMask;

    public Map<String, LandOceanTransitionMaskVariable<double[][][]>> getLandOceanTransitionMask() {
        return landOceanTransitionMask;
    }

    public void setLandOceanTransitionMask(Map<String, LandOceanTransitionMaskVariable<double[][][]>> landOceanTransitionMask) {
        this.landOceanTransitionMask = landOceanTransitionMask;
    }

    private PblHeightVariable<double[][][]> pblHeight;

    public PblHeightVariable<double[][][]> getPblHeight() {
        return pblHeight;
    }

    public void setPblHeight(PblHeightVariable<double[][][]> pblHeight) {
        this.pblHeight = pblHeight;
    }

    private PerturbationTemperatureVariable<double[][][]> perturbationTemperature;

    public PerturbationTemperatureVariable<double[][][]> getPerturbationTemperature() {
        return perturbationTemperature;
    }

    public void setPerturbationTemperature(PerturbationTemperatureVariable<double[][][]> perturbationTemperature) {
        this.perturbationTemperature = perturbationTemperature;
    }

    private PerturbationSpecificHumidityVariable<double[][][]> perturbationSpecificHumidity;

    public PerturbationSpecificHumidityVariable<double[][][]> getPerturbationSpecificHumidity() {
        return perturbationSpecificHumidity;
    }

    public void setPerturbationSpecificHumidity(PerturbationSpecificHumidityVariable<double[][][]> perturbationSpecificHumidity) {
        this.perturbationSpecificHumidity = perturbationSpecificHumidity;
    }

    private CloudFractionVariable<double[][][][]> cloudFraction;

    public CloudFractionVariable<double[][][][]> getCloudFraction() {
        return cloudFraction;
    }

    public void setCloudFraction(CloudFractionVariable<double[][][][]> cloudFraction) {
        this.cloudFraction = cloudFraction;
    }

    private QCWATVariable<double[][][][]> qCWAT;

    public QCWATVariable<double[][][][]> getQCWAT() {
        return qCWAT;
    }

    public void setQCWAT(QCWATVariable<double[][][][]> qCWAT) {
        this.qCWAT = qCWAT;
    }

    private TCWATVariable<double[][][][]> tCWAT;

    public TCWATVariable<double[][][][]> getTCWAT() {
        return tCWAT;
    }

    public void setTCWAT(TCWATVariable<double[][][][]> tCWAT) {
        this.tCWAT = tCWAT;
    }

    private LCWATVariable<double[][][][]> lCWAT;

    public LCWATVariable<double[][][][]> getLCWAT() {
        return lCWAT;
    }

    public void setLCWAT(LCWATVariable<double[][][][]> lCWAT) {
        this.lCWAT = lCWAT;
    }

    private TSICERADVariable<double[][][]> tSICERAD;

    public TSICERADVariable<double[][][]> getTSICERAD() {
        return tSICERAD;
    }

    public void setTSICERAD(TSICERADVariable<double[][][]> tSICERAD) {
        this.tSICERAD = tSICERAD;
    }

    private Map<String, SecondaryTemperatureVariable<double[][][]>> secondaryTemperature;

    public Map<String, SecondaryTemperatureVariable<double[][][]>> getSecondaryTemperature() {
        return secondaryTemperature;
    }

    public void setSecondaryTemperature(Map<String, SecondaryTemperatureVariable<double[][][]>> secondaryTemperature) {
        this.secondaryTemperature = secondaryTemperature;
    }

    private WaterEquivalentSnowDepthVariable<double[][][]> waterEquivalentSnowDepth;

    public WaterEquivalentSnowDepthVariable<double[][][]> getWaterEquivalentSnowDepth() {
        return waterEquivalentSnowDepth;
    }

    public void setWaterEquivalentSnowDepth(WaterEquivalentSnowDepthVariable<double[][][]> waterEquivalentSnowDepth) {
        this.waterEquivalentSnowDepth = waterEquivalentSnowDepth;
    }

    private GridboxLandFractionVariable<double[][][]> gridboxLandFraction;

    public GridboxLandFractionVariable<double[][][]> getGridboxLandFraction() {
        return gridboxLandFraction;
    }

    public void setGridboxLandFraction(GridboxLandFractionVariable<double[][][]> gridboxLandFraction) {
        this.gridboxLandFraction = gridboxLandFraction;
    }

    private FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> fractionOfSfcAreaCoveredBySeaIce;

    public FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> getFractionOfSfcAreaCoveredBySeaIce() {
        return fractionOfSfcAreaCoveredBySeaIce;
    }

    public void setFractionOfSfcAreaCoveredBySeaIce(FractionOfSfcAreaCoveredBySeaIceVariable<double[][][]> fractionOfSfcAreaCoveredBySeaIce) {
        this.fractionOfSfcAreaCoveredBySeaIce = fractionOfSfcAreaCoveredBySeaIce;
    }

    private SeaIceThicknessVariable<double[][][]> seaIceThickness;

    public SeaIceThicknessVariable<double[][][]> getSeaIceThickness() {
        return seaIceThickness;
    }

    public void setSeaIceThickness(SeaIceThicknessVariable<double[][][]> seaIceThickness) {
        this.seaIceThickness = seaIceThickness;
    }

    private TSOCNVariable<double[][][]> tSOCN;

    public TSOCNVariable<double[][][]> getTSOCN() {
        return tSOCN;
    }

    public void setTSOCN(TSOCNVariable<double[][][]> tSOCN) {
        this.tSOCN = tSOCN;
    }

    private Map<String, GridBoxAveragedCondensateAmountVariable<double[][][][]>> gridBoxAveragedCondensateAmount;

    public Map<String, GridBoxAveragedCondensateAmountVariable<double[][][][]>> getGridBoxAveragedCondensateAmount() {
        return gridBoxAveragedCondensateAmount;
    }

    public void setGridBoxAveragedCondensateAmount(Map<String, GridBoxAveragedCondensateAmountVariable<double[][][][]>> gridBoxAveragedCondensateAmount) {
        this.gridBoxAveragedCondensateAmount = gridBoxAveragedCondensateAmount;
    }

    private String conventions;

    @Override
    public String getConventions() {
        return conventions;
    }

    @Override
    public void setConventions(String conventions) {
        this.conventions = conventions;
    }

    private String logname;

    @Override
    public String getLogname() {
        return logname;
    }

    @Override
    public void setLogname(String logname) {
        this.logname = logname;
    }

    private String host;

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    private String source;

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    private String _case;

    @Override
    public String getCase() {
        return _case;
    }

    @Override
    public void setCase(String _case) {
        this._case = _case;
    }

    private String title;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    private String history;

    @Override
    public String getHistory() {
        return history;
    }

    @Override
    public void setHistory(String history) {
        this.history = history;
    }

    private String makeRoss;

    @Override
    public String getMakeRoss() {
        return makeRoss;
    }

    @Override
    public void setMakeRoss(String makeRoss) {
        this.makeRoss = makeRoss;
    }

    // additionalMethods >>
    // << additionalMethods

}
